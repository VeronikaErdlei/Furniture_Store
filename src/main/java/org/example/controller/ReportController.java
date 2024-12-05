package org.example.controller;

import org.example.entity.Product;
import org.example.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController


public class ReportController {

    private final ReportService reportService;



    @Autowired

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports/top-products")
    public ResponseEntity<List<Product>> getTop10Products() {
        List<Product> topProducts = reportService.getTop10Products();
        if (topProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/reports/cancelled-products")
    public ResponseEntity<List<Product>> getTop10CancelledProducts() {
        List<Product> cancelledProducts = reportService.getTop10CancelledProducts();
        if (cancelledProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cancelledProducts);
    }

}
