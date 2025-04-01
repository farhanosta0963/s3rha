package com.s3rha.spring.mapper;

import com.s3rha.spring.dto.UserRegistrationDto;
import com.s3rha.spring.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserInfoMapper {

    private final PasswordEncoder passwordEncoder;
    public Account convertToEntity(UserRegistrationDto userRegistrationDto) {
        Account userInfoEntity = new Account();
        userInfoEntity.setUserName(userRegistrationDto.userName());
        userInfoEntity.setEmail(userRegistrationDto.userEmail());
        userInfoEntity.setPhoneNumber(userRegistrationDto.userMobileNo());
        userInfoEntity.setRoles(userRegistrationDto.userRole());
        userInfoEntity.setPassword(passwordEncoder.encode(userRegistrationDto.userPassword()));
        return userInfoEntity;
    }
}

