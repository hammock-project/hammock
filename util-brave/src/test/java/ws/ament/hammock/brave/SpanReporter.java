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

package ws.ament.hammock.brave;

import zipkin.Span;
import zipkin.reporter.Reporter;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SpanReporter implements Reporter<Span> {
    private List<Span> spans = new ArrayList<>();
    @Override
    public void report(Span span) {
        spans.add(span);
    }

    public List<Span> getSpans() {
        return spans;
    }
}
