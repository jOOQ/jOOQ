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

import static java.lang.Boolean.TRUE;
import static org.jooq.impl.CacheType.CACHE_RECORD_MAPPERS;

import java.io.Serializable;

import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;

/**
 * A default {@link RecordMapperProvider} implementation, providing a
 * {@link DefaultRecordMapper} instance.
 *
 * @author Lukas Eder
 */
public class DefaultRecordMapperProvider implements RecordMapperProvider, Serializable {

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
        if (configuration != null && TRUE.equals(configuration.settings().isCacheRecordMappers()))
            return Cache.run(configuration, () -> new DefaultRecordMapper<>(rowType, type, configuration), CACHE_RECORD_MAPPERS, () -> new Key(rowType, type));
        else
            return new DefaultRecordMapper<>(rowType, type, configuration);
    }

    /**
     * The cache key of a {@link DefaultRecordMapper}.
     * <p>
     * {@link RecordType#equals(Object)} compares fields by name only, but a
     * {@link DefaultRecordMapper} depends on the fields' types as well, e.g.
     * for the converters it looks up. Row types whose fields have the same
     * names but different types must not share a mapper.
     * <p>
     * [#18935] Note: A hand-written class with Eclipse-generated
     * {@link #equals(Object)} and {@link #hashCode()} implementations
     * significantly outperforms the Java 16 record alternative in a JMH
     * benchmark.
     */
    private static final class Key implements Serializable {
        private final RecordType<?> rowType;
        private final Class<?>      type;

        Key(RecordType<?> rowType, Class<?> type) {
            this.rowType = rowType;
            this.type = type;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((rowType == null) ? 0 : rowType.hashCode());
            result = prime * result + ((type == null) ? 0 : type.hashCode());
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
            Key other = (Key) obj;
            if (type == null) {
                if (other.type != null)
                    return false;
            }
            else if (!type.equals(other.type))
                return false;
            if (rowType == null)
                return other.rowType == null;
            else if (rowType == other.rowType)
                return true;
            else if (!rowType.equals(other.rowType))
                return false;

            // Equal row types have the same number of fields
            for (int i = 0; i < rowType.size(); i++)
                if (rowType.field(i).getType() != other.rowType.field(i).getType())
                    return false;

            return true;
        }

        @Override
        public String toString() {
            return "Key [rowType=" + rowType + ", type=" + type + "]";
        }
    }
}
