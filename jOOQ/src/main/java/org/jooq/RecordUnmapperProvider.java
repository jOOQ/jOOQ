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
package org.jooq;

import org.jooq.impl.DefaultRecordUnmapper;
import org.jooq.impl.DefaultRecordUnmapperProvider;

/**
 * A provider for {@link RecordUnmapper} instances.
 * <p>
 * In order to inject custom <code>POJO</code> to {@link Record} mapping
 * behaviour, users can supply a custom {@link RecordUnmapperProvider} to their
 * {@link Configuration} instances. This provider will be used in any of these
 * methods (non-exhaustive list):
 * <h3><code>Record</code></h3>
 * <ul>
 * <li>{@link Record#from(Object)}</li>
 * <li>{@link Record#from(Object, Field...)}</li>
 * <li>{@link Record#from(Object, int...)}</li>
 * <li>{@link Record#from(Object, Name...)}</li>
 * <li>{@link Record#from(Object, String...)}</li>
 * </ul>
 * <h3><code>DSLContext</code></h3>
 * <ul>
 * <li>{@link DSLContext#newRecord(Table, Object)}</li>
 * </ul>
 * <p>
 * While not strictly required, it is advisable to implement a
 * <code>RecordUnmapperProvider</code> whose behaviour is consistent with the
 * configured {@link RecordMapperProvider}.
 *
 * @author Lukas Eder
 * @see RecordUnmapper
 * @see Configuration
 */

@FunctionalInterface

public interface RecordUnmapperProvider {

    /**
     * Provide a <code>RecordUnmapper</code> instance.
     * <p>
     * Implementations are free to choose whether this method returns new
     * instances at every call or whether the same instance is returned
     * repetitively.
     *
     * @param type The user type that was passed into
     *            {@link Record#from(Object)} or any other method.
     * @param recordType The <code>RecordType</code> of records that shall be
     *            mapped by the returned <code>RecordUnmapper</code>.
     * @return A <code>RecordUnmapper</code> instance.
     * @see RecordUnmapper
     * @see DefaultRecordUnmapper
     * @see DefaultRecordUnmapperProvider
     */
    <E, R extends Record> RecordUnmapper<E, R> provide(Class<? extends E> type, RecordType<R> recordType);
}
