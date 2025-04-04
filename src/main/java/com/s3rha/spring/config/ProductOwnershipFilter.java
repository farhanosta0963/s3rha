package com.s3rha.spring.config;

import com.s3rha.spring.DAO.ProductRepo;
import com.s3rha.spring.entity.Product;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.context.SecurityContextHolder;


import java.io.IOException;
@RequiredArgsConstructor
public class ProductOwnershipFilter extends OncePerRequestFilter {

    private final ProductRepo productRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        // TODO add a pass for admin
        // Skip for non-modifying methods or public endpoints
        if (!requiresOwnershipCheck(request)) {
            chain.doFilter(request, response);
            return;
        }

        // Extract product ID from path (e.g., /products/123)
        Long productId = Long.parseLong( extractProductId(request));
        String currentUsername = getCurrentUsername();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Check ownership
        if (!product.getAccount().getUserName().equals(currentUsername)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "You don't own this product");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean requiresOwnershipCheck(HttpServletRequest request) {

        String method = request.getMethod();
        return ("PUT".equals(method)  || "DELETE".equals(method)||"PATCH".equals(method));
    }

    private String extractProductId(HttpServletRequest request) {
        // Implement based on your URL pattern
        String z = request.getRequestURI().split("/")[3] ;
        System.out.println(z);
        return z  ; // /products/{id}
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}