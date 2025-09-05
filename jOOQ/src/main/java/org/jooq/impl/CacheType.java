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


import static org.jooq.impl.CacheType.CacheCategory.PARSING_CONNECTION;
import static org.jooq.impl.CacheType.CacheCategory.RECORD_MAPPER;
import static org.jooq.impl.CacheType.CacheCategory.REFLECTION;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

import org.jooq.CacheProvider;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.RecordMapper;
import org.jooq.RecordType;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;

/**
 * The set of internal cache types.
 * <p>
 * These are all the cache types that jOOQ internally uses for caching various
 * things in a {@link Configuration}, including e.g. reflection lookups,
 * mappers, SQL translations, etc.
 * <p>
 * The actual cache implementations are provided by {@link CacheProvider} and
 * can be overridden.
 * <p>
 * See [#2965] [#8334]
 *
 * @author Lukas Eder
 */
public enum CacheType {

    /**
     * A reflection cache for lookups of JPA annotated getters in
     * {@link DefaultRecordMapper}.
     */
    REFLECTION_CACHE_GET_ANNOTATED_GETTER(REFLECTION, "org.jooq.configuration.reflection-cache.get-annotated-getter", false),

    /**
     * A reflection cache for lookups of JPA annotated members in
     * {@link DefaultRecordMapper}.
     */
    REFLECTION_CACHE_GET_ANNOTATED_MEMBERS(REFLECTION, "org.jooq.configuration.reflection-cache.get-annotated-members", false),

    /**
     * A reflection cache for lookups of JPA annotated setters in
     * {@link DefaultRecordMapper}.
     */
    REFLECTION_CACHE_GET_ANNOTATED_SETTERS(REFLECTION, "org.jooq.configuration.reflection-cache.get-annotated-setters", false),

    /**
     * A reflection cache for lookups of getters matched by name in
     * {@link DefaultRecordMapper}.
     */
    REFLECTION_CACHE_GET_MATCHING_GETTER(REFLECTION, "org.jooq.configuration.reflection-cache.get-matching-getter", false),

    /**
     * A reflection cache for lookups of members matched by name in
     * {@link DefaultRecordMapper}.
     */
    REFLECTION_CACHE_GET_MATCHING_MEMBERS(REFLECTION, "org.jooq.configuration.reflection-cache.get-matching-members", false),

    /**
     * A reflection cache for lookups of setters matched by name in
     * {@link DefaultRecordMapper}.
     */
    REFLECTION_CACHE_GET_MATCHING_SETTERS(REFLECTION, "org.jooq.configuration.reflection-cache.get-matching-setters", false),

    /**
     * A reflection cache to check if a type has any JPA annotations at all, in
     * {@link DefaultRecordMapper}.
     */
    REFLECTION_CACHE_HAS_COLUMN_ANNOTATIONS(REFLECTION, "org.jooq.configuration.reflection-cache.has-column-annotations", false),

    /**
     * A cache used by the {@link DefaultRecordMapperProvider} to cache all
     * {@link RecordMapper} instances and their possibly expensive
     * initialisations per {@link RecordType} and {@link Class} pairs.
     */
    CACHE_RECORD_MAPPERS(RECORD_MAPPER, "org.jooq.configuration.cache.record-mappers", true),

    /**
     * [#8334] A cache for SQL to SQL translations in the
     * {@link DSLContext#parsingConnection()}, to speed up its usage.
     */
    CACHE_PARSING_CONNECTION(PARSING_CONNECTION, "org.jooq.configuration.cache.parsing-connection", false);

    /**
     * The category of the cache, indicating the logic that enables / disables
     * it.
     */
    final CacheCategory category;

    /**
     * The cache key in the backing {@link ConcurrentMap}.
     */
    final String        key;

    /**
     * Whether the backing {@link ConcurrentMap} should be lazy copied in the
     * scope of an {@link ExecuteContext} to reduce contention.
     */
    final boolean       lazyCopy;

    CacheType(CacheCategory category, String key, boolean lazyCopy) {
        this.category = category;
        this.key = key;
        this.lazyCopy = lazyCopy;
    }

    enum CacheCategory {
        REFLECTION(SettingsTools::reflectionCaching),
        RECORD_MAPPER(SettingsTools::recordMapperCaching),
        PARSING_CONNECTION(SettingsTools::parsingConnectionCaching);

        final Predicate<? super Settings> predicate;

        CacheCategory(Predicate<? super Settings> predicate) {
            this.predicate = predicate;
        }
    }
}
