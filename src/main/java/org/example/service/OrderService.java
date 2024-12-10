package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.OrderDTO;
import org.example.dto.OrderItemDTO;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.entity.OrderStatus;
import org.example.entity.Product;
import org.example.mapper.OrderMapper;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDTO).collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toDTO(order);
    }

    public List<Order> getOrderHistory(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Transactional
    public OrderDTO createOrder(Long customerId, List<OrderItemDTO> orderItemDTOs) {
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setCustomerId(customerId);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress("123 Main Street");
        order.setBillingAddress("123 Main Street");
        order.setPaymentStatus("PAID");
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(new BigDecimal("100.00"));

        for (OrderItemDTO itemDTO : orderItemDTOs) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());

            order.addOrderItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDTO(savedOrder);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Transactional
    public OrderDTO addProductToCart(Long productId, int quantity) {
        Optional<Order> order = getCurrentOrder();
        if (order.isEmpty()) {
            order = Optional.of(createNewOrder());
        }

        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        OrderItem existingItem = findOrderItemInCart(order.orElse(null), productId);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            OrderItem newItem = new OrderItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());
            newItem.setOrder(order.get());
            order.get().getOrderItems().add(newItem);
        }

        Order savedOrder = orderRepository.save(order.get());
        return orderMapper.toDTO(savedOrder);
    }

    @Transactional
    public void updateStatuses() {
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        for (Order order : pendingOrders) {
            if (order.getCreatedDate().isBefore(LocalDateTime.now().minusHours(24))) {
                order.setStatus(OrderStatus.EXPIRED);
                orderRepository.save(order);
            }
        }
    }

    private Optional<Order> getCurrentOrder() {
        return orderRepository.findActiveOrderForUser(getCurrentUserId());
    }

    private OrderItem findOrderItemInCart(Order order, Long productId) {
        return order.getOrderItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst().orElse(null);
    }

    private Long getCurrentUserId() {
        return 1L;
    }

    private Order createNewOrder() {
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedDate(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public BigDecimal getDailyProfit(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return order.calculateDailyProfit();
    }

    public OrderDTO toDTO(Order order) {
        return orderMapper.toDTO(order);
    }
}












