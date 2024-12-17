package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: 4. find or save 예제 개선
    @Transactional
    public void reportUsers(List<Long> userIds) {
        //존재하지 않는 ID가 있는지 확인
        for (Long userId : userIds) {
            if(!userRepository.existsById(userId)){
                throw new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다.");
            }
        }
        // 한 번의 쿼리로 모든 사용자 조회
        List<User> users = userRepository.findAllById(userIds);

        // 상태 없데이트
        users.forEach(User::updateStatusToBlocked);

        //한 번의 쿼리로 저장
        userRepository.saveAll(users);

    }
}
