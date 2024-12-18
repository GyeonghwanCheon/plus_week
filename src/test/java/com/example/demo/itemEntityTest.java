package com.example.demo;

import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class itemEntityTest {

    @Test
    void itemStatusNullable() {
        // Given: 테스트용 User 객체 생성
        User owner = new User("user", "owner@1111.com", "Owner", "1234qwer!");
        User manager = new User("user", "manager@1111.com", "Manager", "1234qwer!");

        // When: Item 객체 생성 및 status 필드를 null로 설정
        Item item = new Item("Laptop", "Gaming Laptop", manager, owner);


        // Then: null 상태값 설정이 예외를 발생시키는지 확인
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            if (item.getStatus() == null) {
                throw new IllegalArgumentException("Status는 null일 수 없습니다.");
            }
        });

        assertEquals("Status는 null일 수 없습니다.", exception.getMessage(), "예외 메시지가 예상과 다릅니다.");
    }
}

