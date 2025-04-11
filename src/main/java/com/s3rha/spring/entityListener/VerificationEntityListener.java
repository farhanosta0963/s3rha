//package com.s3rha.spring.entityListener;
//
//import com.s3rha.spring.entity.VerificationCode;
//import jakarta.persistence.PreRemove;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class VerificationEntityListener {
//    @PreRemove
//    private void beforeRemove(VerificationCode verificationCode) {
//        log.warn("PreRemove for {} started", VerificationCode.class.getSimpleName());
//
//        if (verificationCode.getAccount() != null) {
//            verificationCode.getAccount().setVerificationCode(null);
//        }
//    }
//}
