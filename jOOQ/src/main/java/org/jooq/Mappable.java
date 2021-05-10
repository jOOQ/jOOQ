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

import org.jooq.impl.DefaultRecordMapper;

/**
 * A type producing records that can be mapped.
 *
 * @author Lukas Eder
 */
public interface Mappable<R extends Record> {

    /**
     * Create a record mapper that extracts a value by field index.
     */
    RecordMapper<R, ?> mapper(int fieldIndex);

    /**
     * Create a record mapper that extracts a value by field name.
     */
    RecordMapper<R, ?> mapper(String fieldName);

    /**
     * Create a record mapper that extracts a value by field name.
     */
    RecordMapper<R, ?> mapper(Name fieldName);

    /**
     * Create a record mapper that extracts a value by field reference.
     */
    <T> RecordMapper<R, T> mapper(Field<T> field);

    /**
     * Create a record mapper that maps records to a new
     * {@link RecordQualifier#getRecordType()}.
     */
    <S extends Record> RecordMapper<R, S> mapper(Table<S> table);

    /**
     * Create a record mapper that maps records to a {@link Class} using the
     * configured {@link Configuration#recordMapperProvider()} (the
     * {@link DefaultRecordMapper}, by default).
     */
    <E> RecordMapper<R, E> mapper(Configuration configuration, Class<? extends E> type);

}
