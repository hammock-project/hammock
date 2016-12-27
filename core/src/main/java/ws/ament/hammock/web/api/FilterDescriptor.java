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
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

public class FilterDescriptor extends AnnotationLiteral<WebFilter> implements WebFilter {
    private final String name;
    private final String[] value;
    private final String[] urlPatterns;
    private final DispatcherType[] dispatcherTypes;
    private final WebInitParam[] initParams;
    private final boolean asyncSupported;
    private final String[] servletNames;
    private final Class<? extends Filter> clazz;

    public FilterDescriptor(String name, String[] value, String[] urlPatterns, DispatcherType[] dispatcherTypes,
                            WebInitParam[] initParams, boolean asyncSupported, String[] servletNames, Class<? extends Filter> clazz) {
        this.name = name;
        this.value = value;
        this.urlPatterns = urlPatterns;
        this.dispatcherTypes = dispatcherTypes;
        this.initParams = initParams;
        this.asyncSupported = asyncSupported;
        this.servletNames = servletNames;
        this.clazz = clazz;
    }

    @Override
    public String description() {
        return name;
    }

    @Override
    public String displayName() {
        return name;
    }

    @Override
    public WebInitParam[] initParams() {
        return initParams;
    }

    @Override
    public String filterName() {
        return name;
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
    public String[] servletNames() {
        return servletNames;
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
    public DispatcherType[] dispatcherTypes() {
        return dispatcherTypes;
    }

    @Override
    public boolean asyncSupported() {
        return asyncSupported;
    }

    public Class<? extends Filter> getClazz() {
        return clazz;
    }
}
