package com.novabank.repository.memoria;

import com.novabank.model.Cuenta;
import com.novabank.repository.interfaz.CuentaRepository;

import java.util.*;

public class CuentaRepositoryMemory implements CuentaRepository {

    private Map<String, Cuenta> cuentas = new HashMap<>();

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        cuentas.put(cuenta.getNumeroCuenta(), cuenta);
        return cuenta;
    }

    @Override
    public Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta) {
        return Optional.ofNullable(cuentas.get(numeroCuenta));
    }

    @Override
    public List<Cuenta> listarCuentas() {
        return new ArrayList<>(cuentas.values());
    }

    @Override
    public List<Cuenta> buscarPorClienteId(Long clienteId) {
        List<Cuenta> resultado = new ArrayList<>();

        for (Cuenta cuenta : cuentas.values()) {
            if (cuenta.getTitular().getId().equals(clienteId)) {
                resultado.add(cuenta);
            }
        }

        return resultado;
    }

    @Override
    public void actualizarSaldo(Cuenta cuenta) {

    }
}