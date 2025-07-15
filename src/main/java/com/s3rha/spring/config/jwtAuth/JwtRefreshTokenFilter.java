package com.s3rha.spring.config.jwtAuth;

import com.s3rha.spring.config.RSAKeyRecord;
import com.s3rha.spring.dto.TokenType;
import com.s3rha.spring.DAO.RefreshTokenRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;


@RequiredArgsConstructor
@Slf4j
public class JwtRefreshTokenFilter extends OncePerRequestFilter {

    private  final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenRepo refreshTokenRepo;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            log.warn("[JwtRefreshTokenFilter:doFilterInternal] :: Started ");

            log.warn("[JwtRefreshTokenFilter:doFilterInternal]Filtering the Http Request:{}", request.getRequestURI());


            // 1. Extract refresh token from HttpOnly cookie (not headers!)

            log.warn(Arrays.toString(request.getCookies())); ;
            String refreshToken = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("refresh_token"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new RuntimeException("Refresh token missing"));







//            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();

//            if (authHeader == null) {
//                filterChain.doFilter(request,response);
//                return;
//            }else if (!authHeader.startsWith(TokenType.Bearer.name())){
//                filterChain.doFilter(request,response);
//                return;
//            }

//            final String token = authHeader.substring(7);
            final Jwt jwtRefreshToken = jwtDecoder.decode(refreshToken);


            final String userName = jwtTokenUtils.getUserName(jwtRefreshToken);


            if (!userName.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                //Check if refreshToken isPresent in database and is valid
                var isRefreshTokenValidInDatabase =
                        refreshTokenRepo.findByRefreshToken(jwtRefreshToken.getTokenValue())
                        .map(refreshTokenEntity -> !refreshTokenEntity.isRevoked())
                        .orElse(false);

                UserDetails userDetails = jwtTokenUtils.userDetails(userName);
                //TODO i think it is better to decode the JWT that to  reach the database
                if (jwtTokenUtils.isTokenValid(jwtRefreshToken, userDetails) && isRefreshTokenValidInDatabase) {
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(createdToken);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
            log.warn("[JwtRefreshTokenFilter:doFilterInternal] Completed");
            filterChain.doFilter(request, response);
        }catch (JwtValidationException jwtValidationException){
            log.error("[JwtRefreshTokenFilter:doFilterInternal] Exception due to :{}", jwtValidationException.getMessage());
            authenticationEntryPoint.commence(request, response,
                    new BadCredentialsException(jwtValidationException.getMessage(), jwtValidationException));
            return;
            // Important: stop the filter chainl
        }
    }
}
