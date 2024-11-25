package com.uydev.converter;


import com.uydev.dto.CategoryDTO;
import com.uydev.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryConverter implements Converter<String, CategoryDTO> {
    private final CategoryService categoryService;

    @Override
    public CategoryDTO convert(String categoryId) {
        if (categoryId == null || categoryId.isEmpty()) {
            return null;

        }
        return categoryService.getCategoryById(Long.parseLong(categoryId));
    }
}
