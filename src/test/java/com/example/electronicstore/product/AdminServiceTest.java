package com.example.electronicstore.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.example.electronicstore.deal.None;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    AdminService adminService;

    @Test
    void When_createProduct_Then_saveInRepository() {
        Product input = new Product(null, "mouse", new BigDecimal("10.5"), new None());
        Product created = new Product(1L, "mouse", new BigDecimal("10.5"), new None());
        when(productRepository.save(input)).thenReturn(created);

        Product actual = adminService.createProduct(input);
        verify(productRepository).save(input);
        assertEquals(created, actual);
    }

    @Test
    void Given_productInRepository_When_getProduct_Then_returnFromRepository() {
        Product existing = new Product(1L, "mouse", new BigDecimal("10.5"), new None());
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

        Product actual = adminService.getProduct(1L);

        verify(productRepository).findById(1L);
        assertEquals(existing, actual);
    }

    @Test
    void Given_noProductNotInRepository_When_getProduct_Then_throwProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> adminService.getProduct(1L));

        verify(productRepository).findById(1L);
    }

    @Test
    void Given_productInRepository_When_updateProduct_Then_saveInRepository() {
        Product existingProduct = new Product(1L, "mouse", new BigDecimal("10.5"), new None());
        Product updatedProduct = new Product(1L, "mouse2", new BigDecimal("11.5"), new None());
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        Product actual = adminService.updateProduct(new Product(1L, "mouse2", new BigDecimal("11.5"), new None()), 1L);
        verify(productRepository).findById(1L);
        verify(productRepository).save(updatedProduct);
        assertEquals(updatedProduct, actual);
    }

    @Test
    void When_deleteProduct_Then_deleteInRepository() {
        adminService.deleteProduct(1L);
        verify(productRepository).deleteById(1L);
    }
}