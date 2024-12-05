package org.example.controller;

import org.example.dto.ProductDTO;
import org.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")

public class ProductFilterController {

    private final ProductService productService;

    public ProductFilterController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/custom-filter")
    public ResponseEntity<List<ProductDTO>> filterProducts(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean hasDiscount,
            @RequestParam(required = false) String categoryName,
            @RequestParam(defaultValue = "name") String sortBy
    ) {

        List<ProductDTO> filteredProducts = productService.filterAndSortProducts
                (minPrice, maxPrice, hasDiscount, categoryName, sortBy);

        return ResponseEntity.ok(filteredProducts);
    }
}
