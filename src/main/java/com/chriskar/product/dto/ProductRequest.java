package com.chriskar.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        @NotBlank
        @Size(min = 2, max = 100)
        String name,
        @NotBlank
        @Size(min = 2, max = 1000)
        String description,
        @NotNull
        @DecimalMin("0.00")
        BigDecimal price,
        List<@NotNull Long> categoryIds
) {
}
