package com.example.demo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncoderTest {

    /**
     * 메서드가 비밀번호를 암호화하고 결과가 유효한지 테스트
     */
    @Test
    void encodePassword() {
        // Given: 원본 비밀번호를 준비
        String rawPassword = "1234qwer!";

        // When: encode 메서드를 호출하여 비밀번호를 암호화
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        // Then: 암호화된 비밀번호가 유효한지 검증
        assertNotNull(encodedPassword, "비밀번호가 null일 순 없습니다.");
        assertNotEquals(rawPassword, encodedPassword, "인코드한 비밀번호와 같으면 안됩니다.");
        assertTrue(encodedPassword.startsWith("$2a$"), "비밀번호 형식이 BCrypt 형식입니다.");
    }

    @Test
    void PasswordMatches() {
        // Given: 원본 비밀번호를 암호화
        String rawPassword = "1234qwer!";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        // When: matches 메서드를 호출하여 비밀번호 일치 여부를 확인
        boolean isMatch = PasswordEncoder.matches(rawPassword, encodedPassword);

        // Then: 비밀번호가 일치하면 true
        assertTrue(isMatch, "기존 비밀번호와 인코드한 비밀번호가 같습니다.");
    }

    /**
     * matches 메서드가 잘못된 비밀번호와 암호화된 비밀번호를 비교했을 때 false를 반환하는지 테스트
     */
    @Test
    void PasswordNotMatches() {
        // Given: 원본 비밀번호를 암호화
        String rawPassword = "1234qwer!";
        String wrongPassword = "4321qwer!";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        // When: matches 메서드를 호출하여 잘못된 비밀번호와 암호화된 비밀번호를 비교
        boolean isMatch = PasswordEncoder.matches(wrongPassword, encodedPassword);

        // Then: 비밀번호가 일치하지 않으면 false
        assertFalse(isMatch, "기존 비밀번호와 인코드한 비밀번호가 다릅니다.");
    }
}