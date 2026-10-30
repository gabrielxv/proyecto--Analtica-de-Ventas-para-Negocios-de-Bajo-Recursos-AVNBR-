# Implementación del Desarrollador 2

## Alcance

El módulo del Desarrollador 2 comprende clientes, ventas, detalles de venta y analítica, integrado con el módulo de productos/inventario mediante contratos definidos por la arquitectura.

## Componentes

- Modelos: `Cliente`, `Venta`, `DetalleVenta`.
- Persistencia: `ClienteRepository`, `VentaRepository`, `SqliteClienteRepository`, `SqliteVentaRepository`.
- Servicios: `ClienteService`, `VentaService`, `AnaliticaService`.
- Controladores: `ClienteController`, `VentaController`, `AnaliticaController`.
- Contratos de integración: `ClienteLookup`, `ProductOperations`.
- Esquema SQLite: `customers`, `sales`, `sale_details`.

## Reglas de negocio

- El documento del cliente es obligatorio y único.
- Una venta requiere un cliente existente, método de pago, al menos un detalle y cantidades positivas.
- El precio de cada detalle se toma del producto registrado, no del precio enviado por la interfaz.
- Se valida disponibilidad de inventario a través del contrato `ProductOperations`; el módulo D2 no duplica la entidad Product ni las reglas de stock.
- El stock se descuenta al registrar la venta. Si el guardado falla, el servicio intenta compensar los cambios de stock.
- Se conserva el nombre del cliente, el nombre del producto, la categoría y el precio aplicado como datos históricos de la venta.
- Las ventas se consultan por rango de fechas inclusivo.

## Aplicación de SOLID

- **SRP:** `ClienteService`, `VentaService` y `AnaliticaService` separan responsabilidades de dominio.
- **ISP:** `VentaService` consume `ClienteLookup` y `ProductOperations`, contratos limitados a lo que necesita.
- **DIP:** las dependencias entre ventas, analítica y clientes/productos se expresan mediante abstracciones.
- **OCP:** los contratos permiten sustituir implementaciones de consulta y persistencia; nuevas políticas comerciales aún no están implementadas.
- **LSP:** no se declara completo hasta disponer de pruebas de contrato para implementaciones alternativas.

## Aplicación de GRASP

- **Experto:** `Venta` calcula total y unidades a partir de sus detalles.
- **Creador:** `VentaService` construye venta y detalles con datos validados.
- **Controlador:** `VentaController` y `ClienteController` delegan operaciones.
- **Bajo acoplamiento:** contratos `ClienteLookup` y `ProductOperations` aíslan dependencias.
- **Alta cohesión:** cada servicio agrupa operaciones de su módulo.
- **Fabricación pura e Indirección:** servicios y repositorios separan dominio y persistencia.
- **Variaciones protegidas:** los contratos protegen de cambios de implementación; las reglas de negocio variables aún no tienen estrategias propias.
- **Polimorfismo:** no se afirma como patrón de negocio implementado; requiere variantes de políticas seleccionables por contrato.

## Indicadores

`AnaliticaService` expone total de ventas, cantidad de transacciones, unidades vendidas, ticket promedio, producto más vendido, ventas por categoría, ventas por método de pago, productos con stock bajo y variación porcentual entre períodos.

## Pruebas y verificación

Ejecutar `mvn clean verify` para compilar y correr las pruebas automatizadas. Deben comprobarse persistencia de ventas y detalles, descuento de stock, indicadores, rechazo de documentos duplicados y rechazo de ventas con stock insuficiente.

No se declara una ejecución exitosa hasta disponer del resultado real del comando en el entorno de integración.
