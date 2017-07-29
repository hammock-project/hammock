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

package ws.ament.hammock.jwt;

import org.eclipse.microprofile.config.spi.Converter;
import ws.ament.hammock.jwt.processor.JWTProcessor;

import javax.annotation.Priority;
import javax.enterprise.inject.spi.CDI;

@Priority(1)
public class JWTProcessorConverter implements Converter<JWTProcessor> {
    public static final Converter<JWTProcessor> INSTANCE = new JWTProcessorConverter();

    @Override
    public JWTProcessor convert(String s) {
        try {
            Class<JWTProcessor> aClass = (Class<JWTProcessor>)Class.forName(s);
            return CDI.current().select(aClass).get();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No class "+s,e);
        }
    }
}
