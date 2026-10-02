# Contrato de integracion

## Producto
Propietario: Desarrollador 1. Ventas puede consultar productos por identificador o codigo, pero no modificar su estructura sin coordinacion.

## Inventario
Propietario: Desarrollador 1. Ventas debe solicitar validacion de disponibilidad y actualizacion de existencias; no duplicar la logica de stock.

## Categoria
Propietario: Desarrollador 1. Otros modulos pueden consultar categorias cuando sea necesario.

## Cliente, Venta y DetalleVenta
Propietario: Desarrollador 2. Cada DetalleVenta debe referenciar un producto existente.

## Analitica
Propietario: Desarrollador 2, con validacion del lider.

## Reglas
1. No duplicar entidades.
2. No duplicar reglas de negocio.
3. No acceder directamente a la base de datos desde UI o Controller.
4. No modificar clases de otro modulo sin coordinacion.
5. Mantener explicitas las dependencias entre modulos.
6. Revisar cambios que afecten contratos antes de integrarlos.