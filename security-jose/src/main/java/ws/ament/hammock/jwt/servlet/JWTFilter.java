/*
 * Copyright 2017 Hammock and its contributors
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

package ws.ament.hammock.jwt.servlet;

import ws.ament.hammock.jwt.JWTConfiguration;
import ws.ament.hammock.jwt.JWTIdentity;
import ws.ament.hammock.jwt.JWTPrincipal;
import ws.ament.hammock.jwt.bean.JWTIdentityHolder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter(urlPatterns = {"${jwt.filter.uris}"},filterName = "JWT")
@Dependent
public class JWTFilter implements Filter {
    private static final String BEARER = "Bearer";
    @Inject
    private JWTConfiguration jwtConfiguration;

    @Inject
    private JWTIdentityHolder jwtIdentityHolder;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(jwtConfiguration.isJwtHeaderEnabled() || jwtConfiguration.isJwtQueryParamEnabled()) {
            String jwt = null;
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            if(jwtConfiguration.isJwtQueryParamEnabled()) {
                jwt = httpServletRequest.getParameter(jwtConfiguration.getJwtQueryParamName());
            }

            if(jwtConfiguration.isJwtHeaderEnabled()) {
                String auth = httpServletRequest.getHeader("Authorization");
                if(auth != null && auth.startsWith(BEARER)) {
                    jwt = auth.replaceFirst(BEARER,"").trim();
                }
            }

            if(jwt != null) {
                Map<String, Object> jwtBody = jwtConfiguration.getJwtProcessor().process(jwt);
                JWTPrincipal principal = new JWTPrincipal(jwtBody);
                jwtIdentityHolder.setJwtIdentity(new JWTIdentity(principal));
                servletRequest = new JWTRequest(principal, httpServletRequest);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
