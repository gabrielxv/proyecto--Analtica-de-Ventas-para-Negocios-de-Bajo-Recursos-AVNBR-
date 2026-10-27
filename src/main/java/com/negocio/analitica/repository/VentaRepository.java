package com.negocio.analitica.repository;

import com.negocio.analitica.model.Venta;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VentaRepository {
    Venta save(Venta venta);
    Optional<Venta> findById(Long id);
    List<Venta> findAll();
    List<Venta> findByPeriod(LocalDate desde, LocalDate hasta);
}
