package com.s3rha.spring.controller;

import com.s3rha.spring.DAO.ProductRepo;
import com.s3rha.spring.dto.StoreAccountByUserRegistrationDto;
import com.s3rha.spring.dto.StoreAccountRegistrationDto;
import com.s3rha.spring.dto.UserAccountRegistrationDto;
import com.s3rha.spring.dto.VerifyUserDto;

import com.s3rha.spring.entity.Product;
import com.s3rha.spring.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final EntityManager entityManager;
    private final ProductRepo productRepo ;


//    @GetMapping("/user")
//    public Map<String, Object> user(Principal principal) {
//        return Collections.singletonMap("name", principal.getName());
//    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(Authentication authentication,HttpServletResponse response){

        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication,response));
    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @PostMapping("/sign-up-user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserAccountRegistrationDto userAccountRegistrationDto,
                                          BindingResult bindingResult, HttpServletResponse httpServletResponse){

        log.warn("[AuthController:registerUser]Signup Process Started for user:{}",userAccountRegistrationDto.userName());
//        if (bindingResult.hasErrors()) {
//            List<String> errorMessage = bindingResult.getAllErrors().stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .toList();
//            log.error("[AuthController:registerUser]Errors in user account:{}",errorMessage);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//        }
        return ResponseEntity.ok(authService.registerUser(userAccountRegistrationDto,httpServletResponse));
    }
//
//    @GetMapping("/sign-up-oauth2")
//    public ResponseEntity<?> registerOauth2(@AuthenticationPrincipal OAuth2User principal ,  HttpServletResponse httpServletResponse){
//        log.warn("[AuthController:registerUser]Signup Process Started for user:{}",principal.getName());
////
//        return ResponseEntity.ok(authService.registerorloginOauthUser(principal,httpServletResponse));
//    }
    @PostMapping("/sign-up-store")
    public ResponseEntity<?> registerStore(@Valid @RequestBody StoreAccountRegistrationDto storeAccountRegistrationDto,
                                       HttpServletResponse httpServletResponse) throws Exception {

        log.warn("[AuthController:registerUser]Signup Process Started for store account:{}",storeAccountRegistrationDto.userName());
//        if (bindingResult.hasErrors()) {
//            List<String> errorMessage = bindingResult.getAllErrors().stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .toList();
//            log.error("[AuthController:registerUser]Errors in Store:{}",errorMessage);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//        }
        return ResponseEntity.ok(authService.registerStore(storeAccountRegistrationDto,httpServletResponse));
    }
    @PostMapping("/sign-up-storeByUser")
    public ResponseEntity<?> registerStore(@Valid @RequestBody StoreAccountByUserRegistrationDto storeAccountByUserRegistrationDto,
                                          BindingResult bindingResult, HttpServletResponse httpServletResponse){

        log.warn("[AuthController:registerUser]Signup Process Started for store account byyyyyyyyy User:{}",storeAccountByUserRegistrationDto.name());
//        if (bindingResult.hasErrors()) {
//            List<String> errorMessage = bindingResult.getAllErrors().stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .toList();
//            log.error("[AuthController:registerUser]Errors in store byyyyy User:{}",errorMessage);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//        }
        return ResponseEntity.ok(authService.registerStoreByUser(storeAccountByUserRegistrationDto,httpServletResponse));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser( @Valid @RequestBody VerifyUserDto verifyUserDto) {
        authService.verifyUser(verifyUserDto);
        return ResponseEntity.ok("Account verified successfully");
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode( @RequestParam String email) throws MessagingException {
        authService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent");
    }
}
