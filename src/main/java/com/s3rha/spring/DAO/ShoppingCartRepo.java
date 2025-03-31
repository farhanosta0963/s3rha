package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShoppingCartRepo extends JpaRepository <ShoppingCart,Long>{

}
