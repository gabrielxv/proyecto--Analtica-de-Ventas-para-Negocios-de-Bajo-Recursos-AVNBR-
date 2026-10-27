package com.negocio.analitica;

import com.negocio.analitica.controller.CategoryController;
import com.negocio.analitica.controller.ProductController;
import com.negocio.analitica.model.Category;
import com.negocio.analitica.model.Inventory;
import com.negocio.analitica.model.Product;
import com.negocio.analitica.repository.memory.InMemoryCategoryRepository;
import com.negocio.analitica.repository.memory.InMemoryProductRepository;
import com.negocio.analitica.service.CategoryService;
import com.negocio.analitica.service.ProductService;

/**
 * Runnable console demo. Uses in-memory repositories; no database is configured.
 * Run with: mvn compile exec:java -Dexec.mainClass=com.negocio.analitica.Application
 */
public final class Application {
    private Application() { }

    public static void main(String[] args) {
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        InMemoryProductRepository productRepository = new InMemoryProductRepository();

        CategoryController categories = new CategoryController(new CategoryService(categoryRepository));
        ProductController products = new ProductController(new ProductService(productRepository, categoryRepository));
        Inventory inventory = new Inventory();

        Category category = categories.createCategory(new Category(null, "Abarrotes", "Productos básicos"));
        Product product = products.createProduct(new Product(null, "Arroz", 3500.0, 12, 4, category));

        System.out.println("=== AVNBR | Demostración ejecutable ===");
        System.out.printf("Categoría creada: %s (id=%d)%n", category.getName(), category.getId());
        System.out.printf("Producto creado: %s | precio=%.2f | stock=%d%n",
                product.getName(), product.getPrice(), product.getStock());

        inventory.registerEntry(product, 5);
        System.out.printf("Entrada de 5 unidades. Stock actual: %d%n", product.getStock());

        inventory.registerExit(product, 3);
        System.out.printf("Salida de 3 unidades. Stock actual: %d%n", product.getStock());
        System.out.printf("¿Stock bajo o igual al mínimo (%d)? %s%n",
                product.getMinimumStock(), inventory.hasLowStock(product));
        System.out.printf("Productos registrados: %d%n", products.getAllProducts().size());
        System.out.println("Demo terminada. Aviso: los datos son temporales y se pierden al cerrar.");
    }
}
