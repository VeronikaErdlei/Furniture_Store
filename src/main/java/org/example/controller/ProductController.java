package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Add product to favorites", description = "Adds a product to the user's favorites list")
    @PostMapping("/favorites")
    public ResponseEntity<String> addToFavorites(@RequestParam Long userId, @RequestParam Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        productService.addToFavorites(user, product);
        return ResponseEntity.ok("Product added to favorites");
    }

    @Operation(summary = "Get favorites", description = "Retrieves the list of the user's favorite products")
    @GetMapping("/favorites")
    public ResponseEntity<List<Product>> getFavorites(@RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        List<Product> favorites = productService.getFavorites(user);
        return ResponseEntity.ok(favorites);
    }

    @Operation(summary = "Get all products", description = "Retrieves a list of all products")
    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all product categories")
    @GetMapping("/category")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Add a new product", description = "Adds a new product to the catalog")
    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        if (product == null) {
            return ResponseEntity.badRequest().body("Product cannot be null");
        }
        productRepository.save(product);
        return ResponseEntity.ok("Product added successfully");
    }

    @Operation(summary = "Update an existing product", description = "Updates the details of an existing product")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        product.setId(id);
        productRepository.save(product);
        return ResponseEntity.ok("Product updated successfully");
    }

    @Operation(summary = "Delete a product", description = "Deletes a product from the catalog")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @Operation(summary = "Create a new order", description = "Creates a new order for a product")
    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        orderRepository.save(order);
        return ResponseEntity.ok("Order created successfully");
    }

    @Operation(summary = "Get orders by user ID", description = "Retrieves a list of orders for a specific user")
    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(orders);
    }

    @Operation(summary = "Get product of the day", description = "Retrieves the product of the day")
    @GetMapping("/product-of-the-day")
    public ResponseEntity<Product> getProductOfTheDay() {
        Product product = productService.getProductOfTheDay();
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Apply discount to a product", description = "Applies a discount to a specific product")
    @PutMapping("/apply-discount/{productId}")
    public ResponseEntity<String> applyDiscount(@PathVariable Long productId, @RequestParam Double discount) {
        productService.applyDiscount(productId, discount);
        return ResponseEntity.ok("Discount applied successfully");
    }

    @Operation(summary = "Search products by name", description = "Searches for products by their name")
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String name) {
        List<Product> products = productService.searchProductsByName(name);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get products by category name", description = "Retrieves products by their category name")
    @GetMapping("/category-name/{category}")
    public ResponseEntity<List<Product>> getProductsByCategoryName(@PathVariable String category) {
        List<Product> products = productService.findProductsByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get products by category ID", description = "Retrieves products by their category ID")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> findByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.findByCategory(categoryId);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }
}

