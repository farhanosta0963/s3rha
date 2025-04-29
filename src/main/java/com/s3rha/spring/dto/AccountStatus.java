package com.s3rha.spring.dto;
public enum AccountStatus {
    PENDING,        // Waiting for email verification
    ACTIVATED,      // Verified and active
    ACTIVATED_WITH_GOOGLE,
    ACTIVATED_WITH_FACEBOOK,
    SUSPENDED,      // Temporarily locked
    DEACTIVATED,    // Manually disabled
    BANNED  // Permanently blocked

}