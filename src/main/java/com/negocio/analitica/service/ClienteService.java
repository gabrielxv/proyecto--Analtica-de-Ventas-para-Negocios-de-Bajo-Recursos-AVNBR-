package com.negocio.analitica.service;

import com.negocio.analitica.model.Cliente;
import com.negocio.analitica.repository.ClienteRepository;
import java.util.List;

public class ClienteService implements ClienteLookup {
    private final ClienteRepository repository;
    public ClienteService(ClienteRepository repository) {
        if (repository == null) throw new IllegalArgumentException("ClienteRepository cannot be null.");
        this.repository = repository;
    }
    public Cliente crear(Cliente cliente) {
        validar(cliente);
        if (repository.findByDocumento(cliente.getDocumento().trim()).isPresent())
            throw new IllegalArgumentException("Ya existe un cliente con ese documento.");
        cliente.setDocumento(cliente.getDocumento().trim());
        return repository.save(cliente);
    }
    public Cliente actualizar(Cliente cliente) {
        validar(cliente);
        if (cliente.getId() == null || repository.findById(cliente.getId()).isEmpty())
            throw new IllegalArgumentException("Cliente no encontrado.");
        repository.findByDocumento(cliente.getDocumento().trim())
                .filter(c -> !c.getId().equals(cliente.getId()))
                .ifPresent(c -> { throw new IllegalArgumentException("El documento ya pertenece a otro cliente."); });
        cliente.setDocumento(cliente.getDocumento().trim());
        return repository.save(cliente);
    }
    public Cliente buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado."));
    }
    public List<Cliente> listar() { return repository.findAll(); }
    public void eliminar(Long id) {
        if (repository.findById(id).isEmpty()) throw new IllegalArgumentException("Cliente no encontrado.");
        repository.deleteById(id);
    }
    private void validar(Cliente c) {
        if (c == null) throw new IllegalArgumentException("Cliente obligatorio.");
        if (c.getNombre() == null || c.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        if (c.getDocumento() == null || c.getDocumento().isBlank())
            throw new IllegalArgumentException("El documento del cliente es obligatorio.");
    }
}
