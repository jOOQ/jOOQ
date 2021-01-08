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
import java.util.function.Function;
import java.util.function.IntFunction;

import static java.util.Collections.nCopies;

/**
 * A stack to register elements that are visible to a certain scope.
 *
 * @author Lukas Eder
 */
final class ScopeStack<K, V> implements Iterable<V> {

    private int                  scopeLevel = -1;
    private Map<K, List<V>>      stack;
    private final IntFunction<V> constructor;

    ScopeStack(V defaultValue) {
        this(scopeLevel -> defaultValue);
    }

    ScopeStack(IntFunction<V> constructor) {
        this.constructor = constructor;
    }

    private final Map<K, List<V>> stack() {
        if (stack == null)
            stack = new LinkedHashMap<>();

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

    final boolean isEmpty() {
        return !iterator().hasNext();
    }

    final Iterable<Value<V>> valueIterable() {
        return () -> new ScopeStackIterator<Value<V>>(Value::lastOf);
    }

    @Override
    public final Iterator<V> iterator() {
        return new ScopeStackIterator<>(list -> list.get(list.size() - 1));
    }

    static final class Value<V> {
        final int scopeLevel;
        final V   value;

        Value(int scopeLevel, V value) {
            this.scopeLevel = scopeLevel;
            this.value = value;
        }

        static <V> Value<V> of(int scopeLevel, V value) {
            return value == null ? null : new Value<>(scopeLevel, value);
        }

        static <V> Value<V> lastOf(List<V> list) {
            int size = list.size();
            V value = list.get(size - 1);
            return of(size - 1, value);
        }
    }

    private final class ScopeStackIterator<U> implements Iterator<U> {
        final Iterator<List<V>>    it = stack().values().iterator();
        final Function<List<V>, U> valueExtractor;
        U                          next;

        ScopeStackIterator(Function<List<V>, U> valueExtractor) {
            this.valueExtractor = valueExtractor;
        }

        @Override
        public boolean hasNext() {
            return move() != null;
        }

        @Override
        public U next() {
            if (next == null) {
                return move();
            }
            else {
                U result = next;
                next = null;
                return result;
            }
        }

        private U move() {
            for (List<V> list; it.hasNext() && ((list = it.next()).isEmpty() || ((next = valueExtractor.apply(list)) == null)););
            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }

    final void setAll(V value) {
        for (K key : stack().keySet())
            set(key, value);
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
        return result != null ? result : create0(list);
    }

    final V create(K key) {
        return create0(list(key));
    }

    private final V create0(List<V> list) {
        V result = constructor.apply(scopeLevel);
        set0(list, result);
        return result;
    }

    private void set0(List<V> list, V value) {
        int l = scopeLevel + 1;
        int size = list.size();
        if (size < l)
            list.addAll(nCopies(l - size, null));
        list.set(scopeLevel, value);
    }

    private List<V> list(K key) {
        return stack().computeIfAbsent(key, k -> new ArrayList<>());
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

    @Override
    public String toString() {
        return stack().toString();
    }
}
