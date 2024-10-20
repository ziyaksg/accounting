package com.uydev.dto;

import com.uydev.enums.ProductUnit;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductDTO {

    private Long id;

    private String name;

    private Integer quantityInStock;

    private Integer lowLimitAlert;

    private ProductUnit productUnit;

    private CategoryDTO category;
}
