package com.negocio.analitica.service;

import com.negocio.analitica.model.Category;
import com.negocio.analitica.repository.CategoryRepository;
import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        if (categoryRepository == null) {
            throw new IllegalArgumentException("CategoryRepository cannot be null.");
        }
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        validateCategory(category);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category) {
        validateCategory(category);
        if (category.getId() == null ||
                !categoryRepository.findById(category.getId()).isPresent()) {
            throw new IllegalArgumentException("Category does not exist.");
        }
        return categoryRepository.save(category);
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found."));
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Category not found.");
        }
        categoryRepository.deleteById(id);
    }

    private void validateCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }
    }
}
