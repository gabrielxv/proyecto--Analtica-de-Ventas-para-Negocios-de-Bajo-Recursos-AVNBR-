package com.negocio.analitica.repository.sqlite;

import com.negocio.analitica.model.Cliente;
import com.negocio.analitica.repository.ClienteRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SqliteClienteRepository implements ClienteRepository {
    private final SqliteDatabase database;
    public SqliteClienteRepository(SqliteDatabase database) {
        if (database == null) throw new IllegalArgumentException("Database cannot be null.");
        this.database = database;
    }
    @Override public Cliente save(Cliente cliente) {
        if (cliente == null) throw new IllegalArgumentException("Cliente cannot be null.");
        String sql = cliente.getId() == null
                ? "INSERT INTO customers(name,document,phone,address) VALUES(?,?,?,?)"
                : "UPDATE customers SET name=?,document=?,phone=?,address=? WHERE id=?";
        try (Connection c = database.getConnection(); PreparedStatement s = c.prepareStatement(sql)) {
            bind(s, cliente);
            if (cliente.getId() != null) s.setLong(5, cliente.getId());
            if (s.executeUpdate() == 0) throw new IllegalArgumentException("Cliente no existe.");
            if (cliente.getId() == null) try (Statement q = c.createStatement();
                    ResultSet rs = q.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) cliente.setId(rs.getLong(1));
            }
            return cliente;
        } catch (SQLException e) { throw new IllegalStateException("No fue posible guardar el cliente.", e); }
    }
    @Override public Optional<Cliente> findById(Long id) {
        if (id == null) return Optional.empty();
        try (Connection c = database.getConnection();
             PreparedStatement s = c.prepareStatement("SELECT * FROM customers WHERE id=?")) {
            s.setLong(1,id);
            try (ResultSet rs=s.executeQuery()) { return rs.next()?Optional.of(map(rs)):Optional.empty(); }
        } catch (SQLException e) { throw new IllegalStateException("No fue posible consultar el cliente.",e); }
    }
    @Override public Optional<Cliente> findByDocumento(String documento) {
        if (documento == null) return Optional.empty();
        try (Connection c=database.getConnection();
             PreparedStatement s=c.prepareStatement("SELECT * FROM customers WHERE document=?")) {
            s.setString(1,documento.trim());
            try (ResultSet rs=s.executeQuery()) { return rs.next()?Optional.of(map(rs)):Optional.empty(); }
        } catch (SQLException e) { throw new IllegalStateException("No fue posible buscar el documento.",e); }
    }
    @Override public List<Cliente> findAll() {
        List<Cliente> result=new ArrayList<>();
        try (Connection c=database.getConnection(); Statement s=c.createStatement();
             ResultSet rs=s.executeQuery("SELECT * FROM customers ORDER BY name")) {
            while(rs.next()) result.add(map(rs));
            return result;
        } catch(SQLException e) { throw new IllegalStateException("No fue posible listar clientes.",e); }
    }
    @Override public void deleteById(Long id) {
        if(id==null) return;
        try(Connection c=database.getConnection(); PreparedStatement s=c.prepareStatement("DELETE FROM customers WHERE id=?")) {
            s.setLong(1,id); s.executeUpdate();
        } catch(SQLException e) { throw new IllegalStateException("No se puede eliminar el cliente: puede tener ventas asociadas.",e); }
    }
    private static void bind(PreparedStatement s, Cliente c) throws SQLException {
        s.setString(1,c.getNombre()); s.setString(2,c.getDocumento().trim());
        s.setString(3,c.getTelefono()); s.setString(4,c.getDireccion());
    }
    private static Cliente map(ResultSet rs) throws SQLException {
        return new Cliente(rs.getLong("id"),rs.getString("name"),rs.getString("document"),
                rs.getString("phone"),rs.getString("address"));
    }
}
