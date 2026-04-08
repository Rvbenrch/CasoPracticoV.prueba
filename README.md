# NovaBank
Sistema bancario en consola desarrollado en Java



---

<details><summary>CASO PRACTICO 1</summary>

## Descripción

![bankpicture.jpg](pictures/bankpicture.jpg)

Aplicación que simula un sistema bancario básico:

    - Gestión de clientes
    - Gestión de cuentas
    - Operaciones financieras
    - Consultas de movimientos

    ✔ Arquitectura por capas
    ✔ Validaciones y control de errores
    ✔ Tests con JUnit

---

## Funcionalidades

CLIENTES
- Alta
- Búsqueda
- Listado

CUENTAS
- Creación
- Consulta

OPERACIONES
- Ingreso
- Retirada
- Transferencia

CONSULTAS
- Saldo
- Movimientos (con filtros)

---

## Arquitectura

MODEL
Entidades del dominio

REPOSITORY
Almacenamiento en memoria (Map)

SERVICE
Lógica de negocio

MENUS
Interacción por consola

---

## Modelo de datos

CLIENTE   1 ─── N   CUENTA   1 ─── N   MOVIMIENTO

---

## Estructura

![menuInicial.png](pictures/menuInicial.png)

<details> <summary>Desplegar estructura:</summary>
<p align="center">
  <img src="pictures/estructura.png" width="400">
</p>

</details>

---

## Testing

- ClienteServiceTest
- CuentaServiceTest
- ConsultaServiceTest

Validación de lógica y control de errores

![testPasados.png](pictures/testPasados.png)

---

## Tecnologías

- Java 17
- Maven
- JUnit 5
- Mockito
- Git + GitHub

## Ejecución

Compilar:

    mvn clean compile

Ejecutar:

    mvn exec:java

Tests:

    mvn test


## Requisitos

- Java 17
- Maven 3.8 o superior

## Codespaces

Ejecución sin instalación local:

    Code → Codespaces → Create Codespace
    mvn exec:java

---

## Repositorio

https://github.com/Rvbenrch/Caso_Practico_NovaBank

---

## Estado

- Funcional
- Testeado
- Preparado para ampliaciones

---

## IMÁGENES FLUJO TRABAJO


![img.png](pictures/img.png)

![flujotrabajo1.png](pictures/flujotrabajo1.png)
---

</details>


## CASO PRACTICO 2

---
<div align="center">

### Presentación

</div>

---



Se intuye que, las actividades que se piden llevar a cabo para el caso práctico 2:
#### 1. Arquitectura por capas:
- Presentación: tener los menús, lectura de teclado e impresión de consola por separado
- Lógica de Servicios: reglas que debe cumplir el banco.
- El Modelo con las clases: **Cliente**, **Cuenta** y **Movimiento**
- La persistencia, en este caso sustituir el **HashMap** por acceso a la base de datos.

#### 2. Sustituiremos el almacenamiento en memoria (PostgreSQL).

En el caso práctico 1 ya habíamos diseñado algunas de las tablas en nuesto archivo `schema.sql`, sin embargo, esta base de datos
debe de crearse en **PostgreSQL**, también implementar los repositorios JDBC, como podrían ser los siguientes:

- ClienteRepositoryJDBC
- CuentaRepositoryJDBC
- MovimientoRepositoryJDBC

Teniendo en cuenta que los **Servicios** que ahora dependen de `HashMap`, pasarían a usar la base de datos.

#### 3. Patrones de diseño: `Repository`, `Service Layer`, `Singleton`.

Ya contamos con una lógica creada para nuestro proyecto, pero esta lógica está actualmente utilizando los repositorios en memoria de `HashMap`.
Simplemente, debemos de llevar a cabo una sustitución de los repositorios usando **JDBC**.

- `Service Layer`: Capa que contiene la lógica del negocio para hacer uso de los repositorios `ClienteService`, `CuentaService`, `ConsultaService` ya están implementados de manera correcta. Falta por implementar, 
que estos servicios se ajusten para trabajar con JDBC 
- `Singleton`: Crearemos una clase, normalmente instancia compartida, para la **conexión con la base de datos**.


```public class DatabaseConnection {
private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            // crear conexión JDBC a PostgreSQL
        }
        return connection;
    }
}
```

#### 4. Programación funcional `stream()`, `filter`,`map`, `collect`.

Remplazar los bucles tradicionales existentes en algunas clases, cuando su uso sea coherente y necesario.
El uso de la programación funcional encaja en nuestro proyecto:
- Filtrando movimientos por rango de fechas.
- Ordenar movimientos por fecha.
- Transformar listas de entidades a DTOs o texto.

#### 5. Resultados que se esperan.

 NovaBank continúe funcionando en consola pero ahora:
 - Usando `PostgreSQL` como almacenamiento.
 - Con los repositorios JDBC.
 - Arquitectura por capas limpia.
 - Singleton para la conexión.
 - Streams/lambdas en las consultas.

---
<div align="center">
  
### Desarrollo

</div>

---

#### Modificaciones de `ClienteRepository` y `CuentaRepository`.

1. `ClienteRepository`.

En primer lugar, **ClienteRepository** está usando 4 HashMap, para realizar la búsqueda por DNI, email, teléfono e ID.
Para llevar esto acabo tiene los métodos `guardar`, `buscarPorDni`, `buscarPorTelefono`, `buscarPorId` y `buscarTodos`.
```
Separar CONTRATO de IMPLEMENTACIÓN:
Lo que hemos hecho es aplicar el patrón Repository “de verdad”:
    - ClienteRepository → define el contrato (qué se puede hacer).
    - ClienteRepositoryMemory → define la implementación (cómo se hace)
```

2. `CuentaRepository`.

Por otro lado, **CuentaRepository** usa un  `HashMap<String,Cuenta>`, y usa los métodos 
`guardar`, `buscarPorNumeroCuenta`, `listarCuentas`, `buscarPorClienteId`.
Además, no hay interfaz, por lo que no puedo cambiar a JDBC sin tocar servicios.




3. Nos falta por crear `MovimientosRepository` y `MovimientosRepositoryMemory`.
- MovimientoRepository (interfaz → contrato)
- MovimientoRepositoryMemory (implementación en memoria).
- Preparado para crear MovimientoRepositoryJDBC más adelante.

---
<div align="center">

### Creación DataBase

</div>

---

Creamos un nuevo paquete llamado `config` en el que añadimos la clase `DatabaseConnection` que es la encargada de 
llevar a cabo las operaciones **CRUD**.

```
public class DatabaseConnection {

    private static Connection connection;

    private static final String URL = 
    "jdbc:postgresql://localhost:5432/novabank";
    private static final String USER = "postgres";
    private static final String PASSWORD = "tu_password";

    private DatabaseConnection() {
        
    }
public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión a PostgreSQL 
                                    establecida correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos: "
                                    + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return connection;
```
- Se guarda la única conexión que se va a usar para la aplicación:
 `private static Connection connection;`

- Constructor privado, para evitar `new DatabaseConnection();`
- Si la conexión no existe se crea, si existe la devuelve.
- Con `DriverManager.getConnection(..)` llamamos para conectarnos a PostgreSQL usando JDBC.

---

<div align="center">

#### Adaptación de clases

</div>

--- 
Se necesita realizar modificaciones en las clases `ClienteService` y en la clase `CuentaService`.
1. `CuentaService` tenía una función que buscaba por número de cuenta, si la cuenta era null, entonces saltaba una excepción.
En las modificaciones en el repositorio hemos puesto
```
Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta);
```
Por tanto, ahora ya no se devuelve cuenta, sino un `Optional<Cuenta>`. Lo 
modificamos para que devuelva un opcional y nos lance la excepción si está vacío.

2. `ClienteService` también debe ser modificado puesto que en ciertos lugares se usan expresiones como

```
if (repository.buscarPorDni(dni) != null)
```

Pero como `buscarPorDni` ahora mismo devuelve un Opcional, ese opcional núnca puede ser nullo, por lo que el compilador detecta 
que hay un problema dónde esperamos un Cliente.


3. `Cliente` se ha ampliado con un segundo constructor que permite crear instancias a partir de datos provenientes de 
la base de datos, evitando el incremento automático del identificador interno. Además, se ha añadido un método
setId(Long id) para permitir que el repositorio JDBC asigne el identificador generado por PostgreSQL tras una inserción.
Estas modificaciones permiten que la clase sea compatible tanto con repositorios en memoria como con repositorios basados en JDBC.

--- 
<div align="center">

#### JDBC Configuration

</div>

---

Para este apartado necesitamos llevar a cabo una implementación de los repositorios para que puedan comunicarse con la 
base de datos PostgreSQL, su función es actuar como puente ejecutando consultas SQL reales y conviertiendo los resultados 
en objetos.

1. `ClienteRepositoryJDBC` lleva a cabo los siguientes métodos **CRUD**:
- guardar(): Ejecuta un `INSERT` para recuperar el id generado por la base de datos.
- `buscarPorId()`, `buscarPorDni()`, `buscarPorEmail()`, `buscarPorTelefono()`  ejecutan consultas `SELECT` con filtros
- `buscarTodos()` devuelve todos los clientes de la tabla.
-  Para convertir una fila de la base de datos (ResultSet) en un objeto Cliente de Java se usa `mapRowToCliente`


2. `CuenteRepositoryJDBC`
3. `MovimientoRepositoryJDBC`
