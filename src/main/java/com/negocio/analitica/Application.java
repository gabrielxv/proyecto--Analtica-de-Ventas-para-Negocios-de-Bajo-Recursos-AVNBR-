package com.negocio.analitica;

import com.negocio.analitica.controller.AnaliticaController;
import com.negocio.analitica.controller.CategoryController;
import com.negocio.analitica.controller.ClienteController;
import com.negocio.analitica.controller.ProductController;
import com.negocio.analitica.controller.VentaController;
import com.negocio.analitica.model.Category;
import com.negocio.analitica.model.Inventory;
import com.negocio.analitica.model.Product;
import com.negocio.analitica.repository.sqlite.SqliteCategoryRepository;
import com.negocio.analitica.repository.sqlite.SqliteClienteRepository;
import com.negocio.analitica.repository.sqlite.SqliteDatabase;
import com.negocio.analitica.repository.sqlite.SqliteProductRepository;
import com.negocio.analitica.repository.sqlite.SqliteVentaRepository;
import com.negocio.analitica.service.AnaliticaService;
import com.negocio.analitica.service.CategoryService;
import com.negocio.analitica.service.ClienteService;
import com.negocio.analitica.service.ProductService;
import com.negocio.analitica.service.VentaService;
import java.time.LocalDate;

/** Console demo backed by SQLite. */
public final class Application {
    private Application() { }
    public static void main(String[] args) {
        SqliteDatabase database = new SqliteDatabase("jdbc:sqlite:data/avnbr.db");
        SqliteCategoryRepository categoryRepository = new SqliteCategoryRepository(database);
        SqliteProductRepository productRepository = new SqliteProductRepository(database);
        SqliteClienteRepository clienteRepository = new SqliteClienteRepository(database);
        SqliteVentaRepository ventaRepository = new SqliteVentaRepository(database);

        CategoryController categories = new CategoryController(new CategoryService(categoryRepository));
        ProductService productService = new ProductService(productRepository, categoryRepository);
        ProductController products = new ProductController(productService);
        ClienteController clientes = new ClienteController(new ClienteService(clienteRepository));
        VentaController ventas = new VentaController(new VentaService(
                new ClienteService(clienteRepository), productService, ventaRepository));
        AnaliticaController analitica = new AnaliticaController(new AnaliticaService(ventaRepository, productService));
        Inventory inventory = new Inventory();

        Category category = categoryRepository.findAll().stream()
                .filter(existing -> "Abarrotes".equals(existing.getName()))
                .findFirst()
                .orElseGet(() -> categories.createCategory(new Category(null, "Abarrotes", "Productos básicos")));
        Product product = productRepository.findAll().stream()
                .filter(existing -> "Arroz".equals(existing.getName()))
                .findFirst()
                .orElseGet(() -> products.createProduct(new Product(null, "Arroz", 3500.0, 12, 4, category)));

        System.out.println("=== AVNBR | Demostración con SQLite ===");
        System.out.printf("Categoría: %s (id=%d)%n", category.getName(), category.getId());
        System.out.printf("Producto: %s | precio=%.2f | stock inicial=%d%n",
                product.getName(), product.getPrice(), product.getStock());
        System.out.printf("Clientes registrados: %d%n", clientes.listar().size());
        System.out.printf("Ventas registradas: %d%n", ventas.listar().size());
        System.out.printf("Ingresos acumulados: %.2f%n",
                analitica.totalVentas(LocalDate.of(2000, 1, 1), LocalDate.now()));
        System.out.printf("¿Stock bajo o igual al mínimo (%d)? %s%n",
                product.getMinimumStock(), inventory.hasLowStock(product));
        System.out.println("Base de datos: data/avnbr.db");
        System.out.printf("Productos persistidos: %d%n", products.getAllProducts().size());
        System.out.println("Los datos se conservan entre ejecuciones.");
    }
}
