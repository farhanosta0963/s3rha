package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByAccount(Account account);
}