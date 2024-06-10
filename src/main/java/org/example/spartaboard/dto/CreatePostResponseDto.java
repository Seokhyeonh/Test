package org.example.spartaboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.spartaboard.entity.Post;
import org.example.spartaboard.entity.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CreatePostResponseDto {
    private long id;
    private User userid;
    private String content;
    private LocalDateTime create_at;
    private LocalDateTime update_at;


    public CreatePostResponseDto(Post post) {
        this.id = post.getPostId();
        this.userid = post.getUser();
        this.content = post.getContents();
    }
}