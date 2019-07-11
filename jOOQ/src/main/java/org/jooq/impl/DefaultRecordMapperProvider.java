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

import static java.lang.Boolean.TRUE;
import static org.jooq.impl.Tools.DataCacheKey.DATA_CACHE_RECORD_MAPPERS;

import java.io.Serializable;

import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.impl.Tools.Cache;
import org.jooq.impl.Tools.Cache.CachedOperation;

/**
 * A default {@link RecordMapperProvider} implementation, providing a
 * {@link DefaultRecordMapper} instance.
 *
 * @author Lukas Eder
 */
public class DefaultRecordMapperProvider implements RecordMapperProvider, Serializable {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -5333521849740568028L;

    private final Configuration configuration;

    public DefaultRecordMapperProvider() {
        this(null);
    }

    /**
     * Create a new {@link RecordMapperProvider} with a {@link Configuration}
     * that can be used by jOOQ for caching reflection information.
     */
    protected DefaultRecordMapperProvider(Configuration configuration) {
        // The configuration parameter may not yet be fully initialised at this point!
        this.configuration = configuration;
    }

    @Override
    public final <R extends Record, E> RecordMapper<R, E> provide(final RecordType<R> rowType, final Class<? extends E> type) {
        if (TRUE.equals(configuration.settings().isCacheRecordMappers()))
            return Cache.run(configuration, new CachedOperation<RecordMapper<R, E>>() {
                @Override
                public RecordMapper<R, E> call() {
                    return new DefaultRecordMapper<>(rowType, type, configuration);
                }
            }, DATA_CACHE_RECORD_MAPPERS, Cache.key(rowType, type));
        else
            return new DefaultRecordMapper<>(rowType, type, configuration);
    }
}
