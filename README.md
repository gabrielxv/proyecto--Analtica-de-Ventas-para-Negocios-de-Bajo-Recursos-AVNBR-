# Analitica de Ventas para Negocios de Bajo Recursos (AVNBR)

Sistema orientado al registro de productos, clientes, ventas, control de inventario e indicadores de gestion.

## Arquitectura

UI -> Controller -> Service -> Repository -> Database

## Responsabilidades

- Lider: arquitectura, contratos, integracion, calidad, pruebas y documentacion.
- Desarrollador 1: categorias, productos e inventario.
- Desarrollador 2: clientes, ventas y analitica.

## Normas

El desarrollo debe respetar responsabilidades unicas, nombres claros, metodos pequenos, validaciones en la capa adecuada, bajo acoplamiento y separacion estricta entre interfaz, negocio y persistencia.

Documentacion del lider:
- docs/ARCHITECTURE.md
- docs/INTEGRATION_CONTRACT.md
- docs/CLEAN_CODE.md