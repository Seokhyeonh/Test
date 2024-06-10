package org.example.spartaboard.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.spartaboard.entity.Comment;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto  {
    private Long commentId;
    private String contents;
    private Long userid;


    public CommentResponseDto(Long commentId, String contents, Long userId) {
        this.commentId = commentId;
        this.contents = contents;
        this.userid = userid;
    }


    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.contents = comment.getContents();
        this.userid=getUserid();
    }
}
