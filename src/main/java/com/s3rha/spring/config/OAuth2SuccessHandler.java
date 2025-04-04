//package com.s3rha.spring.config;
//
//import com.s3rha.spring.DAO.AccountRepo;
//import com.s3rha.spring.DAO.UserAccountRepo;
//import com.s3rha.spring.service.AuthService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
//
//    private final AccountRepo accountRepo ;
//    private final UserAccountRepo userRepository;  // Your DB repository
//    private final AuthService  authService ;
//
//    @Override
//    public void onAuthenticationSuccess(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Authentication authentication) throws IOException {
//
//        // Extract user details from OAuth2 authentication
//        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
//       authService.registerorloginOauthUser(oauthUser,response) ;
//    }
//
//
//}