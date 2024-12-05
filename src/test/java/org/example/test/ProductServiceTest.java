package org.example.test;


import org.example.entity.Product;
import org.example.mapper.ProductMapper;
import org.example.repository.ProductRepository;
import org.example.service.ProductService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    public ProductServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.getAllProducts();

        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
    }

    @Test
    public void testGetProductById_ProductExists() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertEquals("Test Product", foundProduct.getName());
    }

    @Test
    public void testGetProductById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(1L));
    }

    @Test
    public void testDeleteProduct() {
        long productId = 1L;

        productService.delete(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testFindByCategory() {
    }

    @Test
    public void testSave() {
    }

    @Test
    public void testGetProductById() {
    }

    @Test
    public void testDelete() {
    }

    @Test
    public void testGetTop10BoughtProducts() {
    }

    @Test
    public void testFilterAndSortProducts() {
    }

    @Test
    public void testCreateProduct() {
    }
}
