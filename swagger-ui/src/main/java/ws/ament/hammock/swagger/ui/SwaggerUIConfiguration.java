package ws.ament.hammock.swagger.ui;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class SwaggerUIConfiguration {

    @Inject
    @ConfigProperty(name = "swagger-ui.enable", defaultValue = "true")
    private boolean swaggerUIEnable;

    @Inject
    @ConfigProperty(name = "swagger-ui.version", defaultValue = "3.10.0")
    private String swaggerUIVersion;

    @Inject
    @ConfigProperty(name = "swagger-ui.url", defaultValue = "/openapi.ui")
    private String swaggerUIUrl;

    @Inject
    @ConfigProperty(name = "swagger-ui.api", defaultValue = "/openapi.json")
    private String swaggerUIApi;

    public boolean isSwaggerUIEnable() {
        return swaggerUIEnable;
    }

    public String getSwaggerUIVersion() {
        return swaggerUIVersion;
    }

    public String getSwaggerUIUrl() {
        return swaggerUIUrl;
    }

    public String getSwaggerUIApi() {
        return swaggerUIApi;
    }
}
