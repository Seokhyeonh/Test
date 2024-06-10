package org.example.spartaboard.dto;

import org.example.spartaboard.entity.Post;

public class PostResponseDto {
    private Long postId;
    private String title;
    private String contents;
    private String userId;

    public PostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.userId =  post.getUser().getUserid();

    }

}
