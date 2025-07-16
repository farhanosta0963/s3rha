package com.s3rha.spring.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.s3rha.spring.entity.Account;

@Configuration
public class RestRepositoryConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        ExposureConfiguration exposure = config.getExposureConfiguration();

        exposure.forDomainType(Account.class)
                .withCollectionExposure((metadata, httpMethods) ->
                        httpMethods.disable(HttpMethod.GET)) // disables GET /accounts
                .withItemExposure((metadata, httpMethods) ->
                        httpMethods.disable(HttpMethod.GET)); // disables GET /accounts/{id}



     


    }
}
