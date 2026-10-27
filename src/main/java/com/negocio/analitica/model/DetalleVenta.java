package com.negocio.analitica.model;

public class DetalleVenta {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private String categoriaNombre;
    private int cantidad;
    private double precioUnitario;

    public DetalleVenta() { }
    public DetalleVenta(Long id, Long productoId, String productoNombre, String categoriaNombre,
                        int cantidad, double precioUnitario) {
        this.id = id; this.productoId = productoId; this.productoNombre = productoNombre;
        this.categoriaNombre = categoriaNombre; this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public double getSubtotal() { return cantidad * precioUnitario; }
}
