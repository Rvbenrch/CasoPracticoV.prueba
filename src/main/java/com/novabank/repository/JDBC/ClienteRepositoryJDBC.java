package com.novabank.repository.JDBC;

import com.novabank.config.DatabaseConnection;
import com.novabank.model.Cliente;
import com.novabank.repository.interfaz.ClienteRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteRepositoryJDBC implements ClienteRepository {

    private final Connection connection;

    public ClienteRepositoryJDBC() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public  Cliente  guardar(Cliente  cliente)  {
        String sql  =  "INSERT  INTO  clientes (nombre,  apellidos,  dni,  email,  telefono) "  +
                "VALUES (?,  ?,  ?,  ?,  ?) RETURNING  id";

        try  (PreparedStatement  stmt =  connection.prepareStatement(sql))  {
            stmt.setString(1,  cliente.getNombre());
            stmt.setString(2,  cliente.getApellidos());
            stmt.setString(3,  cliente.getDni());
            stmt.setString(4,  cliente.getEmail());
            stmt.setString(5,  cliente.getTelefono());

            ResultSet  rs  = stmt.executeQuery();
            if (rs.next())  {
                cliente.setId(rs.getLong("id")); //  ✅  ahora  existe  setId
            }

            return  cliente;

        }  catch  (SQLException  e)  {
            throw  new RuntimeException("Error  al  guardar  cliente",  e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToCliente(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por ID", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorDni(String dni) {
        String sql = "SELECT * FROM clientes WHERE dni = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToCliente(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por DNI", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        String sql = "SELECT * FROM clientes WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToCliente(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por email", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorTelefono(String telefono) {
        String sql = "SELECT * FROM clientes WHERE telefono = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, telefono);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToCliente(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por teléfono", e);
        }
    }

    @Override
    public List<Cliente> buscarTodos() {
        String sql = "SELECT * FROM clientes";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            List<Cliente> clientes = new ArrayList<>();
            while (rs.next()) {
                clientes.add(mapRowToCliente(rs));
            }

            return clientes;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar clientes", e);
        }
    }

    private  Cliente mapRowToCliente(ResultSet  rs)  throws  SQLException  {
        return new  Cliente(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("dni"),
                rs.getString("email"),
                rs.getString("telefono")
        );
    }
}