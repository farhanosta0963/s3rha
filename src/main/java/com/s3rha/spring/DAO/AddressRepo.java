package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository <Address,Long>{
}
