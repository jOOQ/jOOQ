/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A stack to register elements that are visible to a certain scope.
 *
 * @author Lukas Eder
 */
final class ScopeStack<K, V> implements Iterable<V> {

    private int                  scopeLevel = -1;
    private Map<K, List<V>>      stack;
    private final Constructor<V> constructor;

    ScopeStack(final V defaultValue) {
        this(new Constructor<V>() {
            @Override
            public V create() {
                return defaultValue;
            }
        });
    }

    ScopeStack(Constructor<V> constructor) {
        this.constructor = constructor;
    }

    private final Map<K, List<V>> stack() {
        if (stack == null)
            stack = new LinkedHashMap<K, List<V>>();

        return stack;
    }

    private final void trim() {
        int l = scopeLevel + 1;
        if (l >= 0) {
            int size;
            for (Iterator<Map.Entry<K, List<V>>> it = stack().entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<K, List<V>> entry = it.next();
                List<V> list = entry.getValue();
                while ((size = list.size()) > l || size > 0 && list.get(size - 1) == null)
                    list.remove(size - 1);
                if (list.isEmpty())
                    it.remove();
            }
        }
    }
    @Override
    public final Iterator<V> iterator() {
        return new Iterator<V>() {
            Iterator<List<V>> it = stack().values().iterator();
            V next;

            @Override
            public boolean hasNext() {
                return move() != null;
            }

            @Override
            public V next() {
                if (next == null) {
                    return move();
                }
                else {
                    V result = next;
                    next = null;
                    return result;
                }
            }

            private V move() {
                while (it.hasNext()) {
                    List<V> list = it.next();

                    if (!list.isEmpty()) {
                        next = list.get(list.size() - 1);
                        break;
                    }
                }

                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    final void set(K key, V value) {
        set0(list(key), value);
    }

    private final V get0(List<V> list) {
        int i = list.size() - 1;
        return i == -1 ? null : list.get(i);
    }

    final V get(K key) {
        return get0(list(key));
    }

    final V getOrCreate(K key) {
        List<V> list = list(key);
        V result = get0(list);

        if (result == null) {
            result = constructor.create();
            set0(list, result);
        }

        return result;
    }

    private void set0(List<V> list, V value) {
        int l = scopeLevel + 1;
        int size = list.size();
        if (size < l)
            list.addAll(Collections.<V> nCopies(l - size, null));
        list.set(scopeLevel, value);
    }

    private List<V> list(K key) {
        List<V> list = stack().get(key);

        if (list == null) {
            stack().put(key, list = new ArrayList<V>());
        }

        return list;
    }

    final boolean inScope() {
        return scopeLevel > -1;
    }

    final void scopeStart() {
        scopeLevel++;
    }

    final void scopeEnd() {
        scopeLevel--;
        trim();
    }

    /**
     * Create a new value
     */

    @FunctionalInterface

    interface Constructor<V> {
        V create();
    }
}
