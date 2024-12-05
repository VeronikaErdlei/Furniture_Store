package org.example.service;

import org.example.entity.Product;
import org.example.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final OrderRepository orderRepository;

    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Product> getTop10Products() {

        return orderRepository.findTop10BoughtProducts().stream()
                .map(result -> (Product) result[0])
                .collect(Collectors.toList());
    }

    public List<Product> getTop10CancelledProducts() {

        return orderRepository.findTop10CancelledProducts().stream()
                .map(result -> (Product) result[0])
                .collect(Collectors.toList());
    }
}



