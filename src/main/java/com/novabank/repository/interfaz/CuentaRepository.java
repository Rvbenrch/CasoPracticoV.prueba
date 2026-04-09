package com.novabank.repository.interfaz;

import com.novabank.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository {

    Cuenta guardar(Cuenta cuenta);

    Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta);

    List<Cuenta> listarCuentas();

    List<Cuenta> buscarPorClienteId(Long clienteId);
}