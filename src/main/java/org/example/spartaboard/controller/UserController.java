package org.example.spartaboard.controller;


import jakarta.validation.Valid;
import org.example.spartaboard.dto.SignupRequestDto;
import org.example.spartaboard.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class UserController {

    private UserService userService;


    @PostMapping("/user/signup")
    public String signup(@Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return "redirect:/api/user/login-page";
    }

}