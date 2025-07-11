package com.s3rha.spring.eventHandler;

import com.s3rha.spring.DAO.ShoppingCartRepo;
import com.s3rha.spring.DAO.SpecialOfferRepo;
import com.s3rha.spring.DAO.StoreAccountRepo;
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
public class ProdOfOfferEventHandler {




    private final SpecialOfferRepo specialOfferRepo;
    private final StoreAccountRepo storeAccountRepo;
    private final OwnershipChecker checker;


    @HandleBeforeLinkSave
    @HandleBeforeLinkDelete
    @HandleBeforeSave
    @HandleBeforeDelete

    public void beforeSave(ProdOfOffer prodOfOffer) {

        log.warn("HandleBeforeSave  for {} started ",ProdOfOffer.class.getSimpleName());
//        checker.assertOwnership(prodOfCart.getShoppingCart().getUserAccount().getUserName());
        SpecialOffer specialOffer =
                specialOfferRepo.findByProdOfOfferListContaining(prodOfOffer)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        StoreAccount storeAccount =
                storeAccountRepo.findBySpecialOfferListContaining(specialOffer)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checker.assertOwnership(storeAccount.getUserName());

    }
}

