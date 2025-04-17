package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface AccountRepo extends JpaRepository <Account,Long>{
   public Optional<Account> findByUserName(String userName) ;

    Optional<Account> findByEmail(String email);
    Optional<Account> findByOauthId(BigInteger oauthId);
    Optional<Account> findByProductListContaining(Product product);


    Optional<Account> findByRatingListContaining(Rating rating);
    Optional<Account> findByRefreshTokenListContaining(RefreshToken refreshToken);
    Optional<Account> findByReportListContaining(Report report);
    Optional<Account> findByReportOnAccountListContaining(ReportOnAccount reportOnAccount);
    Optional<Account> findBySearchHistoryListContaining(SearchHistory searchHistory);

//    Optional<Account> findByEmail(String email);
}
