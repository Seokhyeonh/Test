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

    //게시글 수정
    @Transactional
    public Post updatePost(Long postId, PostUpdateRequestDto requestDto, User user) {
        // postId에 해당하는 게시글을 조회, 없으면 Exception
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));
        // 게시글의 작성자와 현재 사용자가 일치하는지 확인
        if (!post.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
        }
        post.update(post.getTitle(),post.getContents());
        return post;
    }
    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, User user) {
        // postId에 해당하는 게시글을 조회, 없으면 Exception
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));
        // 게시글의 작성자와 현재 사용자가 일치하는지 확인
        if (!post.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다.");
        }
        postRepository.delete(post);
    }
}



