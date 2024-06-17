package org.example.spartaboard.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreatePostRequestDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        // Initialize the validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator(); // No casting needed, correct type used
    }

    @Test
    public void testCreatePostRequestDtoValid() {
        // 올바른 CreatePostRequestDto 객체 생성
        CreatePostRequestDto dto = new CreatePostRequestDto("Valid Content", "testuser123", "Valid Title");

        // 유효성 검증 결과가 비어있는지 확인
        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    public void testCreatePostRequestDtoInvalid() {
        // 잘못된 (내용이 없는) CreatePostRequestDto 객체 생성
        CreatePostRequestDto dto = new CreatePostRequestDto("", "testuser123", "Valid Title");

        // 유효성 검증 결과가 비어있지 않은지 확인 (오류가 발생해야 함)
        assertFalse(validator.validate(dto).isEmpty());
    }
}