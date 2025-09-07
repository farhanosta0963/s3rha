package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.Product;
import com.s3rha.spring.entity.RatingOnProduct;
import com.s3rha.spring.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface RatingOnProductRepo extends JpaRepository <RatingOnProduct,Long>{
//    @Override
//    @RestResource(exported = false)
//    void delete(RatingOnProduct entity);
//
//    @Override
//    @RestResource(exported = false)
//    void deleteById(Long id);

    List<RatingOnProduct> findByProduct(Product product);

}
