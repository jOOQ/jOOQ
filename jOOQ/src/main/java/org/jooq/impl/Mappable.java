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

import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordQualifier;
import org.jooq.Table;

import org.jetbrains.annotations.NotNull;

/**
 * A type producing records that can be mapped.
 *
 * @author Lukas Eder
 */
interface Mappable<R extends Record> {

    /**
     * Create a record mapper that extracts a value by field index.
     */
    @NotNull
    RecordMapper<R, ?> mapper(int fieldIndex);

    /**
     * Create a record mapper that extracts a value by field index and converts
     * it using the {@link Configuration#converterProvider()}.
     */
    @NotNull
    <U> RecordMapper<R, U> mapper(int fieldIndex, Configuration configuration, Class<? extends U> type);

    /**
     * Create a record mapper that extracts a value by field index and converts
     * it using a {@link Converter}.
     */
    @NotNull
    <U> RecordMapper<R, U> mapper(int fieldIndex, Converter<?, ? extends U> converter);

    /**
     * Create a record mapper that extracts values by field index.
     */
    @NotNull
    RecordMapper<R, Record> mapper(int[] fieldIndexes);

    /**
     * Create a record mapper that extracts a value by field name.
     */
    @NotNull
    RecordMapper<R, ?> mapper(String fieldName);

    /**
     * Create a record mapper that extracts a value by field name and converts
     * it using the {@link Configuration#converterProvider()}.
     */
    @NotNull
    <U> RecordMapper<R, U> mapper(String fieldName, Configuration configuration, Class<? extends U> type);

    /**
     * Create a record mapper that extracts a value by field name and converts
     * it using a {@link Converter}.
     */
    @NotNull
    <U> RecordMapper<R, U> mapper(String fieldName, Converter<?, ? extends U> converter);

    /**
     * Create a record mapper that extracts values by field name.
     */
    @NotNull
    RecordMapper<R, Record> mapper(String[] fieldNames);

    /**
     * Create a record mapper that extracts a value by field name.
     */
    @NotNull
    RecordMapper<R, ?> mapper(Name fieldName);

    /**
     * Create a record mapper that extracts a value by field name and converts
     * it using the {@link Configuration#converterProvider()}.
     */
    @NotNull
    <U> RecordMapper<R, U> mapper(Name fieldName, Configuration configuration, Class<? extends U> type);

    /**
     * Create a record mapper that extracts a value by field name and converts
     * it using a {@link Converter}.
     */
    @NotNull
    <U> RecordMapper<R, U> mapper(Name fieldName, Converter<?, ? extends U> converter);

    /**
     * Create a record mapper that extracts values by field name.
     */
    @NotNull
    RecordMapper<R, Record> mapper(Name[] fieldNames);

    /**
     * Create a record mapper that extracts a value by field reference.
     */
    @NotNull
    <T> RecordMapper<R, T> mapper(Field<T> field);

    /**
     * Create a record mapper that extracts a value by field reference and converts
     * it using the {@link Configuration#converterProvider()}.
     */
    @NotNull
    <U> RecordMapper<R, U> mapper(Field<?> field, Configuration configuration, Class<? extends U> type);

    /**
     * Create a record mapper that extracts a value by field reference and
     * converts it using a {@link Converter}.
     */
    @NotNull
    <T, U> RecordMapper<R, U> mapper(Field<T> field, Converter<? super T, ? extends U> converter);

    /**
     * Create a record mapper that extracts values by field reference.
     */
    @NotNull
    RecordMapper<R, Record> mapper(Field<?>[] fields);

    /**
     * Create a record mapper that maps records to a new
     * {@link RecordQualifier#getRecordType()}.
     */
    @NotNull
    <S extends Record> RecordMapper<R, S> mapper(Table<S> table);

    /**
     * Create a record mapper that maps records to a {@link Class} using the
     * configured {@link Configuration#recordMapperProvider()} (the
     * {@link DefaultRecordMapper}, by default).
     */
    @NotNull
    <E> RecordMapper<R, E> mapper(Configuration configuration, Class<? extends E> type);

}
