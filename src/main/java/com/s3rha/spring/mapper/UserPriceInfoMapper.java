package com.s3rha.spring.mapper;

import com.s3rha.spring.dto.StoreAccountByUserWithAddressAndPriceDto;
import com.s3rha.spring.entity.StoreAccount;
import com.s3rha.spring.entity.UserPrice;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPriceInfoMapper {
    public UserPrice convertToEntity(StoreAccountByUserWithAddressAndPriceDto storeAccountByUserRegistrationDto) {
//        StoreAccount storeAccountByUser  = new StoreAccount();
//        storeAccountByUser.setImage(storeAccountByUserRegistrationDto.image());
//        storeAccountByUser.setPhoneNumber(storeAccountByUserRegistrationDto.phoneNumber());
//        storeAccountByUser.setName(storeAccountByUserRegistrationDto.name());
//        return storeAccountByUser ;
        UserPrice userPrice= new UserPrice() ;
        userPrice.setPrice(storeAccountByUserRegistrationDto.price());
        userPrice.setUnitOfMeasure(storeAccountByUserRegistrationDto.unitOfMeasure());
        userPrice.setCurrency(storeAccountByUserRegistrationDto.currency());
        return userPrice;
    }
}
