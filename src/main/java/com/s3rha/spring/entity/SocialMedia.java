package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor

public class SocialMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long social_media_id ;

    private String socialMediaAccount;
    private LocalDateTime datetimeOfInsert =LocalDateTime.now() ;
    private String accountType;






    // Getters and setters
}
