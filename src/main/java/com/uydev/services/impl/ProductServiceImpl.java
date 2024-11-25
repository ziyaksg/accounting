package com.uydev.services.impl;

import com.uydev.dto.ProductDTO;
import com.uydev.entity.Product;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.ProductRepository;
import com.uydev.services.ProductService;
import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final SecurityService securityService;
    private final ProductRepository repository;
    private final MapperUtil mapper;

    @Override
    public List<ProductDTO> getAllProducts() {
        Long currentCompanyId = securityService.getLoggedInUser().getCompany().getId();
        List<Product> products = repository.findProductsByCompanyId(currentCompanyId);
        return products.stream()
                .map(pro -> mapper.convert(pro, new ProductDTO())).toList();
    }

    @Override
    public void save(ProductDTO newProduct) {
        repository.save(mapper.convert(newProduct, new Product()));
    }

    @Override
    public ProductDTO findById(Long productId) {
        Product product = repository.findByIdAndIsDeleted(productId, false);
        return mapper.convert(product, new ProductDTO());
    }

    @Override
    public void deleteById(Long id) {
        Product product = repository.findById(id).orElseThrow(() -> new RuntimeException("there is no Product with Id " + id));
        product.setIsDeleted(true);
        repository.save(product);
    }
}
