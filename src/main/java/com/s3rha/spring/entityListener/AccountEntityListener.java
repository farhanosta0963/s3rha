//package com.s3rha.spring.entityListener;
//
//import com.s3rha.spring.entity.Account;
//import com.s3rha.spring.entity.StoreAccount;
//import com.s3rha.spring.entity.VerificationCode;
//import jakarta.persistence.PreRemove;
//import lombok.extern.slf4j.Slf4j;
//
//
//
//
//import com.s3rha.spring.entity.VerificationCode;
//import jakarta.persistence.PreRemove;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class AccountEntityListener {
//    @PreRemove
//    private void beforeRemove(Account account) {
//        log.warn("PreRemove for {} started", Account.class.getSimpleName());
//
////        if (account.getVerificationCode() != null) {
////            account.getVerificationCode().setAccount(null);
////        }
////
//
//
//    }
//}
