# Swagger OpenAPI Integration

This module adds OpenAPI v3 integration.

## Usage

- add hammock _swagger_ artifact
- add annotions like @OpenAPIDefinition and @Operation
- add "/openapi-configuration.json" to classpath resources;

	{
	    "resourcePackages": [
	        "com.example.project"
	    ]
	}
