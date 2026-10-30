package com.negocio.analitica.service;

import com.negocio.analitica.model.Product;
import java.util.List;

/**
 * Contrato mínimo de operaciones de producto que requieren ventas y analítica.
 * Mantiene a esos módulos desacoplados de la implementación ProductService.
 */
public interface ProductOperations {
    Product findProductById(Long id);
    boolean hasAvailability(Long productId, int quantity);
    Product updateProduct(Product product);
    List<Product> findAllProducts();
}
