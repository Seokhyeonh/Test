package org.example.spartaboard.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import org.example.spartaboard.Security.UserDetailsImpl;
import org.example.spartaboard.dto.LoginRequestDto;
import org.example.spartaboard.dto.SignupRequestDto;
import org.example.spartaboard.jwt.JwtUtil;
import org.example.spartaboard.service.UserService;
import org.springframework.http.ResponseEntity;
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
        try {
            userService.login(requestDto, res);
        } catch (Exception e) {
            return "redirect:/api/user/login-page?error";
        }
        return "로그인 완료";
    }

    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 사용자의 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        return ResponseEntity.ok("로그아웃 완료");
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