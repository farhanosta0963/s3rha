package com.s3rha.spring.eventHandler;

import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.DAO.ShoppingCartRepo;
import com.s3rha.spring.DAO.UserAccountRepo;
import com.s3rha.spring.DAO.VerificationCodeRepo;
import com.s3rha.spring.entity.*;
import com.s3rha.spring.service.OwnershipChecker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


@Transactional
@Slf4j
@Component
@RepositoryEventHandler
@RequiredArgsConstructor
public class VerificationCodeEventHandler {


    private final AccountRepo accountRepo;
    private final OwnershipChecker checker;

    private final VerificationCodeRepo verificationCodeRepo;



    @HandleBeforeLinkSave
    @HandleBeforeLinkDelete
    @HandleBeforeSave
    public void beforeSave(VerificationCode verificationCode) {

        log.warn("HandleBeforeSave  for {} started ",VerificationCode.class.getSimpleName());
        checker.assertOwnership(verificationCode.getAccount().getUserName());
    }


    @HandleBeforeDelete
    public void beforeDelete(VerificationCode verificationCode) {

        log.warn("HandleBeforeDelete for {} started ",VerificationCode.class.getSimpleName());
        checker.assertOwnership(verificationCode.getAccount().getUserName());

        // Remove cart from user's collection
        if (verificationCode.getAccount() != null) {
            verificationCode.setAccount(null);
        }

        verificationCodeRepo.saveAndFlush(verificationCode) ;
    }

    @HandleBeforeCreate
    public void beforeCreate(VerificationCode verificationCode) {
        log.warn("HandleBeforeCreate for {} started ",VerificationCode.class.getSimpleName());

//             Get current authenticated user
        String username = checker.getCurrentUser();
        // Find or create user
        Account user = accountRepo.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // Associate product with user
        verificationCode.setAccount(user);
    }
}

