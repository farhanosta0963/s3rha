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
    @JsonIgnore
    private Integer scoreOfActivity;
    @JsonIgnore
    private Integer scoreOfIntegrity;
    public UserAccount() {
        setAccountType("USER");
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id")
    private List<UserPrice> userPriceList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id")
    private List<ShoppingCart> shoppingCartList;

}
