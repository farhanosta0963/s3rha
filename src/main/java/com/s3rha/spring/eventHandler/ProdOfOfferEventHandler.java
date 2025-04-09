package com.s3rha.spring.eventHandler;

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
public class ProdOfOfferEventHandler {


    private final OwnershipChecker checker;


    @HandleBeforeLinkSave
    @HandleBeforeLinkDelete
    @HandleBeforeSave
    public void beforeSave(ProdOfOffer prodOfOffer) {

        log.warn("HandleBeforeSave  for {} started ",ProdOfOffer.class.getSimpleName());
        checker.assertOwnership(prodOfOffer.getOffer().getStoreAccount().getUserName());
    }



}

