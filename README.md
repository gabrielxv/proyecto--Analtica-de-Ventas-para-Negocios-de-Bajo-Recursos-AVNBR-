# AVNBR — Análisis de Ventas para Negocios de Bajos Recursos

Prototipo Java para apoyar a pequeños negocios en la gestión de categorías, productos, inventario, clientes y ventas, con indicadores básicos para consultar el comportamiento comercial.

## Arquitectura

El flujo de responsabilidades sigue el patrón:

`Application / UI → Controller → Service → Repository → SQLite`

- **Model:** representa las entidades del dominio.
- **Controller:** coordina las operaciones solicitadas y delega en los servicios.
- **Service:** aplica las reglas de negocio y coordina operaciones entre módulos.
- **Repository:** separa las operaciones de datos de la lógica de negocio.
- **Persistencia:** SQLite mediante JDBC, con almacenamiento local.

El punto de entrada actual es una demostración de consola. Los controladores son clases Java de coordinación; el proyecto no debe describirse como una aplicación web desplegada ni como una API REST.

## Responsabilidades del equipo

- **Líder técnico:** arquitectura, contratos entre módulos, integración, calidad, pruebas y documentación.
- **Desarrollador 1:** categorías, productos e inventario.
- **Desarrollador 2:** clientes, ventas, detalles de venta y analítica.

## Estado de integración y verificación

- El PR #4 recuperó la arquitectura, la persistencia de productos/inventario, la configuración Maven, CI y las pruebas.
- El PR #5 integró los módulos de clientes, ventas, detalles de venta y analítica.
- El PR #6 corrigió documentación que no reflejaba el estado real de integración.
- Los PR #4, #5 y #6 están fusionados en `main`.
- La ejecución de Java CI revisada terminó correctamente: el job de pruebas finalizó con éxito y el paso de compilación y ejecución de pruebas apareció en verde.
- El equipo informó que también realizó pruebas manuales. Los casos concretos y sus resultados no estaban detallados en este repositorio al momento de actualizar este documento; se recomienda conservar esa evidencia en una lista de verificación o acta de pruebas.

La compilación y las pruebas automatizadas satisfactorias son evidencia de verificación técnica, pero no equivalen por sí solas a una certificación de todos los escenarios funcionales o de requisitos que no estén cubiertos por las pruebas.

## Funcionalidades principales

- Gestión de categorías y productos.
- Validación de precio, existencias y stock mínimo.
- Consulta de disponibilidad y actualización de inventario.
- Registro y consulta de clientes.
- Registro de ventas con uno o varios detalles.
- Validación de cliente, cantidades, productos y stock disponible.
- Cálculo de indicadores: total de ventas, transacciones, unidades, ticket promedio, producto más vendido, ventas por categoría y método de pago, productos con stock bajo y variación entre períodos.
- Persistencia local de categorías, productos, clientes, ventas y detalles de venta.

## Tecnologías

- Java 17
- Maven
- SQLite mediante JDBC
- JUnit 5
- GitHub Actions para integración continua

## Documentación

- [Arquitectura](docs/ARCHITECTURE.md)
- [Contrato de integración](docs/INTEGRATION_CONTRACT.md)
- [Normas Clean Code](docs/CLEAN_CODE.md)
- [Implementación del Desarrollador 1](docs/DEV1_IMPLEMENTATION.md)
- [Implementación del Desarrollador 2](docs/DEVELOPER_2_IMPLEMENTATION.md)
- [Recuperación y persistencia](docs/RECOVERY_AND_PERSISTENCE.md)

## Verificación local

Desde la raíz del proyecto, ejecutar:

```bash
mvn clean verify
```

La base de datos local se crea en `data/avnbr.db` y no debe versionarse. Para una demostración, conviene comprobar el flujo completo: crear o consultar un cliente y un producto, registrar una venta, verificar el descuento de inventario y consultar los indicadores.

## Alcance y mejoras futuras

SQLite es apropiado para el prototipo local actual. Si el alcance exige varios usuarios concurrentes, acceso remoto o despliegue, debe evaluarse una base de datos centralizada y una capa de servicio apropiada. Otras mejoras posibles son hacer atómica la operación de venta y actualización de stock, revisar la representación monetaria y mantener actualizada la evidencia de pruebas.
