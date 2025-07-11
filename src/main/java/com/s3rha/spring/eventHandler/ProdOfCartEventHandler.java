package com.s3rha.spring.eventHandler;

import com.s3rha.spring.DAO.ShoppingCartRepo;
import com.s3rha.spring.DAO.UserAccountRepo;
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
public class ProdOfCartEventHandler {

    private final UserAccountRepo userAccountRepo;
    private final ShoppingCartRepo shoppingCartRepo;
    private final OwnershipChecker checker;


    @HandleBeforeLinkSave
    @HandleBeforeLinkDelete
    @HandleBeforeSave
    @HandleBeforeDelete

    public void beforeSave(ProdOfCart prodOfCart) {

        log.warn("HandleBeforeSave  for {} started ",ProdOfCart.class.getSimpleName());
//        checker.assertOwnership(prodOfCart.getShoppingCart().getUserAccount().getUserName());
        ShoppingCart shoppingCart =
                shoppingCartRepo.findByProdOfCartListContaining(prodOfCart)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserAccount userAccount =
                userAccountRepo.findByShoppingCartListContaining(shoppingCart)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checker.assertOwnership(userAccount.getUserName());

    }
}

