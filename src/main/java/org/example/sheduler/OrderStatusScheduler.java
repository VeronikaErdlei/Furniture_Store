package org.example.sheduler;

import org.example.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderStatusScheduler {

    private final OrderService orderService;

    public OrderStatusScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void updateOrderStatuses() {
        orderService.updateStatuses();
    }
}

