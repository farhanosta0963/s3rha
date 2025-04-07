package com.s3rha.spring.eventHandler;


import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.DAO.ProductRepo;
import com.s3rha.spring.DAO.ShoppingCartRepo;
import com.s3rha.spring.DAO.UserAccountRepo;
import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.Product;
import com.s3rha.spring.entity.ShoppingCart;
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
@Component
@RepositoryEventHandler
@Slf4j
@RequiredArgsConstructor
public class ProductEventHandler {


    private final OwnershipChecker checker;
    private final ShoppingCartRepo cartRepo;
    private final ProductRepo productRepo ;
    private final AccountRepo accountRepo;
    @HandleBeforeLinkDelete
    @HandleBeforeLinkSave
    @HandleBeforeSave
    public void handleProductUpdate(Product cart) {
        Product existing = productRepo.findById(cart.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checker.assertOwnership(existing.getAccount().getUserName());

    }
    @HandleBeforeDelete
    public void handleProductDelete(Product product) {
        log.info("Delete handler triggered for product {}", product.getProductId());

        Product existing = productRepo.findById(product.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checker.assertOwnership(existing.getAccount().getUserName());
        productRepo.delete(existing);
        productRepo.flush();
        log.info("Product {} successfully deleted", existing.getProductId());

    }
    @HandleBeforeCreate
    public void handleProductCreate(Product product) {
        // Get current authenticated user

        String username = checker.getCurrentUser() ;

        // Find or create user
        Account user = accountRepo.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Associate product with user
        product.setAccount(user);

    }
}