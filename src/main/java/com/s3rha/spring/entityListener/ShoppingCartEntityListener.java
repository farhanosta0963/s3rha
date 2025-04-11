//package com.s3rha.spring.entityListener;
//import com.s3rha.spring.DAO.ProdOfCartRepo;
//import com.s3rha.spring.entity.ShoppingCart;
//import jakarta.persistence.PreRemove;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@Slf4j
//public class ShoppingCartEntityListener {
//
//
//    @PreRemove
//    private void beforeRemove(ShoppingCart cart) {
//        log.warn("PreRemove for {} started", ShoppingCart.class.getSimpleName());
//
//
//
////        // Remove cart from user's collection
////        if (cart.getUserAccount() != null) {
////            cart.getUserAccount().getShoppingCartList().remove(cart);
////        }
////        if (cart.getProdOfCartList() != null) {
////            cart.getProdOfCartList().forEach(R->prodOfCartRepo.delete(R));
////        }
//
//
//    }
//}