package com.s3rha.spring.controller;



import com.s3rha.spring.DAO.*;
import com.s3rha.spring.entity.*;
import com.s3rha.spring.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class RatingsController {
    private final UserPriceRepo userPriceRepo ;
    private final AccountRepo accountRepo;
    private final ProductRepo productRepo ;
    private final StoreAccountRepo storeAccountRepo;
    private final RatingOnProductRepo ratingOnProductRepo;
    private final RatingOnStoreRepo ratingOnStoreRepo;

    @Transactional
    @GetMapping("/products/{id}/ratingAverage")
    public ResponseEntity<Integer> getProductAverageRating(@PathVariable Long id) {
        Product product = productRepo.findById(id).orElseThrow(()->{
            log.error("[Rating average for  ] Product id  :{} not found",id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND,"Product NOT FOUND ");});

        List<RatingOnProduct> ratingOnProductList =  ratingOnProductRepo.findByProduct(product) ;
        Integer average =  (int) ratingOnProductList.stream()
                .mapToInt(ratingOnProduct -> ratingOnProduct.getStarsRate())
                .average()
                // Use orElse() to provide a default value (0.0) if the list is empty
                .orElse(0.0);
        return ResponseEntity.ok(average);
    }

    @Transactional
    @GetMapping("/storeAccounts/{id}/ratingAverage")
    public ResponseEntity<Integer> getStoreAccountAverageRating(@PathVariable Long id) {

        StoreAccount storeAccount = storeAccountRepo.findById(id).orElseThrow(()->{
            log.error("[Rating average for  ] StoreAccount id  :{} not found",id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND,"StoreAccount NOT FOUND ");});

        List<RatingOnStore> ratingOnProductList =  ratingOnStoreRepo.findByStoreAccount(storeAccount) ;
        Integer average =  (int) ratingOnProductList.stream()
                .mapToInt(ratingOnProduct -> ratingOnProduct.getStarsRate())
                .average()
                // Use orElse() to provide a default value (0.0) if the list is empty
                .orElse(0.0);
        return ResponseEntity.ok(average);
    }

}
