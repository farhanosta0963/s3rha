package com.s3rha.spring.controller.projection;


import com.s3rha.spring.DAO.StoreAccountRepo;
import com.s3rha.spring.entity.projections.StoreAccountProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreAccountProjectionController {

    private final StoreAccountRepo storeAccountRepo;

//    @GetMapping("/storeAccounts")
//    public List<StoreAccountProjection> getAll() {
//        return storeAccountRepo.findAllProjectedBy() ;
//    }


    @GetMapping("/storeAccounts")
    public Page<StoreAccountProjection> getAll(Pageable pageable) {
        return storeAccountRepo.findAllProjectedBy(pageable);
    }


    @GetMapping("/storeAccounts/{id}")
    public ResponseEntity<StoreAccountProjection> getProjected(@PathVariable Long id) {
        return storeAccountRepo.findProjectedByAccountId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//
//    @GetMapping("/products/{id}")
//    public ResponseEntity<ProductProjection> getProjected(@PathVariable Long id) {
//        return productRepo.findById(id)
//                .map(product -> projectionFactory.createProjection(ProductProjection.class, product))
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
}
