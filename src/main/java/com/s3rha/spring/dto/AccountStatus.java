package com.s3rha.spring.dto;
public enum AccountStatus {
    PENDING,        // Waiting for email verification
    ACTIVATED,      // Verified and active
    DEACTIVATED,    // Manually disabled
    SUSPENDED,      // Temporarily locked
    BANNED          // Permanently blocked
}