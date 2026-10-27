package com.negocio.analitica.repository;

import com.negocio.analitica.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente save(Cliente cliente);
    Optional<Cliente> findById(Long id);
    Optional<Cliente> findByDocumento(String documento);
    List<Cliente> findAll();
    void deleteById(Long id);
}
