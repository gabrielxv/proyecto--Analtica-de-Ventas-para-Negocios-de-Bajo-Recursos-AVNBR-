package com.negocio.analitica.repository.sqlite;

import com.negocio.analitica.model.Category;
import com.negocio.analitica.repository.CategoryRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SqliteCategoryRepository implements CategoryRepository {
    private final SqliteDatabase database;
    public SqliteCategoryRepository(SqliteDatabase database) {
        if (database == null) throw new IllegalArgumentException("Database cannot be null.");
        this.database = database;
    }
    @Override public Category save(Category category) {
        if (category == null) throw new IllegalArgumentException("Category cannot be null.");
        String sql = category.getId() == null
                ? "INSERT INTO categories(name, description) VALUES(?, ?)"
                : "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        try (Connection c = database.getConnection(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, category.getName()); s.setString(2, category.getDescription());
            if (category.getId() != null) s.setLong(3, category.getId());
            if (s.executeUpdate() == 0) throw new IllegalArgumentException("Category does not exist.");
            if (category.getId() == null) try (Statement q = c.createStatement();
                    ResultSet rs = q.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) category.setId(rs.getLong(1));
            }
            return category;
        } catch (SQLException e) { throw new IllegalStateException("Could not save category.", e); }
    }
    @Override public Optional<Category> findById(Long id) {
        if (id == null) return Optional.empty();
        try (Connection c = database.getConnection();
             PreparedStatement s = c.prepareStatement("SELECT id,name,description FROM categories WHERE id=?")) {
            s.setLong(1, id);
            try (ResultSet rs = s.executeQuery()) { return rs.next() ? Optional.of(map(rs)) : Optional.empty(); }
        } catch (SQLException e) { throw new IllegalStateException("Could not find category.", e); }
    }
    @Override public List<Category> findAll() {
        List<Category> result = new ArrayList<>();
        try (Connection c = database.getConnection(); Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id,name,description FROM categories ORDER BY id")) {
            while (rs.next()) result.add(map(rs));
            return result;
        } catch (SQLException e) { throw new IllegalStateException("Could not list categories.", e); }
    }
    @Override public void deleteById(Long id) {
        if (id == null) return;
        try (Connection c = database.getConnection();
             PreparedStatement s = c.prepareStatement("DELETE FROM categories WHERE id=?")) {
            s.setLong(1, id); s.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not delete category; products may reference it.", e);
        }
    }
    private static Category map(ResultSet rs) throws SQLException {
        return new Category(rs.getLong("id"), rs.getString("name"), rs.getString("description"));
    }
}
