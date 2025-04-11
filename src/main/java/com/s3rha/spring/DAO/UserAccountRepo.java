package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.UserAccount;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepo extends JpaRepository <UserAccount,Long>{
    List<UserAccount> findByFname(String fname );
    Optional<UserAccount> findByUserName( String userName) ;

//    @Override
//    @RestResource(exported = false)
//    void delete(UserAccount entity);
//
//    @Override
//    @RestResource(exported = false)
//    void deleteById(Long id);
}
