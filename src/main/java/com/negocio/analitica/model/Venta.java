package com.negocio.analitica.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private Long id;
    private LocalDateTime fecha;
    private Long clienteId;
    private String clienteNombre;
    private String metodoPago;
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() { }
    public Venta(Long id, LocalDateTime fecha, Long clienteId, String clienteNombre,
                 String metodoPago, List<DetalleVenta> detalles) {
        this.id = id; this.fecha = fecha; this.clienteId = clienteId;
        this.clienteNombre = clienteNombre; this.metodoPago = metodoPago;
        setDetalles(detalles);
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public List<DetalleVenta> getDetalles() { return List.copyOf(detalles); }
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles == null ? new ArrayList<>() : new ArrayList<>(detalles);
    }
    public double getTotal() { return detalles.stream().mapToDouble(DetalleVenta::getSubtotal).sum(); }
    public int getUnidadesVendidas() { return detalles.stream().mapToInt(DetalleVenta::getCantidad).sum(); }
}
