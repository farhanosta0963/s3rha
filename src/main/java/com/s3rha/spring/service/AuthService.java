package com.s3rha.spring.service;

import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.DAO.VerificationCodeRepo;
import com.s3rha.spring.DAO.StoreAccountRepo;
import com.s3rha.spring.config.jwtAuth.JwtTokenGenerator;
import com.s3rha.spring.config.user.UserInfoConfig;
import com.s3rha.spring.dto.*;

import com.s3rha.spring.entity.*;


import com.s3rha.spring.mapper.StoreByUserInfoMapper;
import com.s3rha.spring.mapper.StoreInfoMapper;
import com.s3rha.spring.mapper.UserInfoMapper;
import com.s3rha.spring.repo.RefreshTokenRepo;

import jakarta.mail.MessagingException;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;


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
    private final EmailService emailService;
    private final VerificationCodeRepo verificationCodeRepo ;

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
            List<Account> user2 = userInfoRepo.findByEmail(userRegistrationDto.email());
            if(user2.size()>0){
                throw new Exception("User with this Email  Already Exist");
            }

            UserAccount userDetailsEntity = userInfoMapper.convertToEntity(userRegistrationDto);

            Authentication authentication = createAuthenticationObject(userDetailsEntity);


            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            userDetailsEntity.setStatus(AccountStatus.PENDING.name());
            Account savedUserDetails = userInfoRepo.save(userDetailsEntity);
            saveUserRefreshToken(savedUserDetails,refreshToken);

            savedUserDetails.setVerificationCode(saveUserVerificationCode(savedUserDetails
                    ,generateVerificationCode()
                    ,LocalDateTime.now().plusMinutes(15)));
            sendVerificationEmail(savedUserDetails);

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


    public void verifyUser(VerifyUserDto input) {
        List<Account> optionalUser = userInfoRepo.findByEmail(input.getEmail());
        if (optionalUser.size()>0) {
            Account user = optionalUser.get(0);

            if (user.getVerificationCode().getVerificationCodeExpireTime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().getVerificationCode().equals(input.getVerificationCode())) {
                user.setStatus(AccountStatus.ACTIVATED.name());
                verificationCodeRepo.delete(user.getVerificationCode());
                user.setVerificationCode(null);
                userInfoRepo.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) throws MessagingException {
        List<Account> optionalUser = userInfoRepo.findByEmail(email);
        if (optionalUser.size()>0) {
            Account user = optionalUser.get(0);
            if (user.getStatus().equals(AccountStatus.ACTIVATED.name())) {
                throw new RuntimeException("Account is already verified");
            }
            saveUserVerificationCode(user,
                                    generateVerificationCode(),
                                    LocalDateTime.now().plusMinutes(15));

            sendVerificationEmail(user);


        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendVerificationEmail(Account user) throws MessagingException {
        String subject = "Account Verification";

        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode().getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app S3rha !</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);


    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    public AuthResponseDto registerStore(StoreAccountRegistrationDto storeAccountRegistrationDto,
                                         HttpServletResponse httpServletResponse) throws Exception {


            log.info("[AuthService:registerUser]Store Registration Started with :::{}",storeAccountRegistrationDto);


            Optional<Account> user = userInfoRepo.findByUserName(storeAccountRegistrationDto.userName());
            if(user.isPresent()){
                throw new Exception("user name  Already Exist");
            }


            List<Account> user2 = userInfoRepo.findByEmail(storeAccountRegistrationDto.email());
            if(user2.size()>0){
                throw new Exception("Account with this Email  Already Exist");
            }


            StoreAccount userDetailsEntity = storeInfoMapper.convertToEntity(storeAccountRegistrationDto);

            Authentication authentication = createAuthenticationObject( userDetailsEntity);



            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);


            userDetailsEntity.setStatus(AccountStatus.PENDING.name());


            StoreAccount savedUserDetails = storeInfoRepo.save(userDetailsEntity);
            saveUserRefreshToken(savedUserDetails,refreshToken);


            savedUserDetails.setVerificationCode(saveUserVerificationCode(savedUserDetails
                    ,generateVerificationCode()
                    ,LocalDateTime.now().plusMinutes(15)));
            sendVerificationEmail(savedUserDetails);


            creatRefreshTokenCookie(httpServletResponse,refreshToken);


            log.info("[AuthService:registerStore] Store:{} Successfully registered",savedUserDetails.getUserName());
            return   AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .userName(savedUserDetails.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();



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
    private VerificationCode saveUserVerificationCode(Account userInfoEntity, String verificationCode, LocalDateTime dateTime) {
        VerificationCode verificationCodeEntity = VerificationCode.builder()
                .account(userInfoEntity)
                .VerificationCode(verificationCode)
                .VerificationCodeExpireTime(dateTime)
                .build();
       return verificationCodeRepo.save(verificationCodeEntity);
    }
}


