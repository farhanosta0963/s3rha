package com.s3rha.spring.mapper;

import com.s3rha.spring.dto.StoreAccountRegistrationDto;
import com.s3rha.spring.dto.UserAccountRegistrationDto;
import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.StoreAccount;
import com.s3rha.spring.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StoreInfoMapper {

    private final PasswordEncoder passwordEncoder;
    public StoreAccount convertToEntity(StoreAccountRegistrationDto storeAccountRegistrationDto) {
        StoreAccount storeAccount  = new StoreAccount();
        storeAccount.setUserName(storeAccountRegistrationDto.userName());
        storeAccount.setEmail(storeAccountRegistrationDto.email());
        storeAccount.setImage(storeAccountRegistrationDto.image());
        storeAccount.setPhoneNumber(storeAccountRegistrationDto.phoneNumber());
        storeAccount.setPassword(passwordEncoder.encode(storeAccountRegistrationDto.password()));
        storeAccount.setName(storeAccountRegistrationDto.name());
        return storeAccount ;
    }
}

