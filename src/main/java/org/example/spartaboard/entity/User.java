package org.example.spartaboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.spartaboard.dto.ProfileModifyRequestDto;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name="user")
public class User extends Timestamped{

    @Id //찾을 때 추천(고유)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank//가입시 필수
    @Column(nullable = false, unique = true)
    private String userId;

    @NotBlank//가입시 필수
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String introduce;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    //refresh token 설정 필요

    //상태 변경 시간
    //userEntity 에만 필요하므로 User 에 위치시킴
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime StatusChangedAt;

    public void update(ProfileModifyRequestDto requestDto) {
        if (requestDto.getUsername() != null) {
            this.username = requestDto.getUsername();
        }
        if (requestDto.getIntroduce() != null) {
            this.introduce = requestDto.getIntroduce();
        }
        if (requestDto.getNewPassword() != null) {
            this.password = requestDto.getNewPassword();
        }

    }


}
