package org.example.spartaboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.spartaboard.entity.User;

@NoArgsConstructor
@Getter
public class ProfileResponseDto {

    private String userid;
    private String username;
    private String introduce;
    private String email;

    public ProfileResponseDto(User user) {
        this.userid = user.getUserid();
        this.username = user.getUsername();
        this.introduce = user.getIntroduce();
        this.email = user.getEmail();
    }


}
