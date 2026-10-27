package com.negocio.analitica.repository.sqlite;

import com.negocio.analitica.model.Category;
import com.negocio.analitica.model.Product;
import com.negocio.analitica.repository.ProductRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SqliteProductRepository implements ProductRepository {
    private final SqliteDatabase database;
    public SqliteProductRepository(SqliteDatabase database) {
        if (database == null) throw new IllegalArgumentException("Database cannot be null.");
        this.database = database;
    }
    @Override public Product save(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        if (product.getCategory() == null || product.getCategory().getId() == null)
            throw new IllegalArgumentException("A saved product must reference a category.");
        String sql = product.getId() == null
                ? "INSERT INTO products(name,price,stock,minimum_stock,category_id) VALUES(?,?,?,?,?)"
                : "UPDATE products SET name=?,price=?,stock=?,minimum_stock=?,category_id=? WHERE id=?";
        try (Connection c = database.getConnection(); PreparedStatement s = c.prepareStatement(sql)) {
            bind(s, product);
            if (product.getId() != null) s.setLong(6, product.getId());
            if (s.executeUpdate() == 0) throw new IllegalArgumentException("Product does not exist.");
            if (product.getId() == null) try (Statement q = c.createStatement();
                    ResultSet rs = q.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) product.setId(rs.getLong(1));
            }
            return product;
        } catch (SQLException e) { throw new IllegalStateException("Could not save product.", e); }
    }
    @Override public Optional<Product> findById(Long id) {
        if (id == null) return Optional.empty();
        try (Connection c = database.getConnection();
             PreparedStatement s = c.prepareStatement(selectSql() + " WHERE p.id=?")) {
            s.setLong(1, id);
            try (ResultSet rs = s.executeQuery()) { return rs.next() ? Optional.of(map(rs)) : Optional.empty(); }
        } catch (SQLException e) { throw new IllegalStateException("Could not find product.", e); }
    }
    @Override public List<Product> findAll() {
        List<Product> result = new ArrayList<>();
        try (Connection c = database.getConnection(); Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(selectSql() + " ORDER BY p.id")) {
            while (rs.next()) result.add(map(rs));
            return result;
        } catch (SQLException e) { throw new IllegalStateException("Could not list products.", e); }
    }
    @Override public void deleteById(Long id) {
        if (id == null) return;
        try (Connection c = database.getConnection();
             PreparedStatement s = c.prepareStatement("DELETE FROM products WHERE id=?")) {
            s.setLong(1, id); s.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Could not delete product.", e); }
    }
    private static String selectSql() {
        return """
                SELECT p.id,p.name,p.price,p.stock,p.minimum_stock,
                       c.id AS category_id,c.name AS category_name,c.description AS category_description
                FROM products p JOIN categories c ON c.id=p.category_id
                """;
    }
    private static void bind(PreparedStatement s, Product p) throws SQLException {
        s.setString(1,p.getName()); s.setDouble(2,p.getPrice()); s.setInt(3,p.getStock());
        s.setInt(4,p.getMinimumStock()); s.setLong(5,p.getCategory().getId());
    }
    private static Product map(ResultSet rs) throws SQLException {
        Category category = new Category(rs.getLong("category_id"), rs.getString("category_name"),
                rs.getString("category_description"));
        return new Product(rs.getLong("id"), rs.getString("name"), rs.getDouble("price"),
                rs.getInt("stock"), rs.getInt("minimum_stock"), category);
    }
}
