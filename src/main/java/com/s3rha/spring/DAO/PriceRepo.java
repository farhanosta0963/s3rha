package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Price;
import com.s3rha.spring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepo extends JpaRepository <Price,Long>{
    List<Price> findByProduct(Product product) ;
}
