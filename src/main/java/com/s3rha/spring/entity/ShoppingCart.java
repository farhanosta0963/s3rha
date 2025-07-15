package com.s3rha.spring.entity;

//import com.s3rha.spring.entityListener.ShoppingCartEntityListener;
//import com.s3rha.spring.entityListener.ShoppingCartEntityListener;
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
//@EntityListeners(ShoppingCartEntityListener.class)
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingCartId;
    private String name;
    private String description;
    private String image;
    private Boolean publicPrivateFlag;
    private LocalDateTime datetimeOfInsert = LocalDateTime.now();

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "shopping_cart_id")
//    private List<ProdOfCart> prodOfCartList;
//
//    public void addProdOfCart(ProdOfCart prodOfCart) {
//        if (prodOfCartList == null) {
//            prodOfCartList = new ArrayList<>();
//        }
//        prodOfCartList.add(prodOfCart);
//    }
//
//    public void removeProdOfCart(ProdOfCart prodOfCart) {
//        if (prodOfCartList != null) {
//            prodOfCartList.remove(prodOfCart);
//        }
//    }
//

    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;


}

//
//    @ManyToMany(fetch = FetchType.EAGER,
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//                    CascadeType.DETACH, CascadeType.REFRESH})
//    @JoinTable(
//            name = "prod_of_cart",
//            joinColumns = @JoinColumn(name = "Shopping_Cart_id"),
//            inverseJoinColumns = @JoinColumn(name = "Product_id")
//    )
//    private List<Product> products ;

//    @PreRemove
//    public void preremo (){
//        System.out.println("trying to delete this from a preRemove ");
////        this.setUserAccount(null);
////        this.setProducts(null);
//////        shoppingCartRepo.saveAndFlush(this);
//        if (this.getUserAccount() != null) {
//            this.getUserAccount().getShoppingCarts().remove(this);
////        }
//    }
//    }
//    public void addProduct (Product product) {
//
//        if (this.products == null) {
//            products = new ArrayList<>();
//        }
//
//        products.add(product);
//    } }
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


