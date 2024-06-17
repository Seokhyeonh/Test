package org.example.spartaboard.service;

import org.example.spartaboard.dto.CommentRequestDto;
import org.example.spartaboard.dto.CommentResponseDto;
import org.example.spartaboard.entity.Comment;
import org.example.spartaboard.entity.Post;
import org.example.spartaboard.entity.User;
import org.example.spartaboard.exception.DataNotFoundException;
import org.example.spartaboard.repository.CommentRepository;
import org.example.spartaboard.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    private CommentService commentService;
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        postRepository = mock(PostRepository.class);
        commentService = new CommentService(commentRepository, postRepository);
    }

    @Test
    void testCreateComment() {
        // Given
        Long postId = 1L;
        User user = new User();
        user.setUserid("testuser");  // userid is a String

        Post post = new Post();
        post.setPostId(postId);

        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContents("테스트 입니다");
        requestDto.setUserId(user.getId());  // Use String

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment savedComment = invocation.getArgument(0);
            savedComment.setCommentId(1L);
            return savedComment;
        });

        // When
        CommentResponseDto responseDto = commentService.createComment(postId, requestDto, user);

        // Then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getCommentId());
        assertEquals("테스트 입니다", responseDto.getContents());



        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(commentCaptor.capture());
        Comment savedComment = commentCaptor.getValue();
        assertEquals(post, savedComment.getPost());
        assertEquals(user, savedComment.getUser());
        assertEquals("테스트 입니다", savedComment.getContents());
    }

    @Test
    void testCreateComment_PostNotFound() {
        // Given
        Long postId = 1L;
        User user = new User();
        user.setUserid("testuser");

        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContents("테스트 입니다");

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(DataNotFoundException.class, () -> {
            commentService.createComment(postId, requestDto, user);
        });

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testUpdateComment() {
        // Given
        Long commentId = 1L;
        User user = new User();
        user.setUserid("testuser");

        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setUser(user);
        comment.setContents("업데이트 전");

        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContents("Updated content");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // When
        Comment updatedComment = commentService.updateComment(commentId, requestDto, user);

        // Then
        assertNotNull(updatedComment);
        assertEquals("업데이트 전", updatedComment.getContents());
        verify(commentRepository).findById(commentId);
    }

    @Test
    void testUpdateComment_() {
        // Given
        Long commentId = 1L;
        User user = new User();
        user.setUserid("testuser");

        User anotherUser = new User();
        anotherUser.setUserid("anotheruser");

        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setUser(anotherUser);
        comment.setContents("업데이트 전");

        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContents("Updated content");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.updateComment(commentId, requestDto, user);
        });

        verify(commentRepository).findById(commentId);
        assertEquals("업데이트 전", comment.getContents());
    }

    @Test
    void testUpdateComment_CommentNotFound() {
        // Given
        Long commentId = 1L;
        User user = new User();
        user.setUserid("testuser");

        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContents("Updated content");

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(DataNotFoundException.class, () -> {
            commentService.updateComment(commentId, requestDto, user);
        });

        verify(commentRepository).findById(commentId);
    }

    @Test
    void testDeleteComment() {
        // Given
        Long commentId = 1L;
        User user = new User();
        user.setUserid("testuser");

        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setUser(user);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // When
        commentService.deleteComment(commentId, user);

        // Then
        verify(commentRepository).delete(comment);
    }

    @Test
    void testDeleteComment_UnauthorizedUser() {
        // Given
        Long commentId = 1L;
        User user = new User();
        user.setUserid("testuser");

        User anotherUser = new User();
        anotherUser.setUserid("anotheruser");

        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setUser(anotherUser);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.deleteComment(commentId, user);
        });

        verify(commentRepository, never()).delete(comment);
    }

    @Test
    void testDeleteComment_CommentNotFound() {
        // Given
        Long commentId = 1L;
        User user = new User();
        user.setUserid("testuser");

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(DataNotFoundException.class, () -> {
            commentService.deleteComment(commentId, user);
        });

        verify(commentRepository, never()).delete(any(Comment.class));
    }
}
