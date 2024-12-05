package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.entity.Order;
import org.example.entity.OrderStatus;
import org.example.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderRepository orderRepository;

    @Transactional
    @Scheduled(fixedRate = 30000)
    public void updateOrderStatuses() {

        List<Order> orders = orderRepository.findByStatus(OrderStatus.AWAITING_PAYMENT);
        for (Order order : orders) {
            order.updateStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }

        List<Order> inTransitOrders = orderRepository.findByStatus(OrderStatus.IN_TRANSIT);
        for (Order order : inTransitOrders) {
            order.updateStatus(OrderStatus.DELIVERED);
            orderRepository.save(order);
        }
    }
}