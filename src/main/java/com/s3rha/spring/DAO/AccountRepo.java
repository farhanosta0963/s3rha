package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepo extends JpaRepository <Account,Long>{
   public Optional<Account> findByUserName(String userName) ;

    public  List<Account> findByEmail(String email);


//    Optional<Account> findByEmail(String email);
}
