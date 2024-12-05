package org.example.mapper;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.example.dto.OrderDTO;
import org.example.dto.OrderItemDTO;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.entity.OrderStatus;
import org.example.entity.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getCreatedAt());
        dto.setStatus(order.getStatus() != null ? order.getStatus().name() : null);

        dto.setItems(order.getOrderItems() != null ?
                order.getOrderItems().stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()) :
                Collections.emptyList());

        return dto;
    }

    public Order toEntity(OrderDTO dto, List<OrderItem> items) {
        Order order = new Order();
        order.setCreatedAt(dto.getOrderDate());

        order.setStatus(dto.getStatus() != null ? OrderStatus.valueOf(dto.getStatus()) : OrderStatus.CREATED);

        order.setOrderItems(items);
        return order;
    }

    public OrderItemDTO toDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();

        if (orderItem.getProduct() != null) {
            dto.setProductId(orderItem.getProduct().getId());
        }
        dto.setQuantity(orderItem.getQuantity());
        return dto;
    }

    public OrderItem toEntity(OrderItemDTO dto, Product product, Order order) {
        OrderItem orderItem = new OrderItem();

        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setOrder(order);
        return orderItem;
    }

    @Column(name = "created_date")
    private LocalDateTime createdDate;

}

