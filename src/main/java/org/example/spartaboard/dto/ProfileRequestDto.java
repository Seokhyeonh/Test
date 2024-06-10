package org.example.spartaboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProfileRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{10,20}$")
    @NotBlank(message = "ID를 입력해주세요.")
    private String userId;

//    @Min(10)
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{10,}$")
//    @NotBlank(message = "password를 입력해주세요.")
//    private String password;


}
