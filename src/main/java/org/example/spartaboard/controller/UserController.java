package org.example.spartaboard.controller;


import jakarta.validation.Valid;
import org.example.spartaboard.dto.SignupRequestDto;
import org.example.spartaboard.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/user/signup")
    public String signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);

        return "회원가입 완료";
    }
}