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
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singleton;
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
        if(Claims.raw_token.name().equals(name)) {
            if (claimDefinition.returnType == JsonString.class) {
                // hacky AF
                return (T)Json.createArrayBuilder().add(jwtPrincipal.getRawToken()).build().get(0);
            } else {
                return (T) jwtPrincipal.getRawToken();
            }
        }
        JsonObject jwt = jwtPrincipal.getJwt();
        JsonValue result = jwt.get(name);
        boolean isParameterized = claimDefinition.returnType instanceof ParameterizedType;
        boolean isOptional = isParameterized && ((ParameterizedType) claimDefinition.returnType).getRawType() == Optional.class;
        boolean isClaimValue = isParameterized && ((ParameterizedType) claimDefinition.returnType).getRawType() == ClaimValue.class;
        boolean isInnerOptional = false;
        if (result == null) {
            if (isClaimValue) {
                if (claimDefinition.typeParameter instanceof ParameterizedType &&
                        ((ParameterizedType) claimDefinition.typeParameter).getRawType() == Optional.class) {
                    return (T) Optional.empty();
                } else {
                    return null;
                }
            } else if (isOptional) {
                return (T) Optional.empty();
            } else {
                return null;
            }
        } else {
            if (claimDefinition.returnType instanceof Class &&
                    JsonValue.class.isAssignableFrom((Class) claimDefinition.returnType)) {
                return (T) mapType((Class<?>) claimDefinition.returnType, result);
            } else if (isOptional &&
                    claimDefinition.typeParameter instanceof Class &&
                    JsonValue.class.isAssignableFrom((Class) claimDefinition.typeParameter)) {
                return (T)Optional.of(mapType((Class<?>) claimDefinition.typeParameter, result));
            }
            Class<?> valueType;
            if (isOptional || isClaimValue) {
                if (claimDefinition.typeParameter instanceof ParameterizedType) {
                    ParameterizedType typeParameter = (ParameterizedType) claimDefinition.typeParameter;
                    valueType = (Class<?>) typeParameter.getRawType();
                    if (valueType == Optional.class) {
                        isInnerOptional = true;
                        valueType = (Class<?>) typeParameter.getActualTypeArguments()[0];
                    }
                } else {
                    valueType = (Class<?>) claimDefinition.typeParameter;
                }
            } else {
                if (claimDefinition.returnType instanceof ParameterizedType &&
                        !Collection.class.isAssignableFrom((Class<?>) ((ParameterizedType) claimDefinition.returnType).getRawType())) {
                    ParameterizedType typeParameter = (ParameterizedType) claimDefinition.returnType;
                    Type type = typeParameter.getActualTypeArguments()[0];
                    if (type instanceof ParameterizedType) {
                        valueType = (Class<?>) ((ParameterizedType) type).getRawType();
                    } else {
                        valueType = (Class<?>) type;
                    }
                } else if(claimDefinition.returnType instanceof ParameterizedType) {
                    valueType = (Class<?>) ((ParameterizedType) claimDefinition.returnType).getRawType();
                } else {
                    valueType = (Class<?>) claimDefinition.returnType;
                }
            }
            Object value = mapType(valueType, result);

            if (isOptional || isInnerOptional) {
                return (T) Optional.ofNullable(value);
            } else {
                return (T) value;
            }
        }
    }

    private Object mapType(Class<?> type, JsonValue result) {
        if (JsonValue.class.isAssignableFrom(type)) {
            if (!(result instanceof JsonArray) && JsonArray.class.isAssignableFrom(type)) {
                return Json.createArrayBuilder().add(result).build();
            } else {
                return result;
            }
        } else if (type == Boolean.class || type == boolean.class) {
            return JsonValue.TRUE.equals(result);
        } else if ((type == Long.class || type == long.class) && result instanceof JsonNumber) {
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
        } else if (type == Set.class && result instanceof JsonString) {
            return singleton(((JsonString) result).getString());
        } else {
            throw new IllegalArgumentException("Unsupported conversion of " + result + " based on type " + type);
        }
    }
}
