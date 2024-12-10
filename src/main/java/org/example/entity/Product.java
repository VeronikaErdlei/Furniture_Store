package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private BigDecimal discountPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double discount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore

    private Category category;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    @JsonIgnore

    private Integer numberOfPurchases = 0;

    private Boolean onSale;

    @Column(name = "discount_percentage")
    @JsonIgnore

    private BigDecimal discountPercentage;

    @Column(name = "created_date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @JsonIgnore

    private LocalDateTime createdDate;

    public Product(Long id, String name, BigDecimal price, Boolean onSale, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.onSale = onSale;
        this.category = category;
        this.createdDate = LocalDateTime.now();
    }

    public Product(Long id) {
        this.id = id;
    }
}
