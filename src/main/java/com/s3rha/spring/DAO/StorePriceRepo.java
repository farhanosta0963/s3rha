package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.StorePrice;
import com.s3rha.spring.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface StorePriceRepo extends JpaRepository <StorePrice,Long>{
    @Override
    @RestResource(exported = false)
    void delete(StorePrice entity);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);
}
