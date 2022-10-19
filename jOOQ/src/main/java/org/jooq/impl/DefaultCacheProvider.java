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

import static java.util.Collections.synchronizedMap;
import static org.jooq.impl.Tools.settings;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jooq.CacheContext;
import org.jooq.CacheProvider;

/**
 * A default implementation producing a {@link ConcurrentHashMap} in most cases,
 * or a synchronized LRU cache where appropriate.
 *
 * @author Lukas Eder
 */
final class DefaultCacheProvider implements CacheProvider {

    @Override
    public Map<Object, Object> provide(CacheContext ctx) {
        switch (ctx.cacheType()) {

            // TODO: Is there a better implementation than wrapping LinkedHashMap
            // in synchronizedMap(), i.e. one that does not use a monitor?
            case CACHE_PARSING_CONNECTION:
                return synchronizedMap(new LRUCache<>(defaultIfNull(settings(ctx.configuration()).getCacheParsingConnectionLRUCacheSize(), 8912)));

            default:
                return new ConcurrentHashMap<>();
        }
    }
}
