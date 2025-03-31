package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.StorePrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorePriceRepo extends JpaRepository <StorePrice,Long>{
}
