package org.example.controller;

import org.example.dto.CartDTO;
import org.example.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")

public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add/{userId}/{productId}/{quantity}")
    public CartDTO addProductToCart(@PathVariable Long userId, @PathVariable Long productId, @PathVariable int quantity)
    {
        return cartService.addProductToCart(userId, productId, quantity);
    }

    @DeleteMapping("/remove/{userId}/{productId}")
    public CartDTO removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        return cartService.removeProductFromCart(userId, productId);
    }

    @GetMapping("/{userId}")
    public CartDTO getCartForUser(@PathVariable Long userId) {
        return cartService.getCartForUser(userId);
    }

}
