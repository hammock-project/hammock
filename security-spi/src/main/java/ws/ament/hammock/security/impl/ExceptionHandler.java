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

package ws.ament.hammock.security.impl;

import ws.ament.hammock.security.api.MissingRolesException;
import ws.ament.hammock.security.api.NotLoggedInException;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@RequestScoped
@Provider
public class ExceptionHandler implements ExceptionMapper<Exception>{
    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();
        if(e instanceof MissingRolesException) {
            return toResponse((MissingRolesException)e);
        }
        else if(e instanceof NotLoggedInException) {
            return toResponse((NotLoggedInException)e);
        }
        return Response.noContent().build();
    }

    public Response toResponse(MissingRolesException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .build();
    }

    public Response toResponse(NotLoggedInException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE,"Bearer")
                .build();
    }
}
