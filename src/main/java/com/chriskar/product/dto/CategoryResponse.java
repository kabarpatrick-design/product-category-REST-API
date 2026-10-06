package com.chriskar.product.dto;

import com.chriskar.product.models.Category;

import java.util.Date;

public record CategoryResponse(Long id, String name, Date createdAt, Date updatedAt) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
