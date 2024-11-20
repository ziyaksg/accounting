package com.uydev.services;

import com.uydev.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getCategoryList();

    CategoryDTO getCategoryById(Long id);

    void update(CategoryDTO categoryDTO);

    void deleteById(Long id);

    void create(CategoryDTO newCategory);
}
