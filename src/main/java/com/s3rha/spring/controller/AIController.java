package com.s3rha.spring.controller;

import com.s3rha.spring.DAO.ProductRepo;
import com.s3rha.spring.controller.components.Recommendation;
import com.s3rha.spring.dto.PopularResponse;
import com.s3rha.spring.dto.RecommendationResponse;
import com.s3rha.spring.dto.SimilarResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AIController {
    private final RestTemplate restTemplate;
    private final ProductRepo productRepo;
    @GetMapping("/recommend/{id}")
    public ResponseEntity<?> recommendedProducts(@PathVariable Long id ){

            String url = "http://127.0.0.1:5000/recommend/" + id;

            RecommendationResponse response =
                    restTemplate.getForObject(url, RecommendationResponse.class);

            if (response == null || !"success".equalsIgnoreCase(response.getStatus())) {
                throw new RuntimeException("Flask service returned failure or null");
            }

        List<Long> productIds = response.getRecommendations().stream()
                .map(Recommendation::getProduct_id)
                .collect(Collectors.toList());



        // Build the internal SD REST URL with comma-separated IDs
        String idsParam = productIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String sdRestUrl = "http://localhost:8080/api/products/search/findByProductIdIn?ids=" + idsParam;

        // Call Spring Data REST internally
        ResponseEntity<String> sdRestResponse = restTemplate.getForEntity(sdRestUrl, String.class);

        // Return SD REST response as-is to the client
        return ResponseEntity.status(sdRestResponse.getStatusCode())
                .body(sdRestResponse.getBody());

    }


    @GetMapping("/similar/{id}")
    public ResponseEntity<?> similarProducts(@PathVariable Long id ){

        String url = "http://127.0.0.1:5000/similar/" + id;

        SimilarResponse response =
                restTemplate.getForObject(url, SimilarResponse.class);
        System.out.println(response.getProduct_id());
        if (response == null || !"success".equalsIgnoreCase(response.getStatus())) {
            throw new RuntimeException("Flask service returned failure or null");
        }

        List<Long> productIds = response.getSimilar_products().stream()
                .map(Recommendation::getProduct_id)
                .collect(Collectors.toList());

        for (Long z : productIds){
            System.out.println(z);
        }


        // Build the internal SD REST URL with comma-separated IDs
        String idsParam = productIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String sdRestUrl = "http://localhost:8080/api/products/search/findByProductIdIn?ids=" + idsParam;

        // Call Spring Data REST internally
        ResponseEntity<String> sdRestResponse = restTemplate.getForEntity(sdRestUrl, String.class);

        // Return SD REST response as-is to the client
        return ResponseEntity.status(sdRestResponse.getStatusCode())
                .body(sdRestResponse.getBody());

    }

    @GetMapping("/popular")
    public ResponseEntity<?> popularProducts(){

        String url = "http://127.0.0.1:5000/popular";

        PopularResponse response =
                restTemplate.getForObject(url, PopularResponse.class);

        if (response == null || !"success".equalsIgnoreCase(response.getStatus())) {
            throw new RuntimeException("Flask service returned failure or null");
        }

        List<Long> productIds = response.getPopular_products().stream()
                .map(Recommendation::getProduct_id)
                .collect(Collectors.toList());



        // Build the internal SD REST URL with comma-separated IDs
        String idsParam = productIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String sdRestUrl = "http://localhost:8080/api/products/search/findByProductIdIn?ids=" + idsParam;

        // Call Spring Data REST internally
        ResponseEntity<String> sdRestResponse = restTemplate.getForEntity(sdRestUrl, String.class);

        // Return SD REST response as-is to the client
        return ResponseEntity.status(sdRestResponse.getStatusCode())
                .body(sdRestResponse.getBody());

    }



}
