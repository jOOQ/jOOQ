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

import static org.jooq.tools.StringUtils.defaultIfNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.jooq.impl.CacheType;
import org.jooq.Configuration;

/**
 * [#2965] This is a {@link Configuration}-based cache that can cache reflection information and other things
 */
final class Cache {

    /**
     * Run a cached operation in the context of a {@link Configuration}.
     *
     * @param configuration The configuration that may cache the outcome of
     *            the cached operation.
     * @param operation The expensive operation.
     * @param type The cache type to be used.
     * @param key The cache keys.
     * @return The cached value or the outcome of the cached operation.
     */
    @SuppressWarnings("unchecked")
    static final <V> V run(Configuration configuration, Supplier<V> operation, CacheType type, Supplier<?> key) {

        // If no configuration is provided take the default configuration that loads the default Settings
        if (configuration == null)
            configuration = new DefaultConfiguration();

        // Shortcut caching when the relevant Settings flag isn't set.
        if (!type.category.predicate.test(configuration.settings()))
            return operation.get();

        Object cacheOrNull = configuration.data(type);
        if (cacheOrNull == null) {
            synchronized (type) {
                cacheOrNull = configuration.data(type);

                if (cacheOrNull == null)
                    configuration.data(type, cacheOrNull = defaultIfNull(
                        configuration.cacheProvider().provide(new DefaultCacheContext(configuration, type)),
                        NULL
                    ));
            }
        }

        if (cacheOrNull == NULL)
            return operation.get();

        // The cache is guaranteed to be thread safe by the CacheProvider
        // contract. However since we cannot use ConcurrentHashMap.computeIfAbsent()
        // recursively, we have to revert to double checked locking nonetheless.
        Map<Object, Object> cache = (Map<Object, Object>) cacheOrNull;
        Object k = key.get();
        Object v = cache.get(k);
        if (v == null) {
            synchronized (cache) {
                v = cache.get(k);

                if (v == null)
                    cache.put(k, (v = operation.get()) == null ? NULL : v);
            }
        }

        return (V) (v == NULL ? null : v);
    }

    /**
     * A <code>null</code> placeholder to be put in {@link ConcurrentHashMap}.
     */
    private static final Object NULL = new Object();

    /**
     * Create a single-value or multi-value key for caching.
     */
    static final Object key(Object key1, Object key2) {
        return new Key2(key1, key2);
    }

    /**
     * A 2-value key for caching.
     * <p>
     * [#18935] Note: A hand-written class with Eclipse-generated
     * {@link #equals(Object)} and {@link #hashCode()} implementations
     * significantly outperforms the Java 16 record alternative in a JMH
     * benchmark.
     */
    private static final class Key2 implements Serializable {
        private final Object key1;
        private final Object key2;

        Key2(Object key1, Object key2) {
            this.key1 = key1;
            this.key2 = key2;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key1 == null) ? 0 : key1.hashCode());
            result = prime * result + ((key2 == null) ? 0 : key2.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Key2 other = (Key2) obj;
            if (key1 == null) {
                if (other.key1 != null)
                    return false;
            }
            else if (!key1.equals(other.key1))
                return false;
            if (key2 == null) {
                if (other.key2 != null)
                    return false;
            }
            else if (!key2.equals(other.key2))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Key2 [key1=" + key1 + ", key2=" + key2 + "]";
        }
    }

    /**
     * Create a single-value or multi-value key for caching.
     */
    static final Object key(Object key1, Object key2, Object key3) {
        return new Key3(key1, key2, key3);
    }

    /**
     * A 3-value key for caching.
     * <p>
     * [#18935] Note: A hand-written class with Eclipse-generated
     * {@link #equals(Object)} and {@link #hashCode()} implementations
     * significantly outperforms the Java 16 record alternative in a JMH
     * benchmark.
     */
    private static final class Key3 implements Serializable {
        private final Object key1;
        private final Object key2;
        private final Object key3;

        Key3(Object key1, Object key2, Object key3) {
            this.key1 = key1;
            this.key2 = key2;
            this.key3 = key3;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key1 == null) ? 0 : key1.hashCode());
            result = prime * result + ((key2 == null) ? 0 : key2.hashCode());
            result = prime * result + ((key3 == null) ? 0 : key3.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Key3 other = (Key3) obj;
            if (key1 == null) {
                if (other.key1 != null)
                    return false;
            }
            else if (!key1.equals(other.key1))
                return false;
            if (key2 == null) {
                if (other.key2 != null)
                    return false;
            }
            else if (!key2.equals(other.key2))
                return false;
            if (key3 == null) {
                if (other.key3 != null)
                    return false;
            }
            else if (!key3.equals(other.key3))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Key3 [key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + "]";
        }
    }
}