package com.s3rha.spring;

import com.s3rha.spring.config.RSAKeyRecord;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
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
