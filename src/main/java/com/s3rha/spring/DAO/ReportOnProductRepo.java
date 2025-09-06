package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.ReportOnProduct;
import com.s3rha.spring.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface ReportOnProductRepo extends JpaRepository <ReportOnProduct,Long>{
//    @Override
//    @RestResource(exported = false)
//    void delete(ReportOnProduct entity);
//
//    @Override
//    @RestResource(exported = false)
//    void deleteById(Long id);
}
