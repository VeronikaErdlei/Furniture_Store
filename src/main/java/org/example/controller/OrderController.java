package org.example.controller;

import org.example.dto.OrderDTO;
import org.example.entity.Order;
import org.example.entity.OrderStatus;
import org.example.entity.User;
import org.example.mapper.OrderMapper;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")

public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(id));
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO dto) {
        OrderDTO createdOrder = orderService.createOrder(dto);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status)
    {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @PostMapping("/addProduct/{productId}")
    public ResponseEntity<OrderDTO> addProductToCart(@PathVariable Long productId, @RequestParam int quantity)
    {
        return ResponseEntity.ok(orderService.addProductToCart(productId, quantity));
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrderHistory(@PathVariable Long customerId) {
        List<OrderDTO> orderHistory = orderService.getOrderHistory(customerId).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderHistory);
    }

    @GetMapping("/{id}/dailyProfit")
    public ResponseEntity<BigDecimal> getDailyProfit(@PathVariable Long id) {
        BigDecimal profit = orderService.getDailyProfit(id);
        return ResponseEntity.ok(profit);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String message) {
            super(message);
        }
    }

    @GetMapping("/orders/history")
    public List<Order> getOrderHistory(@AuthenticationPrincipal User user) {
        return orderService.getOrderHistory(user);
    }

}
