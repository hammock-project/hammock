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

package ws.ament.hammock.health;

import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import java.io.IOException;

@WebServlet(urlPatterns = {"${health.servlet.uri}"}, name = "HealthServlet")
@Dependent
public class HealthCheckServlet extends HttpServlet {
    @Inject
    private HealthCheckManager healthCheckManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(MediaType.APPLICATION_JSON);
        HealthCheckModel healthCheckModel = healthCheckManager.performHealthChecks();
        if (healthCheckModel.getOutcome().equalsIgnoreCase(HealthCheckResponse.State.UP.name())) {
            resp.setStatus(200);
        } else {
            resp.setStatus(503);
        }
        try (Jsonb jsonb = JsonbBuilder.newBuilder().build()) {
            jsonb.toJson(healthCheckModel, resp.getOutputStream());
        } catch (IOException ex) {
            // Re-throw IOException specifically to maintain existing functionality
            throw ex;
        } catch (Exception ex) {
            // Convert other exceptions thrown by close() into a runtime exception
            throw new RuntimeException(ex);
        }
    }
}
