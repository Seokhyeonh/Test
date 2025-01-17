package org.example.spartaboard;

import jakarta.servlet.http.HttpServletResponse;
import org.example.spartaboard.dto.LoginRequestDto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
    void signupSuccess() {
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

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void test2() {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUserid("testuserid");
        requestDto.setUsername("testusername");
        requestDto.setPassword("testpassword123!");
        requestDto.setEmail("test@example.com");

        given(userRepository.findByUserid("testuserid")).willReturn(Optional.empty());
        given(userRepository.findByEmail("test@example.com")).willReturn(Optional.of(new User()));

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.signup(requestDto);
        });

        // then
        assertEquals("중복된 Email 입니다.", exception.getMessage());
    }
    @Test
    @DisplayName("회원가입 실패 - ID 중복")
    void test3() {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUserid("testuserid11");
        requestDto.setUsername("testusername2");
        requestDto.setPassword("testpassword123!");
        requestDto.setEmail("test@example.com");

        given(userRepository.findByUserid("testuserid11")).willReturn(Optional.of(new User()));

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.signup(requestDto);
        });

        // then
        assertEquals("중복된 사용자가 존재합니다.", exception.getMessage());
    }
    @Test
    @DisplayName("회원가입 실패 - 비밀번호 짧음")
    void test4() {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUserid("testuserid11");
        requestDto.setUsername("testusername2");
        requestDto.setPassword("Pass1!"); // less than 10 characters
        requestDto.setEmail("test@example.com");

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.signup(requestDto);
        });

        // then
        assertEquals("비밀번호는 최소 10글자 이상이어야 합니다.", exception.getMessage());
    }
    @Test
    @DisplayName("회원가입 실패 - 탈퇴 상태")
    void test5() {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUserid("testuserid11");
        requestDto.setUsername("testusername2");
        requestDto.setPassword("testpassword123!!");
        requestDto.setEmail("test@example.com");

        given(userRepository.findByUserid("testuserid11")).willReturn(Optional.empty());

        given(userRepository.findByStatus(UserStatus.INACTIVE)).willReturn(Optional.of(new User()));

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.signup(requestDto);
        });

        // then
        assertEquals("탈퇴 상태인 사용자가 존재합니다.", exception.getMessage());
    }
    @Test
    @DisplayName("로그인성공")
    void test6() {
        // Given
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setUserid("testuserid");
        requestDto.setPassword("testpassword123!");

        User user = new User();
        user.setUsername("testuserid");
        user.setPassword("encodedpassword");
        user.setRole(UserStatus.ACTIVE);

        HttpServletResponse res = mock(HttpServletResponse.class);

        when(userRepository.findByUsername(requestDto.getUserid())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);

        when(jwtUtil.createToken(user.getUsername(), user.getRole())).thenReturn("jwt-token");

        // When
        userService.login(requestDto, res);

        // Then
        verify(jwtUtil, times(1)).addJwtToCookie("jwt-token", res);
    }
    @Test
    @DisplayName("로그인 실패 - 사용자 존재하지 않음")
    void test7() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setUserid("testuserid1");
        requestDto.setPassword("testpassword1234!");

        given(userRepository.findByUsername("testuserid1")).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.login(requestDto, mock(HttpServletResponse.class));
        });

        // then
        assertEquals("등록된 사용자가 없습니다.", exception.getMessage());
    }
    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void test8() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setUserid("testuserid");
        requestDto.setPassword("testpassword1234!");

        User user = new User();
        user.setUserid("testuserid");
        user.setUsername("testpassword123");
        user.setPassword("encodedpassword");

        given(userRepository.findByUsername("testuserid")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("testpassword1234!", "encodedpassword")).willReturn(false);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.login(requestDto, mock(HttpServletResponse.class));
        });

        // then
        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
    }
}

