package org.example.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CartDTO {

    private Long id;
    private Long userId;
    private Set<CartItemDTO> cartItems;
    private BigDecimal totalPrice;

}


