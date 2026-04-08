
import com.novabank.exception.ClienteDuplicadoException;
import com.novabank.model.Cliente;
import com.novabank.repository.ClienteRepositoryMemory;
import com.novabank.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    private ClienteService clienteService;

    @BeforeEach
    void setup() {
        clienteService = new ClienteService(new ClienteRepositoryMemory());
    }

    @Test
    void crearCliente_valido_debeCrear() {
        Cliente c = clienteService.crearCliente("Ruben", "Rodriguez", "12345678A", "ruben@email.com", "600000001");
        assertNotNull(c);
        assertEquals("Ruben", c.getNombre());
    }

    @Test
    void crearCliente_dniDuplicado_debeLanzarExcepcion() {
        clienteService.crearCliente("Ruben", "Rodriguez", "12345678A", "ruben@email.com", "600000001");
        assertThrows(ClienteDuplicadoException.class, () ->
                clienteService.crearCliente("Luis", "Perez", "12345678A", "luis@email.com", "600000002"));
    }

    @Test
    void crearCliente_emailInvalido_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                clienteService.crearCliente("Ana", "Lopez", "98765432B", "analopez", "600000003"));
    }

    @Test
    void emailRepetido(){
        Cliente c = clienteService.crearCliente("Ruben", "Rodriguez", "12345678A", "ruben@email.com", "600000001");
        assertThrows(ClienteDuplicadoException.class, () ->
                clienteService.crearCliente("Anilla","Garcia","32948293","ruben@email.com","76374673"));
    }


    @Test
    void telefonoDuplicado(){
        Cliente c = clienteService.crearCliente("Ruben", "Rodriguez", "12345678A", "ruben@email.com", "600000001");
        assertThrows(ClienteDuplicadoException.class, () ->
                clienteService.crearCliente("Anilla","Garcia","32948293","anacapaca@.com","600000001"));
    }


}