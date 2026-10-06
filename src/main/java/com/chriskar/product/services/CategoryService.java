package com.chriskar.product.services;

import com.chriskar.product.dto.CategoryRequest;
import com.chriskar.product.dto.CategoryResponse;
import com.chriskar.product.dto.ProductResponse;
import com.chriskar.product.exceptions.DuplicateResourceException;
import com.chriskar.product.exceptions.ResourceNotFoundException;
import com.chriskar.product.models.Category;
import com.chriskar.product.models.Product;
import com.chriskar.product.repositories.CategoryRepository;
import com.chriskar.product.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(CategoryResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return CategoryResponse.from(getCategory(id));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findProducts(Long id) {
        getCategory(id);
        return productRepository.findByCategories_Id(id).stream().map(ProductResponse::from).toList();
    }

    public CategoryResponse create(CategoryRequest request) {
        String name = request.name().trim();
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Category '" + name + "' already exists");
        }
        Category category = new Category();
        category.setName(name);
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getCategory(id);
        String name = request.name().trim();
        categoryRepository.findByNameIgnoreCase(name)
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new DuplicateResourceException("Category '" + name + "' already exists");
                });
        category.setName(name);
        // flush so @PreUpdate sets updatedAt before the response is built
        return CategoryResponse.from(categoryRepository.saveAndFlush(category));
    }

    public void delete(Long id) {
        Category category = getCategory(id);
        // Product owns the join table, so the links must be removed from the product side
        for (Product product : productRepository.findByCategories_Id(id)) {
            product.getCategories().remove(category);
        }
        categoryRepository.delete(category);
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category " + id + " not found"));
    }
}
