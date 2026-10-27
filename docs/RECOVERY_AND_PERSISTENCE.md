# Recuperación de integración y persistencia

## Historial de integración

- El PR #2 integró inicialmente la arquitectura y el módulo de productos/inventario.
- El PR #3 revirtió el PR #2. En la conversación de los PR no quedó una explicación técnica concluyente del motivo de la reversión; si sigue siendo relevante, el equipo debe confirmarla.
- El PR #4 recuperó la arquitectura, la persistencia de productos/inventario, la configuración Maven, CI, pruebas y documentación.
- El PR #5 incorporó a `main` los módulos del Desarrollador 2: clientes, ventas, detalles de venta y analítica.
- La ejecución de CI asociada a la integración del PR #5 terminó correctamente.

## Estado actual

La implementación de productos/inventario y la de clientes/ventas/analítica están presentes en `main`. La documentación específica de cada módulo describe sus componentes y reglas de negocio:

- [Implementación del Desarrollador 1](DEV1_IMPLEMENTATION.md)
- [Implementación del Desarrollador 2](DEVELOPER_2_IMPLEMENTATION.md)

La integración de código y la verificación automatizada están completadas. Esto no sustituye una prueba funcional de extremo a extremo en el entorno de ejecución previsto ni la aceptación final del equipo.

## Persistencia del prototipo

Se utiliza SQLite por JDBC como persistencia local inicial, sin necesidad de administrar un servidor. La base se crea en `data/avnbr.db` y queda excluida de Git. Las interfaces de repositorio permiten cambiar el motor posteriormente.

Tablas iniciales del módulo de productos/inventario: `categories` (nombre único) y `products` (precio, stock, mínimo y clave foránea a categoría). El módulo del Desarrollador 2 agrega `customers`, `sales` y `sale_details`.

SQLite es adecuado para un prototipo local. Si varios usuarios necesitan acceder simultáneamente, el equipo debe evaluar PostgreSQL u otro servicio centralizado.

## Verificaciones y próximos pasos

- Ejecutar `mvn clean verify` en el entorno de desarrollo para repetir compilación y pruebas.
- Hacer una prueba manual de extremo a extremo que incluya alta de cliente, registro de venta, descuento de inventario y consulta de indicadores.
- Confirmar con el equipo los criterios de aceptación y el motivo de la reversión del PR #2, si aún hace falta aclararlo.
