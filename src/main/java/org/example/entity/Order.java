package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Getter
@Setter

public class Order {

    private Boolean active;

    @ManyToOne
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "billing_address", nullable = false)
    private String billingAddress;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    @NotNull
    @DecimalMin(value = "0.00", message = "Total price cannot be negative")
    private BigDecimal totalPrice;

    @Setter
    private LocalDateTime createdDate;

    @Setter
    @Getter
    private BigDecimal revenue;

    @Setter
    @Getter
    private BigDecimal expenses;

    public Order() {
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        if (orderItem != null) {
            orderItems.remove(orderItem);
            orderItem.setOrder(null);
        }
    }

    public void addProduct(Product product) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setOrder(this);
        orderProducts.add(orderProduct);
    }

    public BigDecimal calculateDailyProfit() {
        BigDecimal profitPercentage = new BigDecimal("0.1");
        return totalPrice.multiply(profitPercentage);
    }

    @Transient
    public BigDecimal calculateProfit() {
        if (revenue != null && expenses != null) {
            return revenue.subtract(expenses);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public void updateStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

}

