package com.s3rha.spring.entity.projections;


import com.s3rha.spring.entity.StoreAccount;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "storeAccountPublic",  types =  StoreAccount.class )
public interface StoreAccountProjection {
    String getName();
    String getPhoneNumber();
    String getVerifiedFlag();
    String getStatus()  ;
    String getImage();
    Long   getAccountId() ;
//    Boolean getStoreAccountFlag();
//    LocalDateTime getDatetimeOfInsert();
//    BigInteger getOauthId();  // optional — remove if you want to hide it
}
