package ws.ament.hammock.swagger.ui;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ws.ament.hammock.web.api.ServletDescriptor;

@ApplicationScoped
public class SwaggerUIProvider {

    static private final String UI_MATCH_FORWARD = "/*";
    static private final String UI_REDIRECT_LINK = "/index.html?url=";
    static private final String UI_WEBJARS_BASE = "/webjars/swagger-ui/";

    @Inject
    private SwaggerUIConfiguration config;

    @Produces
    public ServletDescriptor swaggerUIDispatcherServlet() {
        String name = SwaggerUIDispatcherServlet.class.getSimpleName();
        String[] uris = new String[] { config.getSwaggerUIUrl(), config.getSwaggerUIUrl() + UI_MATCH_FORWARD };
        WebInitParam[] params = null;
        return new ServletDescriptor(name, uris, uris, 1, params, false, SwaggerUIDispatcherServlet.class);
    }

    @SuppressWarnings("serial")
    static public class SwaggerUIDispatcherServlet extends HttpServlet {

        @Inject
        private SwaggerUIConfiguration config;

        private String uiBootRedirect;

        private String uiBaseForward;

        public void init() throws ServletException {
            uiBootRedirect = config.getSwaggerUIUrl() + UI_REDIRECT_LINK + config.getSwaggerUIApi();
            uiBaseForward = UI_WEBJARS_BASE + config.getSwaggerUIVersion();
        }

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            if (!config.isSwaggerUIEnable()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (request.getPathInfo() == null) {
                response.sendRedirect(uiBootRedirect);
                return;
            }
            RequestDispatcher disp = request.getRequestDispatcher(uiBaseForward + request.getPathInfo());
            if (disp == null) {
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
                return;
            }
            disp.forward(request, response);
        }
    }
}
