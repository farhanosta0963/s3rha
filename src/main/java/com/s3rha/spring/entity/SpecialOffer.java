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
    private LocalDateTime datetimeOfInsert = LocalDateTime.now();
    private BigDecimal price;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "store_price_id")
    private StorePrice storePrice;


    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "offer",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<ProdOfOffer> prodOfOfferList ;
//
//    @ManyToMany(fetch = FetchType.EAGER,
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//                    CascadeType.DETACH, CascadeType.REFRESH})
//    @JoinTable(
//            name = "prod_of_offer",
//            joinColumns = @JoinColumn(name = "offer_id"),
//            inverseJoinColumns = @JoinColumn(name = "Product_id")
//    )
//    private List<Product> products ;
//    public void addProduct(Product product) {
//
//        if (product == null) {
//            products = new ArrayList<>();
//        }
//
//        products.add(product);
//    }
    // Getters and setters
}
