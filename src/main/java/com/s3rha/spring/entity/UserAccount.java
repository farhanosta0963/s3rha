package com.s3rha.spring.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

//3. User Account Entity
//        java
//Copy
@Entity
@PrimaryKeyJoinColumn(name = "account_id")
@Setter
@Getter

public class UserAccount extends Account {

    private String fname;
    private String lname;
    private Integer scoreOfActivity;
    private Integer scoreOfIntegrity;
    public UserAccount() {
        setAccountType("USER");
    }
    @OneToMany(mappedBy = "userAccount",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<UserPrice> userPrice;

    @OneToMany(mappedBy = "userAccount",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})

    private List<ShoppingCart> shoppingCarts;

}
