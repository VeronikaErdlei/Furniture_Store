package org.example.repository;

import org.example.entity.Order;
import org.example.entity.OrderStatus;
import org.example.entity.Product;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByUserId(Long userId);
    List<Order> findByUser(User user);

    @Query("SELECT p, COUNT(p) AS purchases FROM Order o JOIN o.orderProducts op JOIN op.product p WHERE o.status = :status GROUP BY p ORDER BY purchases DESC")
    List<Object[]> findTopBoughtProductsByStatus(@Param("status") OrderStatus status);

    @Query("SELECT p, COUNT(p) AS cancellations FROM Order o JOIN o.orderProducts op JOIN op.product p WHERE o.status = :status GROUP BY p ORDER BY cancellations DESC")
    List<Object[]> findTopCancelledProductsByStatus(@Param("status") OrderStatus status);

    @Query("SELECT o.orderProducts FROM Order o WHERE o.status = 'PENDING' AND o.createdAt < :date")
    List<Product> findProductsPendingForMoreThanNDays(@Param("date") LocalDate date);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    Double findTotalRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.status = 'DELIVERED' AND o.createdAt BETWEEN :startDate AND :endDate")
    Double calculateProfit(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.createdAt < :thresholdDate")
    List<Order> findOrdersPendingForMoreThanNdays(@Param("thresholdDate") LocalDate thresholdDate);

    @Query("SELECT YEAR(o.createdAt), MONTH(o.createdAt), SUM(o.totalPrice) FROM Order o WHERE o.status = 'DELIVERED' AND o.createdAt BETWEEN :startDate AND :endDate GROUP BY YEAR(o.createdAt), MONTH(o.createdAt)")
    List<Object[]> calculateMonthlyProfit(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT DATE(o.createdAt), SUM(o.totalPrice) FROM Order o WHERE o.status = 'DELIVERED' AND o.createdAt BETWEEN :startDate AND :endDate GROUP BY DATE(o.createdAt)")
    List<Object[]> calculateDailyProfit(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT YEAR(o.createdAt), SUM(o.totalPrice) FROM Order o WHERE o.status = 'DELIVERED' AND o.createdAt BETWEEN :startDate AND :endDate GROUP BY YEAR(o.createdAt)")
    List<Object[]> calculateYearlyProfit(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT o FROM Order o WHERE o.status IN :statuses")
    List<Order> findOrdersForAnalysis(@Param("statuses") List<OrderStatus> statuses);

    @NonNull
    Optional<Order> findById(@NonNull Long id);

    @Query("SELECT p, COUNT(p) AS purchases FROM Order o JOIN o.orderProducts op JOIN op.product p WHERE o.status = 'DELIVERED' GROUP BY p ORDER BY purchases DESC")
    List<Object[]> findTop10BoughtProducts();

    @Query("SELECT p, COUNT(p) AS cancellations FROM Order o JOIN o.orderProducts op JOIN op.product p WHERE o.status = 'CANCELLED' GROUP BY p ORDER BY cancellations DESC")
    List<Object[]> findTop10CancelledProducts();

    @Query("SELECT o FROM Order o WHERE o.active = true AND o.user.id = :currentUserId")
    Optional<Order> findActiveOrderForUser(@Param("currentUserId") Long currentUserId);

    @Query("SELECT p FROM Product p JOIN OrderItem oi ON oi.product = p GROUP BY p ORDER BY SUM(oi.quantity) DESC")
    List<Product> findTop10MostSoldProducts();

    @Query("SELECT p FROM Product p ORDER BY p.numberOfPurchases DESC")
    List<Product> findTop10ByOrderByNumberOfPurchasesDesc();

}

