package com.chriskar.product.repositories;

import com.chriskar.product.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategories_Id(Long categoryId);
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
}
