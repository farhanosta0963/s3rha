package com.s3rha.spring.controller.moderator;

import com.s3rha.spring.dto.ProductMergeByUrlRequest;
import com.s3rha.spring.service.OwnershipChecker;
import com.s3rha.spring.service.moderator.ProductModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductModerationController {
    private final OwnershipChecker ownershipChecker;
    private final ProductModerationService productModerationService;


    //TODO i need to add propper logging
    @PostMapping("/merge-by-url")
    public ResponseEntity<?> mergeByUrls(@RequestBody ProductMergeByUrlRequest request) {
        ownershipChecker.assertAdminOrModerator();
        Long keepId = extractIdFromUrl(request.getKeepProductUrl());
        List<Long> mergeIds = request.getDuplicateProductUrls()
                .stream()
                .map(this::extractIdFromUrl)
                .filter(id -> !id.equals(keepId)) // avoid self-merge
                .toList();

        productModerationService.mergeProducts(keepId, mergeIds);

        return ResponseEntity.ok("Products merged successfully");
    }

    private Long extractIdFromUrl(String url) {
        try {
            // Example: http://localhost:8080/api/products/123 → extract 123
            String[] parts = url.split("/");
            return Long.parseLong(parts[parts.length - 1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid product URL: " + url);
        }
    }
}
