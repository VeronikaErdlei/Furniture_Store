package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.service.FavoriteService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;
    private final ProductService productService;

    @Operation(summary = "Add a product to favorites", description = "Adds a specified product to the user's favorites list")
    @PostMapping("/add")
    public void addFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        favoriteService.addFavorite(user, product);
    }

    @Operation(summary = "Get user's favorite products", description = "Retrieves the list of favorite products for a given user")
    @GetMapping("/list")
    public List<Product> getFavorites(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        return favoriteService.getFavorites(user);
    }

    @Operation(summary = "Remove a product from favorites", description = "Removes a specified product from the user's favorites list")
    @PostMapping("/remove")
    public void removeFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        favoriteService.removeFavorite(user, product);
    }
}
