package com.s3rha.spring.controller;


import com.s3rha.spring.DAO.ProductRepo;
import com.s3rha.spring.controller.components.Recommendation;
import com.s3rha.spring.dto.PopularResponse;
import com.s3rha.spring.dto.RecommendationResponse;
import com.s3rha.spring.dto.SimilarResponse;
import com.s3rha.spring.service.ProductCSVService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ProductCsvUploadController  {
    private final RestTemplate restTemplate;
    private final ProductRepo productRepo;
    private final ProductCSVService productCSVService ;

    @PostMapping("/upload-csv")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("CSV file is required");
        }

        try {
            List<String> errors = productCSVService.processCsv(file);
            if (!errors.isEmpty()) {
                return ResponseEntity.ok("we had some error adding prices for these products \n " + errors);
            }
            return ResponseEntity.ok("CSV processed successfully and the prices have been added");
        } catch (Exception e) {
            log.error("Error processing CSV", e);
            return ResponseEntity.internalServerError().body("Failed to process CSV");
        }
    }
}