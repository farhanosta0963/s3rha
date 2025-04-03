package com.s3rha.spring.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserDto {
    @NotEmpty(message = "the email can't be empty ")
    private String email;
    @NotEmpty(message = "the verificationCode can't be empty ")
    private String verificationCode;
}