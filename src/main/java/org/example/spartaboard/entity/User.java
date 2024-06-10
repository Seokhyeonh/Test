package org.example.spartaboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.spartaboard.dto.ProfileModifyRequestDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String userid;

    @NotBlank
    @Column(nullable = false)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String introduce; // intro -> introduce

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime statusChangedAt;

    // Refresh Token을 저장할 필드
    @Column
    private String refreshToken;

    public User(String userid, String username, String password, String email, String introduce, UserStatus role, UserStatus status) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.introduce = introduce;
        this.role = role;
        this.status = status;
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

    public void isInactive(){
        this.status=UserStatus.INACTIVE;
    }
}
