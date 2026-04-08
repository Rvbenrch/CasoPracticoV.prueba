package com.novabank.model;

import java.time.LocalDateTime;

public class Movimiento {

    private Long id;                // Necesario para JDBC
    private Long cuentaId;          // Relación con la cuenta en BD
    private TipoMovimiento tipo;
    private double importe;
    private LocalDateTime fecha;

    // Constructor para movimientos NUEVOS (lógica de negocio)
    public Movimiento(TipoMovimiento tipo, double importe) {
        this.tipo = tipo;
        this.importe = importe;
        this.fecha = LocalDateTime.now();
    }

    // Constructor para movimientos que vienen de la BD
    public Movimiento(Long id, Long cuentaId, TipoMovimiento tipo, double importe, LocalDateTime fecha) {
        this.id = id;
        this.cuentaId = cuentaId;
        this.tipo = tipo;
        this.importe = importe;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public double getImporte() {
        return importe;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
