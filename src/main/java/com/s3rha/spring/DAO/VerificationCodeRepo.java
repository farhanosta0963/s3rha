package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepo extends JpaRepository<VerificationCode,Long> {
    void deleteByAccount(Account account);

}
