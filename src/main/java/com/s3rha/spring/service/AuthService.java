package com.s3rha.spring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s3rha.spring.DAO.*;
import com.s3rha.spring.config.jwtAuth.JwtTokenGenerator;
import com.s3rha.spring.dto.*;

import com.s3rha.spring.entity.*;


import com.s3rha.spring.mapper.*;
import com.s3rha.spring.DAO.RefreshTokenRepo;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Long.parseLong;


@Service
@RequiredArgsConstructor
@Slf4j

public class AuthService {
private  final JwtEncoder jwtEncoder ;
    private final AccountRepo accountRepo;
    private final UserAccountRepo userAccountRepo;
    private final StoreAccountRepo storeInfoRepo;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordResetTokenRepo passwordResetTokenRepo;
    private final UserInfoMapper userInfoMapper;
    private final StoreInfoMapper storeInfoMapper;
    private final StoreByUserInfoMapper storeByUserInfoMapper;
    private final EmailService emailService;
    private final VerificationCodeRepo verificationCodeRepo ;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Random random ;
    private final OwnershipChecker ownershipChecker;
    private final StoreReferenceByUserRepo storeReferenceByUserRepo;
    private final AddressInfoMapper addressInfoMapper;
    private final UserPriceInfoMapper userPriceInfoMapper;
    private final UserPriceRepo userPriceRepo;
    private final AddressRepo addressRepo;
    private final ProductRepo productRepo;
    @Transactional
    public AuthResponseDto  getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try
        {
            var userInfoEntity = accountRepo.findByUserName(authentication.getName())
                    .orElseThrow(()->{
                        AuthService.log.error("[AuthService:userSignInAuth] User :{} not found",authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");});


            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);
            //Let's save the refreshToken as well
            saveUserRefreshToken(userInfoEntity,refreshToken);
            //Creating the cookie
            createRefreshTokenCookie(response,refreshToken);
            AuthService.log.warn("[AuthService:userSignInAuth] Access token for user:{}, has been generated",userInfoEntity.getUserName());
            return  AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(userInfoEntity.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            AuthService.log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
    }
    @Transactional
    private void saveUserRefreshToken(Account userInfoEntity, String refreshToken) {
        var refreshTokenEntity = RefreshToken.builder()
                .refreshToken(refreshToken)
                .account(userInfoEntity)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
//        userInfoEntity.addRefreshToken(refreshTokenEntity);
//        accountRepo.save(userInfoEntity) ;

    }


    private void createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true) // Enable in production
                .maxAge(15 * 24 * 60 * 60) // 15 days
                .path("/auth/refresh-token")
                .build();
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

//    private void createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
//        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
//        refreshTokenCookie.setHttpOnly(true);
////        refreshTokenCookie.setSecure(true); TODO uncomment this for production
//        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
//          refreshTokenCookie .setPath("/api/refresh-token"); ;
//        response.addCookie(refreshTokenCookie);
////        return refreshTokenCookie;
//    }

    @Transactional
    public Object getAccessTokenUsingRefreshToken(HttpServletRequest httpServletRequest) {

        // 1. Extract refresh token from HttpOnly cookie (not headers!)
        String refreshToken = Arrays.stream(httpServletRequest.getCookies())
                .filter(c -> c.getName().equals("refresh_token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Refresh token missing"));






//        if(!authorizationHeader.startsWith(TokenType.Bearer.name())){
//            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
//        }
//
//        final String refreshToken = authorizationHeader.substring(7);

        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.
//        Principal principal = request.getUserPrincipal();

//        log(httpServletRequest.getUserPrincipal().getName()) ;
        RefreshToken refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(tokens-> !tokens.isRevoked())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));

        Account userInfoEntity = refreshTokenEntity.getAccount() ;
//                accountRepo.findByRefreshTokenListContaining(refreshTokenEntity)
//                .orElseThrow(() ->
//                        new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Account doesn't exist"));

//        Account userInfoEntity = refreshTokenEntity.getAccount();

        //Now create the Authentication object
        Authentication authentication =  createAuthenticationObject(userInfoEntity);

        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return  AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(15 * 60)
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

    @Transactional
    public AuthResponseDto registerUser(UserAccountRegistrationDto userRegistrationDto,
                                        HttpServletResponse httpServletResponse) {
        try{
            AuthService.log.warn("[AuthService:registerUser]User Registration Started with :::{}",userRegistrationDto);

            Optional<Account> user = accountRepo.findByUserName(userRegistrationDto.userName());
            if(user.isPresent()){
                throw new Exception("User Already Exist");
            }
            Optional<Account> user2 = accountRepo.findByEmail(userRegistrationDto.email());
            if(user2.isPresent()){
                throw new Exception("User with this Email  Already Exist");
            }

            UserAccount userDetailsEntity = userInfoMapper.convertToEntity(userRegistrationDto);

            Authentication authentication = createAuthenticationObject(userDetailsEntity);


            userDetailsEntity.setStatus(AccountStatus.PENDING.name());

            Account savedUserDetails = accountRepo.save(userDetailsEntity);
            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);


            saveUserRefreshToken(savedUserDetails,refreshToken);

            saveUserVerificationCode(
                    savedUserDetails
                    ,generateVerificationCode()
                    ,LocalDateTime.now().plusMinutes(15));
            log.warn("before sending ");

            sendVerificationEmail(savedUserDetails);
            log.warn("after  sending ");

            createRefreshTokenCookie(httpServletResponse,refreshToken);

            AuthService.log.warn("[AuthService:registerUser] User:{} Successfully registered"+savedUserDetails.getUserName());
            return   AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(savedUserDetails.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            AuthService.log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }


    public void verifyUser(VerifyUserDto input) {

        Optional<Account> optionalUser = accountRepo.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            Account user = optionalUser.get();
            VerificationCode verificationCode = verificationCodeRepo.findByVerificationCode(input.getVerificationCode())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            if (verificationCode.getVerificationCodeExpireTime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (verificationCode.getVerificationCode().equals(input.getVerificationCode())) {
                user.setStatus(AccountStatus.ACTIVATED.name());
                verificationCodeRepo.delete(verificationCode);
                accountRepo.save(user) ;
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }

//
//            List<Account> optionalUser = accountRepo.findByEmail(input.getEmail());
//            if (optionalUser.size()>0) {
//                Account user = optionalUser.get(0);
//
//                if (user.getVerificationCode().getVerificationCodeExpireTime().isBefore(LocalDateTime.now())) {
//                    throw new RuntimeException("Verification code has expired");
//                }
//                if (user.getVerificationCode().getVerificationCode().equals(input.getVerificationCode())) {
//                    user.setStatus(AccountStatus.ACTIVATED.name());
//                    user.setVerificationCode(null);
//                    verificationCodeRepo.saveAndFlush(user.getVerificationCode()) ;
//                    verificationCodeRepo.delete(user.getVerificationCode());
//                } else {
//                    throw new RuntimeException("Invalid verification code");
//                }
//            } else {
//                throw new RuntimeException("User not found");
//            }


    }


    public void resendVerificationCode(String email) throws MessagingException {
        Optional<Account> accountOptional = accountRepo.findByEmail(email);
        if (accountOptional.isPresent()) {
            Account user = accountOptional
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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

        String verificationCode = "VERIFICATION CODE " +verificationCodeRepo
                .findByAccount(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our website S3rha !</h2>"
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
    private void sendPasswordResetEmail(String email, String resetToken) throws MessagingException {
        String subject = "Password Reset Request";
        String resetLink = "https://yourapp.com/reset-password?token=" + resetToken;

        String htmlMessage = "<html>" +
                "<body style=\"font-family: Arial, sans-serif;\">" +
                "<div style=\"background-color: #f5f5f5; padding: 20px;\">" +
                "<h2 style=\"color: #333;\">Password Reset Request for account on S3rha website </h2>" +
                "<p style=\"font-size: 16px;\">Click the link below to reset your password:</p>" +
                "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">" +
                "<a href=\"" + resetLink + "\" style=\"display: inline-block; padding: 10px 20px; background-color: #007bff; color: #fff; text-decoration: none; border-radius: 5px;\">Reset Password</a>" +
                "</div>" +
                "<p style=\"font-size: 14px; color: #666;\">This link will expire in 4 hours.</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        emailService.sendVerificationEmail(email, subject, htmlMessage);
    }

    private String generateVerificationCode() {
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
    private String generatePasswordResetToken() {
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
    @Transactional // TODO add this annotation to all sevice methods that needs it cause without it it wiil cause troubles
    public void requestPasswordReset(String email) throws MessagingException {
        Account account = accountRepo.findByEmail(email)
                .stream()
                .findFirst()
                .orElse(null);

        if (account  != null) {  // Proper null check

            AuthService.log.warn("in request password reset 0");


            String resetToken = generatePasswordResetToken();
            LocalDateTime expiryDate = LocalDateTime.now().plusHours(4);
            AuthService.log.warn("in request password reset 1");
            PasswordResetToken resetTokenEntity = PasswordResetToken.builder()
                    .account(account)
                    .token(resetToken)
                    .expiryDate(expiryDate)
                    .build();
            AuthService.log.warn("in request password reset 2");

            passwordResetTokenRepo.save(resetTokenEntity);
            AuthService.log.warn("in request password reset 3");

            sendPasswordResetEmail(account.getEmail(), resetToken);
        }
    }

    public void validateResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
        }
    }
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
        }

        Account account = resetToken.getAccount();
        account.setPassword(bCryptPasswordEncoder.encode(newPassword));
        accountRepo.save(account);

        // Invalidate all tokens for this user
        passwordResetTokenRepo.deleteByAccount(account);
    }
    @Transactional
    public AuthResponseDto registerStore(StoreAccountRegistrationDto storeAccountRegistrationDto,
                                         HttpServletResponse httpServletResponse) throws Exception {


            AuthService.log.warn("[AuthService:registerUser]Store Registration Started with :::{}",storeAccountRegistrationDto);


            Optional<Account> user = accountRepo.findByUserName(storeAccountRegistrationDto.userName());
            if(user.isPresent()){
                throw new Exception("user name  Already Exist");
            }
            Optional<Account> user2 = accountRepo.findByEmail(storeAccountRegistrationDto.email());
            if(user2.isPresent()){
                throw new Exception("User with this Email  Already Exist");
            }



            StoreAccount userDetailsEntity = storeInfoMapper.convertToEntity(storeAccountRegistrationDto);

            Authentication authentication = createAuthenticationObject( userDetailsEntity);


        userDetailsEntity.setStatus(AccountStatus.PENDING.name());


        StoreAccount savedUserDetails = storeInfoRepo.save(userDetailsEntity);
            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);



            saveUserRefreshToken(savedUserDetails,refreshToken);


            saveUserVerificationCode(savedUserDetails
                    ,generateVerificationCode()
                    ,LocalDateTime.now().plusMinutes(15));
            log.warn("before sending ");
            sendVerificationEmail(savedUserDetails);
            log.warn("after  sending ");


            createRefreshTokenCookie(httpServletResponse,refreshToken);


            AuthService.log.warn("[AuthService:registerStore] Store:{} Successfully registered",savedUserDetails.getUserName());
            return   AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(savedUserDetails.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();



    }
    @Transactional
    public RegisterStoreByUserWithPriceDto registerStoreByUserWithPrice(@Valid StoreAccountByUserWithAddressAndPriceDto storeAccountByUserRegistrationDto, HttpServletResponse httpServletResponse) {
        try{
            AuthService.log.warn("[AuthService:registerUser]Store  byyyy User Registration Started with :::{}",storeAccountByUserRegistrationDto);


            StoreAccount userDetailsEntity = storeByUserInfoMapper.convertToEntity(storeAccountByUserRegistrationDto);
            Address address = addressInfoMapper.convertToEntity(storeAccountByUserRegistrationDto);
            UserPrice userPrice = userPriceInfoMapper.convertToEntity(storeAccountByUserRegistrationDto) ;

            userDetailsEntity.setReferenceMadeByUserFlag(true);
//            StoreAccount savedUserDetails = storeInfoRepo.save(userDetailsEntity);
            String nameOfTheUser = ownershipChecker.getCurrentUser();
            Account accountOfTheUser =   accountRepo.findByUserName(nameOfTheUser)
                    .orElseThrow(()->{
                AuthService.log.error("[AuthService:RegisterStoreByUser ] Account :{} not found",nameOfTheUser);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");});
//            userDetailsEntity.setAccountId(savedUserDetails.getAccountId());

            StoreAccount storeAccountAfterSave = storeInfoRepo.save(userDetailsEntity );
            userPrice.setUserAccount(
                    userAccountRepo.findById(accountOfTheUser.getAccountId()).orElseThrow(()->{
                AuthService.log.error("[AuthService:RegisterStoreByUser ] UserAccount :{} not found",nameOfTheUser);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");}));
            userPrice.setStoreAccount(storeAccountAfterSave);

            String prodcuturlll = storeAccountByUserRegistrationDto.productURL() ;


            String idStr = prodcuturlll.replaceAll(".*/(\\d+)/?$", "$1");
            Product product = productRepo.findById(Long.parseLong(idStr)).orElseThrow(()->{
                AuthService.log.error("[AuthService:RegisterStoreByUser ] Product :{} not found",idStr);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"Product NOT FOUND ");});

        ;   userPrice.setProduct(product);
            UserPrice userPriceAfterSave =  userPriceRepo.save(userPrice) ;

            address.setStoreAccount(storeAccountAfterSave);
            Address addressAfterSave =  addressRepo.save(address);


            StoreReferenceByUser storeReferenceByUser = new StoreReferenceByUser();
            storeReferenceByUser.setReferencedStoreAccount(storeAccountAfterSave);
            storeReferenceByUser.setReferencingAccount(accountOfTheUser);

            Long z = storeReferenceByUserRepo.save(storeReferenceByUser).getReferencedStoreAccount().getAccountId() ;

            RegisterStoreByUserWithPriceDto registerStoreByUserWithPriceDto = new RegisterStoreByUserWithPriceDto( );
            registerStoreByUserWithPriceDto.setStoreLink("http://localhost:8080/api/storeAccounts/"+storeAccountAfterSave.getAccountId());
            registerStoreByUserWithPriceDto.setAddressLink("http://localhost:8080/api/addresses/"+addressAfterSave.getAddress_id());
            registerStoreByUserWithPriceDto.setPriceLink("http://localhost:8080/api/userPrices/"+userPriceAfterSave.getPriceId());


            return  registerStoreByUserWithPriceDto ;


        }catch (Exception e){
            AuthService.log.error("[AuthService:registerStoreByUser]Exception while registering the store byyy user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }

    }
    @Transactional
    private void saveUserVerificationCode(Account userInfoEntity, String verificationCode, LocalDateTime dateTime) {
        VerificationCode verificationCodeEntity = VerificationCode.builder()
                .verificationCode(verificationCode)
                .verificationCodeExpireTime(dateTime)
                .build();
//            VerificationCode v = verificationCodeRepo.save(verificationCodeEntity) ;
            verificationCodeEntity.setAccount(userInfoEntity);
//          userInfoEntity.setVerificationCode(verificationCodeEntity);
            VerificationCode V =  verificationCodeRepo.save(verificationCodeEntity);
            AuthService.log.warn("just created this verification code "+ V.getVerificationCode());

    }
    @Transactional
    public void registerorloginOauthUser(OAuth2User principal, HttpServletResponse httpServletResponse) {


        try{
            AuthService.log.warn("[AuthService:registerorloginOauthUser] Started with :::{}",principal.getName());

            StringBuilder exist = new StringBuilder("NO"); // this for return with response to know if this oauth account
            //existed before or not and sign up a new one : >
            Account user = fromOAuth2User(principal,exist);
            Authentication authentication = createAuthenticationObject( user);



            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            saveUserRefreshToken(user,refreshToken);

            createRefreshTokenCookie(httpServletResponse,refreshToken);

            httpServletResponse.sendRedirect("http://localhost:3000");
            AuthService.log.warn("[AuthService:registerorloginOauthUser] OauthUser registered :{} Successfully ",user.getUserName());
//            AuthResponseOauthDto authResponse = AuthResponseOauthDto.builder()
//                    .accessToken(accessToken)
//                    .accessTokenExpiry(15 * 60)
//                    .userName(user.getUserName())
//                    .tokenType(TokenType.Bearer)
//                    .existAlready(exist.toString())
//                    .build();
//
//            httpServletResponse.setContentType("application/json");
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//            new ObjectMapper().writeValue(httpServletResponse.getWriter(), authResponse);

        }catch (Exception e){
            AuthService.log.error("[AuthService:registerorloginOauthUser]Exception while registering the OauthUser  due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
    @Transactional
    public Account fromOAuth2User(OAuth2User oauth2User,StringBuilder exist) throws Exception {
        Map<String, Object> attributes = oauth2User.getAttributes();

        // Determine the provider from the attributes
        String registrationId = determineProvider(attributes);

        String userName = "";
        String fname = "";
        String lname = "";
        String picture = "";
        String email = "" ;
        BigInteger providerId = null;
        String status = "" ;
        if ("google".equals(registrationId)) {
            providerId =  new BigInteger(((String) attributes.get("sub")));
            Optional<Account> user = accountRepo.findByOauthId(providerId);// TODO it is better to make findBy return optional not List
            AuthService.log.warn("this is the user list with OauthId "+ providerId);
            AuthService.log.warn(user.toString());

            if(user.isPresent()) { // TODO maybe also better to just create a plain Account not user account and continue the registration later
                AuthService.log.warn("this user"+user.get().getUserName()+ " already exists just returning it without creating new account ");
                exist.setLength(0);       // Clear existing content
                exist.append("YES");   // Set new value
                return user.get() ;
            }
            fname = (String) attributes.get("given_name");
            lname = (String) attributes.get("family_name");
            picture = (String) attributes.get("picture");
            userName = generateUsername(fname,lname);
            email = (String) attributes.get("email") ;
            Optional<Account> user2 = accountRepo.findByEmail(email);
            if(user2.isPresent()){
                throw new Exception("User with this Email  Already Exist");
            }

            status = AccountStatus.ACTIVATED_WITH_GOOGLE.name();
        }
        else if ("facebook".equals(registrationId)) {
            providerId =  new BigInteger(((String) attributes.get("id")));
            Optional<Account> user = accountRepo.findByOauthId(providerId);// TODO it is better to make findBy return optional not List
            if(user.isPresent()) { // TODO maybe also better to just create a plain Account not user account and continue the registration later
                AuthService.log.warn("this user"+user.get().getUserName()+ " already exists just returning it without creating new account ");
                exist.setLength(0);       // Clear existing content
                exist.append("YES");   // Set new value
                return user.get() ;
            }
            fname = (String) attributes.get("first_name");
            lname = (String) attributes.get("last_name");
            userName = generateUsername(fname,lname);
            email = (String) attributes.get("email") ;
            Optional<Account> user2 = accountRepo.findByEmail(email);
            if(user2.isPresent()){
                throw new Exception("User with this Email  Already Exist");
            }
            status = AccountStatus.ACTIVATED_WITH_FACEBOOK.name();
            if (attributes.get("picture") != null) {
                Map<String, Object> pictureData = (Map<String, Object>)
                        ((Map<String, Object>) attributes.get("picture")).get("data");
                picture = (String) pictureData.get("url");
            }
        }
        Account account = new Account() ;//BeanUtils.copyProperties(parent, child); // Copies matching fields
//        account.setFname(fname);
//        account.setLname(lname);
        account.setUserName(userName);
        account.setEmail(email);
        account.setStatus(status);
        account.setOauthId(providerId);
        account.setImage(picture);
        return accountRepo.save(account) ;

    }

    private String determineProvider(Map<String, Object> attributes) {
        // Google has 'sub' field
        if (attributes.containsKey("sub")) {
            return "google";
        }
        // Facebook has 'id' field (numeric string)
        else if (attributes.containsKey("id") && attributes.get("id") instanceof String) {
            try {
                parseLong((String) attributes.get("id"));
                return "facebook";
            } catch (NumberFormatException e) {
                // Not a Facebook ID
            }
        }
        // Add more providers as needed

        throw new IllegalArgumentException("Unknown OAuth2 provider");
    }
    public String generateUsername(String firstName, String lastName) {
        try {

            String z =  firstName.toLowerCase() +
                    lastName.toLowerCase().charAt(0) +
                    (random.nextInt(900) + 100); // 100-999
            Optional<Account> user = accountRepo.findByUserName(z);
            while (user.isPresent()){

                z =  firstName.toLowerCase() +
                        lastName.toLowerCase().charAt(0) +
                        (random.nextInt(900) + 100);
                user = accountRepo.findByUserName(z);
            }

            return  z  ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//
}


