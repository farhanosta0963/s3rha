package com.s3rha.spring.eventHandler;

import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.DAO.ShoppingCartRepo;
import com.s3rha.spring.DAO.UserAccountRepo;
import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.Product;
import com.s3rha.spring.entity.ShoppingCart;
import com.s3rha.spring.entity.UserAccount;
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
public class ShoppingCartEventHandler {

    private final UserAccountRepo userRepository;
    private final OwnershipChecker checker;
    private final ShoppingCartRepo cartRepo;
    @HandleBeforeLinkSave
    @HandleBeforeLinkDelete
    @HandleBeforeDelete
    @HandleBeforeSave
    public void checkCartOwner(ShoppingCart cart) {
        ShoppingCart existing = cartRepo.findById(cart.getShoppingCartId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        checker.assertOwnership(existing.getUserAccount().getUserName());
    }







        @HandleBeforeCreate
        public void handleProductCreate(ShoppingCart shoppingCart) {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            // Find or create user
            UserAccount user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            // Associate product with user
            shoppingCart.setUserAccount(user);
        }
    }

