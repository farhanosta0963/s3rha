package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Setter
@Getter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "report_id")
@Table(name = "report_on_product")
public class ReportOnProduct extends Report {



//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.DETACH, CascadeType.REFRESH})
//    @JoinColumn(name = "product_id")
//    private Product product;

    // Getters and setters
}
