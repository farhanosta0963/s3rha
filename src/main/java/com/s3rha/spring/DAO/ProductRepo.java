package com.s3rha.spring.DAO;



import com.s3rha.spring.entity.Product;
import com.s3rha.spring.entity.ProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = ProductProjection.class)

public interface ProductRepo extends JpaRepository <Product,Long>{
}
