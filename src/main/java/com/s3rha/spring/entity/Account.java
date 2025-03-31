package com.s3rha.spring.entity;

//Here are the Spring Data JPA entities for your database schema, designed to work with Spring Data REST:
//
////        1. Base Account Entity (Inheritance Strategy)

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Setter
@Getter
@NoArgsConstructor
public class Account {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String roles ;
    private String email;
    private String userName;


    private String password;
    private String status;
    private String phoneNumber;
    private String image;
    // Lifecycle callback to encrypt password before saving
    @PrePersist
    @PreUpdate
    private void encryptPassword() {
        if (password != null && !password.startsWith("$2a$")) { // Check if already encrypted
            password = passwordEncoder.encode(password);
        }
    }
    @CreatedDate
    private LocalDateTime datetimeOfInsert;


    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Report> reports;
    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Product> products;

    public void add(Report report) {
        if (reports == null) {
            reports = new ArrayList<>();
        }
        reports.add(report);
       }

    public void add(Product product) {
        if (products == null) {
            products =  new ArrayList<>() ;
        }
        products.add(product);
       }



    // Getters and setters
}


//Key Implementation Notes:
//Inheritance Strategy: Uses JOINED inheritance for account types
//
//Composite Keys: Uses @IdClass for tables without single primary key
//
//Date Handling: Uses LocalDateTime for temporal fields
//
//Relationship Mappings:
//
//@ManyToOne for foreign key relationships
//
//@OneToOne for shared primary key relationships
//
//@MapsId for derived identity relationships
//
//Naming Conventions: Converts snake_case to camelCase for Java fields
//
//To expose these entities via REST:
//
//Create repository interfaces for each entity:
//
//java
//        Copy
//public interface AccountRepository extends JpaRepository<Account, Long> {}
//public interface ProductRepository extends JpaRepository<Product, Long> {}
//Customize exposure using @RepositoryRestResource
//
//Use projections for customized responses
//
//This implementation maintains the database relationships while providing RESTful endpoints through Spring Data REST.