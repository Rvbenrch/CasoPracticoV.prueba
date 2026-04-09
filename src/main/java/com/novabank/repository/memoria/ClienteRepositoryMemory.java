package com.novabank.repository.memoria;

import com.novabank.model.Cliente;
import com.novabank.repository.interfaz.ClienteRepository;

import java.util.*;

public class ClienteRepositoryMemory implements ClienteRepository {

    private Map<String, Cliente> clientesPorDni = new HashMap<>();
    private Map<String, Cliente> clientesPorEmail = new HashMap<>();
    private Map<String, Cliente> clientesPorTelefono = new HashMap<>();
    private Map<Long, Cliente> clientesPorId = new HashMap<>();

    @Override
    public Cliente guardar(Cliente cliente) {
        clientesPorDni.put(cliente.getDni(), cliente);
        clientesPorEmail.put(cliente.getEmail(), cliente);
        clientesPorTelefono.put(cliente.getTelefono(), cliente);
        clientesPorId.put(cliente.getId(), cliente);
        return cliente;
    }

    @Override
    public Optional<Cliente> buscarPorDni(String dni) {
        return Optional.ofNullable(clientesPorDni.get(dni));
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return Optional.ofNullable(clientesPorEmail.get(email));
    }

    @Override
    public Optional<Cliente> buscarPorTelefono(String telefono) {
        return Optional.ofNullable(clientesPorTelefono.get(telefono));
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return Optional.ofNullable(clientesPorId.get(id));
    }

    @Override
    public List<Cliente> buscarTodos() {
        return new ArrayList<>(clientesPorDni.values());
    }
}