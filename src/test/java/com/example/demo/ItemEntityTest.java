package com.example.demo;

import com.example.demo.config.CustomConfig;
import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //Jpa와 연관있는 entity, repository만 초기화
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(CustomConfig.class)
public class ItemEntityTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void itemStatusNullable() {
        // Given: 테스트용 User 객체 생성
        User owner = new User("user", "owner@1111.com", "Owner", "1234qwer!");
        User manager = new User("user", "manager@1111.com", "Manager", "1234qwer!");

        userRepository.save(owner);
        userRepository.save(manager);

        // When: Item 객체 생성
        Item item = new Item("Laptop", "Gaming Laptop", manager, owner);

        // Then: null 상태값 설정이 예외를 발생시키는지 확인
        assertThrows(ConstraintViolationException.class, () -> itemRepository.saveAndFlush(item) );

    }
}

