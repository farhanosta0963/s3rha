//package com.s3rha.spring.entityListener;
//
//import com.s3rha.spring.DAO.ShoppingCartRepo;
//import com.s3rha.spring.entity.*;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PreRemove;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//
//
//
//import com.s3rha.spring.entity.VerificationCode;
//import jakarta.persistence.PreRemove;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//@RequiredArgsConstructor
//@Slf4j
//public class UserAccountEntityListener {
//
//
//    @PreRemove
//    private void beforeRemove(UserAccount userAccount) {
//        log.warn("PreRemove for {} started", UserAccount.class.getSimpleName());
////
////        if (userAccount.getShoppingCartList() != null) {
////            userAccount.getShoppingCartList().forEach(R->entityManager.remove(R));
////        }
////        if (userAccount.getUserPriceList() != null) {
////            userAccount.getUserPriceList().forEach(R -> R.setUserAccount(null));
////        }
//
//
//
//
//
//    }
//}
