package com.s3rha.spring.service.moderator;

import com.s3rha.spring.DAO.PriceRepo;
import com.s3rha.spring.DAO.ProdOfCartRepo;
import com.s3rha.spring.DAO.ProdOfOfferRepo;
import com.s3rha.spring.DAO.ProductRepo;
import com.s3rha.spring.entity.Price;
import com.s3rha.spring.entity.ProdOfCart;
import com.s3rha.spring.entity.ProdOfOffer;
import com.s3rha.spring.entity.Product;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class ProductModerationService {
    private final ProductRepo  productRepo ;
    private final PriceRepo priceRepo;
    private final ProdOfCartRepo prodOfCartRepo ;
    private final ProdOfOfferRepo prodOfOfferRepo ;
    @Transactional
    public void mergeProducts(Long keepId, List<Long> mergeIds) {
        Product keepProduct = productRepo.findById(keepId)
                .orElseThrow(() -> new EntityNotFoundException("Keep product not found"));

        for (Long id : mergeIds) {
            Product duplicate = productRepo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Duplicate product not found"));
            List<Price> priceList = priceRepo.findByProduct(duplicate) ;
            for (Price p : priceList){
                p.setProduct(keepProduct);
            }
            List <ProdOfCart> prodOfCartList = prodOfCartRepo.findByProduct(duplicate) ;
            for (ProdOfCart p  : prodOfCartList){
                p.setProduct(keepProduct);
            }
            List<ProdOfOffer> prodOfOfferList = prodOfOfferRepo.findByProduct(duplicate) ;
            for (ProdOfOffer prodOfOffer: prodOfOfferList){
                prodOfOffer.setProduct(duplicate);
            }
            productRepo.delete(duplicate);
        }
    }
}
