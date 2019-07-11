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

import java.io.Serializable;

import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.RecordType;
import org.jooq.RecordUnmapper;
import org.jooq.RecordUnmapperProvider;

/**
 * A default {@link RecordUnmapperProvider} implementation, providing a
 * {@link DefaultRecordUnmapper} instance.
 *
 * @author Lukas Eder
 */
public class DefaultRecordUnmapperProvider implements RecordUnmapperProvider, Serializable {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -5333521849740568028L;

    private final Configuration configuration;

    public DefaultRecordUnmapperProvider() {
        this(null);
    }

    /**
     * Create a new {@link RecordUnmapperProvider} with a {@link Configuration}
     * that can be used by jOOQ for caching reflection information.
     */
    protected DefaultRecordUnmapperProvider(Configuration configuration) {
        // The configuration parameter may not yet be fully initialised at this point!
        this.configuration = configuration;
    }

    @Override
    public final <E, R extends Record> RecordUnmapper<E, R> provide(Class<? extends E> type, RecordType<R> rowType) {
        return new DefaultRecordUnmapper<>(type, rowType, configuration);
    }
}
