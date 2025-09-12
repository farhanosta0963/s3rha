package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Entity
@Setter
@Getter
@PrimaryKeyJoinColumn(name = "price_id")
public class UserPrice extends Price{

    public UserPrice () {
       setIsStorePrice(false);
    }

   private  Integer upVoteCount = 0  ;

   private  Integer downVoteCount = 0  ;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;



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


    public void addUpVotedAccount(Account account) {
        if (!upVotedAccountList.contains(account)) {
            upVotedAccountList.add(account);
        }
    }

    public void removeUpVotedAccount(Account account) {
        upVotedAccountList.remove(account);
    }

    public void addDownVotingAccount(Account account) {
        if (!downVotingAccountList.contains(account)) {
            downVotingAccountList.add(account);
        }
    }

    public void removeDownVotingAccount(Account account) {
        downVotingAccountList.remove(account);
    }

    public boolean hasUpvoted(Account account) {
        if (upVotedAccountList == null || account == null) return false;
        return upVotedAccountList.parallelStream()
                .anyMatch(a -> a.getAccountId().equals(account.getAccountId()));
    }

    /**
     * Check if a given account has downvoted this price
     */
    public boolean hasDownvoted(Account account) {
        if (downVotingAccountList == null || account == null) return false;
        return downVotingAccountList.parallelStream()
                .anyMatch(a -> a.getAccountId().equals(account.getAccountId()));
    }


}
