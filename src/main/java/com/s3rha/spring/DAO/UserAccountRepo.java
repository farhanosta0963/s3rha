package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.ShoppingCart;
import com.s3rha.spring.entity.UserAccount;
import com.s3rha.spring.entity.UserPrice;
import com.s3rha.spring.entity.projections.StoreAccountProjection;
import com.s3rha.spring.entity.projections.UserAccountProjection;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepo extends JpaRepository <UserAccount,Long>{
    List<UserAccount> findByFname(String fname );
    Optional<UserAccount> findByUserName( String userName) ;


    List<UserAccountProjection> findAllProjectedBy();
    Optional<UserAccountProjection> findProjectedByAccountId(Long accountId);
    Page<UserAccountProjection> findAllProjectedBy(Pageable pageable);



//    Optional<UserAccount> findByShoppingCartListContaining(ShoppingCart shoppingCart);

//    Optional<UserAccount> findByUserPriceListContaining(UserPrice userPrice);

//    @Override
//    @RestResource(exported = false)
//    void delete(UserAccount entity);
//
//    @Override
//    @RestResource(exported = false)
//    void deleteById(Long id);
}
