package org.example.service;

import lombok.Getter;
import org.example.dto.CartDTO;
import org.example.entity.Cart;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.mapper.CartMapper;
import org.example.repository.CartItemRepository;
import org.example.repository.CartRepository;
import org.example.repository.ProductRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;


@Getter
@Service

public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository,
                       UserRepository userRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;

    }

    public CartDTO getCartForUser(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new RuntimeException("Cart not found for user with id: " + userId);
        }
        return CartMapper.toDTO(cart);
    }

    public CartDTO addProductToCart(Long userId, Long productId, int quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cart.addProduct(product, quantity);
        cartRepository.save(cart);

        return CartMapper.toDTO(cart);
    }

    public CartDTO removeProductFromCart(Long userId, Long productId) {

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            throw new RuntimeException("Cart not found for user with id: " + userId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cart.removeProduct(product);
        cartRepository.save(cart);

        return CartMapper.toDTO(cart);
    }
}
