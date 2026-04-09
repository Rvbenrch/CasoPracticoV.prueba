package com.novabank.service;

import com.novabank.exception.ClienteDuplicadoException;
import com.novabank.exception.ClienteNoEncontradoException;
import com.novabank.model.Cliente;
import com.novabank.repository.interfaz.ClienteRepository;

import java.util.List;

public class ClienteService {

    private ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente crearCliente(String nombre, String apellidos, String dni, String email, String telefono){

        if (repository.buscarPorDni(dni).isPresent()) {
            throw new ClienteDuplicadoException("Ya existe un cliente con el DNI: " + dni);
        }

        if (repository.buscarPorEmail(email).isPresent()) {
            throw new ClienteDuplicadoException("Ya existe un cliente con el email: " + email);
        }

        if (repository.buscarPorTelefono(telefono).isPresent()) {
            throw new ClienteDuplicadoException("Ya existe un cliente con el teléfono: " + telefono);
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("El email no tiene un formato válido. \n El formato del email tiene que ser: ejemplo@dominio.com");
        }

        Cliente cliente = new Cliente(nombre, apellidos, dni, email, telefono);
        return repository.guardar(cliente);
    }

    public Cliente encontrarPorDni(String dni){
        return repository.buscarPorDni(dni)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente con DNI " + dni + " no encontrado."));
    }

    public Cliente encontrarPorId(long id){
        return repository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente con ID " + id + " no encontrado."));
    }

    public Cliente encontrarPorEmail(String email){
        return repository.buscarPorEmail(email)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente con email " + email + " no encontrado."));
    }

    public List<Cliente> listarClientes() {
        return repository.buscarTodos();
    }
}
