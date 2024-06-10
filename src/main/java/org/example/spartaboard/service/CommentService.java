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
    //댓글 생성
    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
        // postId에 해당하는 게시글을 조회, 없으면 Exception
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));
        // 새로운 댓글 엔티티 생성 및 저장
        Comment comment = new Comment(post, user, requestDto.getContents());
        commentRepository.save(comment);
        // 생성된 댓글의 정보를 담은 DTO 반환
        return new CommentResponseDto(comment.getCommentId(), comment.getContents(), requestDto.getUserId());
    }

    // 댓글 수정
    @Transactional
    public Comment updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        // commentId에 해당하는 댓글을 조회, 없으면 Exception
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("댓글을 찾을 수 없습니다."));
        // 댓글의 작성자와 현재 사용자가 일치하는지 확인
        if (!comment.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        comment.updateComment(comment.getContents());
        return comment;
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, User user) {

        // commentId에 해당하는 댓글을 조회, 없으면 Exception
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("댓글을 찾을 수 없습니다."));
        // 댓글의 작성자와 현재 사용자가 일치하는지 확인
        if (!comment.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}

