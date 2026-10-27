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

La recuperación del módulo de productos/inventario se presenta mediante el PR de esta rama. Clientes, ventas y analítica requieren integrar la implementación real del Desarrollador 2; sus borradores documentales no equivalen a código funcional.

## Documentación

- [Arquitectura](docs/ARCHITECTURE.md)
- [Contrato de integración](docs/INTEGRATION_CONTRACT.md)
- [Normas Clean Code](docs/CLEAN_CODE.md)
- [Implementación del Desarrollador 1](docs/DEV1_IMPLEMENTATION.md)
- [Recuperación y persistencia](docs/RECOVERY_AND_PERSISTENCE.md)
