# Recuperación de integración y persistencia

## Estado encontrado
- El PR #2 integró inicialmente la arquitectura y el módulo de productos/inventario.
- El PR #3 revirtió el PR #2 y también retiró documentación, configuración Maven, CI, pruebas y fuentes.
- No se encontró una explicación técnica del motivo de la reversión en la conversación de los PR; el equipo debe confirmarla.
- La rama de desarrollo conserva los archivos de la implementación.
- No se encontraron fuentes Java implementadas para clientes, ventas ni analítica; los borradores documentales no equivalen a código funcional.

## Recuperación propuesta
Esta rama parte del main actual y vuelve a incorporar explícitamente la documentación, el módulo de productos/inventario, Maven y CI. Se propone mediante un PR, sin modificar main directamente.

## Persistencia elegida para el prototipo
Se incorpora SQLite por JDBC como persistencia local inicial, sin necesidad de administrar un servidor. La base se crea en data/avnbr.db y queda excluida de Git. Las interfaces CategoryRepository y ProductRepository se conservan para permitir cambiar el motor después.

Tablas iniciales: categories (nombre único) y products (precio, stock, mínimo y clave foránea a categoría). Es adecuado para un prototipo local; si varios usuarios necesitan acceder simultáneamente, el equipo debe evaluar PostgreSQL u otro servicio centralizado.

## Pendientes reales
- Integrar las clases reales de clientes, ventas y analítica del Desarrollador 2 cuando estén disponibles.
- Acordar el contrato de ventas con el módulo de inventario.
- Revisar el motivo de la reversión con quien la realizó.
