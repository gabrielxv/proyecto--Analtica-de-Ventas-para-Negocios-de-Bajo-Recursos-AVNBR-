package com.negocio.analitica.service;

import com.negocio.analitica.model.*;
import com.negocio.analitica.repository.sqlite.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DesarrolladorDosIntegrationTest {
    @TempDir Path tempDir;

    @Test void salePersistsDetailsUpdatesStockAndFeedsAnalytics() {
        SqliteDatabase db = new SqliteDatabase("jdbc:sqlite:" + tempDir.resolve("sales-test.db"));
        SqliteCategoryRepository categories = new SqliteCategoryRepository(db);
        SqliteProductRepository products = new SqliteProductRepository(db);
        SqliteClienteRepository clients = new SqliteClienteRepository(db);
        SqliteVentaRepository sales = new SqliteVentaRepository(db);
        Category category = categories.save(new Category(null, "Alimentos", "Prueba"));
        Product product = products.save(new Product(null, "Arroz", 2500, 10, 2, category));
        ClienteService customerService = new ClienteService(clients);
        Cliente client = customerService.crear(new Cliente(null, "Ana", "CC-001", "300", "Centro"));
        ProductService productService = new ProductService(products, categories);
        VentaService saleService = new VentaService(customerService, productService, sales);

        Venta sale = new Venta(null, null, client.getId(), null, "Efectivo",
                List.of(new DetalleVenta(null, product.getId(), null, null, 3, 0)));
        Venta saved = saleService.registrar(sale);

        assertNotNull(saved.getId());
        assertEquals(7500.0, saved.getTotal(), 0.001);
        assertEquals(7, productService.findProductById(product.getId()).getStock());
        assertEquals(1, sales.findAll().size());
        AnaliticaService analytics = new AnaliticaService(sales, productService);
        LocalDate today = LocalDate.now();
        assertEquals(7500.0, analytics.totalVentas(today, today), 0.001);
        assertEquals(3, analytics.unidadesVendidas(today, today));
        assertEquals("Arroz", analytics.productoMasVendido(today, today).orElseThrow());
        assertEquals(7500.0, analytics.ventasPorCategoria(today, today).get("Alimentos"), 0.001);
    }

    @Test void rejectsDuplicateCustomerDocumentAndInsufficientStock() {
        SqliteDatabase db = new SqliteDatabase("jdbc:sqlite:" + tempDir.resolve("validation-test.db"));
        SqliteCategoryRepository categories = new SqliteCategoryRepository(db);
        SqliteProductRepository products = new SqliteProductRepository(db);
        SqliteClienteRepository clients = new SqliteClienteRepository(db);
        SqliteVentaRepository sales = new SqliteVentaRepository(db);
        Category category = categories.save(new Category(null, "Bebidas", ""));
        Product product = products.save(new Product(null, "Agua", 1000, 2, 1, category));
        ClienteService customerService = new ClienteService(clients);
        Cliente client = customerService.crear(new Cliente(null, "Luis", "CC-002", null, null));
        assertThrows(IllegalArgumentException.class,
                () -> customerService.crear(new Cliente(null, "Otro", "CC-002", null, null)));
        VentaService saleService = new VentaService(customerService,
                new ProductService(products, categories), sales);
        Venta request = new Venta(null, null, client.getId(), null, "Tarjeta",
                List.of(new DetalleVenta(null, product.getId(), null, null, 3, 0)));
        assertThrows(IllegalArgumentException.class, () -> saleService.registrar(request));
        assertEquals(2, products.findById(product.getId()).orElseThrow().getStock());
        assertTrue(sales.findAll().isEmpty());
    }
}
