package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.UserAccount;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepo extends JpaRepository <UserAccount,Long>{
    List<UserAccount> findByFname(@Param("fname") String fname );
    Optional<UserAccount> findByUserName(@Param("userName") String userName) ;


}
