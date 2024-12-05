package org.example.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Long categoryId;
}

