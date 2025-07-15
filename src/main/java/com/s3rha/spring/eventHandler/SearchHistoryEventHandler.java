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
@Slf4j
@Component
@RepositoryEventHandler
@RequiredArgsConstructor
public class SearchHistoryEventHandler {



    private final AccountRepo accountRepo ;
    private final OwnershipChecker checker;


    @HandleBeforeLinkSave
    @HandleBeforeLinkDelete
    @HandleBeforeSave
    @HandleBeforeDelete

    public void beforeSave(SearchHistory searchHistory) {

        log.warn("HandleBeforeSave  for {} started ",SearchHistory.class.getSimpleName());
//        checker.assertOwnership(searchHistory.getAccount().getUserName());

        checker.assertOwnership(searchHistory.getAccount().getUserName());
//        accountRepo.findBySearchHistoryListContaining(searchHistory)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
//                .getUserName()
//        );
    }



    @HandleBeforeCreate
    public void beforeCreate(SearchHistory searchHistory) {
        log.warn("HandleBeforeCreate for {} started ",SearchHistory.class.getSimpleName());
        //  Get current authenticated user
        String username = checker.getCurrentUser();
        // Find or create user
        Account account = accountRepo.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        account.addSearchHistory(searchHistory);
        searchHistory.setAccount(account);
    }

}

