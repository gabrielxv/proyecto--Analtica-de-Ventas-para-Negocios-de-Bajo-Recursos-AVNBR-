package com.negocio.analitica.repository.memory;

import com.negocio.analitica.model.Product;
import com.negocio.analitica.repository.ProductRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Prototype adapter. Data lives only for the lifetime of the process.
 * Replace with a database adapter when persistence is selected.
 */
public class InMemoryProductRepository implements ProductRepository {
    private final Map<Long, Product> data = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public synchronized Product save(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        if (product.getId() == null) product.setId(nextId++);
        else nextId = Math.max(nextId, product.getId() + 1);
        data.put(product.getId(), product);
        return product;
    }

    @Override
    public synchronized Optional<Product> findById(Long id) {
        return Optional.ofNullable(id == null ? null : data.get(id));
    }

    @Override
    public synchronized List<Product> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public synchronized void deleteById(Long id) {
        data.remove(id);
    }
}
