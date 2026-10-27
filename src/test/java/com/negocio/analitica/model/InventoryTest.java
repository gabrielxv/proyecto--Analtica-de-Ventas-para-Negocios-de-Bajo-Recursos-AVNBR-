package com.negocio.analitica.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    private final Inventory inventory = new Inventory();

    @Test
    void availabilityRequiresPositiveQuantityAndEnoughStock() {
        assertTrue(inventory.hasAvailability(5, 5));
        assertTrue(inventory.hasAvailability(5, 3));
        assertFalse(inventory.hasAvailability(5, 0));
        assertFalse(inventory.hasAvailability(5, -1));
        assertFalse(inventory.hasAvailability(2, 3));
    }

    @Test
    void registerEntryIncreasesStock() {
        Product product = productWithStock(5, 2);
        inventory.registerEntry(product, 3);
        assertEquals(8, product.getStock());
    }

    @Test
    void registerExitDecreasesStock() {
        Product product = productWithStock(5, 2);
        inventory.registerExit(product, 3);
        assertEquals(2, product.getStock());
    }

    @Test
    void entryRejectsNullProductAndNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class, () -> inventory.registerEntry(null, 1));
        assertThrows(IllegalArgumentException.class, () -> inventory.registerEntry(productWithStock(5, 2), 0));
        assertThrows(IllegalArgumentException.class, () -> inventory.registerEntry(productWithStock(5, 2), -1));
    }

    @Test
    void exitRejectsNullProductInsufficientStockAndNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class, () -> inventory.registerExit(null, 1));
        assertThrows(IllegalArgumentException.class, () -> inventory.registerExit(productWithStock(2, 1), 3));
        assertThrows(IllegalArgumentException.class, () -> inventory.registerExit(productWithStock(2, 1), 0));
    }

    @Test
    void stockIsLowWhenAtOrBelowMinimum() {
        assertTrue(inventory.hasLowStock(productWithStock(2, 2)));
        assertTrue(inventory.hasLowStock(productWithStock(1, 2)));
        assertFalse(inventory.hasLowStock(productWithStock(3, 2)));
    }

    @Test
    void lowStockRejectsNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> inventory.hasLowStock(null));
    }

    private Product productWithStock(int stock, int minimumStock) {
        return new Product(1L, "Test product", 10.0, stock, minimumStock,
                new Category(1L, "Test category", "Test"));
    }
}
