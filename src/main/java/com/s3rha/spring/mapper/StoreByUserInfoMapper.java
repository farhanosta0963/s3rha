package com.s3rha.spring.mapper;

import com.s3rha.spring.dto.StoreAccountByUserRegistrationDto;
import com.s3rha.spring.entity.StoreAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreByUserInfoMapper {
    public StoreAccount convertToEntity(StoreAccountByUserRegistrationDto storeAccountByUserRegistrationDto) {
        StoreAccount storeAccountByUser  = new StoreAccount();
        storeAccountByUser.setImage(storeAccountByUserRegistrationDto.image());
        storeAccountByUser.setPhoneNumber(storeAccountByUserRegistrationDto.phoneNumber());
        storeAccountByUser.setName(storeAccountByUserRegistrationDto.name());
        return storeAccountByUser ;
    }
}
