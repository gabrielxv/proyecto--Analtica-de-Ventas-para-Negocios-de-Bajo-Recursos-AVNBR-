package com.negocio.analitica;

import com.negocio.analitica.controller.*;
import com.negocio.analitica.model.*;
import com.negocio.analitica.repository.sqlite.*;
import com.negocio.analitica.service.*;
import java.util.Scanner;

/** Punto de entrada de AVNBR. Inicia la base local y el menú de operación. */
public final class Application {
    private Application() { }

    public static void main(String[] args) {
        SqliteDatabase database = new SqliteDatabase("jdbc:sqlite:data/avnbr.db");
        SqliteCategoryRepository categoryRepository = new SqliteCategoryRepository(database);
        SqliteProductRepository productRepository = new SqliteProductRepository(database);
        SqliteClienteRepository clienteRepository = new SqliteClienteRepository(database);
        SqliteVentaRepository ventaRepository = new SqliteVentaRepository(database);

        CategoryService categoryService = new CategoryService(categoryRepository);
        ProductService productService = new ProductService(productRepository, categoryRepository);
        ClienteService clienteService = new ClienteService(clienteRepository);

        CategoryController categories = new CategoryController(categoryService);
        ProductController products = new ProductController(productService);
        ClienteController clientes = new ClienteController(clienteService);
        VentaController ventas = new VentaController(
                new VentaService(clienteService, productService, ventaRepository));
        AnaliticaController analitica = new AnaliticaController(
                new AnaliticaService(ventaRepository, productService));

        System.out.println("Base de datos local lista: data/avnbr.db");
        try (Scanner scanner = new Scanner(System.in)) {
            new ConsoleMenu(scanner, categories, products, clientes, ventas, analitica).iniciar();
        }
    }
}
