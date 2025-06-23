//package com.s3rha.spring.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//// Line 1
//@Configuration
//// Line 2
//public class CorsConfig implements WebMvcConfigurer {
//    // Line 3
//    @Override
//    // Line 4
//    public void  addCorsMappings(CorsRegistry registry) { // Note: There's a typo here, it should be 'public void addCorsMappings(CorsRegistry registry)'
//        // Line 5
//        registry.addMapping("/**")
//                // Line 6
//                .allowedOrigins("http://localhost:3000") // Your frontend URL
//                // Line 7
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                // Line 8
//                .allowedHeaders("*")
//                // Line 9
//                .allowCredentials(true)
//                // Line 10
//                .maxAge(3600);
//    }
//}