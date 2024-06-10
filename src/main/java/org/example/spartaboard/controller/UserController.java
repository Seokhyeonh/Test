package org.example.spartaboard.controller;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import org.example.spartaboard.dto.LoginRequestDto;
import org.example.spartaboard.dto.SignupRequestDto;
import org.example.spartaboard.jwt.JwtUtil;
import org.example.spartaboard.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/user/signup")
    public String signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);

        return "회원가입 완료";
    }

    @PostMapping("/user/login")
    public String login(LoginRequestDto requestDto, HttpServletResponse res) {
        userService.login(requestDto, res);

        try {
            userService.login(requestDto, res);
        } catch (Exception e) {
            return "reirect:/api/user/login-page?error";
        }
        return "reirect:/";
    }

    @PostMapping("/user/refresh")
    public String refresh(@RequestHeader("RefreshToken") String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();
            return jwtUtil.createAccessToken(username, userService.getUserByUsername(username).getStatus());
        } else {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

    }
}