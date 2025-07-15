package com.s3rha.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.s3rha.spring.entityListener.UserAccountEntityListener;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//3. User Account Entity
//        java
//Copy
@Entity
@PrimaryKeyJoinColumn(name = "account_id")
@Setter
@Getter
//@EntityListeners(UserAccountEntityListener.class)
public class UserAccount extends Account {

    private String fname;
    private String lname;
    private Integer scoreOfActivity;
/*
     TODO add some
     logging logic To control
     the adding and subtracting
     from this score so that at
     every method that change this
     score it would go and check the logging table if that user
     added so much activity in that date we will lock the score out and don't
     give him  anymore reward for that day
 */
    private Integer scoreOfIntegrity;
    public UserAccount() {
        setStoreAccountFlag(false);
    }

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_account_id")
//    private List<UserPrice> userPriceList;
//
//    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_account_id")
//    private List<ShoppingCart> shoppingCartList;
//
//    public void addUserPrice(UserPrice userPrice) {
//        if (userPriceList == null) {
//            userPriceList = new ArrayList<>();
//        }
//        userPriceList.add(userPrice);
//    }
//
//    public void removeUserPrice(UserPrice userPrice) {
//        if (userPriceList != null) {
//            userPriceList.remove(userPrice);
//        }
//    }
//
//    public void addShoppingCart(ShoppingCart shoppingCart) {
//        if (shoppingCartList == null) {
//            shoppingCartList = new ArrayList<>();
//        }
//        shoppingCartList.add(shoppingCart);
//    }
//
//    public void removeShoppingCart(ShoppingCart shoppingCart) {
//        if (shoppingCartList != null) {
//            shoppingCartList.remove(shoppingCart);
//        }
//    }


}
