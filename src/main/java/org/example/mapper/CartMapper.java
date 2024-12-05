package org.example.mapper;

import org.example.dto.CartDTO;
import org.example.dto.CartItemDTO;
import org.example.entity.Cart;
import org.example.entity.CartItem;
import org.example.entity.Product;
import org.example.entity.User;
import java.math.BigDecimal;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartDTO toDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUser().getId());

        cartDTO.setCartItems(cart.getCartItems().stream()
                .map(CartMapper::toDTO)
                .collect(Collectors.toSet()));

        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cartDTO.setTotalPrice(totalPrice);

        return cartDTO;
    }

    public static CartItemDTO toDTO(CartItem cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setProductId(cartItem.getProduct().getId());
        cartItemDTO.setProductName(cartItem.getProduct().getName());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setPrice(cartItem.getProduct().getPrice());

        return cartItemDTO;
    }

    public static Cart toEntity(CartDTO cartDTO) {
        Cart cart = new Cart();
        cart.setId(cartDTO.getId());
        cart.setUser(new User(cartDTO.getUserId()));

        cart.setCartItems(cartDTO.getCartItems().stream()
                .map(itemDTO -> toEntity(itemDTO, cart))
                .collect(Collectors.toSet()));

        return cart;
    }

    public static CartItem toEntity(CartItemDTO cartItemDTO, Cart cart) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(new Product(cartItemDTO.getProductId()));
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setCart(cart);

        return cartItem;
    }
}
