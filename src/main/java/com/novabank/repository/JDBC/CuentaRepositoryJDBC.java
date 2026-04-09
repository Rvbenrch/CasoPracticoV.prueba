package com.novabank.repository.JDBC;

import com.novabank.config.DatabaseConnection;
import com.novabank.model.Cliente;
import com.novabank.model.Cuenta;
import com.novabank.repository.interfaz.CuentaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuentaRepositoryJDBC implements CuentaRepository {

    private final Connection connection;
    private final ClienteRepositoryJDBC clienteRepository;

    public CuentaRepositoryJDBC() {
        this.connection = DatabaseConnection.getConnection();
        this.clienteRepository = new ClienteRepositoryJDBC();
    }

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        String sql = "INSERT INTO cuentas (numero_cuenta, cliente_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cuenta.getNumeroCuenta());
            stmt.setLong(2, cuenta.getTitular().getId());

            stmt.executeUpdate();
            return cuenta;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar cuenta", e);
        }
    }

    @Override
    public Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta) {
        String sql = "SELECT * FROM cuentas WHERE numero_cuenta = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroCuenta);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToCuenta(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuenta por número de cuenta", e);
        }
    }

    @Override
    public List<Cuenta> listarCuentas() {
        String sql = "SELECT * FROM cuentas";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            List<Cuenta> cuentas = new ArrayList<>();
            while (rs.next()) {
                cuentas.add(mapRowToCuenta(rs));
            }

            return cuentas;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar cuentas", e);
        }
    }

    @Override
    public List<Cuenta> buscarPorClienteId(Long clienteId) {
        String sql = "SELECT * FROM cuentas WHERE cliente_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, clienteId);

            ResultSet rs = stmt.executeQuery();
            List<Cuenta> cuentas = new ArrayList<>();

            while (rs.next()) {
                cuentas.add(mapRowToCuenta(rs));
            }

            return cuentas;

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuentas por cliente", e);
        }
    }

    private Cuenta mapRowToCuenta(ResultSet rs) throws SQLException {
        String numeroCuenta = rs.getString("numero_cuenta");
        Long clienteId = rs.getLong("cliente_id");

        Cliente cliente = clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado para la cuenta"));

        return new Cuenta(cliente, numeroCuenta);
    }
}