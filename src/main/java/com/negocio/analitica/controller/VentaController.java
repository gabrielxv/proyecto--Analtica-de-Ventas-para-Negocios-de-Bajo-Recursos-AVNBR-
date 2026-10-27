package com.negocio.analitica.controller;

import com.negocio.analitica.model.Venta;
import com.negocio.analitica.service.VentaService;
import java.util.List;

public class VentaController {
    private final VentaService service;
    public VentaController(VentaService service) {
        if(service==null) throw new IllegalArgumentException("VentaService cannot be null.");
        this.service=service;
    }
    public Venta registrar(Venta venta) { return service.registrar(venta); }
    public Venta obtener(Long id) { return service.buscarPorId(id); }
    public List<Venta> listar() { return service.listar(); }
}
