package org.example.spartaboard.controller;

import lombok.RequiredArgsConstructor;
import org.example.spartaboard.Security.UserDetailsImpl;
import org.example.spartaboard.dto.ProfileModifyRequestDto;
import org.example.spartaboard.dto.ProfileRequestDto;
import org.example.spartaboard.dto.ProfileResponseDto;
import org.example.spartaboard.entity.User;
import org.example.spartaboard.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class ProfileController {

    private final ProfileService profileService;

    //프로필 조회 (타인의 프로필도 조회할 수 있는)
    @GetMapping("/api/user")
    public ResponseEntity<ProfileResponseDto> showProfile(@RequestParam("userid") String userid) {
        ProfileRequestDto requestDto = new ProfileRequestDto(userid);
        return profileService.showProfile(requestDto);
    }

    //프로필 수정
    @PatchMapping("/update")
    public ResponseEntity<ProfileResponseDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @RequestBody ProfileModifyRequestDto modifyRequestDto) {
        String userid = userDetails.getUser().getUserid();
        return profileService.updateProfile(modifyRequestDto, userid);
    }

    private boolean authorizePassword(String newPassword) {
        return false;
    }
}
