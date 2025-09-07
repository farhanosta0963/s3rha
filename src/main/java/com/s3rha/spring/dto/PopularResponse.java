package com.s3rha.spring.dto;

import com.s3rha.spring.controller.components.Recommendation;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PopularResponse {
    private List<Recommendation> popular_products;
    private String status;



}