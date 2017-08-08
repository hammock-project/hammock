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

package ws.ament.hammock.security.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.security.api.Identity;
import ws.ament.hammock.security.api.LoggedIn;
import ws.ament.hammock.security.api.NotLoggedInException;
import ws.ament.hammock.security.impl.SecurityInterceptor;
import ws.ament.hammock.security.internal.SecurityExtension;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(Arquillian.class)
public class LoggedInTest {

    private static final String SUCCESS = "success";

    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClasses(LoggedIn.class, SecurityInterceptor.class)
                .addAsServiceProviderAndClasses(Extension.class, SecurityExtension.class);
    }

    @Inject
    private MutableIdentity mutableIdentity;

    @Inject
    private LoggedInController loggedInController;

    @Test
    public void shouldOnlyGetSuccessWhenLoggedIn() {
        mutableIdentity.setLoggedIn(true);
        String value = loggedInController.doWork();
        assertThat(value).isEqualTo(SUCCESS);
    }

    @Test
    public void shouldFailWhenNotLoggedIn() {
        mutableIdentity.setLoggedIn(false);
        assertThatThrownBy(() -> loggedInController.doWork())
                .isInstanceOf(NotLoggedInException.class)
                .hasMessageContaining("Not logged in");
    }

    @ApplicationScoped
    public static class MutableIdentity implements Identity {
        private boolean loggedIn;

        @Override
        public boolean isLoggedIn() {
            return loggedIn;
        }

        @Override
        public boolean hasRole(String x) {
            return false;
        }

        @Override
        public Principal getPrincipal() {
            return null;
        }

        void setLoggedIn(boolean loggedIn) {
            this.loggedIn = loggedIn;
        }
    }

    @LoggedIn
    @ApplicationScoped
    public static class LoggedInController {
        String doWork() {
            return SUCCESS;
        }
    }
}
