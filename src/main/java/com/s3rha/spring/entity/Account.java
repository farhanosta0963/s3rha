package com.s3rha.spring.entity;

//Here are the Spring Data JPA entities for your database schema, designed to work with Spring Data REST:
//
////        1. Base Account Entity (Inheritance Strategy)

//import com.s3rha.spring.entityListener.AccountEntityListener;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Setter
@Getter
@Slf4j
//@EntityListeners(AccountEntityListener.class)
public  class   Account {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private BigInteger oauthId;
    private String roles = "ROLE_USER";
    @Email
    private String email;
    @NotEmpty
    private String userName;
    private String password;
    private String status;
    private String phoneNumber;
    private String image;
    private Boolean isStoreAccount ;
    private LocalDateTime datetimeOfInsert = LocalDateTime.now();

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "account_id")
//    private VerificationCode verificationCode;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<RefreshToken> refreshTokenList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<Product> productList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<Rating> ratingList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<Report> reportList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "reported_account_id")
    private List<ReportOnAccount> reportOnAccountList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<SearchHistory> searchHistoryList;


    @PreUpdate
    private void encryptPassword() {
        if (password != null && !password.startsWith("$2a$")) { // Check if already encrypted
            password = passwordEncoder.encode(password);
        }
    }
    public void addRefreshToken(RefreshToken refreshToken) {
        if (refreshTokenList == null) {
            refreshTokenList = new ArrayList<>();
        }
        refreshTokenList.add(refreshToken);
    }

    public void removeRefreshToken(RefreshToken refreshToken) {
        if (refreshTokenList != null) {
            refreshTokenList.remove(refreshToken);
        }
    }

//    public void addVerificationCode(VerificationCode verificationCode) {
//        if (verificationCodeList == null) {
//            verificationCodeList = new ArrayList<>();
//        }
//        verificationCodeList.add(verificationCode);
//    }
//
//    public void removeVerificationCode(VerificationCode verificationCode) {
//        if (verificationCodeList != null) {
//            verificationCodeList.remove(verificationCode);
//        }
//    }

    public void addProduct(Product product) {
        if (productList == null) {
            productList = new ArrayList<>();
        }
        productList.add(product);
    }

    public void removeProduct(Product product) {
        if (productList != null) {
            productList.remove(product);
        }
    }

    public void addRating(Rating rating) {
        if (ratingList == null) {
            ratingList = new ArrayList<>();
        }
        ratingList.add(rating);
    }

    public void removeRating(Rating rating) {
        if (ratingList != null) {
            ratingList.remove(rating);
        }
    }

    public void addReport(Report report) {
        if (reportList == null) {
            reportList = new ArrayList<>();
        }
        reportList.add(report);
    }

    public void removeReport(Report report) {
        if (reportList != null) {
            reportList.remove(report);
        }
    }

    public void addReportOnAccount(ReportOnAccount reportOnAccount) {
        if (reportOnAccountList == null) {
            reportOnAccountList = new ArrayList<>();
        }
        reportOnAccountList.add(reportOnAccount);
    }

    public void removeReportOnAccount(ReportOnAccount reportOnAccount) {
        if (reportOnAccountList != null) {
            reportOnAccountList.remove(reportOnAccount);
        }
    }

    public void addSearchHistory(SearchHistory searchHistory) {
        if (searchHistoryList == null) {
            searchHistoryList = new ArrayList<>();
        }
        searchHistoryList.add(searchHistory);
    }

    public void removeSearchHistory(SearchHistory searchHistory) {
        if (searchHistoryList != null) {
            searchHistoryList.remove(searchHistory);
        }
    }


}
//    @PreRemove
//    public void zxc (){
//        this.accountId
//    }
//    @OneToMany(mappedBy = "account",
//            fetch = FetchType.EAGER,
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//                    CascadeType.DETACH, CascadeType.REFRESH})
//
//    private List<Report> reports;
//    @OneToMany(mappedBy = "account",
//            fetch = FetchType.EAGER,
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//                    CascadeType.DETACH, CascadeType.REFRESH})
//
//
//    private List<Product> products;

//    public void add(Report report) {
//        if (reports == null) {
//            reports = new ArrayList<>();
//        }
//        reports.add(report);
//       }
//
//    public void add(Product product) {
//        if (products == null) {
//            products =  new ArrayList<>() ;
//        }
//        products.add(product);
//       }
//


    // Getters and setters



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