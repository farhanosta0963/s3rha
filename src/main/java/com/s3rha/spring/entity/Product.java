package com.s3rha.spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

//4. Product Entity
//java
//        Copy
@Entity
@Setter
@Getter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotEmpty(message = "Name is required")  // ← Validates non-empty strings
    private String name;
    private String brand;
    private String image;
    private String category;
    private String barCode;
    private String description;
    private LocalDateTime datetimeOfInsert = LocalDateTime.now() ;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id")
//    private List<ProdOfCart> prodOfCartList;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id")
//    private List<ProdOfOffer> prodOfOfferList;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id")
//    private List<Price> priceList;

//    @OneToMany(cascade = CascadeType.ALL )
//    @JoinColumn(name = "product_id")
//    private List<RatingOnProduct> ratingOnProductList;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id")
//    private List<ReportOnProduct> reportOnProductList;

    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "account_id")
    private Account account ;

    // Getters and setters
}
