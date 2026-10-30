package com.negocio.analitica.service;

import com.negocio.analitica.model.Cliente;
import com.negocio.analitica.model.DetalleVenta;
import com.negocio.analitica.model.Product;
import com.negocio.analitica.model.Venta;
import com.negocio.analitica.repository.VentaRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VentaService {
    private final ClienteLookup clienteLookup;
    private final ProductOperations productOperations;
    private final VentaRepository ventaRepository;

    public VentaService(ClienteLookup clienteLookup, ProductOperations productOperations, VentaRepository ventaRepository) {
        if(clienteLookup==null || productOperations==null || ventaRepository==null)
            throw new IllegalArgumentException("Los servicios y el repositorio son obligatorios.");
        this.clienteLookup=clienteLookup; this.productOperations=productOperations; this.ventaRepository=ventaRepository;
    }

    public Venta registrar(Venta solicitud) {
        validarSolicitud(solicitud);
        Cliente cliente=clienteLookup.buscarPorId(solicitud.getClienteId());
        Map<Long,Integer> cantidades=new LinkedHashMap<>();
        for(DetalleVenta d:solicitud.getDetalles()) {
            if(d==null || d.getProductoId()==null || d.getCantidad()<=0)
                throw new IllegalArgumentException("Cada detalle debe indicar producto y cantidad positiva.");
            cantidades.merge(d.getProductoId(),d.getCantidad(),Math::addExact);
        }

        List<DetalleVenta> detalles=new ArrayList<>();
        Map<Long,Product> productosOriginales=new LinkedHashMap<>();
        for(Map.Entry<Long,Integer> entry:cantidades.entrySet()) {
            Product product=productOperations.findProductById(entry.getKey());
            if(!productOperations.hasAvailability(product.getId(),entry.getValue()))
                throw new IllegalArgumentException("Stock insuficiente para el producto: "+product.getName());
            productosOriginales.put(product.getId(),new Product(product.getId(),product.getName(),product.getPrice(),
                    product.getStock(),product.getMinimumStock(),product.getCategory()));
            detalles.add(new DetalleVenta(null,product.getId(),product.getName(),
                    product.getCategory()==null?"Sin categoría":product.getCategory().getName(),
                    entry.getValue(),product.getPrice()));
        }

        Venta venta=new Venta(null,LocalDateTime.now(),cliente.getId(),cliente.getNombre(),
                normalizarMetodoPago(solicitud.getMetodoPago()),detalles);
        List<Product> actualizados=new ArrayList<>();
        try {
            for(Map.Entry<Long,Integer> entry:cantidades.entrySet()) {
                Product product=productosOriginales.get(entry.getKey());
                product.setStock(product.getStock()-entry.getValue());
                productOperations.updateProduct(product);
                actualizados.add(product);
            }
            return ventaRepository.save(venta);
        } catch(RuntimeException error) {
            // Compensación: si falla el registro, se intenta restaurar el stock previamente descontado.
            for(Product updated:actualizados) {
                try {
                    Product current=productOperations.findProductById(updated.getId());
                    current.setStock(current.getStock()+cantidades.get(updated.getId()));
                    productOperations.updateProduct(current);
                } catch(RuntimeException rollbackError) {
                    error.addSuppressed(rollbackError);
                }
            }
            throw error;
        }
    }
    public Venta buscarPorId(Long id) {
        return ventaRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Venta no encontrada."));
    }
    public List<Venta> listar() { return ventaRepository.findAll(); }

    private void validarSolicitud(Venta venta) {
        if(venta==null) throw new IllegalArgumentException("La venta es obligatoria.");
        if(venta.getClienteId()==null) throw new IllegalArgumentException("Debe seleccionar un cliente.");
        if(venta.getDetalles()==null || venta.getDetalles().isEmpty())
            throw new IllegalArgumentException("La venta debe contener al menos un producto.");
        if(venta.getMetodoPago()==null || venta.getMetodoPago().isBlank())
            throw new IllegalArgumentException("El método de pago es obligatorio.");
    }
    private String normalizarMetodoPago(String metodo) {
        String value=metodo.trim();
        if(value.length()>40) throw new IllegalArgumentException("El método de pago no puede superar 40 caracteres.");
        return value;
    }
}
