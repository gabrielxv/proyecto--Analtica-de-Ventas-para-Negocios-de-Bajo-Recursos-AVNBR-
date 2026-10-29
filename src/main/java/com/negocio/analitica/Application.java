package com.negocio.analitica;

import com.negocio.analitica.controller.CategoryController;
import com.negocio.analitica.controller.ProductController;
import com.negocio.analitica.model.Category;
import com.negocio.analitica.model.Product;
import com.negocio.analitica.repository.memory.InMemoryCategoryRepository;
import com.negocio.analitica.repository.memory.InMemoryProductRepository;
import com.negocio.analitica.service.CategoryService;
import com.negocio.analitica.service.ProductService;

import java.util.List;
import java.util.Scanner;

/**
 * Aplicación de consola para gestionar categorías, productos e inventario.
 * Los datos se almacenan en memoria y se pierden al cerrar la aplicación.
 */
public final class Application {
    private static final Scanner SCANNER = new Scanner(System.in);

    private static CategoryController categories;
    private static ProductController products;
    private static ProductService productService;

    private Application() { }

    public static void main(String[] args) {
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        InMemoryProductRepository productRepository = new InMemoryProductRepository();

        CategoryService categoryService = new CategoryService(categoryRepository);
        productService = new ProductService(productRepository, categoryRepository);
        categories = new CategoryController(categoryService);
        products = new ProductController(productService);

        boolean running = true;
        while (running) {
            printMenu();
            int option = readInt("Elige una opción: ");

            try {
                switch (option) {
                    case 1 -> createCategory();
                    case 2 -> listCategories();
                    case 3 -> findCategory();
                    case 4 -> updateCategory();
                    case 5 -> deleteCategory();
                    case 6 -> createProduct();
                    case 7 -> listProducts();
                    case 8 -> findProduct();
                    case 9 -> updateProduct();
                    case 10 -> deleteProduct();
                    case 11 -> registerStockEntry();
                    case 12 -> registerStockExit();
                    case 13 -> listLowStockProducts();
                    case 0 -> running = false;
                    default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            } catch (IllegalArgumentException | ArithmeticException exception) {
                System.out.println("No se pudo completar la operación: " + exception.getMessage());
            } catch (Exception exception) {
                System.out.println("Ocurrió un error inesperado: " + exception.getMessage());
            }

            if (running) {
                System.out.println("\nPresiona Enter para continuar...");
                SCANNER.nextLine();
            }
        }

        System.out.println("Aplicación cerrada. Los datos guardados en memoria se han perdido.");
        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println("\n=== AVNBR | Categorías, productos e inventario ===");
        System.out.println("1. Crear categoría");
        System.out.println("2. Listar categorías");
        System.out.println("3. Buscar categoría por ID");
        System.out.println("4. Actualizar categoría");
        System.out.println("5. Eliminar categoría");
        System.out.println("6. Crear producto");
        System.out.println("7. Listar productos");
        System.out.println("8. Buscar producto por ID");
        System.out.println("9. Actualizar producto");
        System.out.println("10. Eliminar producto");
        System.out.println("11. Registrar entrada de inventario");
        System.out.println("12. Registrar salida de inventario");
        System.out.println("13. Listar productos con poco inventario");
        System.out.println("0. Salir");
    }

    private static void createCategory() {
        String name = readText("Nombre de la categoría: ");
        String description = readText("Descripción: ");
        Category category = categories.createCategory(new Category(null, name, description));
        System.out.println("Categoría creada con el ID " + category.getId());
    }

    private static void listCategories() {
        List<Category> result = categories.getAllCategories();
        if (result.isEmpty()) {
            System.out.println("No hay categorías registradas.");
            return;
        }
        result.forEach(Application::printCategory);
    }

    private static void findCategory() {
        printCategory(categories.getCategory(readLong("ID de la categoría: ")));
    }

    private static void updateCategory() {
        Long id = readLong("ID de la categoría que deseas actualizar: ");
        Category current = categories.getCategory(id);
        String name = readText("Nuevo nombre: ");
        String description = readText("Nueva descripción: ");
        current.setName(name);
        current.setDescription(description);
        categories.updateCategory(current);
        System.out.println("Categoría actualizada.");
    }

    private static void deleteCategory() {
        categories.deleteCategory(readLong("ID de la categoría que deseas eliminar: "));
        System.out.println("Categoría eliminada.");
    }

    private static void createProduct() {
        listCategories();
        Long categoryId = readLong("ID de una categoría existente: ");
        Category category = categories.getCategory(categoryId);
        String name = readText("Nombre del producto: ");
        double price = readDouble("Precio: ");
        int stock = readInt("Inventario inicial: ");
        int minimumStock = readInt("Cantidad mínima para la alerta: ");
        Product product = products.createProduct(
                new Product(null, name, price, stock, minimumStock, category));
        System.out.println("Producto creado con el ID " + product.getId());
    }

    private static void listProducts() {
        List<Product> result = products.getAllProducts();
        if (result.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        result.forEach(Application::printProduct);
    }

    private static void findProduct() {
        printProduct(products.getProduct(readLong("ID del producto: ")));
    }

    private static void updateProduct() {
        Long id = readLong("ID del producto que deseas actualizar: ");
        Product current = products.getProduct(id);
        listCategories();
        Category category = categories.getCategory(readLong("ID de la nueva categoría: "));
        current.setName(readText("Nuevo nombre del producto: "));
        current.setPrice(readDouble("Nuevo precio: "));
        current.setStock(readInt("Nuevo inventario: "));
        current.setMinimumStock(readInt("Nueva cantidad mínima para la alerta: "));
        current.setCategory(category);
        products.updateProduct(current);
        System.out.println("Producto actualizado.");
    }

    private static void deleteProduct() {
        products.deleteProduct(readLong("ID del producto que deseas eliminar: "));
        System.out.println("Producto eliminado.");
    }

    private static void registerStockEntry() {
        Long id = readLong("ID del producto: ");
        int quantity = readInt("Cantidad que deseas agregar: ");
        Product product = productService.registerStockEntry(id, quantity);
        System.out.println("Entrada de inventario registrada. Inventario actual: " + product.getStock());
    }

    private static void registerStockExit() {
        Long id = readLong("ID del producto: ");
        int quantity = readInt("Cantidad que deseas retirar: ");
        Product product = productService.registerStockExit(id, quantity);
        System.out.println("Salida de inventario registrada. Inventario actual: " + product.getStock());
    }

    private static void listLowStockProducts() {
        List<Product> result = productService.findLowStockProducts();
        if (result.isEmpty()) {
            System.out.println("Ningún producto tiene actualmente inventario bajo.");
            return;
        }
        System.out.println("Productos con inventario igual o inferior al mínimo establecido:");
        result.forEach(Application::printProduct);
    }

    private static void printCategory(Category category) {
        System.out.printf("ID: %d | Nombre: %s | Descripción: %s%n",
                category.getId(), category.getName(), category.getDescription());
    }

    private static void printProduct(Product product) {
        String categoryName = product.getCategory() == null
                ? "Sin categoría" : product.getCategory().getName();
        System.out.printf("ID: %d | Nombre: %s | Precio: %.2f | Inventario: %d"
                        + " | Mínimo: %d | Categoría: %s%n",
                product.getId(), product.getName(), product.getPrice(),
                product.getStock(), product.getMinimumStock(), categoryName);
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            String value = readText(prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("Ingresa un número entero.");
            }
        }
    }

    private static long readLong(String prompt) {
        while (true) {
            String value = readText(prompt);
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException exception) {
                System.out.println("Ingresa un ID numérico válido.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            String value = readText(prompt);
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException exception) {
                System.out.println("Ingresa un valor numérico válido.");
            }
        }
    }
}
