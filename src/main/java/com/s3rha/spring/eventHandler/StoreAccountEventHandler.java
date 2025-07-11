package com.s3rha.spring.eventHandler;

import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.DAO.ShoppingCartRepo;
import com.s3rha.spring.DAO.UserAccountRepo;
import com.s3rha.spring.entity.*;
import com.s3rha.spring.service.OwnershipChecker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

@Transactional
@Slf4j
@Component
@RepositoryEventHandler
@RequiredArgsConstructor
public class StoreAccountEventHandler {
    private final UserAccountRepo userRepository;
    private final OwnershipChecker checker;
    private final ShoppingCartRepo cartRepo;
    private final AccountRepo accountRepo;


    //  TODO disable in security the ability to modify on parent account or parent report and so on ....

    @HandleBeforeLinkSave
    @HandleBeforeLinkDelete
    @HandleBeforeSave
    @HandleBeforeDelete

    public void beforeSave(StoreAccount storeAccount) {

        log.warn("HandleBeforeSave  for {} started ",StoreAccount.class.getSimpleName());
        checker.assertOwnership(storeAccount.getUserName());
    }



}
