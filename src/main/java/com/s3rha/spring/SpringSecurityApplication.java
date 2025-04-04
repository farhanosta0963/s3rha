package com.s3rha.spring;

import com.s3rha.spring.config.RSAKeyRecord;

import com.s3rha.spring.service.CustomOidcUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
@EnableJpaAuditing  // <-- Add this annotation
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }
    @Bean
    CommandLineRunner checkEnv(Environment env) {
        return args -> {
            System.out.println("SMTP User: " + env.getProperty("SMTP_USERNAME"));
            // Don't print password in logs!
        };
    }

}
