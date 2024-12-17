package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.entity.QItem.item;
import static com.example.demo.entity.QReservation.reservation;
import static com.example.demo.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Reservation> searchReservations(Long userId, Long itemId) {
        return queryFactory.selectFrom(reservation)
                .leftJoin(reservation.user, user).fetchJoin()
                .leftJoin(reservation.item, item).fetchJoin()
                .where(
                        userIdEq(userId),
                        itemIdEq(itemId)
                )
                .fetch();
    }

    // userId가 null이 아니면 조건 추가
    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? reservation.user.id.eq(userId) : null;
    }

    // itemId가 null이 아니면 조건 추가
    private BooleanExpression itemIdEq(Long itemId) {
        return itemId != null ? reservation.item.id.eq(itemId) : null;
    }

}
