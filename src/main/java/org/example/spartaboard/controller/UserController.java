package org.example.spartaboard.controller;

import org.example.spartaboard.dto.SignupRequestDto;
import org.example.spartaboard.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/user/signup")
    public String signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);

        return "redirect:/api/user/login-page";
    }

}