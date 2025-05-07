package com.s3rha.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@PrimaryKeyJoinColumn(name = "price_id")
public class StorePrice extends Price {
    public StorePrice() {
        setIsStorePrice(true);
    }

    private Integer quantity;

}
