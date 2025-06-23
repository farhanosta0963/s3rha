package com.s3rha.spring.entity;

//import com.s3rha.spring.entityListener.PriceEntityListener;
//import com.s3rha.spring.entityListener.ShoppingCartEntityListener;
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
@Inheritance(strategy = InheritanceType.JOINED)
//@EntityListeners(PriceEntityListener.class)
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PriceId;
    private BigDecimal price;
    private LocalDateTime datetimeOfInsert = LocalDateTime.now();
    private String currency;
    private String unitOfMeasure;
    protected Boolean isStorePrice ;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "store_account_id")
    private StoreAccount storeAccount;



    // Getters and setters
}
