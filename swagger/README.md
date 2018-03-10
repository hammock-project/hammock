# Swagger OpenAPI Integration

This module adds OpenAPI v3 integration.

## Provides

- Automatic scanning of all rest endpoints based on @Path annotation.
- Support for all OpenApi annotations like;
	- `@OpenAPIDefinition`
	- `@Operation`
- Adds two OpenApi specification exports;
	- `@ApplicationPath(/openapi.{json|yaml})`
	- `@ApplicationPath(/openapi)` (accept header variant)
- Adds optional (json|yaml) `MessageBodyWriter` for OpenAPI model.
  (only used for custom endpoint and returning OpenAPI model as entity)

## Usage

- Add hammock _swagger_ artifact.
- Optional configure rest endpoint scanning.
	- Add rest endpoints in jaxrs _Application_ class.
	- Or add `/openapi-configuration.json` in classpath resources;
```
	{
	    "resourcePackages": [
	        "com.example.project"
	    ]
	}
```
