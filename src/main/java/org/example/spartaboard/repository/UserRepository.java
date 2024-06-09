package org.example.spartaboard.repository;

import org.example.spartaboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserid(String userid);
    Optional<User> findByEmail(String email);
}
