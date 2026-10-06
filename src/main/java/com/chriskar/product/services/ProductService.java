package com.chriskar.product.services;

import com.chriskar.product.dto.ProductRequest;
import com.chriskar.product.dto.ProductResponse;
import com.chriskar.product.exceptions.ResourceNotFoundException;
import com.chriskar.product.models.Category;
import com.chriskar.product.models.Product;
import com.chriskar.product.repositories.CategoryRepository;
import com.chriskar.product.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return toResponses(productRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return ProductResponse.from(getProduct(id));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> searchByName(String name) {
        return toResponses(productRepository.findByNameContainingIgnoreCase(name));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findByPriceRange(BigDecimal min, BigDecimal max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must not be greater than max");
        }
        return toResponses(productRepository.findByPriceBetween(min, max));
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        apply(product, request);
        return ProductResponse.from(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getProduct(id);
        apply(product, request);
        // flush so @PreUpdate sets updatedAt before the response is built
        return ProductResponse.from(productRepository.saveAndFlush(product));
    }

    public void delete(Long id) {
        productRepository.delete(getProduct(id));
    }

    private void apply(Product product, ProductRequest request) {
        product.setName(request.name().trim());
        product.setDescription(request.description().trim());
        product.setPrice(request.price());
        product.setCategories(resolveCategories(request.categoryIds()));
    }

    private List<Category> resolveCategories(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new ArrayList<>();
        }
        Set<Long> uniqueIds = new LinkedHashSet<>(categoryIds);
        List<Category> categories = categoryRepository.findAllById(uniqueIds);
        if (categories.size() != uniqueIds.size()) {
            List<Long> found = categories.stream().map(Category::getId).toList();
            List<Long> missing = uniqueIds.stream().filter(id -> !found.contains(id)).toList();
            throw new ResourceNotFoundException("Categories not found: " + missing);
        }
        return new ArrayList<>(categories);
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found"));
    }

    private List<ProductResponse> toResponses(List<Product> products) {
        return products.stream().map(ProductResponse::from).toList();
    }
}
