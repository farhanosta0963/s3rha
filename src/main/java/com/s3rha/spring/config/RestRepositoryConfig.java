package com.s3rha.spring.config;
import com.s3rha.spring.entity.Product;
import com.s3rha.spring.entity.StoreAccount;
import com.s3rha.spring.entity.UserAccount;
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

        exposure.forDomainType(StoreAccount.class)
                .withCollectionExposure((metadata, httpMethods) ->
                        httpMethods.disable(HttpMethod.GET)) // disables GET /StoreAccounts
                .withItemExposure((metadata, httpMethods) ->
                        httpMethods.disable(HttpMethod.GET)); // disables GET /StoreAccounts/{id}

        exposure.forDomainType(UserAccount.class)
                .withCollectionExposure((metadata, httpMethods) ->
                        httpMethods.disable(HttpMethod.GET)) // disables GET /UserAccounts
                .withItemExposure((metadata, httpMethods) ->
                        httpMethods.disable(HttpMethod.GET)); // disables GET /UserAccounts/{id}

//
//        exposure.forDomainType(Product.class)
//                .withCollectionExposure((metadata, httpMethods) ->
//                        httpMethods.disable(HttpMethod.GET)) // disables GET /accounts
//                .withItemExposure((metadata, httpMethods) ->
//                        httpMethods.disable(HttpMethod.GET)); // disables GET /accounts/{id}


    }
}
