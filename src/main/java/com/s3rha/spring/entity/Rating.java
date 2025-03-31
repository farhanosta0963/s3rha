package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//8. Rating Entities
//java
//        Copy
@Entity
@Setter
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;
    private Integer starsRate;
    private String feedback;
    private LocalDateTime datetimeOfInsert;
    @PrePersist // Automatically set before saving
    protected void onCreate() {
        this.datetimeOfInsert = LocalDateTime.now();
    }
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "account_id")
    private Account Account;

    // Getters and setters
}
