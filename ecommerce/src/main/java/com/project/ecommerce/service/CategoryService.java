package com.project.ecommerce.service;

import com.project.ecommerce.entity.Category;
import com.project.ecommerce.payload.CategoryDTO;
import com.project.ecommerce.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
        CategoryDTO addCategory(CategoryDTO category);
        CategoryResponse getCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder);
        CategoryDTO deleteCategory(Long categoryId);
        CategoryDTO updateCategory(CategoryDTO category,Long categoryId);
}
