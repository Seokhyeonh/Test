package org.example.spartaboard.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.spartaboard.dto.LoginRequestDto;
import org.example.spartaboard.dto.SignupRequestDto;
import org.example.spartaboard.entity.User;
import org.example.spartaboard.entity.UserStatus;
import org.example.spartaboard.jwt.JwtUtil;
import org.example.spartaboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {  // 애플리케이션의 비즈니스 로직을 처리하는 서비스 클래스
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ADMIN_TOKEN
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword()); // 비밀번호 암호화
//        String email = requestDto.getEmail();  // SignupRequestDto 확인 후 적용
        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }
        // 사용자 ROLE 확인
        UserStatus role = UserStatus.USER;

        User user = new User(username, password, email, role);
        userRepository.save(user);
    }

    // 로그인 로직
    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // Access Token 및 Refresh Token 생성
        String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername());

        // Refresh Token을 사용자 엔티티에 저장
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // JWT를 쿠키에 저장 후 Response 객체에 추가
        jwtUtil.addJwtToCookie(accessToken, refreshToken, res);
    }

    // Refresh Token을 이용한 Access Token 재발급
    public String refresh(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();
            User user = userRepository.findByUsername(username).orElseThrow(
                    () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
            );

            // Refresh Token 일치 확인
            if (!refreshToken.equals(user.getRefreshToken())) {
                throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
            }

            // 새로운 Access Token 생성
            return jwtUtil.createAccessToken(username, user.getRole());
        } else {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }
    }
}