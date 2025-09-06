package com.s3rha.spring;

import com.s3rha.spring.DAO.*;
import com.s3rha.spring.config.RSAKeyRecord;

import com.s3rha.spring.entity.Product;
import com.s3rha.spring.entity.ShoppingCart;
import com.s3rha.spring.entity.UserAccount;
import com.s3rha.spring.entity.VerificationCode;
import com.s3rha.spring.service.CustomOidcUserService;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.aspectj.SpringConfiguredConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.net.InetAddress;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
@EnableJpaAuditing  // <-- Add this annotation
@EnableAsync
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
//        ShoppingCart s = entityManager.find(ShoppingCart.class,4) ;
//            entityManager.remove(s);
    }

    @Bean
    CommandLineRunner checkEnv(Environment env, EntityManager entityManager,
                               ShoppingCartRepo shoppingCartRepo,
                               ProdOfCartRepo prodOfCartRepo,
                               UserAccountRepo userAccountRepo,
                               VerificationCodeRepo verificationCodeRepo,
                               BCryptPasswordEncoder bCryptPasswordEncoder,
                               ProductRepo productRepo,
                               DataSource dataSource) {
        return args -> {
            System.out.println("SMTP User: " + env.getProperty("SMTP_USERNAME"));
            System.out.println("here is a user you can use for autherntication" +
                    "user:farhan,password:123far123");
//            String scriptPath = "init.sql";
//
//            try (var connection = dataSource.getConnection()) {
//                ScriptUtils.executeSqlScript(connection, new ClassPathResource(scriptPath));
//            } catch (Exception e) {
//                // Handle exceptions, e.g., if the script fails to run
//                System.err.println("Failed to run SQL script: " + scriptPath);
//                e.printStackTrace();
//            }



            };
    }

}
