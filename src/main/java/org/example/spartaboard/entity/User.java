package org.example.spartaboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 20)
    private String userid;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String intro;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;


    public User(String userid, String username, String password, String email, String intro, UserRoleEnum role, UserStatus userStatus) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.intro = intro;
        this.role = role;
        this.userStatus = userStatus;

    }

}
