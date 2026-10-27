# Implementación del Desarrollador 2

## Alcance
Se implementaron los módulos de clientes, ventas, detalle de venta y analítica siguiendo la arquitectura por capas del proyecto. Los componentes respetan el contrato de integración: Product y el stock siguen siendo responsabilidad del módulo de productos/inventario.

## Componentes
- Modelos: Cliente, Venta, DetalleVenta.
- Persistencia: ClienteRepository, VentaRepository, SqliteClienteRepository, SqliteVentaRepository.
- Servicios: ClienteService, VentaService, AnaliticaService.
- Controladores: ClienteController, VentaController, AnaliticaController.
- Esquema SQLite: customers, sales, sale_details.

## Reglas de negocio
- El documento del cliente es obligatorio y único.
- Una venta requiere un cliente existente, método de pago, al menos un detalle y cantidades positivas.
- El precio de cada detalle se toma del producto registrado, no del precio enviado por la interfaz.
- Se valida disponibilidad de inventario a través de ProductService; el módulo D2 no duplica la entidad Product.
- El stock se descuenta al registrar la venta. Si el guardado falla, el servicio intenta compensar los cambios de stock.
- Se conserva el nombre del cliente, el nombre del producto, la categoría y el precio aplicado como datos históricos de la venta.
- Las ventas se consultan por rango de fechas inclusivo.

## Indicadores
AnaliticaService expone total de ventas, cantidad de transacciones, unidades vendidas, ticket promedio, producto más vendido, ventas por categoría, ventas por método de pago, productos con stock bajo y variación porcentual entre períodos.

## Pruebas
DesarrolladorDosIntegrationTest comprueba persistencia de una venta y sus detalles, descuento de stock, indicadores de ventas, rechazo de documentos duplicados y rechazo de ventas con stock insuficiente.

## Ejecución
El proyecto utiliza Java 17, Maven, JUnit 5 y SQLite. Ejecutar mvn clean verify para compilar y correr las pruebas. La base local de demostración se crea en data/avnbr.db.
