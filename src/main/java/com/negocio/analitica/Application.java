package com.negocio.analitica;

import com.negocio.analitica.controller.CategoryController;
import com.negocio.analitica.controller.ProductController;
import com.negocio.analitica.model.Category;
import com.negocio.analitica.model.Inventory;
import com.negocio.analitica.model.Product;
import com.negocio.analitica.repository.sqlite.SqliteCategoryRepository;
import com.negocio.analitica.repository.sqlite.SqliteDatabase;
import com.negocio.analitica.repository.sqlite.SqliteProductRepository;
import com.negocio.analitica.service.CategoryService;
import com.negocio.analitica.service.ProductService;

/**
 * Console demo backed by a local SQLite database.
 * Run with: mvn clean verify
 * Then: mvn compile exec:java -Dexec.mainClass=com.negocio.analitica.Application
 */
public final class Application {
    private Application() { }

    public static void main(String[] args) {
        SqliteDatabase database = new SqliteDatabase("jdbc:sqlite:data/avnbr.db");
        SqliteCategoryRepository categoryRepository = new SqliteCategoryRepository(database);
        SqliteProductRepository productRepository = new SqliteProductRepository(database);

        CategoryController categories = new CategoryController(new CategoryService(categoryRepository));
        ProductController products = new ProductController(new ProductService(productRepository, categoryRepository));
        Inventory inventory = new Inventory();

        Category category = categoryRepository.findAll().stream()
                .filter(existing -> "Abarrotes".equals(existing.getName()))
                .findFirst()
                .orElseGet(() -> categories.createCategory(
                        new Category(null, "Abarrotes", "Productos básicos")));

        Product product = productRepository.findAll().stream()
                .filter(existing -> "Arroz".equals(existing.getName()))
                .findFirst()
                .orElseGet(() -> products.createProduct(
                        new Product(null, "Arroz", 3500.0, 12, 4, category)));

        System.out.println("=== AVNBR | Demostración con SQLite ===");
        System.out.printf("Categoría: %s (id=%d)%n", category.getName(), category.getId());
        System.out.printf("Producto: %s | precio=%.2f | stock inicial=%d%n",
                product.getName(), product.getPrice(), product.getStock());

        System.out.println("Base de datos: data/avnbr.db");
        System.out.printf("Productos persistidos: %d%n", products.getAllProducts().size());
        System.out.printf("¿Stock bajo o igual al mínimo (%d)? %s%n",
                product.getMinimumStock(), inventory.hasLowStock(product));
        System.out.println("Los datos se conservan entre ejecuciones.");
    }
}
