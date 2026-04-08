

import com.novabank.exception.CuentaNoEncontrada;
import com.novabank.model.Cliente;
import com.novabank.model.Cuenta;
import com.novabank.repository.ClienteRepositoryMemory;
import com.novabank.repository.CuentaRepositoryMemory;
import com.novabank.service.ClienteService;
import com.novabank.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CuentaServiceTest {

    private ClienteService clienteService;
    private CuentaService cuentaService;

    @BeforeEach
    void setup() {
        ClienteRepositoryMemory cr = new ClienteRepositoryMemory();
        CuentaRepositoryMemory cur = new CuentaRepositoryMemory();
        clienteService = new ClienteService(cr);
        cuentaService = new CuentaService(cur, clienteService);
    }

    @Test
    void crearCuenta_clienteExistente_debeCrearCuenta() {
        Cliente c = clienteService.crearCliente("Ruben","Rodriguez","123","ruben@email.com","600000001");
        Cuenta cuenta = cuentaService.crearCuenta(c.getId());
        assertNotNull(cuenta);
        assertEquals(c.getId(), cuenta.getTitular().getId());
    }

    @Test
    void ingresar_valido_debeActualizarSaldo() {
        Cliente c = clienteService.crearCliente("Ruben","Rodriguez","123","ruben@email.com","600000001");
        Cuenta cuenta = cuentaService.crearCuenta(c.getId());
        cuentaService.ingresar(cuenta.getNumeroCuenta(), 500);
        assertEquals(500, cuenta.getSaldo());
    }

    @Test
    void retirar_saldoSuficiente_debeActualizarSaldo() {
        Cliente c = clienteService.crearCliente("Ruben","Rodriguez","123","ruben@email.com","600000001");
        Cuenta cuenta = cuentaService.crearCuenta(c.getId());
        cuentaService.ingresar(cuenta.getNumeroCuenta(), 500);
        cuentaService.retirar(cuenta.getNumeroCuenta(), 300);
        assertEquals(200, cuenta.getSaldo());
    }

    @Test
    void retirar_saldoInsuficiente_debeLanzarExcepcion() {
        Cliente c = clienteService.crearCliente("Ruben","Rodriguez","123","ruben@email.com","600000001");
        Cuenta cuenta = cuentaService.crearCuenta(c.getId());
        assertThrows(IllegalArgumentException.class, () -> cuentaService.retirar(cuenta.getNumeroCuenta(), 300));
    }

    @Test
    void transferir_valido_debeActualizarSaldos() {
        Cliente c1 = clienteService.crearCliente("Ruben","Rodriguez","123","ruben@email.com","600000001");
        Cliente c2 = clienteService.crearCliente("Ana","Lopez","124","ana@email.com","600000002");
        Cuenta cuenta1 = cuentaService.crearCuenta(c1.getId());
        Cuenta cuenta2 = cuentaService.crearCuenta(c2.getId());
        cuentaService.ingresar(cuenta1.getNumeroCuenta(), 500);

        cuentaService.transferir(cuenta1.getNumeroCuenta(), cuenta2.getNumeroCuenta(), 200);
        assertEquals(300, cuenta1.getSaldo());
        assertEquals(200, cuenta2.getSaldo());
    }

    @Test
    void transferir_aMismaCuenta_debeLanzarExcepcion() {
        Cliente c1 = clienteService.crearCliente("Ruben","Rodriguez","123","ruben@email.com","600000001");
        Cuenta cuenta = cuentaService.crearCuenta(c1.getId());
        cuentaService.ingresar(cuenta.getNumeroCuenta(), 500);
        assertThrows(IllegalArgumentException.class, () -> cuentaService.transferir(cuenta.getNumeroCuenta(), cuenta.getNumeroCuenta(), 200));
    }

    @Test
    void buscarCuenta_inexistente_debeLanzarExcepcion() {
        assertThrows(CuentaNoEncontrada.class, () -> cuentaService.buscarPorNumeroCuenta("0000"));
    }

}