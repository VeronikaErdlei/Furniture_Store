package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user", "cartItems"})

public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", columnDefinition = "int")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    public void addProduct(Product product, int quantity) {
        CartItem cartItem = new CartItem(this, product, quantity);
        this.cartItems.add(cartItem);
    }

    public void removeProduct(Product product) {
        CartItem itemToRemove = cartItems.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElse(null);
        if (itemToRemove != null) {
            cartItems.remove(itemToRemove);
        }
    }
}
