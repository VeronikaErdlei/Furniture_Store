package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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






