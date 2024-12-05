package org.example.controller;

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

    @PostMapping("/add")
    public void addFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        favoriteService.addFavorite(user, product);
    }

    @GetMapping("/list")
    public List<Product> getFavorites(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        return favoriteService.getFavorites(user);
    }

    @PostMapping("/remove")
    public void removeFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        favoriteService.removeFavorite(user, product);
    }
}