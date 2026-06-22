package com.finshop.finshop.service;

import com.finshop.finshop.model.dto.ProductDTO;
import com.finshop.finshop.model.entity.Category;
import com.finshop.finshop.model.entity.Product;
import com.finshop.finshop.model.request.ProductRequest;
import com.finshop.finshop.repository.CategoryRepository;
import com.finshop.finshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .nom("Électronique")
                .build();

        product = Product.builder()
                .id(1L)
                .nom("iPhone 15")
                .description("Smartphone Apple")
                .prix(new BigDecimal("999.99"))
                .stock(50)
                .category(category)
                .build();

        productRequest = new ProductRequest();
        productRequest.setNom("iPhone 15");
        productRequest.setDescription("Smartphone Apple");
        productRequest.setPrix(new BigDecimal("999.99"));
        productRequest.setStock(50);
        productRequest.setCategoryId(1L);
    }

    @Test
    void createProduct_ShouldReturnProductDTO_WhenValidRequest() {
        // ARRANGE
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any())).thenReturn(product);

        // ACT
        ProductDTO result = productService.createProduct(productRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals("iPhone 15", result.getNom());
        assertEquals(new BigDecimal("999.99"), result.getPrix());
        assertEquals("Électronique", result.getCategoryNom());
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void createProduct_ShouldThrowException_WhenCategoryNotFound() {
        // ARRANGE
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.createProduct(productRequest));
        assertEquals("Catégorie non trouvée", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void getAllProducts_ShouldReturnListOfProductDTOs() {
        // ARRANGE
        when(productRepository.findAll()).thenReturn(List.of(product));

        // ACT
        List<ProductDTO> results = productService.getAllProducts();

        // ASSERT
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("iPhone 15", results.get(0).getNom());
    }

    @Test
    void getProductById_ShouldThrowException_WhenProductNotFound() {
        // ARRANGE
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.getProductById(99L));
        assertEquals("Produit non trouvé", exception.getMessage());
    }
}
