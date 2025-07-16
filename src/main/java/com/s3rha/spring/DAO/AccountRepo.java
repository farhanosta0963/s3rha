package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.*;
import com.s3rha.spring.projections.AccountPublicProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
@RepositoryRestResource(

        excerptProjection = AccountPublicProjection.class
)
public interface AccountRepo extends JpaRepository <Account,Long>{
   public Optional<Account> findByUserName(String userName) ;

    Optional<Account> findByEmail(String email);
    Optional<Account> findByOauthId(BigInteger oauthId);

//
//    // prevent default /accounts/{id}
//    @Override
//    @RestResource(exported = false)
//    Optional<Account> findById(Long aLong);
//
//    //  Disable list: GET /accounts
//    @Override
//    @RestResource(exported = false)
//    List<Account> findAll();

//    Optional<Account> findByProductListContaining(Product product);

//
//    Optional<Account> findByRatingListContaining(Rating rating);
//    Optional<Account> findByRefreshTokenListContaining(RefreshToken refreshToken);
//    Optional<Account> findByReportListContaining(Report report);
//    Optional<Account> findByReportOnAccountListContaining(ReportOnAccount reportOnAccount);
//    Optional<Account> findBySearchHistoryListContaining(SearchHistory searchHistory);

//    Optional<Account> findByEmail(String email);
}
