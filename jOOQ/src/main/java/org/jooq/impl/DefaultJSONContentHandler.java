/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A default implementation of the {@link JSONContentHandler} API that returns
 * {@link ArrayList} and {@link LinkedHashMap} elements.
 *
 * @author Lukas Eder
 */
final class DefaultJSONContentHandler implements JSONContentHandler {

    private final List<Object> result;

    DefaultJSONContentHandler() {
        result = new ArrayList<>();
    }

    @Override
    public final void startObject() {
        result.add(new LinkedHashMap<>());
    }

    @Override
    public final void endObject() {
        add(remove());
    }

    @Override
    public final void property(String key) {
        result.add(key);
    }

    @Override
    public final void startArray() {
        result.add(new ArrayList<>());
    }

    @Override
    public final void endArray() {
        add(remove());
    }

    @Override
    public final void valueNull() {
        add(null);
    }

    @Override
    public final void valueFalse() {
        add(false);
    }

    @Override
    public final void valueTrue() {
        add(true);
    }

    @Override
    public final void valueNumber(String string) {
        switch (string) {
            case "NaN":
            case "-NaN":
            case "Infinity":
            case "-Infinity":
                add(Double.valueOf(string));
                break;

            default:
                add(new BigDecimal(string));
                break;
        }
    }

    @Override
    public final void valueString(String string) {
        add(string);
    }

    @SuppressWarnings("unchecked")
    private final void add(Object object) {
        Object last = result.isEmpty() ? null : result();

        if (last instanceof List) {
            ((List<Object>) last).add(object);
        }
        else if (last instanceof String property) {
            remove();
            ((Map<String, Object>) result()).put(property, object);
        }
        else
            result.add(object);
    }

    final Object remove() {
        return result.remove(result.size() - 1);
    }

    final Object result() {
        return result.get(result.size() - 1);
    }
}
