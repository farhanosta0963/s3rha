package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

//9. Junction Table Entities
//        java
//Copy
@Entity
@Setter
@Getter
@NoArgsConstructor
public class ProdOfCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    private Integer quantity;

    // Getters and setters
}
