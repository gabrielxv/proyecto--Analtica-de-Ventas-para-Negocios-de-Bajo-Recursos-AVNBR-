# Arquitectura del proyecto

AVNBR utiliza una arquitectura por capas: UI, Controller, Service, Repository/Persistence, Model y Database.

## Flujo general

`UI -> Controller -> Service -> Repository -> Database`

- **UI:** recibe entradas y muestra resultados. En consola, esta función está en `ConsoleMenu`.
- **Controller:** recibe la operación y delega en el servicio correspondiente.
- **Service:** concentra reglas y casos de uso.
- **Repository:** define el acceso a datos.
- **Model:** representa las entidades del dominio.
- **Database:** SQLite conserva la información local.

## Punto de composición

`Application` crea las implementaciones SQLite y las conecta con servicios y controladores. Así, los servicios pueden depender de interfaces mientras la aplicación decide qué implementación utilizar.

## Contratos entre módulos

- `ClienteLookup`: consulta mínima de clientes que necesita el proceso de ventas.
- `ProductOperations`: operaciones de producto/inventario necesarias para ventas y analítica.
- `ClienteRepository`, `VentaRepository`, `ProductRepository` y `CategoryRepository`: contratos de persistencia.

## Responsabilidades y principios

El menú no contiene reglas de negocio ni SQL. La lectura CSV está separada en `CsvClienteImporter`. Las reglas de clientes, ventas, productos y analítica se mantienen en sus servicios.

La matriz de evidencia de SOLID y GRASP está en [SOLID_GRASP.md](SOLID_GRASP.md). Las funciones del líder técnico y su lista de revisión están en [TECHNICAL_LEAD_GUIDE.md](TECHNICAL_LEAD_GUIDE.md).
