package com.negocio.analitica.controller;

import com.negocio.analitica.model.Product;
import com.negocio.analitica.model.Venta;
import com.negocio.analitica.service.AnaliticaService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnaliticaController {
    private final AnaliticaService service;
    public AnaliticaController(AnaliticaService service) {
        if(service==null) throw new IllegalArgumentException("AnaliticaService cannot be null.");
        this.service=service;
    }
    public List<Venta> ventasEntre(LocalDate desde, LocalDate hasta) { return service.ventasEntre(desde,hasta); }
    public double totalVentas(LocalDate desde, LocalDate hasta) { return service.totalVentas(desde,hasta); }
    public int cantidadTransacciones(LocalDate desde, LocalDate hasta) { return service.cantidadTransacciones(desde,hasta); }
    public int unidadesVendidas(LocalDate desde, LocalDate hasta) { return service.unidadesVendidas(desde,hasta); }
    public double ticketPromedio(LocalDate desde, LocalDate hasta) { return service.ticketPromedio(desde,hasta); }
    public Optional<String> productoMasVendido(LocalDate desde, LocalDate hasta) { return service.productoMasVendido(desde,hasta); }
    public Map<String,Double> ventasPorCategoria(LocalDate desde, LocalDate hasta) { return service.ventasPorCategoria(desde,hasta); }
    public Map<String,Double> ventasPorMetodoPago(LocalDate desde, LocalDate hasta) { return service.ventasPorMetodoPago(desde,hasta); }
    public List<Product> productosConStockBajo() { return service.productosConStockBajo(); }
}
