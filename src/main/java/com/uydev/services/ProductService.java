package com.uydev.services;

import com.uydev.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    void save(ProductDTO newProduct);

    ProductDTO findById(Long productId);

    void deleteById(Long id);
}
