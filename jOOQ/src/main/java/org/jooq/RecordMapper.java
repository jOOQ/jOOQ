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

/**
 * A <code>RecordMapper</code> is a mapper that can receive {@link Record}
 * objects, when fetching data from the database, transforming them into a
 * custom type <code>&lt;E&gt;</code>.
 * <p>
 * <code>RecordMapper</code> is used behind the scenes in methods like
 * {@link ResultQuery#fetchInto(Class)}, {@link Result#into(Class)},
 * {@link Record#into(Class)} and other methods called <code>into(Class)</code>,
 * where the argument class is a <code>Class</code> of type <code>E</code>.
 * <p>
 * The default <code>RecordMapper</code> behaviour in the context of a
 * {@link Configuration} can be overridden through that
 * <code>configuration</code>'s {@link Configuration#recordMapperProvider()}
 * SPI.
 * <p>
 * The inverse operation is modelled by {@link RecordUnmapper}.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface RecordMapper<R extends Record, E> {

    /**
     * Map a record into a POJO.
     *
     * @param record The record to be mapped. This is never null.
     */
    E map(R record);
}
