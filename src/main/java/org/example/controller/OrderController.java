package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.dto.OrderDTO;
import org.example.entity.Order;
import org.example.entity.OrderStatus;
import org.example.mapper.OrderMapper;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Get all orders", description = "Retrieves a list of all orders")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Operation(summary = "Get order by ID", description = "Retrieves a single order by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(id));
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Delete an order", description = "Deletes an order by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order successfully deleted");
    }

    @Operation(summary = "Update order status", description = "Updates the status of an order by its ID")
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @Operation(summary = "Get orders by status", description = "Retrieves orders filtered by their status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @Operation(summary = "Add product to order", description = "Adds a product to the cart for an order")
    @PostMapping("/addProduct/{productId}")
    public ResponseEntity<OrderDTO> addProductToCart(@PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(orderService.addProductToCart(productId, quantity));
    }

    @Operation(summary = "Get order history", description = "Retrieves the order history for a specific customer")
    @GetMapping("/history/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrderHistory(@PathVariable Long customerId) {
        List<OrderDTO> orderHistory = orderService.getOrderHistory(customerId).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderHistory);
    }

    @Operation(summary = "Get daily profit", description = "Retrieves the daily profit of an order")
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
}
