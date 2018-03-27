# Swagger UI Integration

This module adds Swagger UI v3 integration.

## Provides

- Swagger UI v3 web resources.
- Boot ui redirect with configurable api url.

## Usage

- Add hammock _swagger-ui_ artifact.
- When not using hammock _swagger_ then provide the swagger api url.
- To disable the online validator use; `swagger-ui.redirect=index.html?validatorUrl=`

## Config

Property | default | Description
--- | --- | ---
swagger-ui.enable | `true` | Enable/Disable swagger ui per environment.
swagger-ui.version | `<version>` | Allows for usage of different webjar version.
swagger-ui.redirect | `index.html` | UI Boot redirect url, note; needs to start with slash or http.
swagger-ui.path | `/openapi.ui` | Base path for ui resources.
swagger-ui.api | `/openapi.json` | The url location to start the swagger ui with.

### Default request sequence

- request /openapi.ui
- redirect /openapi.ui/index.html?url=/openapi.json
- forwards /openapi.ui/* to /META-INF/resources/webjars/swagger-ui/

### UI Customization

There are a couple of options but most common would be a cloned index;

- Make a copy of the original index.html
- place it in `static.resource.location` like META-INF/resources/example/index.html
- Modify the js/css resources paths to prefix with /openapi.ui
- Change `swagger-ui.redirect` to /example/index.html
