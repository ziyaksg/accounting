package com.uydev.controller;

import com.uydev.dto.ProductDTO;
import com.uydev.enums.ProductUnit;
import com.uydev.services.CategoryService;
import com.uydev.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String getAllProductList(Model model){
        model.addAttribute("products", productService.getAllProducts());
        return "/product/product-list";
    }

    @GetMapping("/create")
    public String createProduct(Model model){
        model.addAttribute("newProduct", new ProductDTO());
        model.addAttribute("categories", categoryService.getCategoryList());
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
        return "/product/product-create";
    }

    @PostMapping("/create")
    public String saveNewProduct(@ModelAttribute("newProduct") ProductDTO newProduct){
        productService.save(newProduct);
        return "redirect:/products/list";
    }


    @GetMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long productId, Model model){
        model.addAttribute("product", productService.findById(productId));
        model.addAttribute("categories", categoryService.getCategoryList());
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
        return "/product/product-update";
    }

    @PostMapping("/update/{id}")
    public String saveUpdatedProduct(@ModelAttribute("product") ProductDTO product){
        productService.save(product);
        return "redirect:/products/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id){
        productService.deleteById(id);
        return "redirect:/products/list";
    }

}
