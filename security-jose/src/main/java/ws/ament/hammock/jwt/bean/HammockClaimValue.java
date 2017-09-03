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

package ws.ament.hammock.jwt.bean;

import org.eclipse.microprofile.jwt.ClaimValue;
import org.eclipse.microprofile.jwt.Claims;
import ws.ament.hammock.jwt.JWTPrincipal;

import javax.json.*;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class HammockClaimValue<T> implements ClaimValue<T> {
    private final String name;
    private final T realValue;
    private final JWTPrincipal jwtPrincipal;
    private final ClaimDefinition claimDefinition;

    public HammockClaimValue(JWTPrincipal jwtPrincipal, ClaimDefinition claimDefinition) {
        this.jwtPrincipal = jwtPrincipal;
        this.claimDefinition = claimDefinition;
        this.name = claimDefinition.claim.standard() == Claims.UNKNOWN ?
                claimDefinition.claim.value() : claimDefinition.claim.standard().name();
        this.realValue = getRealValue();
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public T getValue() {
        return realValue;
    }

    private T getRealValue() {
        JsonObject jwt = jwtPrincipal.getJwt();
        JsonValue result = jwt.get(name);
        if(result == null) {
            if(claimDefinition.returnType == Optional.class) {
                return (T)Optional.empty();
            }else {
                return null;
            }
        } else {
            if(claimDefinition.returnType instanceof Class &&
                    JsonValue.class.isAssignableFrom((Class)claimDefinition.returnType)) {
                return (T)result;
            }
            else if(claimDefinition.returnType == Optional.class &&
                    claimDefinition.typeParameter instanceof Class &&
                    JsonValue.class.isAssignableFrom((Class)claimDefinition.typeParameter)) {
                return (T)Optional.ofNullable(result);
            }
            Class<?> valueType;
            if (claimDefinition.returnType == Optional.class) {
                if(claimDefinition.typeParameter instanceof ParameterizedType) {
                    ParameterizedType typeParameter = (ParameterizedType) claimDefinition.typeParameter;
                    valueType = (Class<?>)typeParameter.getRawType();
                }
                else {
                    valueType = (Class<?>)claimDefinition.typeParameter;
                }
            }
            else {
                valueType = (Class<?>)claimDefinition.returnType;
            }
            Object value = mapType(valueType, result);

            if(claimDefinition.returnType == Optional.class) {
                return (T)Optional.ofNullable(value);
            }
            else {
                return (T)value;
            }
        }
    }

    private Object mapType(Class<?> type, JsonValue result) {
        if(JsonValue.class.isAssignableFrom(type)) {
            return result;
        } else if (type == Boolean.class){
            return JsonValue.TRUE.equals(result);
        } else if (type == Long.class && result instanceof JsonNumber) {
            return JsonNumber.class.cast(result).longValue();
        } else if (type == String.class && result instanceof JsonString) {
            return JsonString.class.cast(result).getString();
        } else if (type == Set.class && result instanceof JsonArray) {
            return ((JsonArray) result).getValuesAs(JsonString.class)
                    .stream().map(JsonString::getString)
                    .collect(toSet());
        } else if (type == List.class && result instanceof JsonArray) {
            return ((JsonArray) result).getValuesAs(JsonString.class)
                    .stream().map(JsonString::getString)
                    .collect(toList());
        } else {
            throw new IllegalArgumentException("Unsupported conversion of "+result+" based to type "+type);
        }
    }
}
