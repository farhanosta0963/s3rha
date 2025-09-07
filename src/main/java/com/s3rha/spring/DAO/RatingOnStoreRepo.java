package com.s3rha.spring.DAO;


import com.s3rha.spring.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface RatingOnStoreRepo extends JpaRepository <RatingOnStore,Long>{
//    @Override
//    @RestResource(exported = false)
//    void delete(RatingOnStore entity);
//TODO

//    @Override
//    @RestResource(exported = false)
//    void deleteById(Long id);

    List<RatingOnStore> findByStoreAccount(StoreAccount storeAccount);


}
