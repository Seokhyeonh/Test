package org.example.spartaboard.repository;

import org.example.spartaboard.entity.User;
import org.example.spartaboard.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //signup
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    //profile
    boolean existsUserByUserIdAndStatus(String requestUserId, UserStatus userStatus);
    Optional<User>findByUserId(String UserId);

}
