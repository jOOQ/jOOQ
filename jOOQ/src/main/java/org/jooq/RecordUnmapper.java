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
 */
package org.jooq;

import org.jooq.exception.MappingException;

/**
 * A <code>RecordUnmapper</code> is a mapper that can receive user objects and
 * convert them back to {@link Record}.
 * <p>
 * <code>RecordUnmapper</code> is used behind the scenes in methods like
 * {@link DSLContext#newRecord(Table, Object)}, {@link Record#from(Object)} and
 * other methods called <code>from(Object)</code>, where the argument object is
 * an object of type <code>E</code>.
 * <p>
 * The default <code>RecordUnmapper</code> behaviour in the context of a
 * {@link Configuration} can be overridden through that
 * <code>configuration</code>'s {@link Configuration#recordUnmapperProvider()}
 * SPI.
 * <p>
 * The inverse operation is modelled by {@link RecordMapper}.
 *
 * @author Lukas Eder
 */

@FunctionalInterface

public interface RecordUnmapper<E, R extends Record> {

    /**
     * Unmap a POJO into a record.
     *
     * @param source The source object to copy data from. This is never null.
     * @throws MappingException wrapping any reflection exception that might
     *             have occurred while mapping records.
     */
    R unmap(E source) throws MappingException;
}
