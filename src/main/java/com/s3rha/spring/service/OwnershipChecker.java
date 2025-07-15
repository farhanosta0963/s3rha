package com.s3rha.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Slf4j
@Service
@RequiredArgsConstructor
public class OwnershipChecker {

    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("SCOPE_WRITE"));
    }

    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public void assertOwnership(String entityOwnerUsername) {
        if (isAdmin()) return;

        String current = getCurrentUser();
        if (current== null  ||  !current.equals(entityOwnerUsername)) {
            log.warn("User {} attempted to modify this owned by {}");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't own this resource");
        }
    }
}
