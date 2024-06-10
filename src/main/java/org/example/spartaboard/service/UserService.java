package org.example.spartaboard.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.spartaboard.dto.LoginRequestDto;
import org.example.spartaboard.dto.SignupRequestDto;
import org.example.spartaboard.entity.User;
import org.example.spartaboard.entity.UserStatus;
import org.example.spartaboard.jwt.JwtUtil;
import org.example.spartaboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {
        String userid = requestDto.getUserid();
        String username = requestDto.getUsername();
        String intro = requestDto.getIntro();
        String passwordBefore = requestDto.getPassword();
        if(passwordBefore.length()<10){
            throw new IllegalArgumentException("비밀번호는 최소 10글자 이상이어야 합니다.");
        }
        String password = passwordEncoder.encode(requestDto.getPassword());

        //회원 사용자 ID 조건 확인
        if (!(userid.length() >= 10 && userid.length() <= 20)) {
            throw new IllegalArgumentException("사용자 ID는 최소 10글자 이상, 최대 20글자 이하여야 합니다.");
        }

        // 회원 중복 확인
        Optional<User> checkUserid = userRepository.findByUserId(userid);
        if (checkUserid.isPresent()) {
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
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserStatus.ADMIN;
        }

        UserStatus userStatus = UserStatus.ACTIVE;

        // 사용자 등록
        User user = new User(userid, username, password, email, intro, role, userStatus);
        userRepository.save(user);
    }

    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
        String username = requestDto.getUserid();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());
        jwtUtil.addJwtToCookie(token, res);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));
    }
}
