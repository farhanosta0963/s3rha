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

    public boolean isAdminOrModerator() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                        || a.getAuthority().equals("SCOPE_WRITE")
                            || a.getAuthority().equals("ROLE_MODERATOR"));
    }

    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public void assertOwnership(String entityOwnerUsername) {
        if (isAdminOrModerator()) return;

        String current = getCurrentUser();
        if (current== null  ||  !current.equals(entityOwnerUsername)) {
            log.warn("User {} attempted to modify this owned by {}",current,entityOwnerUsername);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't own this resource");
        }
    }

    public void assertAdminOrModerator() {
        log.warn("user {} is being checked if he is admin or moderator ",getCurrentUser());
        if(!isAdminOrModerator()) {
            log.warn("user {} is NOT  admin or moderator ",getCurrentUser());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are neither an admin nor a moderator ");
        }
    }



}
