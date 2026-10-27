# proyecto---Analtica-de-Ventas-para-Negocios-de-Bajo-Recursos-AVNBR-
Estructuración de modelo de control y gestión de pequeños negocios en el ámbito de materiales y valores netos de consumo

## Arquitectura

Sistema orientado al registro de productos, clientes, ventas, control de inventario e indicadores.

Flujo de capas: UI -> Controller -> Service -> Repository -> Database.

## Responsabilidades

- Líder: arquitectura, contratos, integración, calidad, pruebas y documentación.
- Desarrollador 1: categorías, productos e inventario.
- Desarrollador 2: clientes, ventas y analítica.

## Estado de integración

- El módulo de productos/inventario está integrado en `main`.
- El módulo del Desarrollador 2 (clientes, ventas, detalles de venta y analítica) también está integrado en `main` mediante el PR #5.
- La integración de arquitectura, persistencia y productos/inventario se recuperó mediante el PR #4.
- La ejecución de CI asociada a la integración del Desarrollador 2 terminó correctamente.
- La integración de código y la verificación automatizada están completadas. Antes de dar el proyecto por finalizado, se recomienda una revisión funcional de extremo a extremo y confirmar con el equipo los criterios finales de aceptación.

## Documentación

- [Arquitectura](docs/ARCHITECTURE.md)
- [Contrato de integración](docs/INTEGRATION_CONTRACT.md)
- [Normas Clean Code](docs/CLEAN_CODE.md)
- [Implementación del Desarrollador 1](docs/DEV1_IMPLEMENTATION.md)
- [Implementación del Desarrollador 2](docs/DEVELOPER_2_IMPLEMENTATION.md)
- [Recuperación y persistencia](docs/RECOVERY_AND_PERSISTENCE.md)
