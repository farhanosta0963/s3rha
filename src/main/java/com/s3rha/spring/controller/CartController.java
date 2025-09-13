package com.s3rha.spring.controller;

import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.DAO.ProductRepo;
import com.s3rha.spring.DAO.ShoppingCartRepo;
import com.s3rha.spring.DAO.UserAccountRepo;
import com.s3rha.spring.controller.components.Recommendation;
import com.s3rha.spring.dto.RecommendationResponse;
import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CartController {
    private final RestTemplate restTemplate;
    private final ProductRepo productRepo;
    private final AccountRepo accountRepo ;
    private final UserAccountRepo userAccountRepo;
    private final ShoppingCartRepo shoppingCartRepo;


    @PostMapping("/my-carts")
    public ResponseEntity<?> recommendedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserAccount account = userAccountRepo.findByUserName(userName)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User with UserName " + userName + " not found"));
        Long id = account.getAccountId();

        return ResponseEntity.ok(shoppingCartRepo.findByUserAccount(account));


    }
}
