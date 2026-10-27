package com.negocio.analitica.controller;

import com.negocio.analitica.model.Category;
import com.negocio.analitica.service.CategoryService;
import java.util.List;

public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        if (categoryService == null) throw new IllegalArgumentException("CategoryService cannot be null.");
        this.categoryService = categoryService;
    }

    public Category createCategory(Category category) { return categoryService.createCategory(category); }
    public Category updateCategory(Category category) { return categoryService.updateCategory(category); }
    public Category getCategory(Long id) { return categoryService.findCategoryById(id); }
    public List<Category> getAllCategories() { return categoryService.findAllCategories(); }
    public void deleteCategory(Long id) { categoryService.deleteCategory(id); }
}
