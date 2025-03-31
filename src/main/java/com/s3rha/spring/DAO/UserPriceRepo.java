package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.UserPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface UserPriceRepo extends JpaRepository <UserPrice,Long>{
    public UserPrice findByprice (BigDecimal price) ;
}
