package com.s3rha.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;


public record UserAccountRegistrationDto (
        @NotEmpty(message = "User Name must not be empty")
        String userName,
        @NotEmpty(message = "first Name must not be empty")

        String fname,
        @NotEmpty(message = "last Name must not be empty")

        String lname,
        String phoneNumber,
        @NotEmpty(message = "User email must not be empty") //Neither null nor 0 size
        @Email(message = "Invalid email format")//this annotations is used with @valid and binding result with dto only 
        String email,
        String image,

        @NotEmpty(message = "User password must not be empty")
        String password

){ }
