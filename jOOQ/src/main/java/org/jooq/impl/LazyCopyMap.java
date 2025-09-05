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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jooq.ExecuteContext;


/**
 * A lazy copy of a backing map, typically to speed up cache access locally, for
 * example for the scope of an {@link ExecuteContext}.
 * <p>
 * The lazy copy map may not be in sync with the backing map, meaning that if
 * the backing map is an {@link LRUCache}, or a similar cache like structure
 * with eviction policies, it may be the case that an eviction happens in the
 * backing map, but not in the local copy. While methods like {@link Map#size()}
 * or {@link Map#entrySet()} will call through to the backing data structure,
 * {@link Map#get(Object)} or {@link Map#containsKey(Object)} may not, but find
 * a stale object that is no longer in the backing cache. The assumption here is
 * that in the cache use-case, the backing cache would fill up the the same
 * missing value again in case of a {@link Map#get(Object)} call, evicting
 * another entry. These caveats will not produce any issues if only "cache
 * relevant" methods like {@link Map#get(Object)} and
 * {@link Map#put(Object, Object)} are called.
 * <p>
 * The same is true if the backing map is modified for any other reason.
 * <p>
 * This map is not thread safe, although it may reference a thread safe backing
 * map. It is intended for thread local usage.
 * <p>
 * This map does not support <code>null</code> keys or values.
 *
 * @author Lukas Eder
 */
final class LazyCopyMap<K, V> extends AbstractMap<K, V> {

    final Map<K, V> copy;
    final Map<K, V> delegate;

    LazyCopyMap(Map<K, V> delegate) {
        this.copy = new HashMap<>();
        this.delegate = delegate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        if (key == null)
            throw new NullPointerException();

        V value = copy.get(key);

        if (value == null) {
            value = delegate.get(key);

            if (value != null)
                copy.put((K) key, value);
        }

        return value;
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null)
            throw new NullPointerException();

        copy.put(key, value);
        return delegate.put(key, value);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {

        // [#19003] To keep things simple, Iterator::remove isn't supported
        // [#19003] TODO: This currently doesn't copy contents, but it could
        return Collections.unmodifiableSet(delegate.entrySet());
    }

    // keySet() and valueSet() from AbstractMap call through to entrySet()

    @Override
    public V putIfAbsent(K key, V value) {
        if (key == null || value == null)
            throw new NullPointerException();

        get(key);
        copy.putIfAbsent(key, value);
        return delegate.putIfAbsent(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        if (key == null || oldValue == null || newValue == null)
            throw new NullPointerException();

        copy.replace(key, oldValue, newValue);
        return delegate.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        if (key == null || value == null)
            throw new NullPointerException();

        copy.replace(key, value);
        return delegate.replace(key, value);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        copy.replaceAll(function);
        delegate.replaceAll(function);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        if (key == null)
            throw new NullPointerException();

        get(key);
        copy.computeIfAbsent(key, mappingFunction);
        return delegate.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (key == null)
            throw new NullPointerException();

        get(key);
        copy.computeIfPresent(key, remappingFunction);
        return delegate.computeIfPresent(key, remappingFunction);
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (key == null)
            throw new NullPointerException();

        get(key);
        copy.compute(key, remappingFunction);
        return delegate.compute(key, remappingFunction);
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (key == null || value == null)
            throw new NullPointerException();

        get(key);
        copy.merge(key, value, remappingFunction);
        return delegate.merge(key, value, remappingFunction);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return copy.isEmpty()
            && delegate.isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        return copy.containsValue(value)
            || delegate.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return copy.containsKey(key)
            || delegate.containsKey(key);
    }

    @Override
    public V remove(Object key) {
        copy.remove(key);
        return delegate.remove(key);
    }

    @Override
    public boolean remove(Object key, Object value) {
        copy.remove(key, value);
        return delegate.remove(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        copy.putAll(m);
        delegate.putAll(m);
    }

    @Override
    public void clear() {
        copy.clear();
        delegate.clear();
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}

