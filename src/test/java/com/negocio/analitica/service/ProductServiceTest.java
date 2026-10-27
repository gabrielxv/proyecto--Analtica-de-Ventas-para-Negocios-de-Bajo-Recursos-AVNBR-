package com.negocio.analitica.service;

import com.negocio.analitica.model.Category;
import com.negocio.analitica.model.Product;
import com.negocio.analitica.repository.CategoryRepository;
import com.negocio.analitica.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    private final InMemoryCategoryRepository categories = new InMemoryCategoryRepository();
    private final InMemoryProductRepository products = new InMemoryProductRepository();
    private final ProductService service = new ProductService(products, categories);

    @Test
    void createsProductOnlyWithExistingCategory() {
        Category category = categories.save(new Category(null, "Food", "Food items"));
        Product input = new Product(null, "Rice", 3.5, 10, 2, new Category(category.getId(), null, null));
        Product saved = service.createProduct(input);
        assertNotNull(saved.getId());
        assertEquals(category.getId(), saved.getCategory().getId());
    }

    @Test
    void rejectsMissingCategory() {
        Product input = new Product(null, "Rice", 3.5, 10, 2, new Category(99L, "Missing", ""));
        assertThrows(IllegalArgumentException.class, () -> service.createProduct(input));
    }

    @Test
    void rejectsInvalidProductFields() {
        Category category = categories.save(new Category(null, "Food", ""));
        assertThrows(IllegalArgumentException.class, () -> service.createProduct(
                new Product(null, " ", 3.5, 1, 0, category)));
        assertThrows(IllegalArgumentException.class, () -> service.createProduct(
                new Product(null, "Rice", 0, 1, 0, category)));
        assertThrows(IllegalArgumentException.class, () -> service.createProduct(
                new Product(null, "Rice", Double.NaN, 1, 0, category)));
        assertThrows(IllegalArgumentException.class, () -> service.createProduct(
                new Product(null, "Rice", 3.5, -1, 0, category)));
        assertThrows(IllegalArgumentException.class, () -> service.createProduct(
                new Product(null, "Rice", 3.5, 1, -1, category)));
    }

    @Test
    void availabilityRequiresPositiveQuantityAndSufficientStock() {
        Category category = categories.save(new Category(null, "Food", ""));
        Product saved = service.createProduct(new Product(null, "Rice", 3.5, 4, 1, category));
        assertTrue(service.hasAvailability(saved.getId(), 4));
        assertFalse(service.hasAvailability(saved.getId(), 0));
        assertFalse(service.hasAvailability(saved.getId(), 5));
    }

    private static class InMemoryCategoryRepository implements CategoryRepository {
        private final Map<Long, Category> data = new HashMap<>();
        private long nextId = 1;
        public Category save(Category category) {
            if (category.getId() == null) category.setId(nextId++);
            data.put(category.getId(), category);
            return category;
        }
        public Optional<Category> findById(Long id) { return Optional.ofNullable(data.get(id)); }
        public List<Category> findAll() { return new ArrayList<>(data.values()); }
        public void deleteById(Long id) { data.remove(id); }
    }

    private static class InMemoryProductRepository implements ProductRepository {
        private final Map<Long, Product> data = new HashMap<>();
        private long nextId = 1;
        public Product save(Product product) {
            if (product.getId() == null) product.setId(nextId++);
            data.put(product.getId(), product);
            return product;
        }
        public Optional<Product> findById(Long id) { return Optional.ofNullable(data.get(id)); }
        public List<Product> findAll() { return new ArrayList<>(data.values()); }
        public void deleteById(Long id) { data.remove(id); }
    }
}
