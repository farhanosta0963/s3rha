package com.s3rha.spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ProductMergeByUrlRequest {
    private String keepProductUrl;
    private List<String> duplicateProductUrls;

    // Getters and setters
}
