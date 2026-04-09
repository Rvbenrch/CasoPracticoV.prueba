package com.novabank.repository.interfaz;

import com.novabank.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    Cliente guardar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    Optional<Cliente> buscarPorDni(String dni);

    Optional<Cliente> buscarPorEmail(String email);

    Optional<Cliente> buscarPorTelefono(String telefono);

    List<Cliente> buscarTodos();
}