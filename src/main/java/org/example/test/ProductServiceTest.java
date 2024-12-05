package org.example.test;

import org.example.dto.ProductDTO;
import org.example.entity.Category;
import org.example.entity.Product;
import org.example.mapper.ProductMapper;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductRepository;
import org.example.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest


public class ProductServiceTest {

    @DataJpaTest
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ProductMapper productMapper;

    @Test
    public void testCreateProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Table");
        productDTO.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);

        Product product = new Product();
        product.setName("Table");

        Product savedProduct = new Product();
        savedProduct.setId(10L);
        savedProduct.setName("Table");

        ProductDTO savedProductDTO = new ProductDTO();
        savedProductDTO.setId(10L);
        savedProductDTO.setName("Table");

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Mockito.when(productMapper.toEntity(productDTO, category)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(savedProduct);
        Mockito.when(productMapper.toDTO(savedProduct)).thenReturn(savedProductDTO);

        ProductDTO result = productService.createProduct(productDTO);

        assertEquals(10L, result.getId());
        assertEquals("Table", result.getName());
    }
}
