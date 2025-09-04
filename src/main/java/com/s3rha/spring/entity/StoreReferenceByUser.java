package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class StoreReferenceByUser {
//    CREATE TABLE store_refrence_by_user (
//            refrence_id bigint primary key ,
//            refrencing_account_id bigint ,
//            refrenced_store_id bigint,
//            name VARCHAR(255),
//    verified_flag BOOLEAN,
//    refrence_made_by_user_flag BOOLEAN,
//    owner_proof_doc TEXT ,
//    FOREIGN KEY ( refrenced_store_id ) REFERENCES Store_account (account_id)
//    ON UPDATE CASCADE
//    ON DELETE CASCADE ,
//    FOREIGN KEY ( refrencing_account_id ) REFERENCES Account (account_id)
//    ON UPDATE CASCADE
//    ON DELETE CASCADE
//);
//

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long referenceId ;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "referencing_account_id")
    private Account referencingAccount;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "referenced_store_id")
    private StoreAccount referencedStoreAccount;


}
