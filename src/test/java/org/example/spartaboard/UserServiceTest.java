package org.example.spartaboard;

import org.example.spartaboard.dto.SignupRequestDto;
import org.example.spartaboard.entity.User;
import org.example.spartaboard.entity.UserStatus;
import org.example.spartaboard.jwt.JwtUtil;
import org.example.spartaboard.repository.UserRepository;
import org.example.spartaboard.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;


    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    void Test1() {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUserid("testuserid");
        requestDto.setUsername("testusername");
        requestDto.setPassword("testpassword123");
        requestDto.setEmail("test@example.com");
        requestDto.setIntro("테스트 중입니다.");


        given(passwordEncoder.encode("testpassword123")).willReturn("encodedpassword");
        given(userRepository.findByUserid("testuserid")).willReturn(Optional.empty());
        given(userRepository.findByEmail("test@example.com")).willReturn(Optional.empty());
        given(userRepository.findByStatus(UserStatus.INACTIVE)).willReturn(Optional.empty());

        // when
        userService.signup(requestDto);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("testuserid", savedUser.getUserid());
        assertEquals("testusername", savedUser.getUsername());
        assertEquals("encodedpassword", savedUser.getPassword());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals(UserStatus.ACTIVE, savedUser.getStatus());
        assertEquals(UserStatus.USER, savedUser.getRole());
    }
}

