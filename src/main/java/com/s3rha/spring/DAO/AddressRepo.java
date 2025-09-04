package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.Address;
import com.s3rha.spring.entity.StoreAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository <Address,Long>{
    List<Address> findByStoreAccount(StoreAccount storeAccount);

}
