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
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A {@link ConcurrentMap} that wraps another {@link Map} making it thread safe.
 * <p>
 * Unlike ordinary {@link Map} implementations, this map will not throw any
 * {@link ConcurrentModificationException}, but rather risks deadlocking if a
 * thread modifies the map while traversing it.
 */
final class ConcurrentReadWriteMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {

    private final Map<K, V>     map;
    private final ReadWriteLock rw;
    private final Lock          read;
    private final Lock          write;

    ConcurrentReadWriteMap(Map<K, V> map) {
        this.map = map;
        this.rw = new ReentrantReadWriteLock();
        this.read = rw.readLock();
        this.write = rw.writeLock();
    }

    // -------------------------------------------------------------------------
    // XXX: Read operations
    // -------------------------------------------------------------------------

    @Override
    public int size() {
        read.lock();

        try {
            return map.size();
        }
        finally {
            read.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        read.lock();

        try {
            return map.isEmpty();
        }
        finally {
            read.unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        read.lock();

        try {
            return map.containsKey(key);
        }
        finally {
            read.unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        read.lock();

        try {
            return map.containsValue(value);
        }
        finally {
            read.unlock();
        }
    }

    @Override
    public V get(Object key) {
        read.lock();

        try {
            return map.get(key);
        }
        finally {
            read.unlock();
        }
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        read.lock();

        try {
            return map.getOrDefault(key, defaultValue);
        }
        finally {
            read.unlock();
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        read.lock();

        try {
            map.forEach(action);
        }
        finally {
            read.unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        read.lock();

        try {
            return map.equals(o);
        }
        finally {
            read.unlock();
        }
    }

    @Override
    public int hashCode() {
        read.lock();

        try {
            return map.hashCode();
        }
        finally {
            read.unlock();
        }
    }

    @Override
    public String toString() {
        read.lock();

        try {
            return map.toString();
        }
        finally {
            read.unlock();
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Write operations
    // -------------------------------------------------------------------------

    // [#18987] TODO: A lot of these write operations could be further optimised
    // in terms of lock acquisition, if we assume that writes are rare and reads
    // are very frequent. For example, putIfAbsent() may make the assumption
    // that the "absent" event is very rare, and thus rely on the
    // Map::putIfAbsent default implementation that just calls get() and put()
    // in succession, rather than pessimistically locking on the write lock.
    // We can still do this when the need arises.

    @Override
    public V put(K key, V value) {
        write.lock();

        try {
            return map.put(key, value);
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        write.lock();

        try {
            return map.remove(key);
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        write.lock();

        try {
            map.putAll(m);
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public void clear() {
        write.lock();

        try {
            map.clear();
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        write.lock();

        try {
            return map.remove(key, value);
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        write.lock();

        try {
            return map.replace(key, oldValue, newValue);
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        write.lock();

        try {
            map.replaceAll(function);
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        write.lock();

        try {
            return map.putIfAbsent(key, value);
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public V replace(K key, V value) {
        write.lock();

        try {
            return map.replace(key, value);
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        write.lock();

        try {
            return map.computeIfAbsent(key, mappingFunction);
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        write.lock();

        try {
            return map.computeIfPresent(key, remappingFunction);
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        write.lock();

        try {
            return map.compute(key, remappingFunction);
        }
        finally {
            write.unlock();
        }
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        write.lock();

        try {
            return map.merge(key, value, remappingFunction);
        }
        finally {
            write.unlock();
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Views
    // -------------------------------------------------------------------------

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> s = map.entrySet();

        return new AbstractSet<Entry<K, V>>() {
            @Override
            public int size() {
                return ConcurrentReadWriteMap.this.size();
            }

            @Override
            public Iterator<Entry<K, V>> iterator() {
                Iterator<Entry<K, V>> it = s.iterator();

                return new Iterator<Entry<K, V>>() {
                    @Override
                    public boolean hasNext() {
                        read.lock();

                        try {
                            return it.hasNext();
                        }
                        finally {
                            read.unlock();
                        }
                    }

                    @Override
                    public Entry<K, V> next() {
                        read.lock();

                        try {
                            return it.next();
                        }
                        finally {
                            read.unlock();
                        }
                    }
                };
            }
        };
    }
}
