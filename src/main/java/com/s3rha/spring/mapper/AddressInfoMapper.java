package com.s3rha.spring.mapper;

import com.s3rha.spring.dto.StoreAccountByUserWithAddressAndPriceDto;
import com.s3rha.spring.entity.Address;
import com.s3rha.spring.entity.StoreAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AddressInfoMapper {
    public Address convertToEntity(StoreAccountByUserWithAddressAndPriceDto storeAccountByUserRegistrationDto) {
//        StoreAccount storeAccountByUser  = new StoreAccount();
//        storeAccountByUser.setImage(storeAccountByUserRegistrationDto.image());
//        storeAccountByUser.setPhoneNumber(storeAccountByUserRegistrationDto.phoneNumber());
//        storeAccountByUser.setName(storeAccountByUserRegistrationDto.name());
//        return storeAccountByUser ;
        Address address = new Address();
        address.setLatitude(storeAccountByUserRegistrationDto.latitude());
        address.setLongitude(storeAccountByUserRegistrationDto.longitude());
        address.setAddress(storeAccountByUserRegistrationDto.address());
        return address;
    }
}
