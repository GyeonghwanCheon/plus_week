package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: 4. find or save 예제 개선
    @Transactional
    public void reportUsers(List<Long> userIds) {
        // userIds로 모든 사용자 조회
        List<User> users = userRepository.findAllById(userIds);

        // 조회된 사용자의 ID 목록을 추출
        List<Long> foundUserIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        // userIds 중 존재하지 않는 ID 검증
        List<Long> missingIds = userIds.stream()
                .filter(id -> !foundUserIds.contains(id))
                .collect(Collectors.toList());
        if (!missingIds.isEmpty()) {
            throw new IllegalArgumentException("다음 ID는 존재하지 않습니다: " + missingIds);
        }

        // 상태 없데이트
        users.forEach(User::updateStatusToBlocked);

        //한 번의 쿼리로 저장
        userRepository.saveAll(users);

    }
}
