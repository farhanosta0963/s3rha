package com.s3rha.spring.projections;


import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.StoreAccount;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Projection(name = "accountPublic",  types = { Account.class, StoreAccount.class })
public interface AccountPublicProjection {
    Long getAccountId();
    String getUserName();
    String getPhoneNumber();
//    String getImage();
//    Boolean getStoreAccountFlag();
//    LocalDateTime getDatetimeOfInsert();
//    BigInteger getOauthId();  // optional — remove if you want to hide it
}
