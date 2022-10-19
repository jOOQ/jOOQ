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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
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

import static org.jooq.tools.StringUtils.defaultIfNull;

import java.io.Serializable;
import java.util.Map;
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
     */
    private static record Key2(Object key1, Object key2) implements Serializable {}
}