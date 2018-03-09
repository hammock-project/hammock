# Swagger UI Integration

This module adds Swagger UI v3 integration.

## Provides

- Swagger UI v3 web resources.
- Boot ui redirect with configurable api url.

## Usage

- Add hammock _swagger-ui_ artifact.
- When not using hammock _swagger_ then provide the swagger api url.

## Config

Property | default | Description
--- | --- | ---
swagger-ui.enable | `true` | Enable/Disable swagger ui per environment.
swagger-ui.version | `<version>` | Allows for usage of different webjar version.
swagger-ui.url | `/openapi.ui` | Redirects and hosts the ui.
swagger-ui.api | `/openapi.json` | The location to start the swagger ui with.

