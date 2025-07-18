package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.*;
import com.s3rha.spring.entity.projections.StoreAccountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;
@RepositoryRestResource(

        excerptProjection = StoreAccountProjection.class
)
public interface StoreAccountRepo extends JpaRepository <StoreAccount,Long>{
    Optional<StoreAccount> findByUserName(@Param("userName" )String userName) ;





    List<StoreAccountProjection> findAllProjectedBy();
    Optional<StoreAccountProjection> findProjectedByAccountId(Long accountId);
    Page<StoreAccountProjection> findAllProjectedBy(Pageable pageable);






//    Optional<StoreAccount> findBySpecialOfferListContaining(SpecialOffer specialOffer);
//
//    Optional<StoreAccount> findByRatingOnStoreListContaining(RatingOnStore ratingOnStore);
//    Optional<StoreAccount> findByAddressListContaining(Address address);
//    Optional<StoreAccount> findByPriceListContaining(Price price);
//    Optional<StoreAccount> findBySocialMediaContaining(SocialMedia socialMedia);
//    Optional<StoreAccount> findByRatingOnProductListContaining(RatingOnProduct ratingOnProduct);

    List<StoreAccount> findByName(String name ) ;
//    @Override
//    @RestResource(exported = false)
//    void delete(StoreAccount entity);
//
//    @Override
//    @RestResource(exported = false)
//    void deleteById(Long id);

}
