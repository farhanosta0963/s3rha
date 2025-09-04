package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Address_id ;
    private String address;
    private BigDecimal latitude ;
    private BigDecimal longitude ;
    private LocalDateTime datetimeOfInsert = LocalDateTime.now() ;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "store_account_id")
    private StoreAccount storeAccount;


    // Getters and setters
}
