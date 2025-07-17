package com.s3rha.spring.entity.projections;

import com.s3rha.spring.entity.StoreAccount;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "storeAccountPublic",  types =  StoreAccount.class )
public interface UserAccountProjection {
    String getFname();
    String getLname();
    String getPhoneNumber();
    String getImage();
    Long   getAccountId() ;
//    Boolean getStoreAccountFlag();
//    LocalDateTime getDatetimeOfInsert();
//    BigInteger getOauthId();  // optional — remove if you want to hide it
}
