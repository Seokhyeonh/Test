package org.example.spartaboard.repository;

import org.example.spartaboard.entity.Comment;
import org.example.spartaboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUser(User user);
}
