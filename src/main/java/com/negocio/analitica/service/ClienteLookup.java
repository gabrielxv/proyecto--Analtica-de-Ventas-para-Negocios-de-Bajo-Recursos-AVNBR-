package com.negocio.analitica.service;

import com.negocio.analitica.model.Cliente;

/**
 * Puerto de consulta de clientes requerido por los casos de uso que necesitan
 * validar la existencia de un comprador, sin depender de ClienteService.
 */
@FunctionalInterface
public interface ClienteLookup {
    Cliente buscarPorId(Long id);
}
