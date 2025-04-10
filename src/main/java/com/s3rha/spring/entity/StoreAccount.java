package com.s3rha.spring.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//2. Store Account Entity
////        java
//Copy
@Entity
@PrimaryKeyJoinColumn(name = "account_id")
@Setter
@Getter

public class StoreAccount extends Account {

    public StoreAccount() {
        setAccountType("STORE");
    }
    private String name;
    private Boolean verifiedFlag;

//    @OneToMany(mappedBy = "storeAccount",
//            fetch = FetchType.EAGER,
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//                    CascadeType.DETACH, CascadeType.REFRESH})
//    private List<Address> addresses;

//    public void add(Address address) {
//        if (addresses == null) {
//            addresses = new ArrayList<>();
//        }
//        addresses.add(address);
//         }
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH},mappedBy = "storeAccount")

    private List<RatingOnStore> ratingOnStores;

    // Getters and setters
}
