package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.UserAccount;
import com.s3rha.spring.entity.UserPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.math.BigDecimal;

public interface UserPriceRepo extends JpaRepository <UserPrice,Long>{
    public UserPrice findByprice (BigDecimal price) ;
    @Override
    @RestResource(exported = false)
    void delete(UserPrice entity);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);
}
