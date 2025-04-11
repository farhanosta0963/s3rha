package com.s3rha.spring.mapper;


import com.s3rha.spring.dto.UserAccountRegistrationDto;
import com.s3rha.spring.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserInfoMapper {

    private final PasswordEncoder passwordEncoder;
    public UserAccount convertToEntity(UserAccountRegistrationDto userRegistrationDto) {
        UserAccount   userInfoEntity = new UserAccount();
        userInfoEntity.setUserName(userRegistrationDto.userName());
        userInfoEntity.setFname(userRegistrationDto.fname());
        userInfoEntity.setLname(userRegistrationDto.lname());
        userInfoEntity.setEmail(userRegistrationDto.email());
        userInfoEntity.setImage(userRegistrationDto.image());
        userInfoEntity.setPhoneNumber(userRegistrationDto.phoneNumber());
        userInfoEntity.setPassword(passwordEncoder.encode(userRegistrationDto.password()));
        return userInfoEntity;
    }
}

