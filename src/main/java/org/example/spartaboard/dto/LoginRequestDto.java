package org.example.spartaboard.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    private String userId;  // 회원가입된 사용자 ID와 비밀번호가 일치하는 사용자만 로그인할 수 있습니다.
    private String password;
}
