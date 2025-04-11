package com.s3rha.spring.entity;

//import com.s3rha.spring.entityListener.PriceEntityListener;
//import com.s3rha.spring.entityListener.ShoppingCartEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
//@EntityListeners(PriceEntityListener.class)
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PriceId;
    private BigDecimal price;
    private LocalDateTime datetimeOfInsert = LocalDateTime.now();
    private String currency;
    private String unitOfMeasure;






    // Getters and setters
}
