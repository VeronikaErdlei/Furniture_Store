package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.ProductDTO;
import org.example.entity.Category;
import org.example.entity.Favorite;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.mapper.ProductMapper;
import org.example.repository.CategoryRepository;
import org.example.repository.FavoriteRepository;
import org.example.repository.ProductRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final FavoriteRepository favoriteRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper,
                          CategoryRepository categoryRepository, FavoriteRepository favoriteRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> findByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        if (products.isEmpty()) {
            throw new RuntimeException("No products found for category with ID: " + categoryId);
        }
        return products;
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductDTO> filterAndSortProducts(Double minPrice, Double maxPrice, Boolean onSale,
                                                  String category, String sortBy) {
        List<Product> products = productRepository.filterAndSort(minPrice, maxPrice, onSale, category, sortBy);
        return products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO createProduct(@NotNull ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productDTO.getCategoryId()));

        Product product = productMapper.toEntity(productDTO, category);
        Product savedProduct = productRepository.save(product);

        return productMapper.toDTO(savedProduct);
    }

    public Product getProductOfTheDay() {
        return productRepository.findTopByOrderByDiscountDesc();
    }

    public void applyDiscount(Long productId, Double discount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        product.setDiscount(discount);
        productRepository.save(product);
    }

    public void addToFavorites(User user, Product product) {
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);
    }

    public List<Product> getFavorites(User user) {
        List<Favorite> favorites = favoriteRepository.findByUser(user);
        return favorites.stream().map(Favorite::getProduct).collect(Collectors.toList());
    }
    public List<Product> findProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }
}

