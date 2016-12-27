/*
 * Copyright 2016 Hammock and its contributors
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

package ws.ament.hammock.web.api;

import javax.enterprise.util.AnnotationLiteral;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

public class ServletDescriptor extends AnnotationLiteral<WebServlet> implements WebServlet{

    private final String name;
    private final String[] value;
    private final String[] urlPatterns;
    private final int loadOnStartup;
    private final WebInitParam[] initParams;
    private final boolean asyncSupported;
    private final Class<? extends HttpServlet> servletClass;

    public ServletDescriptor(String name, String[] value, String[] urlPatterns, int loadOnStartup, WebInitParam[] initParams,
                             boolean asyncSupported, Class<? extends HttpServlet> servletClass) {
        this.name = name;
        this.value = value;
        this.urlPatterns = urlPatterns;
        this.loadOnStartup = loadOnStartup;
        this.initParams = initParams;
        this.asyncSupported = asyncSupported;
        this.servletClass = servletClass;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String[] value() {
        return value;
    }

    @Override
    public String[] urlPatterns() {
        return urlPatterns;
    }

    @Override
    public int loadOnStartup() {
        return loadOnStartup;
    }

    @Override
    public WebInitParam[] initParams() {
        return initParams;
    }

    @Override
    public boolean asyncSupported() {
        return asyncSupported;
    }

    public Class<? extends HttpServlet> servletClass() {
        return servletClass;
    }

    @Override
    public String smallIcon() {
        return null;
    }

    @Override
    public String largeIcon() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String displayName() {
        return null;
    }
}
