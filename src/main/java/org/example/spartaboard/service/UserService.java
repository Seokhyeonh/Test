package org.example.spartaboard.service;

import org.example.spartaboard.dto.SignupRequestDto;
import org.example.spartaboard.entity.User;
import org.example.spartaboard.entity.UserStatus;
import org.example.spartaboard.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // ADMIN_TOKEN
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
// 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
// email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }
// 사용자 ROLE 확인
        UserStatus role = UserStatus.USER;

        User user = new User(username, password, email, role);
        userRepository.save(user);
    }
}