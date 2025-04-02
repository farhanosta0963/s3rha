package com.s3rha.spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    private String fname;
    @NotEmpty
    private String lname;
    private Integer scoreOfActivity;
    private Integer scoreOfIntegrity;


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
