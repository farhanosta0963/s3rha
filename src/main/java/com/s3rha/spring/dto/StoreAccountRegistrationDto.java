package com.s3rha.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record StoreAccountRegistrationDto(
        @NotBlank(message = "User Name must not be empty")
        String userName,
        @NotBlank(message = "Name must not be empty")
        String name,
        String phoneNumber,
        @NotBlank (message = "User email must not be empty") //Neither null nor 0 size
        @Email(message = "Invalid email format")
        String email,
        String image,

        @NotBlank (message = "User password must not be empty")
        String password
) {}
