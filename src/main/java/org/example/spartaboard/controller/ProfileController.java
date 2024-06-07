package org.example.spartaboard.controller;

import lombok.RequiredArgsConstructor;
import org.example.spartaboard.Security.UserDetailsImpl;
import org.example.spartaboard.dto.ProfileModifyRequestDto;
import org.example.spartaboard.dto.ProfileRequestDto;
import org.example.spartaboard.dto.ProfileResponseDto;
import org.example.spartaboard.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class ProfileController {

    private final ProfileService profileService;

    //프로필 조회 (타인의 프로필도 조회할 수 있는)
    @GetMapping("")
    public ResponseEntity<ProfileResponseDto> showProfile(@RequestBody ProfileRequestDto requestDto) {
        return profileService.showProfile(requestDto);
    }

    //프로필 수정
    @PatchMapping("/update")
    public ResponseEntity<ProfileResponseDto> updateProfile( @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ProfileModifyRequestDto modifyRequestDto) {

        String userId = userDetails.getUser().getUserId();
        return profileService.updateProfile(modifyRequestDto, userId);
    }

    private boolean authorizePassword(String newPassword) {
        return false;
    }


}
