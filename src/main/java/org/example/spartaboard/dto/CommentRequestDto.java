package org.example.spartaboard.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.spartaboard.entity.Comment;
@Setter
@Getter
public class CommentRequestDto {
    private String Contents;
    private Long commentId;
    private Long userId;

}
