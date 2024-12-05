package org.example.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CartItemDTO {

    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;

}




