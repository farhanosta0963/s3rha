package com.s3rha.spring.eventHandler;
import com.s3rha.spring.DAO.ShoppingCartRepo;
import com.s3rha.spring.entity.UserAccount;
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
public class UserAccountEventHandler {
    private final OwnershipChecker checker;
    private final ShoppingCartRepo cartRepo;


    //  TODO disable in security the ability to modify on parent account or parent report and so on ....

    @HandleBeforeLinkSave
    @HandleBeforeLinkDelete
    @HandleBeforeSave
    @HandleBeforeDelete

    public void beforeSave(UserAccount userAccount) {

        log.warn("HandleBeforeSave  for {} started ",UserAccount.class.getSimpleName());
        checker.assertOwnership(userAccount.getUserName());
    }



}
