package com.negocio.analitica.repository.sqlite;

import com.negocio.analitica.model.DetalleVenta;
import com.negocio.analitica.model.Venta;
import com.negocio.analitica.repository.VentaRepository;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SqliteVentaRepository implements VentaRepository {
    private final SqliteDatabase database;
    public SqliteVentaRepository(SqliteDatabase database) {
        if (database == null) throw new IllegalArgumentException("Database cannot be null.");
        this.database = database;
    }
    @Override public Venta save(Venta venta) {
        if (venta == null) throw new IllegalArgumentException("Venta cannot be null.");
        String insert = "INSERT INTO sales(sold_at,customer_id,customer_name,payment_method,total) VALUES(?,?,?,?,?)";
        try (Connection c=database.getConnection()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement s=c.prepareStatement(insert)) {
                    s.setString(1,venta.getFecha().toString()); s.setLong(2,venta.getClienteId());
                    s.setString(3,venta.getClienteNombre()); s.setString(4,venta.getMetodoPago());
                    s.setDouble(5,venta.getTotal()); s.executeUpdate();
                }
                try (Statement s=c.createStatement(); ResultSet rs=s.executeQuery("SELECT last_insert_rowid()")) {
                    if(rs.next()) venta.setId(rs.getLong(1));
                }
                String detailSql = "INSERT INTO sale_details(sale_id,product_id,product_name,category_name,quantity,unit_price) VALUES(?,?,?,?,?,?)";
                try (PreparedStatement s=c.prepareStatement(detailSql)) {
                    for(DetalleVenta d:venta.getDetalles()) {
                        s.setLong(1,venta.getId()); s.setLong(2,d.getProductoId());
                        s.setString(3,d.getProductoNombre()); s.setString(4,d.getCategoriaNombre());
                        s.setInt(5,d.getCantidad()); s.setDouble(6,d.getPrecioUnitario()); s.addBatch();
                    }
                    s.executeBatch();
                }
                c.commit();
                return venta;
            } catch (SQLException | RuntimeException ex) {
                c.rollback();
                throw ex;
            } finally { c.setAutoCommit(true); }
        } catch(SQLException e) { throw new IllegalStateException("No fue posible registrar la venta.",e); }
    }
    @Override public Optional<Venta> findById(Long id) {
        if(id==null) return Optional.empty();
        try(Connection c=database.getConnection(); PreparedStatement s=c.prepareStatement("SELECT * FROM sales WHERE id=?")) {
            s.setLong(1,id);
            try(ResultSet rs=s.executeQuery()) { return rs.next()?Optional.of(mapSale(c,rs)):Optional.empty(); }
        } catch(SQLException e) { throw new IllegalStateException("No fue posible consultar la venta.",e); }
    }
    @Override public List<Venta> findAll() { return query(null,null); }
    @Override public List<Venta> findByPeriod(LocalDate desde, LocalDate hasta) {
        if(desde==null || hasta==null || hasta.isBefore(desde))
            throw new IllegalArgumentException("El período de fechas no es válido.");
        return query(desde,hasta);
    }
    private List<Venta> query(LocalDate desde, LocalDate hasta) {
        List<Venta> result=new ArrayList<>();
        String sql="SELECT * FROM sales"+(desde==null?"":" WHERE date(sold_at) BETWEEN ? AND ?")+" ORDER BY sold_at,id";
        try(Connection c=database.getConnection(); PreparedStatement s=c.prepareStatement(sql)) {
            if(desde!=null) { s.setString(1,desde.toString()); s.setString(2,hasta.toString()); }
            try(ResultSet rs=s.executeQuery()) { while(rs.next()) result.add(mapSale(c,rs)); }
            return result;
        } catch(SQLException e) { throw new IllegalStateException("No fue posible consultar ventas.",e); }
    }
    private Venta mapSale(Connection c, ResultSet rs) throws SQLException {
        long id=rs.getLong("id");
        Venta v=new Venta(id,LocalDateTime.parse(rs.getString("sold_at")),rs.getLong("customer_id"),
                rs.getString("customer_name"),rs.getString("payment_method"),new ArrayList<>());
        List<DetalleVenta> details=new ArrayList<>();
        try(PreparedStatement s=c.prepareStatement("SELECT * FROM sale_details WHERE sale_id=? ORDER BY id")) {
            s.setLong(1,id);
            try(ResultSet d=s.executeQuery()) {
                while(d.next()) details.add(new DetalleVenta(d.getLong("id"),d.getLong("product_id"),
                        d.getString("product_name"),d.getString("category_name"),d.getInt("quantity"),d.getDouble("unit_price")));
            }
        }
        v.setDetalles(details);
        return v;
    }
}
