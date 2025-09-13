package com.s3rha.spring.mapper;


import com.s3rha.spring.dto.StoreAccountByUserWithAddressAndPriceDto;
import com.s3rha.spring.entity.StoreAccount;
import com.s3rha.spring.entity.StorePrice;
import com.s3rha.spring.entity.UserPrice;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorePriceInfoMapper {
    public StorePrice convertToEntity(StoreAccountByUserWithAddressAndPriceDto storeAccountByUserRegistrationDto) {
//        StoreAccount storeAccountByUser  = new StoreAccount();
//        storeAccountByUser.setImage(storeAccountByUserRegistrationDto.image());
//        storeAccountByUser.setPhoneNumber(storeAccountByUserRegistrationDto.phoneNumber());
//        storeAccountByUser.setName(storeAccountByUserRegistrationDto.name());
//        return storeAccountByUser ;

        StorePrice storePrice = new StorePrice();
        storePrice.setPrice(storeAccountByUserRegistrationDto.price());
        storePrice.setUnitOfMeasure(storeAccountByUserRegistrationDto.unitOfMeasure());
        storePrice.setCurrency(storeAccountByUserRegistrationDto.currency());

        return storePrice;
    }
}
