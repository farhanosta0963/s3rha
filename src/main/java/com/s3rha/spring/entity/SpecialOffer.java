package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//7. Special Offer Entity
//        java
//Copy
@Entity
@Setter
@Getter
@NoArgsConstructor
public class SpecialOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;
    private String name;
    private String description;
    private String image;
    private LocalDateTime datetimeOfStart;
    private LocalDateTime datetimeOfEnd;
    private LocalDateTime datetimeOfInsert;
    private BigDecimal price;
    @PrePersist // Automatically set before saving
    protected void onCreate() {
        this.datetimeOfInsert = LocalDateTime.now();
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "store_price_id")
    private StorePrice storePrice;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "prod_of_offer",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "Product_id")
    )
    private List<Product> products ;

    public void addProduct(Product product) {

        if (product == null) {
            products = new ArrayList<>();
        }

        products.add(product);
    }
    // Getters and setters
}
