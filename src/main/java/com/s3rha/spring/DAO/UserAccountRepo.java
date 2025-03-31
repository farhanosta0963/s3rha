package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAccountRepo extends JpaRepository <UserAccount,Long>{
    List<UserAccount> findByFname(@Param("fname") String fname );
    UserAccount findByUserName(@Param("userName") String userName) ;


}
