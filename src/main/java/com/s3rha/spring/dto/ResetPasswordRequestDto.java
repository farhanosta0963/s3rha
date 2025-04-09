package com.s3rha.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDto(
        @NotBlank String token,
        @NotBlank
        String newPassword
) {}