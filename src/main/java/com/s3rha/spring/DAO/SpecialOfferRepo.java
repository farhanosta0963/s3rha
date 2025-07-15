package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.ProdOfCart;
import com.s3rha.spring.entity.ProdOfOffer;
import com.s3rha.spring.entity.ShoppingCart;
import com.s3rha.spring.entity.SpecialOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialOfferRepo extends JpaRepository<SpecialOffer,Long> {
//    Optional<SpecialOffer> findByProdOfOfferListContaining(ProdOfOffer prodOfOffer);

}
