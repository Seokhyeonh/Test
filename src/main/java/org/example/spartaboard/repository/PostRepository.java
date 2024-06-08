package org.example.spartaboard.repository;

import org.example.spartaboard.entity.Post;
import org.example.spartaboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUser(User user);
}
