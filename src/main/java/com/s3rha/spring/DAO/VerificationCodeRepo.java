package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface VerificationCodeRepo extends JpaRepository<VerificationCode,Long> {
    Optional<VerificationCode> findByVerificationCode(String verificationCode);
    Optional<VerificationCode> findByAccount(Account account);



}
