package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.StoreAccount;
import com.s3rha.spring.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

public interface StoreAccountRepo extends JpaRepository <StoreAccount,Long>{
    Optional<StoreAccount> findByUserName(@Param("userName" )String userName) ;
//    @Override
//    @RestResource(exported = false)
//    void delete(StoreAccount entity);
//
//    @Override
//    @RestResource(exported = false)
//    void deleteById(Long id);

}
