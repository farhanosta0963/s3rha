package com.s3rha.spring.DAO;





import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//@RepositoryRestResource(
//
//        excerptProjection = ProductProjection.class
//)
public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findByAccount(Account account);
    List<Product> findByProductIdIn(List<Long> ids);

    Optional<Product> findByBarCode(String  barCode);


    List<Product> findByCategory(String category);

//    List<Product> findByIdIn(@Param("ids") List<Long> ids);
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
