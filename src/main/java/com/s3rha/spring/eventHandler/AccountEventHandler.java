//package com.s3rha.spring.eventHandler;
//
//import com.s3rha.spring.DAO.AccountRepo;
//import com.s3rha.spring.DAO.ShoppingCartRepo;
//import com.s3rha.spring.DAO.UserAccountRepo;
//import com.s3rha.spring.entity.Account;
//import com.s3rha.spring.entity.ShoppingCart;
//import com.s3rha.spring.entity.VerificationCode;
//import com.s3rha.spring.service.OwnershipChecker;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.rest.core.annotation.*;
//import org.springframework.stereotype.Component;
//
//@Transactional
//@Slf4j
//@Component
//@RepositoryEventHandler
//@RequiredArgsConstructor
//public class AccountEventHandler {
//    private final UserAccountRepo userRepository;
//    private final OwnershipChecker checker;
//    private final ShoppingCartRepo cartRepo;
//    private final AccountRepo accountRepo;
//
//
////  TODO disable in security the ability to modify on parent account or parent report and so on ....
//    @HandleBeforeDelete
//    public void beforeDelete(Account account) {
//        log.warn("HandleBeforeDelete for {} started ", Account.class.getSimpleName());
//        if (account.getVerificationCode() != null) {
//            account.getVerificationCode().setAccount(null) ;
//            accountRepo.save(account);
//        }
//
//    }
//
//
//
//}
