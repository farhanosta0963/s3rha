package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.StoreAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface StoreAccountRepo extends JpaRepository <StoreAccount,Long>{
    StoreAccount findByUserName(@Param("userName" )String userName) ;

}
