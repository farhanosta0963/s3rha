package com.s3rha.spring;

import com.s3rha.spring.config.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
public class SpringSecurityApplication {

    public static void main(String[] args) {
        System.out.println("this is for a commit test ");
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

}
