package com.s3rha.spring.dto;


import com.s3rha.spring.controller.components.Recommendation;
import lombok.*;

import java.util.List;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimilarResponse {
    private List<Recommendation> similar_products;
    private String status;
    private Long product_id;


}
