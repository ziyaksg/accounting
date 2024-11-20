package com.uydev.services.impl;

import com.uydev.dto.CategoryDTO;
import com.uydev.entity.Category;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.CategoryRepository;
import com.uydev.services.CategoryService;
import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
private final CategoryRepository categoryRepository;
private final SecurityService securityService;
private final MapperUtil mapperUtil;
    @Override
    public List<CategoryDTO> getCategoryList() {
        Long companyId=securityService.getLoggedInUser().getCompany().getId();
      List<Category> category= categoryRepository.findCategoriesByCompany_IdAndIsDeleted(companyId,false);
        return  category.stream().map(category1 ->mapperUtil.convert(category1,new CategoryDTO())).toList();
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return mapperUtil.convert( categoryRepository.findById(id),new CategoryDTO());
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
       save(categoryDTO);
    }

    @Override
    public void deleteById(Long id) {
   Category category= categoryRepository.findById(id).get();
   category.setIsDeleted(true);
   categoryRepository.save(category);

    }

    @Override
    public void create(CategoryDTO newCategory) {
      save(newCategory);
    }

    public void save(CategoryDTO category) {
        category.setCompany(securityService.getLoggedInUser().getCompany());
        categoryRepository.save(mapperUtil.convert(category,new Category()));
    }
}
