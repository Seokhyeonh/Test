package org.example.spartaboard.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.spartaboard.Security.UserDetailsImpl;
import org.example.spartaboard.dto.PostResponseDto;
import org.example.spartaboard.dto.PostUpdateRequestDto;
import org.example.spartaboard.entity.Post;
import org.example.spartaboard.entity.User;
import org.example.spartaboard.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    //게시글 수정
    @PutMapping("{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequestDto requestDto

            ) {
        User user = new User();
        Post post = postService.updatePost(postId, requestDto, user);
        PostResponseDto PostResponseDto = new PostResponseDto(post);

        return new ResponseEntity<>(PostResponseDto, HttpStatus.OK);
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        postService.deletePost(postId, userDetails.getUser());
        return ResponseEntity.ok("게시글 삭제 완료");
    }
}
