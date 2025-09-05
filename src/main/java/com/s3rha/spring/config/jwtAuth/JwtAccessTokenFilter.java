package com.s3rha.spring.config.jwtAuth;

import com.s3rha.spring.config.RSAKeyRecord;
import com.s3rha.spring.dto.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class JwtAccessTokenFilter extends OncePerRequestFilter {
    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, JwtValidationException {

        try{
//            if (request.getRequestURI().equals("/api/ccc")) {
//            filterChain.doFilter(request, response); // Skip JWT processing for /api/ccc
//            return;
//        }
            log.warn("[JwtAccessTokenFilter:doFilterInternal] :: Started ");

            log.warn("[JwtAccessTokenFilter:doFilterInternal]Filtering the Http Request:{}",request.getRequestURI());

            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            log.warn(authHeader);
            JwtDecoder jwtDecoder =  NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
            if (authHeader == null) {
                filterChain.doFilter(request,response);
                return;
            }else if (!authHeader.startsWith(TokenType.Bearer.name())){
                filterChain.doFilter(request,response);
                return;
            }


            final String token = authHeader.substring(7);
            final Jwt jwtToken = jwtDecoder.decode(token);


            final String userName = jwtTokenUtils.getUserName(jwtToken);

            if(!userName.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null){


                UserDetails userDetails = jwtTokenUtils.userDetails(userName);
                boolean validFlag = jwtTokenUtils.isTokenValid(jwtToken,userDetails) ;

                if(validFlag){
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
            log.warn("[JwtAccessTokenFilter:doFilterInternal] Completed");

            filterChain.doFilter(request,response);
        }catch (JwtException | IllegalArgumentException   jwtValidationException){
            log.error("[JwtAccessTokenFilter:doFilterInternal] Exception due to :{}", jwtValidationException.getMessage());
            authenticationEntryPoint.commence(request, response,
                    new BadCredentialsException(jwtValidationException.getMessage(), jwtValidationException));

            return;
            // Important: stop the filter chainl
        }
    }
}
