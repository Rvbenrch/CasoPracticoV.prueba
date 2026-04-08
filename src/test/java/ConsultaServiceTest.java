

import com.novabank.model.Cliente;
import com.novabank.model.Cuenta;
import com.novabank.repository.ClienteRepositoryMemory;
import com.novabank.repository.CuentaRepositoryMemory;
import com.novabank.service.ClienteService;
import com.novabank.service.ConsultaService;
import com.novabank.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsultaServiceTest {

    private ClienteService clienteService;
    private CuentaService cuentaService;
    private ConsultaService consultaService;

    @BeforeEach
    void setup() {
        clienteService = new ClienteService(new ClienteRepositoryMemory());
        cuentaService = new CuentaService(new CuentaRepositoryMemory(), clienteService);
        consultaService = new ConsultaService(cuentaService);
    }

    @Test
    void consultarSaldo_debeRetornarSaldo() {
        Cliente c = clienteService.crearCliente("Ruben","Rodriguez","123","ruben@email.com","600000001");
        Cuenta cuenta = cuentaService.crearCuenta(c.getId());
        cuentaService.ingresar(cuenta.getNumeroCuenta(), 1000);

        double saldo = consultaService.consultarSaldo(cuenta.getNumeroCuenta());
        assertEquals(1000, saldo);
    }

    @Test
    void historialMovimientos_debeRetornarMovimientosOrdenados() {
        Cliente c = clienteService.crearCliente("Ruben","Rodriguez","123","ruben@email.com","600000001");
        Cuenta cuenta = cuentaService.crearCuenta(c.getId());
        cuentaService.ingresar(cuenta.getNumeroCuenta(), 500);
        cuentaService.retirar(cuenta.getNumeroCuenta(), 200);

        List<?> movimientos = consultaService.obtenerHistorial(cuenta.getNumeroCuenta());
        assertEquals(2, movimientos.size());
    }

    @Test
    void movimientosPorRangoFechas_debeFiltrarCorrectamente() {
        Cliente c = clienteService.crearCliente("Ruben","Rodriguez","123","ruben@email.com","600000001");
        Cuenta cuenta = cuentaService.crearCuenta(c.getId());
        cuentaService.ingresar(cuenta.getNumeroCuenta(), 500);

        LocalDate inicio = LocalDate.now().minusDays(1);
        LocalDate fin = LocalDate.now().plusDays(1);

        List<?> movs = consultaService.obtenerMovimientosPorRango(cuenta.getNumeroCuenta(), inicio, fin);
        assertEquals(1, movs.size());
    }

}