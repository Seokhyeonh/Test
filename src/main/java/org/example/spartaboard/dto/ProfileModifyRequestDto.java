package org.example.spartaboard.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ProfileModifyRequestDto {

    private String username;
    private String introduce;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{10,}$")
    private String newPassword;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{10,}$")
    private String oldPassword;

}
