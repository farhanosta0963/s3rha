package com.s3rha.spring.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterStoreByUserWithPriceDto {
    String storeLink ;
    String addressLink ;
    String priceLink ;
}
