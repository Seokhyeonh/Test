package org.example.spartaboard.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;  // 회원가입 기능
                            // 신규 가입자는 사용자 ID, 비밀번호를 입력하여 서비스에 가입할 수 있습니다. 확인 후 적용
    private boolean admin = false;
    private String adminToken = "";
}
