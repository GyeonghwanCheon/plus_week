package com.example.demo.service;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.*;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.status.ReservationStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RentalLogService rentalLogService;
    private final JPAQueryFactory queryFactory;

    public ReservationService(ReservationRepository reservationRepository,
                              ItemRepository itemRepository,
                              UserRepository userRepository,
                              RentalLogService rentalLogService,
                              JPAQueryFactory queryFactory) {
        this.reservationRepository = reservationRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.rentalLogService = rentalLogService;
        this.queryFactory = queryFactory;
    }

    // TODO: 1. 트랜잭션 이해
    @Transactional
    public void createReservation(Long itemId, Long userId, LocalDateTime startAt, LocalDateTime endAt) {
        // 쉽게 데이터를 생성하려면 아래 유효성검사 주석 처리
        List<Reservation> haveReservations = reservationRepository.findConflictingReservations(itemId, startAt, endAt);
        if(!haveReservations.isEmpty()) {
            throw new ReservationConflictException("해당 물건은 이미 그 시간에 예약이 있습니다.");
        }

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다."));
        Reservation reservation = new Reservation(item, user, ReservationStatus.PENDING, startAt, endAt);
        Reservation savedReservation = reservationRepository.save(reservation);

        RentalLog rentalLog = new RentalLog(savedReservation, "로그 메세지", "CREATE");
        rentalLogService.save(rentalLog);
    }

    // TODO: 3. N+1 문제
    public List<ReservationResponseDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllUserAndItem();
//        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream().map(reservation -> {
            User user = reservation.getUser();
            Item item = reservation.getItem();

            return new ReservationResponseDto(
                    reservation.getId(),
                    user.getNickname(),
                    item.getName(),
                    reservation.getStartAt(),
                    reservation.getEndAt()
            );
        }).toList();
    }

    // TODO: 5. QueryDSL 검색 개선
    public List<ReservationResponseDto> searchAndConvertReservations(Long userId, Long itemId) {

        List<Reservation> reservations = searchReservations(userId, itemId);
        return convertToDto(reservations);
    }

    public List<Reservation> searchReservations(Long userId, Long itemId) {
        QReservation reservation = QReservation.reservation;

        // QueryDSL 동적 쿼리 생성
        return queryFactory.selectFrom(reservation)
                .where(
                        userId != null ? reservation.user.id.eq(userId) : null,
                        itemId != null ? reservation.item.id.eq(itemId) : null
                )
                .fetch();
    }

    private List<ReservationResponseDto> convertToDto(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> new ReservationResponseDto(
                        reservation.getId(),
                        reservation.getUser().getNickname(),
                        reservation.getItem().getName(),
                        reservation.getStartAt(),
                        reservation.getEndAt()
                ))
                .toList();
    }

    // TODO: 7. 리팩토링
    @Transactional
    public void updateReservationStatus(Long reservationId, String status) {
        Reservation reservation = reservationRepository.findByReservationOrElseThrow(reservationId);

        if (ReservationStatus.APPROVED.name().equals(status)) {
            validateStatus(reservation.getStatus() == ReservationStatus.PENDING,
                    "PENDING 상태만 APPROVED로 변경 가능합니다.");
            reservation.updateStatus(ReservationStatus.APPROVED);
            return;
        }

        if (ReservationStatus.CANCELED.name().equals(status)) {
            validateStatus(reservation.getStatus() != ReservationStatus.EXPIRED,
                    "EXPIRED 상태인 예약은 취소할 수 없습니다.");
            reservation.updateStatus(ReservationStatus.CANCELED);
            return;
        }

        if (ReservationStatus.EXPIRED.name().equals(status)) {
            validateStatus(reservation.getStatus() == ReservationStatus.PENDING,
                    "PENDING 상태만 EXPIRED로 변경 가능합니다.");
            reservation.updateStatus(ReservationStatus.EXPIRED);
            return;
        }

        throw new IllegalArgumentException("올바르지 않은 상태: " + status);
    }

    /**
     * 조건 검증 메서드
     * @param condition
     * @param errorMessage
     */
    private void validateStatus(boolean condition, String errorMessage) {
        // condition이 false일 경우 Exception을 발생, true일 경우 계속 진행
        if (!condition) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
