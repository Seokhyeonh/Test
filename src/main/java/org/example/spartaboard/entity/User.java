package org.example.spartaboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.spartaboard.dto.ProfileModifyRequestDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank//가입시 필수
    @Column(nullable = false, unique = true)
    private String userId;

    @NotBlank//가입시 필수
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column
    private String introduce;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus role;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime statusChangedAt;

    // Refresh Token을 저장할 필드
    private String refreshToken;

    public User(String username, String password, String email, UserStatus role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

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
