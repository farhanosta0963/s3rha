package com.s3rha.spring.controller;

import com.s3rha.spring.DAO.ProductRepo;
import com.s3rha.spring.dto.*;
import jakarta.servlet.http.Cookie;

import com.s3rha.spring.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
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

//    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(HttpServletRequest httpServletRequest){

        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(httpServletRequest));
        //TODO add refresh token in the httponly
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
//        User savedUser = userService.save(user); //TODO better return 201 istead of 200
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(savedUser.getId())
//                .toUri();
//
//        return ResponseEntity.created(location).body(savedUser);
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
    // email verify
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


    //pass reset
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) throws MessagingException {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok("Password reset link sent to email if account exists");
    }

    @PostMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {
        authService.validateResetToken(token);
        return ResponseEntity.ok("Token is valid");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
          @Valid  @RequestBody ResetPasswordRequestDto resetPasswordRequest) {
        authService.resetPassword(
                resetPasswordRequest.token(),
                resetPasswordRequest.newPassword()
        );
        return ResponseEntity.ok("Password has been reset successfully");
    }
}
