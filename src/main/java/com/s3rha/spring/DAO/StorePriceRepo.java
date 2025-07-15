package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.StoreAccount;
import com.s3rha.spring.entity.StorePrice;
import com.s3rha.spring.entity.UserAccount;
import com.s3rha.spring.entity.UserPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface StorePriceRepo extends JpaRepository <StorePrice,Long>{
//    List<StorePrice> findByStoreAccount(StoreAccount storeAccount);

    @Override
    @RestResource(exported = false)
    void delete(StorePrice entity);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);
}
