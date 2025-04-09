package com.s3rha.spring.entity;

import org.springframework.data.rest.core.config.Projection;

@Projection( name = "007",types = { Product.class })
public interface ProductProjection {
    String getName();
}
