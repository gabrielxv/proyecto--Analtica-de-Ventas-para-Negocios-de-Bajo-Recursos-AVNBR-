package com.negocio.analitica.controller;

import com.negocio.analitica.model.Cliente;
import com.negocio.analitica.service.ClienteService;
import java.util.List;

public class ClienteController {
    private final ClienteService service;
    public ClienteController(ClienteService service) {
        if (service == null) throw new IllegalArgumentException("ClienteService cannot be null.");
        this.service = service;
    }
    public Cliente crear(Cliente cliente) { return service.crear(cliente); }
    public Cliente actualizar(Cliente cliente) { return service.actualizar(cliente); }
    public Cliente obtener(Long id) { return service.buscarPorId(id); }
    public List<Cliente> listar() { return service.listar(); }
    public void eliminar(Long id) { service.eliminar(id); }
}
