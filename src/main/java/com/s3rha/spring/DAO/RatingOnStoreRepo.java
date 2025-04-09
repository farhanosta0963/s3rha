package com.s3rha.spring.DAO;


import com.s3rha.spring.entity.RatingOnStore;
import com.s3rha.spring.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface RatingOnStoreRepo extends JpaRepository <RatingOnStore,Long>{
    @Override
    @RestResource(exported = false)
    void delete(RatingOnStore entity);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);
}
