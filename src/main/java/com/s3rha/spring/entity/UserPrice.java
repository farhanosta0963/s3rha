package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Setter
@Getter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "price_id")
public class UserPrice extends Price{

   private  int upVoteCount ;
   private  int downVoteCount ;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;


}
