package com.chriskar.product.dto;

import com.chriskar.product.models.Product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Date createdAt,
        Date updatedAt,
        List<CategoryResponse> categories
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getCategories().stream().map(CategoryResponse::from).toList()
        );
    }
}
