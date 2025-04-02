package com.s3rha.spring.service;

import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.DAO.StoreAccountRepo;
import com.s3rha.spring.config.jwtAuth.JwtTokenGenerator;
import com.s3rha.spring.dto.*;

import com.s3rha.spring.entity.Account;


import com.s3rha.spring.entity.RefreshToken;
import com.s3rha.spring.entity.StoreAccount;
import com.s3rha.spring.mapper.StoreByUserInfoMapper;
import com.s3rha.spring.mapper.StoreInfoMapper;
import com.s3rha.spring.mapper.UserInfoMapper;
import com.s3rha.spring.repo.RefreshTokenRepo;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Store;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;



@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AccountRepo userInfoRepo;
    private final StoreAccountRepo storeInfoRepo;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserInfoMapper userInfoMapper;
    private final StoreInfoMapper storeInfoMapper;
    private final StoreByUserInfoMapper storeByUserInfoMapper;
    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try
        {
            var userInfoEntity = userInfoRepo.findByUserName(authentication.getName())
                    .orElseThrow(()->{
                        log.error("[AuthService:userSignInAuth] User :{} not found",authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");});


            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);
            //Let's save the refreshToken as well
            saveUserRefreshToken(userInfoEntity,refreshToken);
            //Creating the cookie
            creatRefreshTokenCookie(response,refreshToken);
            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated",userInfoEntity.getUserName());
            return  AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(userInfoEntity.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
    }

    private void saveUserRefreshToken(Account userInfoEntity, String refreshToken) {
        var refreshTokenEntity = RefreshToken.builder()
                .account(userInfoEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
    }

    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {

        if(!authorizationHeader.startsWith(TokenType.Bearer.name())){
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.
        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(tokens-> !tokens.isRevoked())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));

        Account userInfoEntity = refreshTokenEntity.getAccount();

        //Now create the Authentication object
        Authentication authentication =  createAuthenticationObject(userInfoEntity);

        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return  AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(userInfoEntity.getUserName())
                .tokenType(TokenType.Bearer)
                .build();
    }

    private static Authentication createAuthenticationObject(Account userInfoEntity) {
        // Extract user details from UserDetailsEntity
        String username = userInfoEntity.getUserName();
        String password = userInfoEntity.getPassword();
        String roles = userInfoEntity.getRoles();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    public AuthResponseDto registerUser(UserAccountRegistrationDto userRegistrationDto,
                                        HttpServletResponse httpServletResponse) {
        try{
            log.info("[AuthService:registerUser]User Registration Started with :::{}",userRegistrationDto);

            Optional<Account> user = userInfoRepo.findByUserName(userRegistrationDto.userName());
            if(user.isPresent()){
                throw new Exception("User Already Exist");
            }

            Account userDetailsEntity = userInfoMapper.convertToEntity(userRegistrationDto);
            Authentication authentication = createAuthenticationObject(userDetailsEntity);


            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            Account savedUserDetails = userInfoRepo.save(userDetailsEntity);
            saveUserRefreshToken(userDetailsEntity,refreshToken);

            creatRefreshTokenCookie(httpServletResponse,refreshToken);

            log.info("[AuthService:registerUser] User:{} Successfully registered",savedUserDetails.getUserName());
            return   AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .userName(savedUserDetails.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
    public AuthResponseDto registerStore(StoreAccountRegistrationDto storeAccountRegistrationDto,
                                         HttpServletResponse httpServletResponse) {
        try{
            log.info("[AuthService:registerUser]Store Registration Started with :::{}",storeAccountRegistrationDto);

            Optional<Account> user = userInfoRepo.findByUserName(storeAccountRegistrationDto.userName());
            if(user.isPresent()){
                throw new Exception("User Already Exist");
            }

            StoreAccount userDetailsEntity = storeInfoMapper.convertToEntity(storeAccountRegistrationDto);
            Authentication authentication = createAuthenticationObject(userDetailsEntity);


            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            StoreAccount savedUserDetails = storeInfoRepo.save(userDetailsEntity);
            saveUserRefreshToken(userDetailsEntity,refreshToken);

            creatRefreshTokenCookie(httpServletResponse,refreshToken);

            log.info("[AuthService:registerStore] Store:{} Successfully registered",savedUserDetails.getUserName());
            return   AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .userName(savedUserDetails.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    public StoreAccount registerStoreByUser(@Valid StoreAccountByUserRegistrationDto storeAccountByUserRegistrationDto, HttpServletResponse httpServletResponse) {
        try{
            log.info("[AuthService:registerUser]Store  byyyy User Registration Started with :::{}",storeAccountByUserRegistrationDto);


            StoreAccount userDetailsEntity = storeByUserInfoMapper.convertToEntity(storeAccountByUserRegistrationDto);

            StoreAccount savedUserDetails = storeInfoRepo.save(userDetailsEntity);

            userDetailsEntity.setAccountId(savedUserDetails.getAccountId());

            return userDetailsEntity  ;


        }catch (Exception e){
            log.error("[AuthService:registerStoreByUser]Exception while registering the store byyy user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
}
