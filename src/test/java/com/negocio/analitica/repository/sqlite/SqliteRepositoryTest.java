package com.negocio.analitica.repository.sqlite;

import com.negocio.analitica.model.Category;
import com.negocio.analitica.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class SqliteRepositoryTest {
    @TempDir Path tempDir;

    @Test void persistsCategoriesAndProductsAcrossRepositoryInstances() {
        String url = "jdbc:sqlite:" + tempDir.resolve("avnbr-test.db");
        SqliteDatabase database = new SqliteDatabase(url);
        Category category = new SqliteCategoryRepository(database)
                .save(new Category(null, "Food", "Basic foods"));
        Product saved = new SqliteProductRepository(database)
                .save(new Product(null, "Rice", 3500.0, 12, 4, category));

        SqliteDatabase reopened = new SqliteDatabase(url);
        Product loaded = new SqliteProductRepository(reopened).findById(saved.getId()).orElseThrow();
        assertEquals("Rice", loaded.getName());
        assertEquals(12, loaded.getStock());
        assertEquals("Food", loaded.getCategory().getName());
        assertEquals(1, new SqliteCategoryRepository(reopened).findAll().size());
    }

    @Test void categoryCannotBeDeletedWhileProductsReferenceIt() {
        SqliteDatabase database = new SqliteDatabase("jdbc:sqlite:" + tempDir.resolve("fk-test.db"));
        SqliteCategoryRepository categories = new SqliteCategoryRepository(database);
        Category category = categories.save(new Category(null, "Food", ""));
        new SqliteProductRepository(database).save(new Product(null, "Rice", 1.0, 1, 0, category));
        assertThrows(IllegalStateException.class, () -> categories.deleteById(category.getId()));
    }
}
