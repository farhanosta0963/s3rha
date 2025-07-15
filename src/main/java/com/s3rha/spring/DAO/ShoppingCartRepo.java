package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.ProdOfCart;
import com.s3rha.spring.entity.ShoppingCart;
import com.s3rha.spring.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepo extends JpaRepository <ShoppingCart,Long>{
//    Optional<ShoppingCart> findByProdOfCartListContaining(ProdOfCart prodOfCart);




//    List<ShoppingCart> findByUserAccount(@Param("userAccount") UserAccount userAccount);

//    @Transactional
//    @Override
//    void delete(ShoppingCart entity);
//@Transactional
//    @Override
//    void deleteById(Long id);
}
