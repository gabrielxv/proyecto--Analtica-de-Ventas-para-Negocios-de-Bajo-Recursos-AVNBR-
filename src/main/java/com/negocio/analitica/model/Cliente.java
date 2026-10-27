package com.negocio.analitica.model;

public class Cliente {
    private Long id;
    private String nombre;
    private String documento;
    private String telefono;
    private String direccion;

    public Cliente() { }
    public Cliente(Long id, String nombre, String documento, String telefono, String direccion) {
        this.id = id; this.nombre = nombre; this.documento = documento;
        this.telefono = telefono; this.direccion = direccion;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
