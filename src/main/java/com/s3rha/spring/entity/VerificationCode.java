package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class VerificationCode {//TODO put the cascade on delete with sql for this >>>>

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Increase the length to a value that can accommodate your actual token lengths
    @Column( nullable = false, length = 10000)
    private String verificationCode;

    private LocalDateTime verificationCodeExpireTime;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "account_id")
    private Account account;

//@PreRemove
//    public void asdfzx (){
//    this.setAccount(null);
//
//}
}
