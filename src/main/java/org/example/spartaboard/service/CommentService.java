package org.example.spartaboard.service;

import jakarta.transaction.Transactional;
import org.example.spartaboard.dto.CommentRequestDto;
import org.example.spartaboard.dto.CommentResponseDto;
import org.example.spartaboard.entity.Comment;
import org.example.spartaboard.entity.Post;
import org.example.spartaboard.entity.User;
import org.example.spartaboard.exception.DataNotFoundException;
import org.example.spartaboard.repository.CommentRepository;
import org.example.spartaboard.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment(post, user, requestDto.getContents());
        commentRepository.save(comment);
        return new CommentResponseDto(comment.getCommentId(), comment.getContents(), requestDto.getUserId());
    }



    @Transactional
    public Comment updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        comment.updateComment(comment.getContents());
        return comment;
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}

