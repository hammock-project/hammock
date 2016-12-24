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

package ws.ament.hammock;

import ws.ament.hammock.bootstrap.Bootstrapper;

public class DummyBootstrap implements Bootstrapper {
    private static int START_CALLED = 0;
    private static int STOP_CALLED = 0;
    @Override
    public void start() {
        START_CALLED++;
    }

    @Override
    public void stop() {
        STOP_CALLED++;
    }

    public static int getStartCalled() {
        return START_CALLED;
    }

    public static int getStopCalled() {
        return STOP_CALLED;
    }
}
