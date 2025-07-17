//package com.s3rha.spring.controller.projection;
//
//import com.s3rha.spring.DAO.ProductRepo;
//import com.s3rha.spring.projections.ProductProjection;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api")
//public class ProductProjectionController {
//
//    private final ProductRepo productRepo;
//
//    @GetMapping("/products")
//    public List<ProductProjection> getAll() {
//        return productRepo.findAllProjectedBy() ;
//    }
//
//    @GetMapping("/products/{id}")
//    public ResponseEntity<ProductProjection> getProjected(@PathVariable Long id) {
//        return productRepo.findProjectedByProductId(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
////
////    @GetMapping("/products/{id}")
////    public ResponseEntity<ProductProjection> getProjected(@PathVariable Long id) {
////        return productRepo.findById(id)
////                .map(product -> projectionFactory.createProjection(ProductProjection.class, product))
////                .map(ResponseEntity::ok)
////                .orElse(ResponseEntity.notFound().build());
////    }
//}
