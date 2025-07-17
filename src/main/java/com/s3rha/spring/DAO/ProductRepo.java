package com.s3rha.spring.DAO;



import com.s3rha.spring.entity.Product;
//import com.s3rha.spring.entity.ProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;

//@RepositoryRestResource(
//
//        excerptProjection = ProductProjection.class
//)
public interface ProductRepo extends JpaRepository <Product,Long>{

//    // This will return projection directly
//    List<ProductProjection> findAllProjectedBy();
//    Optional<ProductProjection> findProjectedByProductId(Long productId);
//    Page<ProductProjection> findAllProjectedBy(Pageable pageable);
//
//    //  Disable list: GET /accounts
//    @Override
//    @RestResource(exported = false)
//    List<Product> findAll();
}
