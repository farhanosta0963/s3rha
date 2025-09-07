package com.s3rha.spring.controller.components;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Setter
@Getter
public class Recommendation {
    private String category;
    private String name;
    private Long product_id;
    private String reason;
    private Double score;

}
