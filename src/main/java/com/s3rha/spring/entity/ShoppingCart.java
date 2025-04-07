package com.s3rha.spring.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//5. Shopping Cart Entity
//        java
//Copy
@Entity
@Setter
@Getter
@NoArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingCartId;
    private String name;
    private String description;
    private String image;
    private Boolean publicPrivateFlag;
    private LocalDateTime datetimeOfInsert = LocalDateTime.now();

    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "prod_of_cart",
            joinColumns = @JoinColumn(name = "Shopping_Cart_id"),
            inverseJoinColumns = @JoinColumn(name = "Product_id")
    )
    private List<Product> products ;

    public void addProduct(Product product) {

        if (product == null) {
            products = new ArrayList<>();
        }

        products.add(product);
    }
//
//    @PostPersist
//    public void onCreate() {
//        System.out.println("Cart persisted in DB");
//    }
////    @PostConstruct
////    public void onConstruct () {
////        System.out.println("Cart construct ");
////    }
    // Getters and setters

}
