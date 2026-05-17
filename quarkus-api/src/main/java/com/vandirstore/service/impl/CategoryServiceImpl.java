package com.vandirstore.service.impl;

import com.vandirstore.dto.CategoryDTO;
import com.vandirstore.model.Category;
import com.vandirstore.repository.CategoryRepository;
import com.vandirstore.service.ICategoryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryServiceImpl implements ICategoryService {

    @Inject
    CategoryRepository categoryRepository;

    private CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    @Override
    public CategoryDTO findById(Integer id) {
        return toDTO(categoryRepository.findById(id.longValue()));
    }

    @Override
    public List<CategoryDTO> listAllCategories() {
        return categoryRepository.listAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        
        categoryRepository.persist(category);
        return toDTO(category);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Integer id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id.longValue());
        if (existingCategory != null) {
            existingCategory.setName(categoryDTO.getName());
            existingCategory.setDescription(categoryDTO.getDescription());
        }
        return toDTO(existingCategory);
    }

    @Override
    @Transactional
    public boolean deleteCategory(Integer id) {
        return categoryRepository.deleteById(id.longValue());
    }
}
