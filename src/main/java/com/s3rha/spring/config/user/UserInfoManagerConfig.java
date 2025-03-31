package com.s3rha.spring.config.user;

import com.s3rha.spring.DAO.AccountRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserInfoManagerConfig implements UserDetailsService {

    private final AccountRepo userInfoRepo;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userInfoRepo
                .findByUserName(userName)
                .map(UserInfoConfig::new)
                .orElseThrow(()-> new UsernameNotFoundException("UserEmail: "+userName+" does not exist"));
    }
}
