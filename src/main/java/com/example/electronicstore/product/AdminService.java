package com.example.electronicstore.product;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AdminService {
    private final ProductRepository productRepository;

    public AdminService(@Autowired ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product, Long id) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException());
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDeal(product.getDeal());
        return productRepository.save(existingProduct);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException());
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
