package com.s3rha.spring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "price_id")
public class StorePrice extends Price {

    private Integer quantity;

}
