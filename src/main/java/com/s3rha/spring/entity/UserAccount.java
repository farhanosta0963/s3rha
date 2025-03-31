package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

//3. User Account Entity
//        java
//Copy
@Entity
@PrimaryKeyJoinColumn(name = "account_id")
@Setter
@Getter
@NoArgsConstructor
public class UserAccount extends Account {
    private String fname;
    private String lname;
    private Integer scoreOfActivity;
    private Integer scoreOfIntegrity;

    // Getters and setters

    @OneToMany(mappedBy = "userAccount",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<UserPrice> userPrice;

    @OneToMany(mappedBy = "userAccount",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})

    private List<ShoppingCart> shoppingCarts;

}
