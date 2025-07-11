package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

public interface StoreAccountRepo extends JpaRepository <StoreAccount,Long>{
    Optional<StoreAccount> findByUserName(@Param("userName" )String userName) ;
    Optional<StoreAccount> findBySpecialOfferListContaining(SpecialOffer specialOffer);

    Optional<StoreAccount> findByRatingOnStoreListContaining(RatingOnStore ratingOnStore);
    Optional<StoreAccount> findByAddressListContaining(Address address);
    Optional<StoreAccount> findByPriceListContaining(Price price);
    Optional<StoreAccount> findBySocialMediaContaining(SocialMedia socialMedia);
    Optional<StoreAccount> findByRatingOnProductListContaining(RatingOnProduct ratingOnProduct);

    List<StoreAccount> findByName(String name ) ;
//    @Override
//    @RestResource(exported = false)
//    void delete(StoreAccount entity);
//
//    @Override
//    @RestResource(exported = false)
//    void deleteById(Long id);

}
