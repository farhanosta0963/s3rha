package com.s3rha.spring.service;

import com.s3rha.spring.DAO.ProductRepo;
import com.s3rha.spring.DAO.StoreAccountRepo;
import com.s3rha.spring.DAO.StorePriceRepo;
import com.s3rha.spring.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCSVService {
    private final ProductRepo productRepository;
    private final StorePriceRepo storePriceRepo;
    private final StoreAccountRepo storeAccountRepo;
    private final OwnershipChecker ownershipChecker;
    @Transactional
    public List<String> processCsv(MultipartFile file) throws IOException {
        String nameOfTheStore = ownershipChecker.getCurrentUser();
        StoreAccount accountOfTheStore =   storeAccountRepo.findByUserName(nameOfTheStore)
                .orElseThrow(()->{
                    log.error("[ processCsv ] StoreAccount :{} not found",nameOfTheStore);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");});

        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int rowIndex = 0;

            // Skip header row
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                rowIndex++;
                String[] values = line.split(",");


                String name = values[0].trim();
                String description = values[1].trim();
                String category = values[2].trim();
                String brand = values[3].trim();
                String barcode = values[4].trim();
                String priceStr = values[5].trim();
                String currency = values[6].trim();
                String unitOfMeasure = values[7].trim();
                String image = values[8].trim();

                // ✅ Enforce barcode and price
                if (barcode.isEmpty()) {
                    errors.add("Row " + rowIndex + ": Barcode is required");
                    continue; // Skip this row
                }

                if (priceStr.isEmpty()) {
                    errors.add("Row " + rowIndex + ": Price is required");
                    continue; // Skip this row
                }

                // Parse price
                BigDecimal priceValue;
                try {
                    priceValue = new BigDecimal(priceStr);
                } catch (NumberFormatException e) {
                    errors.add("Row " + rowIndex + ": Invalid price format");
                    continue;
                }

// Check if product exists
                Product product = productRepository.findByBarCode(barcode).orElse(null);
                if (product == null) {
                    product = new Product();
                    product.setName(name);
                    product.setDescription(description);
                    product.setCategory(category);
                    product.setBarCode(barcode);
                    product.setImage(image);
                    product.setAccount(accountOfTheStore);
                }

// Create price entity
                StorePrice priceEntity = new StorePrice();
                priceEntity.setProduct(product);
                priceEntity.setPrice(priceValue);
                priceEntity.setCurrency(currency);
                priceEntity.setUnitOfMeasure(unitOfMeasure);
                priceEntity.setStoreAccount(accountOfTheStore);
// Optionally set store if you have it in CSV
// priceEntity.setStoreAccount(storeAccount);
                storePriceRepo.save(priceEntity);
            }

            return errors;
        }
    }
}
