package org.example.spartaboard.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]{10,20}$", message = "아이디는 영어와 숫자만 사용하여 10~20자리여야 합니다.")
    private String userid;
    @NotBlank
    private String username;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{10,20}$", message = "비밀번호는 10~20자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
    @Email
    @NotBlank
    private String email;
    private String intro;
    private boolean INACTIVE = false;
    private boolean admin = false;
    private String adminToken = "";
}
