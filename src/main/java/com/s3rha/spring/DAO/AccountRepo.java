package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository <Account,Long>{
    Optional<Account> findByUserName(String userName) ;


//    Optional<Account> findByEmail(String email);
}
