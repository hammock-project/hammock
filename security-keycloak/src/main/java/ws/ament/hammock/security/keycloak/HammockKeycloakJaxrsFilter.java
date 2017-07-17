/*
 * Copyright 2017 Hammock and its contributors
 *
 * Much of this logic comes from the Keycloak default implementation
 * https://github.com/keycloak/keycloak/blob/master/adapters/oidc/jaxrs-oauth-client/src/main/java/org/keycloak/jaxrs/JaxrsBearerTokenFilterImpl.java
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ws.ament.hammock.security.keycloak;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.*;
import org.keycloak.adapters.spi.AuthChallenge;
import org.keycloak.adapters.spi.AuthOutcome;
import org.keycloak.adapters.spi.UserSessionManagement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Set;

@ApplicationScoped
@PreMatching
@Priority(Priorities.AUTHENTICATION)
@Provider
public class HammockKeycloakJaxrsFilter implements ContainerRequestFilter {
    private final static Logger log = LogManager.getLogger(HammockKeycloakJaxrsFilter.class);

    @Inject
    private KeycloakConfigResolver keycloakConfigResolver;
    @Inject
    private UserSessionManagement userSessionManagement;

    private NodesRegistrationManagement nodesRegistrationManagement;
    private AdapterDeploymentContext deploymentContext;

    @PostConstruct
    public void init() {
        deploymentContext = new AdapterDeploymentContext(keycloakConfigResolver);
        nodesRegistrationManagement = new NodesRegistrationManagement();
    }

    @PreDestroy
    public void shutdown() {
        nodesRegistrationManagement.stop();
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        SecurityContext securityContext = containerRequestContext.getSecurityContext();
        JaxrsHttpFacade facade = new JaxrsHttpFacade(containerRequestContext, securityContext);
        if (handlePreauth(facade)) {
            return;
        }

        KeycloakDeployment resolvedDeployment = deploymentContext.resolveDeployment(facade);

        nodesRegistrationManagement.tryRegister(resolvedDeployment);

        bearerAuthentication(facade, containerRequestContext, resolvedDeployment);
    }

    private boolean handlePreauth(JaxrsHttpFacade facade) {
        PreAuthActionsHandler handler = new PreAuthActionsHandler(userSessionManagement, deploymentContext, facade);
        if (handler.handleRequest()) {
            if (!facade.isResponseFinished()) {
                facade.getResponse().end();
            }
            return true;
        }

        return false;
    }

    private void bearerAuthentication(JaxrsHttpFacade facade, ContainerRequestContext request, KeycloakDeployment resolvedDeployment) {
        BearerTokenRequestAuthenticator authenticator = new BearerTokenRequestAuthenticator(resolvedDeployment);
        AuthOutcome outcome = authenticator.authenticate(facade);

        if (outcome == AuthOutcome.NOT_ATTEMPTED) {
            authenticator = new QueryParamterTokenRequestAuthenticator(resolvedDeployment);
            outcome = authenticator.authenticate(facade);
        }

        if (outcome == AuthOutcome.NOT_ATTEMPTED && resolvedDeployment.isEnableBasicAuth()) {
            authenticator = new BasicAuthRequestAuthenticator(resolvedDeployment);
            outcome = authenticator.authenticate(facade);
        }

        if (outcome == AuthOutcome.FAILED || outcome == AuthOutcome.NOT_ATTEMPTED) {
            AuthChallenge challenge = authenticator.getChallenge();
            boolean challengeSent = challenge.challenge(facade);
            if (!challengeSent) {
                // Use some default status code
                facade.getResponse().setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
            }

            // Send response now (if not already sent)
            if (!facade.isResponseFinished()) {
                facade.getResponse().end();
            }
            return;
        } else {
            if (verifySslFailed(facade, resolvedDeployment)) {
                return;
            }
        }

        propagateSecurityContext(facade, request, resolvedDeployment, authenticator);
        handleAuthActions(facade, resolvedDeployment);
    }

    private void propagateSecurityContext(JaxrsHttpFacade facade, ContainerRequestContext request, KeycloakDeployment resolvedDeployment, BearerTokenRequestAuthenticator bearer) {
        final RefreshableKeycloakSecurityContext skSession = new RefreshableKeycloakSecurityContext(resolvedDeployment, null, bearer.getTokenString(), bearer.getToken(), null, null, null);
        facade.setSecurityContext(skSession);
        final String principalName = AdapterUtils.getPrincipalName(resolvedDeployment, bearer.getToken());
        final KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal = new KeycloakPrincipal<>(principalName, skSession);
        final Set<String> roles = AdapterUtils.getRolesFromSecurityContext(skSession);
        request.setSecurityContext(new HammockSecurityContext(principal, roles, request.getSecurityContext().isSecure()));
    }

    private boolean verifySslFailed(JaxrsHttpFacade facade, KeycloakDeployment deployment) {
        if (!facade.getRequest().isSecure() && deployment.getSslRequired().isRequired(facade.getRequest().getRemoteAddr())) {
            log.warn("SSL is required to authenticate, but request is not secured");
            facade.getResponse().sendError(403, "SSL required!");
            return true;
        }
        return false;
    }

    private void handleAuthActions(JaxrsHttpFacade facade, KeycloakDeployment deployment) {
        AuthenticatedActionsHandler authActionsHandler = new AuthenticatedActionsHandler(deployment, facade);
        if (authActionsHandler.handledRequest()) {
            if (!facade.isResponseFinished()) {
                facade.getResponse().end();
            }
        }
    }
}
