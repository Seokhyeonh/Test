package org.example.spartaboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.spartaboard.entity.User;

@Getter
@Setter
@AllArgsConstructor
public class CreatePostRequestDto {
    @NotBlank
    private String content;
    private User userid;
    private String title;
}