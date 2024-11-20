package com.uydev.converter;

import com.uydev.dto.ClientVendorDTO;
import com.uydev.dto.ProductDTO;
import com.uydev.services.ClientVendorService;
import com.uydev.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConvertor implements Converter<String, ProductDTO> {
    private final ProductService productService;

    @Override
    public ProductDTO convert(String id) {
        return productService.findById(Long.parseLong(id));
    }
}
