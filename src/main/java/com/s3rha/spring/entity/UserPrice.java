package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Setter
@Getter
@PrimaryKeyJoinColumn(name = "price_id")
public class UserPrice extends Price{

    public UserPrice () {
       setIsStorePrice(false);
    }

   private  Integer upVoteCount ;
   private  Integer downVoteCount ;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<Product> productList;

    @ManyToMany(fetch = FetchType.LAZY,//TODO test for delete  issues here
            cascade =  {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "up_vote_on_user_price",
            joinColumns = @JoinColumn(name = "price_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> upVotedAccountList;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade =  {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "down_vote_on_user_price",
            joinColumns = @JoinColumn(name = "price_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> downVotingAccountList;

}
