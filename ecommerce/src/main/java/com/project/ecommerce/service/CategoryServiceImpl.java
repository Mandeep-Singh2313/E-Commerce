package com.project.ecommerce.service;

import com.project.ecommerce.entity.Category;
import com.project.ecommerce.exception.APIException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.payload.CategoryDTO;
import com.project.ecommerce.payload.CategoryResponse;
import com.project.ecommerce.repo.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private CategoryRepository categoryRepository;

    private ModelMapper modelMapper;

     public CategoryServiceImpl(CategoryRepository categoryRepository,ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper=modelMapper;
    }

    @Override
    @Transactional
    public CategoryDTO addCategory(CategoryDTO category) {
        Category savedCategory=categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory!=null){
            throw new APIException("Category "+category.getCategoryName()+" already exists");
        }
        Category category1=modelMapper.map(category,Category.class);
        Category saved=categoryRepository.save(category1);
        return modelMapper.map(saved,CategoryDTO.class);
    }

    @Override
    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize,
                                          String sortBy, String sortOrder) {
         Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage=categoryRepository.findAll(pageDetails);

        List<Category> categories=categoryPage.getContent();
        if (categories.isEmpty()){
            throw new APIException("No categories found");
        }
        List<CategoryDTO> categoryDTOS=categories.stream()
                .map(category -> modelMapper.map(category,CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        List<Category> categories=categoryRepository.findAll();
        Category catgory=categories.stream()
                .filter(c->c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElse(null);
        if (catgory==null){
            throw new ResourceNotFoundException("Category","Category ID", categoryId );
        }
        categoryRepository.delete(catgory);
        return modelMapper.map(catgory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO category, Long categoryId) {
        List<Category> categories=categoryRepository.findAll();
        Category existingCategory=categories.stream()
                .filter(c->c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElse(null);
        if (existingCategory==null){
            throw new ResourceNotFoundException("Category","Category ID", categoryId);
        }
        existingCategory.setCategoryName(category.getCategoryName());
        Category saved=categoryRepository.save(existingCategory);
        return modelMapper.map(saved,CategoryDTO.class);
    }
}
