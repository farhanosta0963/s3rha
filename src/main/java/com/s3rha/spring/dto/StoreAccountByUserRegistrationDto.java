package com.s3rha.spring.dto;

import jakarta.validation.constraints.NotBlank;

public record StoreAccountByUserRegistrationDto (



            @NotBlank(message = "Name must not be empty")
            String name,
            String phoneNumber,
            String image



){}
