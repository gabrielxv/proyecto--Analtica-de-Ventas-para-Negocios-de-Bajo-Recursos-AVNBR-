package com.negocio.analitica.controller;

import com.negocio.analitica.model.Product;
import com.negocio.analitica.service.ProductService;
import java.util.List;

public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        if (productService == null) {
            throw new IllegalArgumentException("ProductService cannot be null.");
        }
        this.productService = productService;
    }

    public Product createProduct(Product product) {
        return productService.createProduct(product);
    }

    public Product updateProduct(Product product) {
        return productService.updateProduct(product);
    }

    public Product getProduct(Long id) {
        return productService.findProductById(id);
    }

    public List<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }

    public Product registerStockEntry(Long productId, int quantity) {
        return productService.registerStockEntry(productId, quantity);
    }

    public Product registerStockExit(Long productId, int quantity) {
        return productService.registerStockExit(productId, quantity);
    }

    public List<Product> getLowStockProducts() {
        return productService.findLowStockProducts();
    }
}
