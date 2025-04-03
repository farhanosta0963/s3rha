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
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Increase the length to a value that can accommodate your actual token lengths
    @Column( nullable = false, length = 10000)
    private String VerificationCode;

    private LocalDateTime VerificationCodeExpireTime;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
