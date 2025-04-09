package com.s3rha.spring.eventHandler;


import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.DAO.ShoppingCartRepo;
import com.s3rha.spring.DAO.StoreAccountRepo;
import com.s3rha.spring.DAO.UserAccountRepo;
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
public class StorePriceEventHandler {


    private final StoreAccountRepo storeAccountRepo;
    private final OwnershipChecker checker;


    @HandleBeforeLinkSave
    @HandleBeforeLinkDelete
    @HandleBeforeSave
    public void beforeSave(StorePrice storePrice) {

        log.warn("HandleBeforeSave  for {} started ",StorePrice.class.getSimpleName());
        checker.assertOwnership(storePrice.getStoreAccount().getUserName());
    }



    @HandleBeforeCreate
    public void beforeCreate(StorePrice storePrice) {
        log.warn("HandleBeforeCreate for {} started ",StorePrice.class.getSimpleName());
//             Get current authenticated user
        String username = checker.getCurrentUser();
        // Find or create user
        StoreAccount storeAccount = storeAccountRepo.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // Associate product with user
        storePrice.setStoreAccount(storeAccount);
    }

}

