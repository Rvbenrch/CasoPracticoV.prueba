package com.novabank.service;

import com.novabank.exception.ClienteNoEncontradoException;
import com.novabank.exception.CuentaNoEncontrada;
import com.novabank.model.Cliente;
import com.novabank.model.Cuenta;
import com.novabank.model.Movimiento;
import com.novabank.model.TipoMovimiento;
import com.novabank.repository.interfaz.CuentaRepository;
import com.novabank.repository.interfaz.MovimientoRepository;

import java.util.List;
import java.util.UUID;

public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteService clienteService;
    private final MovimientoRepository movimientoRepository;

    public CuentaService(CuentaRepository cuentaRepository,
                         ClienteService clienteService,
                         MovimientoRepository movimientoRepository) {

        this.cuentaRepository = cuentaRepository;
        this.clienteService = clienteService;
        this.movimientoRepository = movimientoRepository;
    }

    public Cuenta crearCuenta(Long clienteId) {

        Cliente cliente = clienteService.encontrarPorId(clienteId);

        if (cliente == null) {
            throw new ClienteNoEncontradoException("El cliente no se ha encontrado.");
        }

        // Generación segura del número de cuenta
        String numeroCuenta = generarNumeroCuenta();

        Cuenta cuenta = new Cuenta(cliente, numeroCuenta);
        cuentaRepository.guardar(cuenta);

        return cuenta;
    }

    private String generarNumeroCuenta() {
        // Genera un número de cuenta único y seguro
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return "ES91" + uuid.substring(0, 20);
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

        // Registrar movimiento en BD
        Movimiento movimiento = new Movimiento(TipoMovimiento.DEPOSITO, cantidad);
        movimientoRepository.guardar(cuenta.getId(), movimiento);

        // Persistir el nuevo saldo
        cuentaRepository.actualizarSaldo(cuenta);

        return cuenta;
    }

    public Cuenta retirar(String numeroCuenta, double cantidad) {
        Cuenta cuenta = buscarPorNumeroCuenta(numeroCuenta);

        cuenta.retirar(cantidad);

        Movimiento movimiento = new Movimiento(TipoMovimiento.RETIRO, cantidad);
        movimientoRepository.guardar(cuenta.getId(), movimiento);

        // Persistir el nuevo saldo
        cuentaRepository.actualizarSaldo(cuenta);

        return cuenta;
    }

    public void transferir(String origen, String destino, double cantidad) {
        Cuenta cuentaOrigen = buscarPorNumeroCuenta(origen);
        Cuenta cuentaDestino = buscarPorNumeroCuenta(destino);

        cuentaOrigen.transferirA(cuentaDestino, cantidad);

        // Registrar movimientos
        movimientoRepository.guardar(cuentaOrigen.getId(),
                new Movimiento(TipoMovimiento.TRANSFERENCIA_SALIENTE, cantidad));

        movimientoRepository.guardar(cuentaDestino.getId(),
                new Movimiento(TipoMovimiento.TRANSFERENCIA_ENTRANTE, cantidad));

        // Persistir ambos saldos
        cuentaRepository.actualizarSaldo(cuentaOrigen);
        cuentaRepository.actualizarSaldo(cuentaDestino);
    }
}