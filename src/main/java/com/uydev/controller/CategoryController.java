package com.uydev.controller;


import com.uydev.dto.CategoryDTO;
import com.uydev.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller

@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private  final CategoryService categoryService;
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("categories", categoryService.getCategoryList());
        return "/category/category-list";
    }
@GetMapping("/create")
public String create(Model model) {
    model.addAttribute("newCategory", new CategoryDTO());
        return "/category/category-create";
}
    @PostMapping("/create")
    public String create(@ModelAttribute("newCategory") CategoryDTO newCategory) {
   categoryService.create(newCategory);
        return "redirect:/categories/list";
    }
    @GetMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category",categoryService.getCategoryById(id));
        return "/category/category-update";
    }

    @PostMapping("/update/{id}")
    public String saveUpdatedCategory(@ModelAttribute CategoryDTO categoryDTO) {
  categoryService.update(categoryDTO);
        return "redirect:/categories/list";

    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
       categoryService.deleteById(id);
       return "redirect:/categories/list";
    }
}
