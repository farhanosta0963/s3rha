package com.s3rha.spring.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StoreAccountByUserWithAddressAndPriceDto (

            @NotBlank(message = "Name must not be empty")
            String name,
            String phoneNumber,
            String image,

            String address,
            BigDecimal latitude,
            BigDecimal longitude,

            BigDecimal price,
            String unitOfMeasure,
            String currency,

            String productURL

            ){}
