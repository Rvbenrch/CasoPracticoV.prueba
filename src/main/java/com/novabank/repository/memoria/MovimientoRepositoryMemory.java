package com.novabank.repository.memoria;

import com.novabank.model.Movimiento;
import com.novabank.repository.interfaz.MovimientoRepository;

import java.time.LocalDateTime;
import java.util.*;

public class MovimientoRepositoryMemory implements MovimientoRepository {

    private Map<Long, List<Movimiento>> movimientosPorCuenta = new HashMap<>();

    @Override
    public Movimiento guardar(Long cuentaId, Movimiento movimiento) {
        movimientosPorCuenta
                .computeIfAbsent(cuentaId, id -> new ArrayList<>())
                .add(movimiento);

        return movimiento;
    }

    @Override
    public List<Movimiento> buscarPorCuentaId(Long cuentaId) {
        return movimientosPorCuenta.getOrDefault(cuentaId, Collections.emptyList());
    }

    @Override
    public List<Movimiento> buscarPorCuentaIdYRangoFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin) {
        return movimientosPorCuenta
                .getOrDefault(cuentaId, Collections.emptyList())
                .stream()
                .filter(m -> !m.getFecha().isBefore(inicio) && !m.getFecha().isAfter(fin))
                .toList();
    }

    @Override
    public List<Movimiento> buscarTodos() {
        return movimientosPorCuenta.values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}