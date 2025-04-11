//package com.s3rha.spring.entityListener;
//
//import com.s3rha.spring.entity.Price;
//import jakarta.persistence.PreRemove;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class PriceEntityListener {
//    @PreRemove
//    private void beforeRemove(Price price) {
//        log.warn("PreRemove for {} started", Price.class.getSimpleName());
//
//        if (price.getProduct() != null) {
//            price.getProduct().getPrices().remove(price);
//        }
//
//    }
//}
