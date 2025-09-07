package com.s3rha.spring.dto;

import com.s3rha.spring.controller.components.Recommendation;
import lombok.*;

import java.util.List;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponse {
    private List<Recommendation> recommendations;
    private String status;
    private Long user_id;


}
