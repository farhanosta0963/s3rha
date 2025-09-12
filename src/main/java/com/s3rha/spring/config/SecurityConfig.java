package com.s3rha.spring.config;

import com.s3rha.spring.DAO.ProductRepo;
import com.s3rha.spring.config.jwtAuth.JwtAccessTokenFilter;
import com.s3rha.spring.config.jwtAuth.JwtRefreshTokenFilter;
import com.s3rha.spring.config.jwtAuth.JwtTokenUtils;
import com.s3rha.spring.config.user.UserInfoManagerConfig;
import com.s3rha.spring.DAO.RefreshTokenRepo;
import com.s3rha.spring.service.AuthService;
import com.s3rha.spring.service.CustomOidcUserService;
import com.s3rha.spring.service.LogoutHandlerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {



    private final UserInfoManagerConfig userInfoManagerConfig;
    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenRepo refreshTokenRepo;
    private final LogoutHandlerService logoutHandlerService;
    private final ProductRepo productRepo ;
      // Defer initialization
    private final   AuthService authService ;

//    @Order(1)
//    @Bean
//        public SecurityFilterChain signInSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
//        return httpSecurity
//                .securityMatcher(new AntPathRequestMatcher("/**"))
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                //.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET).permitAll().anyRequest().authenticated())
// 
////                    .oauth2Login(oauth -> oauth
////                            .authorizationEndpoint(auth -> auth
////                                    .baseUri("/oauth2/authorization") // Starting endpoint
////                                    .authorizationRequestRepository(authRequestRepository())
////                            )
////                            .redirectionEndpoint(redir -> redir
////                                    .baseUri("/login/oauth2/code/google")
////                            )  .build();
//                               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
////                .oauth2Login(oauth -> oauth
////                .authorizationEndpoint(auth -> auth
////                        .baseUri("/oauth2/authorization")
//////                        .authorizationRequestRepository(authRequestRepository())
////                )
////                .redirectionEndpoint(redir -> redir
////                        .baseUri("/login/oauth2/code/google")
////             ).successHandler((request, response, authentication) -> {
////                                    OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
////                                    authService.registerorloginOauthUser(oauthUser, response);
////                                })
////                )
//                .build() ;
////                .tokenEndpoint(token -> {
////                                // Don't specify client - Spring will provide default
////                            })
////                .userInfoEndpoint(user -> user
////                        .oidcUserService(oidcUserService())
////                )
//
//
//
////                .oauth2Login(Customizer.withDefaults())
////               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .exceptionHandling(ex -> {
////                    ex.authenticationEntryPoint((request, response, authException) ->
////                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
////                })
////                .exceptionHandling(ex -> {
////                    ex.authenticationEntryPoint((request, response, authException) -> {
////                        log.error("Authentication error: {}", authException.getMessage());
////                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
////                    });
////                    ex.accessDeniedHandler((request, response, accessDeniedException) -> {
////                        log.error("Access denied: {}", accessDeniedException.getMessage());
////                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
////                    });
////                })
//
//
//    }
//













//TODO better disable the csrf
    @Order(1)
    @Bean
        public SecurityFilterChain signInSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/auth/sign-in"))
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- This line enables CORS for Spring Security
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth. anyRequest().permitAll())
                //                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET).permitAll().anyRequest().authenticated())
                .userDetailsService(userInfoManagerConfig)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
                .exceptionHandling(ex -> {//TODO do this exception hanndling for the rest of the chains :L
                    ex.authenticationEntryPoint((request, response, authException) -> {
                        log.warn("Authentication failed: {} - Path: {}",
                                authException.getMessage(),
                                request.getRequestURI());

                        // Delegate to the standard BearerToken handler
                        new BearerTokenAuthenticationEntryPoint().commence(request, response, authException);
                    });

                    ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                        log.warn("Access denied for {}: {} - Path: {}",
                                SecurityContextHolder.getContext().getAuthentication().getName(),
                                accessDeniedException.getMessage(),
                                request.getRequestURI());

                        new BearerTokenAccessDeniedHandler().handle(request, response, accessDeniedException);
                    });
                })
//                .exceptionHandling(ex -> {
//                    ex.authenticationEntryPoint((request, response, authException) -> {
//                        log.error("Authentication error: {}", authException.getMessage());
//                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                    });
//                    ex.accessDeniedHandler((request, response, accessDeniedException) -> {
//                        log.error("Access denied: {}", accessDeniedException.getMessage());
//                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
//                    });
//                })

                .httpBasic(withDefaults())
                .build();
    }
////
////    public SecurityFilterChain signInSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
////        return httpSecurity
////                .securityMatcher(new AntPathRequestMatcher("/sign-in/**"))
////                .csrf(AbstractHttpConfigurer::disable)
////                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
////                .userDetailsService(userInfoManagerConfig)
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .exceptionHandling(ex -> {
////                    ex.authenticationEntryPoint((request, response, authException) ->
////                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
////                })
////                .httpBasic(withDefaults())
////                .build();
////    }
//







    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//
//@Bean // This bean defines your CORS rules for Spring Security
//CorsConfigurationSource corsConfigurationSource() { // TODO do this for all beans !!
//    CorsConfiguration configuration = new CorsConfiguration();
//    configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Your frontend URL
//    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//    configuration.setAllowedHeaders(List.of("*")); // Allow all headers
//    configuration.setAllowCredentials(true); // Allow credentials (cookies, auth headers)
//    configuration.setMaxAge(3600L); // Max age for preflight cache
//
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", configuration); // Apply this CORS config to all paths
//    return source;
//}

    @Order(2)
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        AuthenticationEntryPoint entryPoint = new BearerTokenAuthenticationEntryPoint();
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/api/**"))
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- This line enables CORS for Spring Security
//                .cors().disable()
                .csrf(csrf -> csrf.disable()) // Only disable CSRF if you know what you're doing for API; handle properly in production!
                .authorizeHttpRequests(auth -> auth
                        // Permit all GET requests under /api/**
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()


                        // Require authentication for any other requests under /api/**
                        .anyRequest().authenticated()
                )


                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(new JwtAccessTokenFilter(rsaKeyRecord, jwtTokenUtils,entryPoint), UsernamePasswordAuthenticationFilter.class)
//              .addFilterAfter(new ProductOwnershipFilter(productRepo),
//                        BasicAuthenticationFilter.class)

                .httpBasic(withDefaults())
                .exceptionHandling(ex -> {//TODO do this exception hanndling for the rest of the chains :L
                    ex.authenticationEntryPoint((request, response, authException) -> {
                        log.warn("Authentication failed: {} - Path: {}",
                                authException.getMessage(),
                                request.getRequestURI());

                        // Delegate to the standard BearerToken handler
                        new BearerTokenAuthenticationEntryPoint().commence(request, response, authException);
                    });

                    ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                        log.warn("Access denied for {}: {} - Path: {}",
                                SecurityContextHolder.getContext().getAuthentication().getName(),
                                accessDeniedException.getMessage(),
                                request.getRequestURI());

                        new BearerTokenAccessDeniedHandler().handle(request, response, accessDeniedException);
                    });
                })
                .build();
    }



//                .exceptionHandling(ex -> {
//                    log.error("[SecurityConfig:apiSecurityFilterChain] Exception due to :{}",ex);
//                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());//401
//                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());//403
//                })


    @Order(3)
    @Bean
    public SecurityFilterChain refreshTokenSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        AuthenticationEntryPoint entryPoint = new BearerTokenAuthenticationEntryPoint();
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/auth/refresh-token/**"))
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- This line enables CORS for Spring Security

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtRefreshTokenFilter(rsaKeyRecord,jwtTokenUtils,refreshTokenRepo,entryPoint), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint((request, response, authException) -> {
                        log.warn("Authentication failed: {} - Path: {}",
                                authException.getMessage(),
                                request.getRequestURI());

                        // Delegate to the standard BearerToken handler
                        new BearerTokenAuthenticationEntryPoint().commence(request, response, authException);
                    });

                    ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                        log.warn("Access denied for {}: {} - Path: {}",
                                SecurityContextHolder.getContext().getAuthentication().getName(),
                                accessDeniedException.getMessage(),
                                request.getRequestURI());

                        new BearerTokenAccessDeniedHandler().handle(request, response, accessDeniedException);
                    });
                })


//                .exceptionHandling(ex -> {
//                    log.error("[SecurityConfig:refreshTokenSecurityFilterChain] Exception due to :{}");
//                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
//                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
//                })
                .httpBasic(withDefaults())
                .build();
    }

    @Order(4)
    @Bean
    public SecurityFilterChain logoutSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        AuthenticationEntryPoint entryPoint = new BearerTokenAuthenticationEntryPoint();
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/auth/logout/**"))
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- This line enables CORS for Spring Security

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtRefreshTokenFilter(rsaKeyRecord,jwtTokenUtils,refreshTokenRepo,entryPoint), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandlerService)
                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                ).exceptionHandling(ex -> {
                    ex.authenticationEntryPoint((request, response, authException) -> {
                        log.warn("Authentication failed: {} - Path: {}",
                                authException.getMessage(),
                                request.getRequestURI());

                        // Delegate to the standard BearerToken handler
                        new BearerTokenAuthenticationEntryPoint().commence(request, response, authException);
                    });

                    ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                        log.warn("Access denied for {}: {} - Path: {}",
                                SecurityContextHolder.getContext().getAuthentication().getName(),
                                accessDeniedException.getMessage(),
                                request.getRequestURI());

                        new BearerTokenAccessDeniedHandler().handle(request, response, accessDeniedException);
                    });
                })
//                .exceptionHandling(ex -> {
//                    log.error("[SecurityConfig:logoutSecurityFilterChain] Exception due to :{}",ex);
//                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
//                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
//                })
                .build();
    }

    @Order(5)
    @Bean
    public SecurityFilterChain registerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/auth/**"))
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- This line enables CORS for Spring Security

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> {//TODO do this exception hanndling for the rest of the chains :L
                    ex.authenticationEntryPoint((request, response, authException) -> {
                        log.warn("Authentication failed: {} - Path: {}",
                                authException.getMessage(),
                                request.getRequestURI());

                        // Delegate to the standard BearerToken handler
                        new BearerTokenAuthenticationEntryPoint().commence(request, response, authException);
                    });

                    ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                        log.warn("Access denied for {}: {} - Path: {}",
                                SecurityContextHolder.getContext().getAuthentication().getName(),
                                accessDeniedException.getMessage(),
                                request.getRequestURI());

                        new BearerTokenAccessDeniedHandler().handle(request, response, accessDeniedException);
                    });
                })
                .build();
    }


    @Order(6)
    @Bean
        public SecurityFilterChain signInWithOauth2SecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/oauth2/**"),
                        new AntPathRequestMatcher("/login/oauth2/code/**")
                ))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                //.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET).permitAll().anyRequest().authenticated())

//                    .oauth2Login(oauth -> oauth
//                            .authorizationEndpoint(auth -> auth
//                                    .baseUri("/oauth2/authorization") // Starting endpoint
//                                    .authorizationRequestRepository(authRequestRepository())
//                            )
//                            .redirectionEndpoint(redir -> redir
//                                    .baseUri("/login/oauth2/code/google")
//                            )  .build();
//                               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .oauth2Login(oauth -> oauth
                .authorizationEndpoint(auth -> auth
                        .baseUri("/oauth2/authorization")
//                        .authorizationRequestRepository(authRequestRepository())
                )
                .redirectionEndpoint(redir -> redir
                        .baseUri("/login/oauth2/code/google")
             ).successHandler((request, response, authentication) -> {
                                    OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                                    authService.registerorloginOauthUser(oauthUser, response);
                                })
                )
                .build() ;
//                .tokenEndpoint(token -> {
//                                // Don't specify client - Spring will provide default
//                            })
//                .userInfoEndpoint(user -> user
//                        .oidcUserService(oidcUserService())
//                )



//                .oauth2Login(Customizer.withDefaults())
//               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling(ex -> {
//                    ex.authenticationEntryPoint((request, response, authException) ->
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
//                })
//                .exceptionHandling(ex -> {
//                    ex.authenticationEntryPoint((request, response, authException) -> {
//                        log.error("Authentication error: {}", authException.getMessage());
//                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                    });
//                    ex.accessDeniedHandler((request, response, accessDeniedException) -> {
//                        log.error("Access denied: {}", accessDeniedException.getMessage());
//                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
//                    });
//                })


    }














    //    @Order(6)
//    @Bean
//    public SecurityFilterChain h2ConsoleSecurityFilterChainConfig(HttpSecurity httpSecurity) throws Exception{
//        return httpSecurity
//                .securityMatcher(new AntPathRequestMatcher(("/h2-console/**")))
//                .authorizeHttpRequests(auth->auth.anyRequest().permitAll())
//                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
//                // to display the h2Console in Iframe
//                .headers(headers -> headers.frameOptions(withDefaults()).disable())
//                .build();
//    }
    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new DefaultAuthorizationCodeTokenResponseClient();
    }
    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return new CustomOidcUserService(); // Your custom implementation
    }


//    public AuthResponseDto registerorloginOauthUser(OAuth2User principal, HttpServletResponse httpServletResponse) {
//
//
//        try{
//            log.warn("[AuthService:registerorloginOauthUser]OauthUser Registration Started with :::{}",principal.getName());
//
//
//            Account user = fromOAuth2User(principal);
//            Authentication authentication = createAuthenticationObject( user);
//
//
//
//            // Generate a JWT token
//            String accessToken = generateAccessToken(authentication);
//            String refreshToken = generateRefreshToken(authentication);
//
//            saveUserRefreshToken(user,refreshToken);
//
//            creatRefreshTokenCookie(httpServletResponse,refreshToken);
//
//
//            log.warn("[AuthService:registerorloginOauthUser] OauthUser:{} Successfully registered",user.getUserName());
//            return   AuthResponseDto.builder()
//                    .accessToken(accessToken)
//                    .accessTokenExpiry(15 * 60)
//                    .userName(user.getUserName())
//                    .tokenType(TokenType.Bearer)
//                    .build();
//
//        }catch (Exception e){
//            log.error("[AuthService:registerorloginOauthUser]Exception while registering the OauthUser  due to :"+e.getMessage());
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
//        }
//    }
//    public Account fromOAuth2User(OAuth2User oauth2User) throws Exception {
//        Map<String, Object> attributes = oauth2User.getAttributes();
//
//        // Determine the provider from the attributes
//        String registrationId = determineProvider(attributes);
//
//        String userName = "";
//        String fname = "";
//        String lname = "";
//        String picture = "";
//        String email = "" ;
//        BigInteger providerId = null;
//        String status = "" ;
//        if ("google".equals(registrationId)) {
//            providerId =  new BigInteger(((String) attributes.get("sub")));
//            List<Account> user = accountRepo.findByOauthId(providerId);// TODO it is better to make findBy return optional not List
//            if(user.size()>0) { // TODO maybe also better to just create a plain Account not user account and continue the registration later
//                log.warn("this user"+user.get(0).getUserName()+ " already exists just returning it without creating new account ");
//
//                return user.get(0) ;
//            }
//            fname = (String) attributes.get("given_name");
//            lname = (String) attributes.get("family_name");
//            picture = (String) attributes.get("picture");
//            userName = generateUsername(fname,lname);
//            email = (String) attributes.get("email") ;
//            List<Account> user2 = accountRepo.findByEmail(email);
//            if(user2.size()>0){
//                throw new Exception("Account with this Email  Already Exist");
//            }
//            status = AccountStatus.ACTIVATED_WITH_GOOGLE.name();
//        }
//        else if ("facebook".equals(registrationId)) {
//            providerId =  new BigInteger(((String) attributes.get("id")));
//            List<Account> user = accountRepo.findByOauthId(providerId);// TODO it is better to make findBy return optional not List
//            if(user.size()>0) { // TODO maybe also better to just create a plain Account not user account and continue the registration later
//                log.warn("this user"+user.get(0).getUserName()+ " already exists just returning it without creating new account ");
//                return user.get(0) ;
//            }
//            fname = (String) attributes.get("first_name");
//            lname = (String) attributes.get("last_name");
//            userName = generateUsername(fname,lname);
//            email = (String) attributes.get("email") ;
//            List<Account> user2 = accountRepo.findByEmail(email);
//            if(user2.size()>0){
//                throw new Exception("Account with this Email  Already Exist");
//            }
//            status = AccountStatus.ACTIVATED_WITH_FACEBOOK.name();
//            if (attributes.get("picture") != null) {
//                Map<String, Object> pictureData = (Map<String, Object>)
//                        ((Map<String, Object>) attributes.get("picture")).get("data");
//                picture = (String) pictureData.get("url");
//            }
//        }
//        UserAccount userAccount = new UserAccount() ;
//        userAccount.setFname(fname);
//        userAccount.setLname(lname);
//        userAccount.setUserName(userName);
//        userAccount.setEmail(email);
//        userAccount.setStatus(status);
//        userAccount.setFname(fname);
//        userAccount.setOauthId(providerId);
//        userAccount.setImage(picture);
//        return  userAccountRepo.save(userAccount) ;
//
//    }
//
//    private String determineProvider(Map<String, Object> attributes) {
//        // Google has 'sub' field
//        if (attributes.containsKey("sub")) {
//            return "google";
//        }
//        // Facebook has 'id' field (numeric string)
//        else if (attributes.containsKey("id") && attributes.get("id") instanceof String) {
//            try {
//                Long.parseLong((String) attributes.get("id"));
//                return "facebook";
//            } catch (NumberFormatException e) {
//                // Not a Facebook ID
//            }
//        }
//        // Add more providers as needed
//
//        throw new IllegalArgumentException("Unknown OAuth2 provider");
//    }
//    public String generateUsername(String firstName, String lastName) {
//        try {
//
//            String z =  firstName.toLowerCase() +
//                    lastName.toLowerCase().charAt(0) +
//                    (random.nextInt(900) + 100); // 100-999
//            Optional<Account> user = accountRepo.findByUserName(z);
//            while (user.isPresent()){
//
//                z =  firstName.toLowerCase() +
//                        lastName.toLowerCase().charAt(0) +
//                        (random.nextInt(900) + 100);
//                user = accountRepo.findByUserName(z);
//            }
//
//            return  z  ;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        private final JwtEncoder jwtEncoder;
//
//        public String generateAccessToken(Authentication authentication) {
//
//            log.warn("[JwtTokenGenerator:generateAccessToken] Token Creation Started for:{}", authentication.getName());
//
//            String roles = getRolesOfUser(authentication);
//
//            String permissions = getPermissionsFromRoles(roles);
//
//            JwtClaimsSet claims = JwtClaimsSet.builder()
//                    .issuer("s3rha")
//                    .issuedAt(Instant.now())
//                    .expiresAt(Instant.now().plus(15 , ChronoUnit.MINUTES))
//                    .subject(authentication.getName())
//                    .claim("scope", permissions)
//                    .build();
//
//            return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//        }
//
//
//        public String generateRefreshToken(Authentication authentication) {
//
//            log.warn("[JwtTokenGenerator:generateRefreshToken] Token Creation Started for:{}", authentication.getName());
//
//            JwtClaimsSet claims = JwtClaimsSet.builder()
//                    .issuer("atquil")
//                    .issuedAt(Instant.now())
//                    .expiresAt(Instant.now().plus(15 , ChronoUnit.DAYS))
//                    .subject(authentication.getName())
//                    .claim("scope", "REFRESH_TOKEN")
//                    .build();
//
//            return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//        }
//        private static String getRolesOfUser(Authentication authentication) {
//            return authentication.getAuthorities().stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .collect(Collectors.joining(" "));
//        }
//
//        private String getPermissionsFromRoles(String roles) {
//            Set<String> permissions = new HashSet<>();
//
//            if (roles.contains("ROLE_ADMIN")) {
//                permissions.addAll(List.of("READ", "WRITE", "DELETE"));
//            }
//            if (roles.contains("ROLE_MANAGER")) {
//                permissions.add("READ");
//            }
//            if (roles.contains("ROLE_USER")) {
//                permissions.add("READ");
//            }
//
//            return String.join(" ", permissions);
//        }

    }
