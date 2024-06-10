package org.example.spartaboard.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.spartaboard.Security.UserDetailsImpl;
import org.example.spartaboard.dto.CreatePostRequestDto;
import org.example.spartaboard.dto.CreatePostResponseDto;
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

    @PostMapping("/createPost")
    public ResponseEntity<CreatePostResponseDto> createPost(
            @RequestBody CreatePostRequestDto createPostRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        CreatePostResponseDto response = postService.createPost(createPostRequestDto, user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/readPost")
    public List<CreatePostResponseDto> getAllPosts() {

        return postService.getAllPosts();
    }

    //게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Post post = postService.updatePost(postId, requestDto, user);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return ResponseEntity.ok(postResponseDto);
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        postService.deletePost(postId, user);
        return ResponseEntity.ok("게시글 삭제 완료");
    }
}
