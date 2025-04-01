package com.s3rha.spring.eventHandler;


import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RepositoryEventHandler
public class ProductEventHandler {

    @Autowired
    private AccountRepo userRepository;

    @HandleBeforeCreate
    public void handleProductCreate(Product product) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Find or create user
        Account user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Associate product with user
        product.setAccount(user);

    }
}