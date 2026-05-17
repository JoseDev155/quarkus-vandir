package com.vandirstore.service;

import com.vandirstore.dto.CategoryDTO;
import java.util.List;

public interface ICategoryService {
    CategoryDTO findById(Integer id);
    List<CategoryDTO> listAllCategories();
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Integer id, CategoryDTO categoryDTO);
    boolean deleteCategory(Integer id);
}
