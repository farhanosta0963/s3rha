package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepo extends JpaRepository <Price,Long>{
}
