package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Price;
import com.s3rha.spring.entity.ProdOfCart;
import com.s3rha.spring.entity.ProdOfOffer;
import com.s3rha.spring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdOfOfferRepo extends JpaRepository <ProdOfOffer,Long>{
    List<ProdOfOffer> findByProduct(Product product) ;

}
