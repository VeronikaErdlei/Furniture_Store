package org.example.repository;

import org.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContaining(String name);

    Product findTopByOrderByDiscountDesc();


    @Query("SELECT p FROM Product p WHERE " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:onSale IS NULL OR p.onSale = :onSale) AND " +
            "(:category IS NULL OR p.category.name = :category) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'NAME' THEN p.name " +
            "WHEN :sortBy = 'PRICE' THEN p.price " +
            "WHEN :sortBy = 'DATE' THEN p.createdDate " +
            "ELSE p.name END ASC")
    List<Product> filterAndSort(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("onSale") Boolean onSale,
            @Param("category") String category,
            @Param("sortBy") String sortBy
    );

    @Query("SELECT p FROM Product p WHERE" +
            " p.category.name = :category")
    List<Product> findByCategoryName(
            @Param("category") String category
    );

}
