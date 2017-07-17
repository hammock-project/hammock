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

package ws.ament.hammock.bootstrap.weld;

import org.junit.Test;
import ws.ament.hammock.bootstrap.weld3.Weld3Bootstrapper;
import ws.ament.hammock.utils.Unmanageable;

import javax.enterprise.inject.spi.CDI;

import static org.assertj.core.api.Assertions.assertThat;

public class Weld3BootstrapperTest {
    @Test
    public void shouldCreateWeldContainer() {
        Weld3Bootstrapper bootstrapper = new Weld3Bootstrapper();
        bootstrapper.start();
        SomeBean someBean = CDI.current().select(SomeBean.class).get();
        assertThat(someBean).isNotNull();
        bootstrapper.stop();
    }

    @Test
    public void shouldHaveUsableUnmanageable() {
        Weld3Bootstrapper bootstrapper = new Weld3Bootstrapper();
        bootstrapper.start();
        try(Unmanageable<SomeBean> unmanageable = new Unmanageable<>(SomeBean.class)) {
            SomeBean actual = unmanageable.get();
            assertThat(actual).isNotNull();
            assertThat(actual.getKey1()).isEqualTo("prop1");
            assertThat(actual.getKey2()).isEqualTo("prop2");
        }
        bootstrapper.stop();
    }
}
