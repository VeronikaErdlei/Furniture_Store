package org.example.mapper;

import org.example.dto.ProductDTO;
import org.example.entity.Category;
import org.example.entity.Product;
import org.springframework.stereotype.Component;

@Component


public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());

        dto.setPrice(product.getPrice());
        dto.setDiscountPrice(product.getDiscountPrice());

        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        return dto;
    }

    public Product toEntity(ProductDTO dto, Category category) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());

        product.setPrice(dto.getPrice());
        product.setDiscountPrice(dto.getDiscountPrice());

        product.setCategory(category);
        return product;
    }
}