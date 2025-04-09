package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.ReportOnAccount;
import com.s3rha.spring.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface ReportOnAccountRepo extends JpaRepository <ReportOnAccount,Long>{
    @Override
    @RestResource(exported = false)
    void delete(ReportOnAccount entity);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);
}
