package com.negocio.analitica.repository.memory;

import com.negocio.analitica.model.Category;
import com.negocio.analitica.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Prototype adapter. Data lives only for the lifetime of the process.
 * Replace with a database adapter when persistence is selected.
 */
public class InMemoryCategoryRepository implements CategoryRepository {
    private final Map<Long, Category> data = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public synchronized Category save(Category category) {
        if (category == null) throw new IllegalArgumentException("Category cannot be null.");
        if (category.getId() == null) category.setId(nextId++);
        else nextId = Math.max(nextId, category.getId() + 1);
        data.put(category.getId(), category);
        return category;
    }

    @Override
    public synchronized Optional<Category> findById(Long id) {
        return Optional.ofNullable(id == null ? null : data.get(id));
    }

    @Override
    public synchronized List<Category> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public synchronized void deleteById(Long id) {
        data.remove(id);
    }
}
