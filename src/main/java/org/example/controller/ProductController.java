package org.example.controller;

import org.example.entity.Category;
import org.example.entity.Order;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.repository.CategoryRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.repository.UserRepository;
import org.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")


public class ProductController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    public ProductController(
            ProductRepository productRepository,
            OrderRepository orderRepository,
            CategoryRepository categoryRepository,
            ProductService productService,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    @PostMapping("/favorites")
    public ResponseEntity<String> addToFavorites(@RequestParam Long userId, @RequestParam Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        productService.addToFavorites(user, product);
        return ResponseEntity.ok("Product added to favorites");
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<Product>> getFavorites(@RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        List<Product> favorites = productService.getFavorites(user);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        if (product == null) {
            return ResponseEntity.badRequest().body("Product cannot be null");
        }
        productRepository.save(product);
        return ResponseEntity.ok("Product added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        product.setId(id);
        productRepository.save(product);
        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        orderRepository.save(order);
        return ResponseEntity.ok("Order created successfully");
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(orders);
    }


    @GetMapping("/product-of-the-day")
    public ResponseEntity<Product> getProductOfTheDay() {
        Product product = productService.getProductOfTheDay();
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PutMapping("/apply-discount/{productId}")
    public ResponseEntity<String> applyDiscount(@PathVariable Long productId, @RequestParam Double discount) {
        productService.applyDiscount(productId, discount);
        return ResponseEntity.ok("Discount applied successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String name) {
        List<Product> products = productService.searchProductsByName(name);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category-name/{category}")
    public ResponseEntity<List<Product>> getProductsByCategoryName(@PathVariable String category) {
        List<Product> products = productService.findProductsByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> findByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.findByCategory(categoryId);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

}

