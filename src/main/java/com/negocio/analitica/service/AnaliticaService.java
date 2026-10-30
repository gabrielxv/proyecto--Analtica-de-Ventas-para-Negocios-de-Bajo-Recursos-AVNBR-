package com.negocio.analitica.service;

import com.negocio.analitica.model.DetalleVenta;
import com.negocio.analitica.model.Product;
import com.negocio.analitica.model.Venta;
import com.negocio.analitica.repository.VentaRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnaliticaService {
    private final VentaRepository ventaRepository;
    private final ProductOperations productOperations;
    public AnaliticaService(VentaRepository ventaRepository, ProductOperations productOperations) {
        if(ventaRepository==null || productOperations==null) throw new IllegalArgumentException("Dependencias obligatorias.");
        this.ventaRepository=ventaRepository; this.productOperations=productOperations;
    }
    public List<Venta> ventasEntre(LocalDate desde, LocalDate hasta) {
        return ventaRepository.findByPeriod(desde,hasta);
    }
    public double totalVentas(LocalDate desde, LocalDate hasta) {
        return ventasEntre(desde,hasta).stream().mapToDouble(Venta::getTotal).sum();
    }
    public int cantidadTransacciones(LocalDate desde, LocalDate hasta) {
        return ventasEntre(desde,hasta).size();
    }
    public int unidadesVendidas(LocalDate desde, LocalDate hasta) {
        return ventasEntre(desde,hasta).stream().mapToInt(Venta::getUnidadesVendidas).sum();
    }
    public double ticketPromedio(LocalDate desde, LocalDate hasta) {
        List<Venta> ventas=ventasEntre(desde,hasta);
        return ventas.isEmpty()?0.0:ventas.stream().mapToDouble(Venta::getTotal).sum()/ventas.size();
    }
    public Optional<String> productoMasVendido(LocalDate desde, LocalDate hasta) {
        Map<String,Integer> unidades=new LinkedHashMap<>();
        for(Venta venta:ventasEntre(desde,hasta))
            for(DetalleVenta d:venta.getDetalles()) unidades.merge(d.getProductoNombre(),d.getCantidad(),Integer::sum);
        return unidades.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
    }
    public Map<String,Double> ventasPorCategoria(LocalDate desde, LocalDate hasta) {
        Map<String,Double> result=new LinkedHashMap<>();
        for(Venta v:ventasEntre(desde,hasta)) for(DetalleVenta d:v.getDetalles())
            result.merge(d.getCategoriaNombre(),d.getSubtotal(),Double::sum);
        return Map.copyOf(result);
    }
    public Map<String,Double> ventasPorMetodoPago(LocalDate desde, LocalDate hasta) {
        Map<String,Double> result=new LinkedHashMap<>();
        for(Venta v:ventasEntre(desde,hasta)) result.merge(v.getMetodoPago(),v.getTotal(),Double::sum);
        return Map.copyOf(result);
    }
    public List<Product> productosConStockBajo() {
        return productOperations.findAllProducts().stream()
                .filter(p->p.getStock()<=p.getMinimumStock())
                .sorted(Comparator.comparing(Product::getName)).toList();
    }
    public double variacionPorcentual(double totalAnterior, double totalActual) {
        if(totalAnterior<0 || totalActual<0) throw new IllegalArgumentException("Los totales no pueden ser negativos.");
        if(totalAnterior==0) return totalActual==0?0.0:100.0;
        return ((totalActual-totalAnterior)/totalAnterior)*100.0;
    }
}
