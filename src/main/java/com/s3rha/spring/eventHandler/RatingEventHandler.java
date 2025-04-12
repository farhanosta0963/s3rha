package com.s3rha.spring.eventHandler;



import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.DAO.ShoppingCartRepo;
import com.s3rha.spring.DAO.StoreAccountRepo;
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
@Slf4j
@Component
@RepositoryEventHandler
@RequiredArgsConstructor
public class RatingEventHandler {


    private final StoreAccountRepo storeAccountRepo;
    private final AccountRepo accountRepo ;
    private final OwnershipChecker checker;


    @HandleBeforeLinkSave
    @HandleBeforeLinkDelete
    @HandleBeforeSave
    public void beforeSave(Rating rating) {

        log.warn("HandleBeforeSave  for {} started ",Rating.class.getSimpleName());
//        checker.assertOwnership(rating.getAccount().getUserName());

        checker.assertOwnership(accountRepo.findByRatingListContaining(rating)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getUserName());
    }



    @HandleBeforeCreate
    public void beforeCreate(Rating rating) {
        log.warn("HandleBeforeCreate for {} started ",Rating.class.getSimpleName());
        //  Get current authenticated user
        String username = checker.getCurrentUser();
        // Find or create user
        Account account = accountRepo.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // Associate product with user
       account.addRating(rating);
    }

}

