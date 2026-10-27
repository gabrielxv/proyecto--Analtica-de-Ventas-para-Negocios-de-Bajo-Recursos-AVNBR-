# Recuperación de integración y persistencia

## Historial de integración

- El PR #2 integró inicialmente la arquitectura y el módulo de productos/inventario.
- El PR #3 revirtió el PR #2. En el historial revisado no se encontró una explicación técnica concluyente del motivo; si es necesario para la evaluación, debe confirmarse con el equipo que realizó la reversión.
- El PR #4 recuperó la arquitectura, la persistencia de productos/inventario, la configuración Maven, CI, pruebas y documentación.
- El PR #5 incorporó a `main` los módulos del Desarrollador 2: clientes, ventas, detalles de venta y analítica.
- El PR #6 actualizó el README y este documento para reflejar el estado real de la integración.
- Los PR #4, #5 y #6 están fusionados en `main`.

## Estado actual

Los módulos de productos/inventario y clientes/ventas/analítica están presentes en `main`. La implementación y las responsabilidades se describen en:

- [Implementación del Desarrollador 1](DEV1_IMPLEMENTATION.md)
- [Implementación del Desarrollador 2](DEVELOPER_2_IMPLEMENTATION.md)

La ejecución de Java CI revisada terminó con éxito, incluyendo compilación y ejecución de pruebas. El equipo también informó que ya realizó pruebas manuales. Para mejorar la trazabilidad, se recomienda guardar los escenarios ejecutados, los resultados observados, la fecha y la persona responsable; este documento no atribuye resultados concretos a pruebas manuales que no estén registrados aquí.

## Persistencia del prototipo

El proyecto utiliza SQLite por JDBC como persistencia local inicial, sin necesidad de administrar un servidor. La base se crea en `data/avnbr.db` y debe permanecer excluida de Git. Las interfaces de repositorio separan la lógica de negocio del mecanismo concreto de almacenamiento.

Las tablas del esquema actual son:

| Tabla | Propósito |
|---|---|
| `categories` | Categorías; el nombre es único. |
| `products` | Productos, precio, existencias, mínimo y relación con categoría. |
| `customers` | Clientes; el documento es único. |
| `sales` | Cabecera de la venta, fecha, cliente, método de pago y total. |
| `sale_details` | Líneas de venta con producto, cantidad y precio aplicado. |

Las claves foráneas mantienen relaciones entre registros. Los detalles de venta conservan campos descriptivos y el precio aplicado para ayudar a preservar el contexto histórico de la transacción.

## Verificación y mantenimiento

La ejecución de CI revisada está en verde. Para repetir la verificación en un entorno de desarrollo, desde la raíz del proyecto:

```bash
mvn clean verify
```

Como el equipo informó que ya realizó las pruebas manuales, no se registran aquí como pendientes de ejecución. El pendiente documental es conservar sus resultados concretos. Una lista de evidencia puede usar este formato:

| Caso | Pasos resumidos | Resultado esperado | Resultado real | Responsable / fecha |
|---|---|---|---|---|
| Alta o consulta de cliente | Registrar o consultar un cliente. | El cliente queda disponible para la venta. | Completar con el resultado observado. | Completar por el equipo. |
| Venta válida | Registrar una venta con cliente, producto y cantidad disponibles. | La venta y sus detalles se guardan; el stock disminuye. | Completar con el resultado observado. | Completar por el equipo. |
| Stock insuficiente | Intentar vender más unidades que las disponibles. | Se rechaza la venta y no se confirma el descuento de stock. | Completar con el resultado observado. | Completar por el equipo. |
| Analítica | Consultar indicadores después de registrar ventas. | Los indicadores corresponden a los datos guardados. | Completar con el resultado observado. | Completar por el equipo. |

## Alcance y consideraciones

SQLite es adecuado para un prototipo local. Si varios usuarios necesitan acceder simultáneamente o desde equipos remotos, debe evaluarse una base centralizada como PostgreSQL y la arquitectura de despliegue correspondiente.

La aplicación actual se presenta como una demostración Java de consola; no debe describirse como una aplicación web desplegada ni como una API REST. Como mejoras futuras se puede reforzar la atomicidad entre la persistencia de la venta y la actualización del inventario, y revisar la representación de importes monetarios si los requisitos requieren precisión financiera estricta.
