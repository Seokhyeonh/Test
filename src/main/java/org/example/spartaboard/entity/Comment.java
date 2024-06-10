package org.example.spartaboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.spartaboard.dto.CommentRequestDto;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "comments")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    public Comment(Post post, User user, String contents) {
        this.contents = contents;
        this.post = post;
        this.user = user;
    }

    public void updateComment(String contents) {
        if (contents != null && !contents.isBlank()) {
            this.contents = contents;
        }
    }
}

