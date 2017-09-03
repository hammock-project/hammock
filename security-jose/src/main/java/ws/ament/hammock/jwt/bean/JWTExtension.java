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

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;
import ws.ament.hammock.jwt.HammockClaimValue;
import ws.ament.hammock.jwt.JWTPrincipal;
import ws.ament.hammock.utils.BiFunctionBean;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import javax.inject.Provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

public class JWTExtension implements Extension {

    private static final Predicate<InjectionPoint> NOT_PROVIDERS = ip -> (ip.getType() instanceof Class) || (ip.getType() instanceof ParameterizedType && ((ParameterizedType) ip.getType()).getRawType() != Provider.class);

    private Set<InjectionPoint> injectionPoints = new HashSet<>();

    public void locateClaims(@Observes ProcessInjectionPoint<?, ?> pip) {
        Claim claim = pip.getInjectionPoint().getAnnotated().getAnnotation(Claim.class);
        if (claim != null) {
            injectionPoints.add(pip.getInjectionPoint());
        }
    }

    public void registerConfigProducer(@Observes AfterBeanDiscovery abd) {
        Set<ClaimDefinition> claimDefinitions = injectionPoints.stream()
                .filter(NOT_PROVIDERS)
                .map(JWTExtension::toDefinition)
                .collect(Collectors.toSet());

        Set<ClaimDefinition> providers = injectionPoints.stream()
                .filter(NOT_PROVIDERS.negate())
                .map(JWTExtension::toDefinition)
                .collect(Collectors.toSet());

        claimDefinitions.addAll(providers);

        claimDefinitions.forEach(claimDefinition -> {
            abd.addBean(new BiFunctionBean<>(BiFunctionBean.class, claimDefinition.rawType,
                    claimDefinition, singleton(claimDefinition.claim),
                    RequestScoped.class, ClaimProducer.INSTANCE));
        });
    }

    private static ClaimDefinition toDefinition(InjectionPoint ip) {
        Type rawType = ip.getType();
        Type type = (ip.getType() instanceof ParameterizedType) ?
                ((ParameterizedType) ip.getType()).getActualTypeArguments()[0] : ip.getType();
        return new ClaimDefinition(getClaim(ip.getQualifiers()), rawType, type);
    }

    private static Claim getClaim(Set<Annotation> annotations) {
        for (Annotation a : annotations) {
            if (a instanceof Claim) {
                return (Claim) a;
            }
        }
        return null;
    }

    private static final class ClaimProducer implements BiFunction<CreationalContext<Object>,
            ClaimDefinition, Object> {
        static ClaimProducer INSTANCE = new ClaimProducer();

        @Override
        public Object apply(CreationalContext<Object> cc, ClaimDefinition claimDefinition) {
            Claim claim = claimDefinition.claim;
            JWTPrincipal jwtPrincipal = CDI.current().select(JWTPrincipal.class).get();
            if (claimDefinition.rawType == Optional.class) {
                return Optional.ofNullable(jwtPrincipal.getClaim(claim.value()));
            } else if (claimDefinition.rawType instanceof ParameterizedType &&
                    ((Class)((ParameterizedType)claimDefinition.rawType).getRawType()).isAssignableFrom(ClaimValue.class)) {
                return new HammockClaimValue<>(claimDefinition.claim.value(), jwtPrincipal.getClaim(claim.value()));
            } else {
                Object value = jwtPrincipal.getClaim(claim.value());
                return value;
            }
        }
    }

    private static class ClaimDefinition {
        private final Claim claim;
        private final Type rawType;
        private final Type type;

        ClaimDefinition(Claim claim, Type rawType, Type type) {
            this.claim = claim;
            this.rawType = rawType;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClaimDefinition that = (ClaimDefinition) o;

            if (claim != null ? !claim.equals(that.claim) : that.claim != null) return false;
            if (rawType != null ? !rawType.equals(that.rawType) : that.rawType != null) return false;
            return type != null ? type.equals(that.type) : that.type == null;
        }

        @Override
        public int hashCode() {
            int result = claim != null ? claim.hashCode() : 0;
            result = 31 * result + (rawType != null ? rawType.hashCode() : 0);
            result = 31 * result + (type != null ? type.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ClaimDefinition{" + "claim=" + claim +
                    ", rawType=" + rawType +
                    ", type=" + type +
                    '}';
        }
    }
}
