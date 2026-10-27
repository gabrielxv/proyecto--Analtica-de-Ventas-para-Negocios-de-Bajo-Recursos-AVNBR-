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

El módulo de productos/inventario está integrado en main. En esta rama se incorpora la implementación del Desarrollador 2 para clientes, ventas, detalles de venta y analítica, pendiente de revisión mediante pull request y de confirmar el resultado de CI.

## Documentación

- [Arquitectura](docs/ARCHITECTURE.md)
- [Contrato de integración](docs/INTEGRATION_CONTRACT.md)
- [Normas Clean Code](docs/CLEAN_CODE.md)
- [Implementación del Desarrollador 1](docs/DEV1_IMPLEMENTATION.md)
- [Implementación del Desarrollador 2](docs/DEVELOPER_2_IMPLEMENTATION.md)
- [Recuperación y persistencia](docs/RECOVERY_AND_PERSISTENCE.md)
