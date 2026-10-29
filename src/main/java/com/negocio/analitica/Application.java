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
 * Interactive console application for the categories, products, and inventory module.
 * Uses in-memory repositories; data is lost when the application closes.
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
            int option = readInt("Choose an option: ");

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
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (IllegalArgumentException | ArithmeticException exception) {
                System.out.println("Operation failed: " + exception.getMessage());
            } catch (Exception exception) {
                System.out.println("Unexpected error: " + exception.getMessage());
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                SCANNER.nextLine();
            }
        }

        System.out.println("Application closed. In-memory data has not been saved.");
        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println("\n=== AVNBR | Categories, Products and Inventory ===");
        System.out.println("1. Create category");
        System.out.println("2. List categories");
        System.out.println("3. Find category by ID");
        System.out.println("4. Update category");
        System.out.println("5. Delete category");
        System.out.println("6. Create product");
        System.out.println("7. List products");
        System.out.println("8. Find product by ID");
        System.out.println("9. Update product");
        System.out.println("10. Delete product");
        System.out.println("11. Register stock entry");
        System.out.println("12. Register stock exit");
        System.out.println("13. List products with low stock");
        System.out.println("0. Exit");
    }

    private static void createCategory() {
        String name = readText("Category name: ");
        String description = readText("Description: ");
        Category category = categories.createCategory(new Category(null, name, description));
        System.out.println("Category created with ID " + category.getId());
    }

    private static void listCategories() {
        List<Category> result = categories.getAllCategories();
        if (result.isEmpty()) {
            System.out.println("No categories registered.");
            return;
        }
        result.forEach(Application::printCategory);
    }

    private static void findCategory() {
        printCategory(categories.getCategory(readLong("Category ID: ")));
    }

    private static void updateCategory() {
        Long id = readLong("Category ID to update: ");
        Category current = categories.getCategory(id);
        String name = readText("New name: ");
        String description = readText("New description: ");
        current.setName(name);
        current.setDescription(description);
        categories.updateCategory(current);
        System.out.println("Category updated.");
    }

    private static void deleteCategory() {
        categories.deleteCategory(readLong("Category ID to delete: "));
        System.out.println("Category deleted.");
    }

    private static void createProduct() {
        listCategories();
        Long categoryId = readLong("Existing category ID: ");
        Category category = categories.getCategory(categoryId);
        String name = readText("Product name: ");
        double price = readDouble("Price: ");
        int stock = readInt("Initial stock: ");
        int minimumStock = readInt("Minimum stock alert: ");
        Product product = products.createProduct(
                new Product(null, name, price, stock, minimumStock, category));
        System.out.println("Product created with ID " + product.getId());
    }

    private static void listProducts() {
        List<Product> result = products.getAllProducts();
        if (result.isEmpty()) {
            System.out.println("No products registered.");
            return;
        }
        result.forEach(Application::printProduct);
    }

    private static void findProduct() {
        printProduct(products.getProduct(readLong("Product ID: ")));
    }

    private static void updateProduct() {
        Long id = readLong("Product ID to update: ");
        Product current = products.getProduct(id);
        listCategories();
        Category category = categories.getCategory(readLong("New category ID: "));
        current.setName(readText("New product name: "));
        current.setPrice(readDouble("New price: "));
        current.setStock(readInt("New stock: "));
        current.setMinimumStock(readInt("New minimum stock alert: "));
        current.setCategory(category);
        products.updateProduct(current);
        System.out.println("Product updated.");
    }

    private static void deleteProduct() {
        products.deleteProduct(readLong("Product ID to delete: "));
        System.out.println("Product deleted.");
    }

    private static void registerStockEntry() {
        Long id = readLong("Product ID: ");
        int quantity = readInt("Quantity to add: ");
        Product product = productService.registerStockEntry(id, quantity);
        System.out.println("Stock entry registered. Current stock: " + product.getStock());
    }

    private static void registerStockExit() {
        Long id = readLong("Product ID: ");
        int quantity = readInt("Quantity to remove: ");
        Product product = productService.registerStockExit(id, quantity);
        System.out.println("Stock exit registered. Current stock: " + product.getStock());
    }

    private static void listLowStockProducts() {
        List<Product> result = productService.findLowStockProducts();
        if (result.isEmpty()) {
            System.out.println("No products currently have low stock.");
            return;
        }
        System.out.println("Products at or below their minimum stock:");
        result.forEach(Application::printProduct);
    }

    private static void printCategory(Category category) {
        System.out.printf("ID: %d | Name: %s | Description: %s%n",
                category.getId(), category.getName(), category.getDescription());
    }

    private static void printProduct(Product product) {
        String categoryName = product.getCategory() == null
                ? "Unassigned" : product.getCategory().getName();
        System.out.printf("ID: %d | Name: %s | Price: %.2f | Stock: %d"
                        + " | Minimum: %d | Category: %s%n",
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
                System.out.println("Enter a whole number.");
            }
        }
    }

    private static long readLong(String prompt) {
        while (true) {
            String value = readText(prompt);
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid ID number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            String value = readText(prompt);
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid numeric value.");
            }
        }
    }
}
