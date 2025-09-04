package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.StoreReferenceByUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreReferenceByUserRepo extends JpaRepository<StoreReferenceByUser,Long>  {

}
