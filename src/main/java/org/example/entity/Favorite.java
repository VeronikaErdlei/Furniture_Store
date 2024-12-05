package org.example.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "favorites")
@Data
public class Favorite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Favorite() {}
}
