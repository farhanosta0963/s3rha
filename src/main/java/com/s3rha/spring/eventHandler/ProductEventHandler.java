package com.s3rha.spring.eventHandler;


import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.entity.*;
import com.s3rha.spring.service.OwnershipChecker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
@Transactional
@Component
@RepositoryEventHandler
@Slf4j
@RequiredArgsConstructor
public class ProductEventHandler {
    private final OwnershipChecker checker;
    private final AccountRepo accountRepo;

    @HandleBeforeLinkDelete
    @HandleBeforeLinkSave
    @HandleBeforeSave
    public void handleProductUpdate(Product product) {
        log.warn("HandleBeforeSave for {} started ",Product.class.getSimpleName());
//        checker.assertOwnership(product.getAccount().getUserName());
        checker.assertOwnership(accountRepo.findByProductListContaining(product)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getUserName());

    }

    @HandleBeforeCreate
    public void handleProductCreate(Product product) {
        // Get current authenticated user
        log.warn("HandleBeforeCreate for {} started ",Product.class.getSimpleName());

        String username = checker.getCurrentUser() ;

        // Find or create user
        Account user = accountRepo.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.addProduct(product);
        accountRepo.save(user);
    }

}
