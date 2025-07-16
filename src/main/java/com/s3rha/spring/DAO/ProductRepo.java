package com.s3rha.spring.DAO;



import com.s3rha.spring.entity.Product;
//import com.s3rha.spring.entity.ProductProjection;
import com.s3rha.spring.projections.AccountPublicProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

//@RepositoryRestResource(excerptProjection = ProductProjection.class)
//@RepositoryRestResource(
//
//        excerptProjection = productProjection.class
//)
public interface ProductRepo extends JpaRepository <Product,Long>{
}
