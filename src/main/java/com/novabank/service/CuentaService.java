package com.novabank.service;

import com.novabank.exception.ClienteNoEncontradoException;
import com.novabank.exception.CuentaNoEncontrada;
import com.novabank.model.Cliente;
import com.novabank.model.Cuenta;
import com.novabank.repository.CuentaRepositoryMemory;

import java.util.List;

public class CuentaService {
    private CuentaRepositoryMemory cuentaRepository;
    private ClienteService clienteService;
    private static long contadorCuentas = 1;


    public CuentaService(CuentaRepositoryMemory cuentaRepository, ClienteService clienteService) {
        this.cuentaRepository = cuentaRepository;
        this.clienteService = clienteService;
    }

    public Cuenta crearCuenta(Long clienteId) {

        Cliente cliente = clienteService.encontrarPorId(clienteId);

        if (cliente == null) {
            throw new ClienteNoEncontradoException("El cliente no se ha encontrado.");
        }

        String numeroSecuencial = String.format("%012d", contadorCuentas++);
        String numeroCuenta = "ES91210000" + numeroSecuencial;
        Cuenta cuenta = new Cuenta(cliente, numeroCuenta);
        cuentaRepository.guardar(cuenta);
        return cuenta;
    }
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNoEncontrada("La cuenta que buscas no se ha encontrado."));
    }
    public List<Cuenta> listarCuentasPorCliente(Long clienteId) {

        Cliente cliente = clienteService.encontrarPorId(clienteId);

        if (cliente == null) {
            throw new ClienteNoEncontradoException("Cliente no encontrado.");
        }

        return cuentaRepository.buscarPorClienteId(clienteId);
    }

    public Cuenta ingresar(String numeroCuenta, double cantidad) {
        Cuenta cuenta = buscarPorNumeroCuenta(numeroCuenta);
        cuenta.ingresar(cantidad);
        return cuenta;
    }

    public Cuenta retirar(String numeroCuenta, double cantidad) {
        Cuenta cuenta = buscarPorNumeroCuenta(numeroCuenta);
        cuenta.retirar(cantidad);
        return cuenta;
    }

    public void transferir(String origen, String destino, double cantidad) {
        Cuenta cuentaOrigen = buscarPorNumeroCuenta(origen);
        Cuenta cuentaDestino = buscarPorNumeroCuenta(destino);

        cuentaOrigen.transferirA(cuentaDestino, cantidad);
    }


}
