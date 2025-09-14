package com.s3rha.spring.DAO;





import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//@RepositoryRestResource(
//
//        excerptProjection = ProductProjection.class
//)
public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findByAccount(Account account);
//    List<Product> findByProductIdIn(List<Long> ids);
    Page<Product> findByProductIdIn(List<Long> ids, Pageable pageable);
    Optional<Product> findByBarCode(String  barCode);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(String name, String category, Pageable pageable);
    List<Product> findByCategory(String category);
    // Or dynamic category (optional)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND (:category IS NULL OR LOWER(p.category) = LOWER(:category))")
    Page<Product> searchByNameAndOptionalCategory(@Param("name") String name,
                                                  @Param("category") String category,
                                                  Pageable pageable);
    List<Product> findByCategoryIgnoreCase(String category);

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
