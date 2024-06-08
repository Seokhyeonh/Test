package org.example.spartaboard.service;

import jakarta.transaction.Transactional;
import org.example.spartaboard.dto.PostResponseDto;
import org.example.spartaboard.dto.PostUpdateRequestDto;
import org.example.spartaboard.entity.Post;
import org.example.spartaboard.entity.User;
import org.example.spartaboard.exception.DataNotFoundException;
import org.example.spartaboard.repository.PostRepository;
import org.springframework.stereotype.Service;



@Service
public class PostService {

    private PostRepository postRepository;

    @Transactional
    public Post updatePost(Long postId, PostUpdateRequestDto requestDto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
        }
        post.update(post.getTitle(),post.getContents());
        return post;
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다.");
        }
        postRepository.delete(post);
    }
    private Post findOne(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("없는 게시글 입니다."));
    }
}



