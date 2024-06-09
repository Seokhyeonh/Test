package org.example.spartaboard.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String userid;
    private String username;
    private String password;
    private String email;
    private String intro;
    private boolean admin = false;
    private String adminToken = "";
}
