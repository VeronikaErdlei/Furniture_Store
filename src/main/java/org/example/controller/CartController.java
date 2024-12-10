package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Add product to cart", description = "Adds a product to the user's cart with the specified quantity")
    @PostMapping("/add/{userId}/{productId}/{quantity}")
    public CartDTO addProductToCart(@PathVariable Long userId, @PathVariable Long productId, @PathVariable int quantity) {
        return cartService.addProductToCart(userId, productId, quantity);
    }

    @Operation(summary = "Remove product from cart", description = "Removes a product from the user's cart")
    @DeleteMapping("/remove/{userId}/{productId}")
    public CartDTO removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        return cartService.removeProductFromCart(userId, productId);
    }

    @Operation(summary = "Get cart for user", description = "Retrieves the current cart for the specified user")
    @GetMapping("/{userId}")
    public CartDTO getCartForUser(@PathVariable Long userId) {
        return cartService.getCartForUser(userId);
    }
}

