package com.negocio.analitica.model;

public class Inventory {

    public boolean hasAvailability(int currentStock, int quantity) {
        return quantity > 0 && currentStock >= quantity;
    }

    public void registerEntry(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        // Evita que una entrada excesiva desborde el entero y genere stock negativo.
        product.setStock(Math.addExact(product.getStock(), quantity));
    }

    public void registerExit(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (!hasAvailability(product.getStock(), quantity)) {
            throw new IllegalArgumentException("Insufficient stock or invalid quantity.");
        }

        product.setStock(product.getStock() - quantity);
    }

    public boolean hasLowStock(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        return product.getStock() <= product.getMinimumStock();
    }
}
