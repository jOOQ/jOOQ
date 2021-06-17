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

import static org.jooq.Records.intoList;
import static org.jooq.Records.intoResultGroups;
import static org.jooq.impl.Tools.indexOrFail;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;

import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Record11;
import org.jooq.Record12;
import org.jooq.Record13;
import org.jooq.Record14;
import org.jooq.Record15;
import org.jooq.Record16;
import org.jooq.Record17;
import org.jooq.Record18;
import org.jooq.Record19;
import org.jooq.Record2;
import org.jooq.Record20;
import org.jooq.Record21;
import org.jooq.Record22;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Record9;
import org.jooq.RecordHandler;
import org.jooq.RecordMapper;
import org.jooq.Records;
import org.jooq.Result;
import org.jooq.TXTFormat;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.InvalidResultException;
import org.jooq.tools.jdbc.MockResultSet;

/**
 * @author Lukas Eder
 * @author Ivan Dugic
 */
@SuppressWarnings({"rawtypes", "unchecked" })
final class ResultImpl<R extends Record> extends AbstractResult<R> implements Result<R> {

    private final List<R> records;

    ResultImpl(Configuration configuration, Collection<? extends Field<?>> fields) {
        this(configuration, Tools.row0(fields));
    }

    ResultImpl(Configuration configuration, Field<?>... fields) {
        this(configuration, Tools.row0(fields));
    }

    ResultImpl(Configuration configuration, AbstractRow fields) {
        super(configuration, fields);

        this.records = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // XXX: Attachable API
    // -------------------------------------------------------------------------

    @Override
    public final void attach(Configuration c) {
        this.configuration = c;

        for (R record : records)
            if (record != null)
                record.attach(c);
    }

    @Override
    public final void detach() {
        attach(null);
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    // -------------------------------------------------------------------------
    // XXX: Result API
    // -------------------------------------------------------------------------

    @Override
    public final <X, A> X collect(Collector<? super R, A, X> collector) {
        return stream().collect(collector);
    }

    @Override
    public final boolean isEmpty() {
        return records.isEmpty();
    }

    @Override
    public final boolean isNotEmpty() {
        return !records.isEmpty();
    }

    @Override
    public final <T> T getValue(int index, Field<T> field) {
        return get(index).get(field);
    }

    @Override
    public final Object getValue(int index, int fieldIndex) {
        return get(index).get(fieldIndex);
    }

    @Override
    public final Object getValue(int index, String fieldName) {
        return get(index).get(fieldName);
    }

    @Override
    public final <T> List<T> getValues(Field<T> field) {
        return collect(intoList(recordType().mapper(field)));
    }

    @Override
    public final <U> List<U> getValues(Field<?> field, Class<? extends U> type) {
        return collect(intoList(recordType().mapper(field, Tools.configuration(this), type)));
    }

    @Override
    public final <T, U> List<U> getValues(Field<T> field, Converter<? super T, ? extends U> converter) {
        return collect(intoList(recordType().mapper(field, converter)));
    }

    @Override
    public final List<?> getValues(int fieldIndex) {
        return collect(intoList(recordType().mapper(fieldIndex)));
    }

    @Override
    public final <U> List<U> getValues(int fieldIndex, Class<? extends U> type) {
        return collect(intoList(recordType().mapper(fieldIndex, Tools.configuration(this), type)));
    }

    @Override
    public final <U> List<U> getValues(int fieldIndex, Converter<?, ? extends U> converter) {
        return collect(intoList(recordType().mapper(fieldIndex, converter)));
    }

    @Override
    public final List<?> getValues(String fieldName) {
        return collect(intoList(recordType().mapper(fieldName)));
    }

    @Override
    public final <U> List<U> getValues(String fieldName, Class<? extends U> type) {
        return collect(intoList(recordType().mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    public final <U> List<U> getValues(String fieldName, Converter<?, ? extends U> converter) {
        return collect(intoList(recordType().mapper(fieldName, converter)));
    }

    @Override
    public final List<?> getValues(Name fieldName) {
        return collect(intoList(recordType().mapper(fieldName)));
    }

    @Override
    public final <U> List<U> getValues(Name fieldName, Class<? extends U> type) {
        return collect(intoList(recordType().mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    public final <U> List<U> getValues(Name fieldName, Converter<?, ? extends U> converter) {
        return collect(intoList(recordType().mapper(fieldName, converter)));
    }

    final void addRecord(R record) {
        records.add(record);
    }

    @Override
    public final List<Map<String, Object>> intoMaps() {
        return Tools.map(this, R::intoMap);
    }

    @Override
    public final <K> Map<K, R> intoMap(Field<K> key) {
        return collect(Records.intoMap(recordType().mapper(key)));
    }

    @Override
    public final Map<?, R> intoMap(int keyFieldIndex) {
        return collect(Records.intoMap(recordType().mapper(keyFieldIndex)));
    }

    @Override
    public final Map<?, R> intoMap(String keyFieldName) {
        return collect(Records.intoMap(recordType().mapper(keyFieldName)));
    }

    @Override
    public final Map<?, R> intoMap(Name keyFieldName) {
        return collect(Records.intoMap(recordType().mapper(keyFieldName)));
    }

    @Override
    public final <K, V> Map<K, V> intoMap(Field<K> key, Field<V> value) {
        return collect(Records.intoMap(recordType().mapper(key), recordType().mapper(value)));
    }

    @Override
    public final Map<?, ?> intoMap(int keyFieldIndex, int valueFieldIndex) {
        return collect(Records.intoMap(recordType().mapper(keyFieldIndex), recordType().mapper(valueFieldIndex)));
    }

    @Override
    public final Map<?, ?> intoMap(String keyFieldName, String valueFieldName) {
        return collect(Records.intoMap(recordType().mapper(keyFieldName), recordType().mapper(valueFieldName)));
    }

    @Override
    public final Map<?, ?> intoMap(Name keyFieldName, Name valueFieldName) {
        return collect(Records.intoMap(recordType().mapper(keyFieldName), recordType().mapper(valueFieldName)));
    }

    @Override
    public final Map<Record, R> intoMap(int[] keyFieldIndexes) {
        return collect(Records.intoMap(recordType().mapper(keyFieldIndexes)));
    }

    @Override
    public final Map<Record, R> intoMap(String[] keyFieldNames) {
        return collect(Records.intoMap(recordType().mapper(keyFieldNames)));
    }

    @Override
    public final Map<Record, R> intoMap(Name[] keyFieldNames) {
        return collect(Records.intoMap(recordType().mapper(keyFieldNames)));
    }

    @Override
    public final Map<Record, R> intoMap(Field<?>[] keys) {
        return collect(Records.intoMap(recordType().mapper(keys)));
    }

    @Override
    public final Map<Record, Record> intoMap(int[] keyFieldIndexes, int[] valueFieldIndexes) {
        return collect(Records.intoMap(recordType().mapper(keyFieldIndexes), recordType().mapper(valueFieldIndexes)));
    }

    @Override
    public final Map<Record, Record> intoMap(String[] keyFieldNames, String[] valueFieldNames) {
        return collect(Records.intoMap(recordType().mapper(keyFieldNames), recordType().mapper(valueFieldNames)));
    }

    @Override
    public final Map<Record, Record> intoMap(Name[] keyFieldNames, Name[] valueFieldNames) {
        return collect(Records.intoMap(recordType().mapper(keyFieldNames), recordType().mapper(valueFieldNames)));
    }

    @Override
    public final Map<Record, Record> intoMap(Field<?>[] keys, Field<?>[] values) {
        return collect(Records.intoMap(recordType().mapper(keys), recordType().mapper(values)));
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(int[] keyFieldIndexes, Class<? extends E> type) {
        return collect(Records.intoMap(recordType().mapper(keyFieldIndexes).andThen(Record::intoList), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(String[] keyFieldNames, Class<? extends E> type) {
        return collect(Records.intoMap(recordType().mapper(keyFieldNames).andThen(Record::intoList), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(Name[] keyFieldNames, Class<? extends E> type) {
        return collect(Records.intoMap(recordType().mapper(keyFieldNames).andThen(Record::intoList), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(Field<?>[] keys, Class<? extends E> type) {
        return intoMap(keys, recordType().mapper(Tools.configuration(this), type));
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoMap(recordType().mapper(keyFieldIndexes).andThen(Record::intoList), mapper));
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(String[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoMap(recordType().mapper(keyFieldNames).andThen(Record::intoList), mapper));
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(Name[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoMap(recordType().mapper(keyFieldNames).andThen(Record::intoList), mapper));
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoMap(recordType().mapper(keys).andThen(Record::intoList), mapper));
    }

    @Override
    public final <K> Map<K, R> intoMap(Class<? extends K> keyType) {
        return collect(Records.intoMap(recordType().mapper(Tools.configuration(this), keyType)));
    }

    @Override
    public final <K, V> Map<K, V> intoMap(Class<? extends K> keyType, Class<? extends V> valueType) {
        return collect(Records.intoMap(recordType().mapper(Tools.configuration(this), keyType), recordType().mapper(Tools.configuration(this), valueType)));
    }

    @Override
    public final <K, V> Map<K, V> intoMap(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper) {
        return collect(Records.intoMap(recordType().mapper(Tools.configuration(this), keyType), valueMapper));
    }

    @Override
    public final <K> Map<K, R> intoMap(RecordMapper<? super R, K> keyMapper) {
        return collect(Records.intoMap(keyMapper));
    }

    @Override
    public final <K, V> Map<K, V> intoMap(RecordMapper<? super R, K> keyMapper, Class<V> valueType) {
        return collect(Records.intoMap(keyMapper, recordType().mapper(Tools.configuration(this), valueType)));
    }

    @Override
    public final <K, V> Map<K, V> intoMap(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper) {
        return collect(Records.intoMap(keyMapper, valueMapper));
    }

    @Override
    public final <S extends Record> Map<S, R> intoMap(Table<S> table) {
        return collect(Records.intoMap(recordType().mapper(table)));
    }

    @Override
    public final <S extends Record, T extends Record> Map<S, T> intoMap(Table<S> keyTable, Table<T> valueTable) {
        return collect(Records.intoMap(recordType().mapper(keyTable), recordType().mapper(valueTable)));
    }

    @Override
    public final <E, S extends Record> Map<S, E> intoMap(Table<S> table, Class<? extends E> type) {
        return collect(Records.intoMap(recordType().mapper(table), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E, S extends Record> Map<S, E> intoMap(Table<S> table, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoMap(recordType().mapper(table), mapper));
    }

    @Override
    public final <E> Map<?, E> intoMap(int keyFieldIndex, Class<? extends E> type) {
        return collect(Records.intoMap(recordType().mapper(keyFieldIndex), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<?, E> intoMap(String keyFieldName, Class<? extends E> type) {
        return collect(Records.intoMap(recordType().mapper(keyFieldName), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<?, E> intoMap(Name keyFieldName, Class<? extends E> type) {
        return collect(Records.intoMap(recordType().mapper(keyFieldName), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <K, E> Map<K, E> intoMap(Field<K> key, Class<? extends E> type) {
        return collect(Records.intoMap(recordType().mapper(key), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<?, E> intoMap(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoMap(recordType().mapper(keyFieldIndex), mapper));
    }

    @Override
    public final <E> Map<?, E> intoMap(String keyFieldName, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoMap(recordType().mapper(keyFieldName), mapper));
    }

    @Override
    public final <E> Map<?, E> intoMap(Name keyFieldName, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoMap(recordType().mapper(keyFieldName), mapper));
    }

    @Override
    public final <K, E> Map<K, E> intoMap(Field<K> key, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoMap(recordType().mapper(key), mapper));
    }

    @Override
    public final <K> Map<K, Result<R>> intoGroups(Field<K> key) {
        return collect(intoResultGroups(recordType().mapper(key)));
    }

    @Override
    public final Map<?, Result<R>> intoGroups(int keyFieldIndex) {
        return collect(intoResultGroups(recordType().mapper(keyFieldIndex)));
    }

    @Override
    public final Map<?, Result<R>> intoGroups(String keyFieldName) {
        return collect(intoResultGroups(recordType().mapper(keyFieldName)));
    }

    @Override
    public final Map<?, Result<R>> intoGroups(Name keyFieldName) {
        return collect(intoResultGroups(recordType().mapper(keyFieldName)));
    }

    @Override
    public final <K, V> Map<K, List<V>> intoGroups(Field<K> key, Field<V> value) {
        return collect(Records.intoGroups(recordType().mapper(key), recordType().mapper(value)));
    }

    @Override
    public final Map<?, List<?>> intoGroups(int keyFieldIndex, int valueFieldIndex) {
        return (Map) collect(Records.intoGroups(recordType().mapper(keyFieldIndex), recordType().mapper(valueFieldIndex)));
    }

    @Override
    public final Map<?, List<?>> intoGroups(String keyFieldName, String valueFieldName) {
        return (Map) collect(Records.intoGroups(recordType().mapper(keyFieldName), recordType().mapper(valueFieldName)));
    }

    @Override
    public final Map<?, List<?>> intoGroups(Name keyFieldName, Name valueFieldName) {
        return (Map) collect(Records.intoGroups(recordType().mapper(keyFieldName), recordType().mapper(valueFieldName)));
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(int keyFieldIndex, Class<? extends E> type) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldIndex), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(String keyFieldName, Class<? extends E> type) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldName), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(Name keyFieldName, Class<? extends E> type) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldName), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <K, E> Map<K, List<E>> intoGroups(Field<K> key, Class<? extends E> type) {
        return collect(Records.intoGroups(recordType().mapper(key), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <K, E> Map<K, List<E>> intoGroups(Field<K> key, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoGroups(recordType().mapper(key), mapper));
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldIndex), mapper));
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(String keyFieldName, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldName), mapper));
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(Name keyFieldName, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldName), mapper));
    }

    @Override
    public final Map<Record, Result<R>> intoGroups(int[] keyFieldIndexes) {
        return collect(intoResultGroups(recordType().mapper(keyFieldIndexes)));
    }

    @Override
    public final Map<Record, Result<R>> intoGroups(String[] keyFieldNames) {
        return collect(intoResultGroups(recordType().mapper(keyFieldNames)));
    }

    @Override
    public final Map<Record, Result<R>> intoGroups(Name[] keyFieldNames) {
        return collect(intoResultGroups(recordType().mapper(keyFieldNames)));
    }

    @Override
    public final Map<Record, Result<R>> intoGroups(Field<?>[] keys) {
        return collect(intoResultGroups(recordType().mapper(keys)));
    }

    @Override
    public final Map<Record, Result<Record>> intoGroups(int[] keyFieldIndexes, int[] valueFieldIndexes) {
        return collect(intoResultGroups(recordType().mapper(keyFieldIndexes), recordType().mapper(valueFieldIndexes)));
    }

    @Override
    public final Map<Record, Result<Record>> intoGroups(String[] keyFieldNames, String[] valueFieldNames) {
        return collect(intoResultGroups(recordType().mapper(keyFieldNames), recordType().mapper(valueFieldNames)));
    }

    @Override
    public final Map<Record, Result<Record>> intoGroups(Name[] keyFieldNames, Name[] valueFieldNames) {
        return collect(intoResultGroups(recordType().mapper(keyFieldNames), recordType().mapper(valueFieldNames)));
    }

    @Override
    public final Map<Record, Result<Record>> intoGroups(Field<?>[] keys, Field<?>[] values) {
        return collect(intoResultGroups(recordType().mapper(keys), recordType().mapper(values)));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(int[] keyFieldIndexes, Class<? extends E> type) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldIndexes), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(String[] keyFieldNames, Class<? extends E> type) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldNames), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(Name[] keyFieldNames, Class<? extends E> type) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldNames), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(Field<?>[] keys, Class<? extends E> type) {
        return collect(Records.intoGroups(recordType().mapper(keys), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldIndexes), mapper));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(String[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldNames), mapper));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(Name[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoGroups(recordType().mapper(keyFieldNames), mapper));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoGroups(recordType().mapper(keys), mapper));
    }

    @Override
    public final <K> Map<K, Result<R>> intoGroups(Class<? extends K> keyType) {
        return collect(intoResultGroups(recordType().mapper(Tools.configuration(this), keyType)));
    }

    @Override
    public final <K, V> Map<K, List<V>> intoGroups(Class<? extends K> keyType, Class<? extends V> valueType) {
        return collect(Records.intoGroups(recordType().mapper(Tools.configuration(this), keyType), recordType().mapper(Tools.configuration(this), valueType)));
    }

    @Override
    public final <K, V> Map<K, List<V>> intoGroups(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper) {
        return collect(Records.intoGroups(recordType().mapper(Tools.configuration(this), keyType), valueMapper));
    }

    @Override
    public final <K> Map<K, Result<R>> intoGroups(RecordMapper<? super R, K> keyMapper) {
        return collect(intoResultGroups(keyMapper));
    }

    @Override
    public final <K, V> Map<K, List<V>> intoGroups(RecordMapper<? super R, K> keyMapper, Class<V> valueType) {
        return collect(Records.intoGroups(keyMapper, recordType().mapper(Tools.configuration(this), valueType)));
    }

    @Override
    public final <K, V> Map<K, List<V>> intoGroups(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper) {
        return collect(Records.intoGroups(keyMapper, valueMapper));
    }

    @Override
    public final <S extends Record> Map<S, Result<R>> intoGroups(Table<S> table) {
        return collect(intoResultGroups(recordType().mapper(table)));
    }

    @Override
    public final <S extends Record, T extends Record> Map<S, Result<T>> intoGroups(Table<S> keyTable, Table<T> valueTable) {
        return collect(intoResultGroups(recordType().mapper(keyTable), recordType().mapper(valueTable)));
    }

    @Override
    public final <E, S extends Record> Map<S, List<E>> intoGroups(Table<S> table, Class<? extends E> type) {
        return collect(Records.intoGroups(recordType().mapper(table), recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <E, S extends Record> Map<S, List<E>> intoGroups(Table<S> table, RecordMapper<? super R, E> mapper) {
        return collect(Records.intoGroups(recordType().mapper(table), mapper));
    }

    @Override
    @Deprecated
    public final Object[][] intoArray() {
        return intoArrays();
    }

    @Override
    public final Object[][] intoArrays() {
        return collect(Records.intoArray(new Object[0][], R::intoArray));
    }

    @Override
    public final Object[] intoArray(int fieldIndex) {
        return collect(Records.intoArray(field(safeIndex(fieldIndex)).getType(), recordType().mapper(fieldIndex)));
    }

    @Override
    public final <U> U[] intoArray(int fieldIndex, Class<? extends U> type) {
        return collect(Records.intoArray(type, recordType().mapper(fieldIndex, Tools.configuration(this), type)));
    }

    @Override
    public final <U> U[] intoArray(int fieldIndex, Converter<?, ? extends U> converter) {
        return collect(Records.intoArray(converter.toType(), recordType().mapper(fieldIndex, converter)));
    }

    @Override
    public final Object[] intoArray(String fieldName) {
        return collect(Records.intoArray(field(indexOrFail(this, fieldName)).getType(), recordType().mapper(fieldName)));
    }

    @Override
    public final <U> U[] intoArray(String fieldName, Class<? extends U> type) {
        return collect(Records.intoArray(type, recordType().mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    public final <U> U[] intoArray(String fieldName, Converter<?, ? extends U> converter) {
        return collect(Records.intoArray(converter.toType(), recordType().mapper(fieldName, converter)));
    }

    @Override
    public final Object[] intoArray(Name fieldName) {
        return collect(Records.intoArray(field(indexOrFail(this, fieldName)).getType(), recordType().mapper(fieldName)));
    }

    @Override
    public final <U> U[] intoArray(Name fieldName, Class<? extends U> type) {
        return collect(Records.intoArray(type, recordType().mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    public final <U> U[] intoArray(Name fieldName, Converter<?, ? extends U> converter) {
        return collect(Records.intoArray(converter.toType(), recordType().mapper(fieldName, converter)));
    }

    @Override
    public final <T> T[] intoArray(Field<T> field) {
        return collect(Records.intoArray(field.getType(), recordType().mapper(field)));
    }

    @Override
    public final <U> U[] intoArray(Field<?> field, Class<? extends U> type) {
        return collect(Records.intoArray(type, recordType().mapper(field, Tools.configuration(this), type)));
    }

    @Override
    public final <T, U> U[] intoArray(Field<T> field, Converter<? super T, ? extends U> converter) {
        return collect(Records.intoArray(converter.toType(), recordType().mapper(field, converter)));
    }

    @Override
    public final <E> Set<E> intoSet(RecordMapper<? super R, E> mapper) {
        return collect(Records.intoSet(mapper));
    }

    @Override
    public final Set<?> intoSet(int fieldIndex) {
        return collect(Records.intoSet(recordType().mapper(fieldIndex)));
    }

    @Override
    public final <U> Set<U> intoSet(int fieldIndex, Class<? extends U> type) {
        return collect(Records.intoSet(recordType().mapper(fieldIndex, Tools.configuration(this), type)));
    }

    @Override
    public final <U> Set<U> intoSet(int fieldIndex, Converter<?, ? extends U> converter) {
        return collect(Records.intoSet(recordType().mapper(fieldIndex, converter)));
    }

    @Override
    public final Set<?> intoSet(String fieldName) {
        return collect(Records.intoSet(recordType().mapper(fieldName)));
    }

    @Override
    public final <U> Set<U> intoSet(String fieldName, Class<? extends U> type) {
        return collect(Records.intoSet(recordType().mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    public final <U> Set<U> intoSet(String fieldName, Converter<?, ? extends U> converter) {
        return collect(Records.intoSet(recordType().mapper(fieldName, converter)));
    }

    @Override
    public final Set<?> intoSet(Name fieldName) {
        return collect(Records.intoSet(recordType().mapper(fieldName)));
    }

    @Override
    public final <U> Set<U> intoSet(Name fieldName, Class<? extends U> type) {
        return collect(Records.intoSet(recordType().mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    public final <U> Set<U> intoSet(Name fieldName, Converter<?, ? extends U> converter) {
        return collect(Records.intoSet(recordType().mapper(fieldName, converter)));
    }

    @Override
    public final <T> Set<T> intoSet(Field<T> field) {
        return collect(Records.intoSet(recordType().mapper(field)));
    }

    @Override
    public final <U> Set<U> intoSet(Field<?> field, Class<? extends U> type) {
        return collect(Records.intoSet(recordType().mapper(field, Tools.configuration(this), type)));
    }

    @Override
    public final <T, U> Set<U> intoSet(Field<T> field, Converter<? super T, ? extends U> converter) {
        return collect(Records.intoSet(recordType().mapper(field, converter)));
    }

    @Override
    public final Result<Record> into(Field<?>... f) {
        Result<Record> result = new ResultImpl<>(Tools.configuration(this), f);

        for (Record record : this)
            result.add(record.into(f));

        return result;
    }



    @Override
    public final <T1> Result<Record1<T1>> into(Field<T1> field1) {
        return (Result) into(new Field[] { field1 });
    }

    @Override
    public final <T1, T2> Result<Record2<T1, T2>> into(Field<T1> field1, Field<T2> field2) {
        return (Result) into(new Field[] { field1, field2 });
    }

    @Override
    public final <T1, T2, T3> Result<Record3<T1, T2, T3>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return (Result) into(new Field[] { field1, field2, field3 });
    }

    @Override
    public final <T1, T2, T3, T4> Result<Record4<T1, T2, T3, T4>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return (Result) into(new Field[] { field1, field2, field3, field4 });
    }

    @Override
    public final <T1, T2, T3, T4, T5> Result<Record5<T1, T2, T3, T4, T5>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6> Result<Record6<T1, T2, T3, T4, T5, T6>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7> Result<Record7<T1, T2, T3, T4, T5, T6, T7>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8> Result<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> Result<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Result<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Result<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Result<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Result<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Result<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Result<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Result<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Result<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Result<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Result<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Result<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Result<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Result<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return (Result) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



    @Override
    public final <E> List<E> into(Class<? extends E> type) {
        return collect(intoList(recordType().mapper(Tools.configuration(this), type)));
    }

    @Override
    public final <Z extends Record> Result<Z> into(Table<Z> table) {
        Result<Z> list = new ResultImpl<>(Tools.configuration(this), (AbstractRow) table.fieldsRow());

        for (R record : this)
            list.add(record.into(table));

        return list;
    }

    @Override
    public final <H extends RecordHandler<? super R>> H into(H handler) {
        forEach(handler);
        return handler;
    }

    @Override
    public final ResultSet intoResultSet() {
        return new MockResultSet(this);
    }

    @Override
    public final <E> List<E> map(RecordMapper<? super R, E> mapper) {
        return Tools.map(this, mapper::map);
    }

    @Override
    public final <T extends Comparable<? super T>> Result<R> sortAsc(Field<T> field) {
        return sortAsc(field, new NaturalComparator<>());
    }

    @Override
    public final Result<R> sortAsc(int fieldIndex) {
        return sortAsc(fieldIndex, new NaturalComparator());
    }

    @Override
    public final Result<R> sortAsc(String fieldName) {
        return sortAsc(fieldName, new NaturalComparator());
    }

    @Override
    public final Result<R> sortAsc(Name fieldName) {
        return sortAsc(fieldName, new NaturalComparator());
    }

    @Override
    public final <T> Result<R> sortAsc(Field<T> field, Comparator<? super T> comparator) {
        return sortAsc(indexOrFail(fieldsRow(), field), comparator);
    }

    @Override
    public final Result<R> sortAsc(int fieldIndex, Comparator<?> comparator) {
        return sortAsc(new RecordComparator(fieldIndex, comparator));
    }

    @Override
    public final Result<R> sortAsc(String fieldName, Comparator<?> comparator) {
        return sortAsc(indexOrFail(fieldsRow(), fieldName), comparator);
    }

    @Override
    public final Result<R> sortAsc(Name fieldName, Comparator<?> comparator) {
        return sortAsc(indexOrFail(fieldsRow(), fieldName), comparator);
    }

    @Override
    public final Result<R> sortAsc(Comparator<? super R> comparator) {
        sort(comparator);
        return this;
    }

    @Override
    public final <T extends Comparable<? super T>> Result<R> sortDesc(Field<T> field) {
        return sortAsc(field, Collections.reverseOrder(new NaturalComparator<T>()));
    }

    @Override
    public final Result<R> sortDesc(int fieldIndex) {
        return sortAsc(fieldIndex, Collections.reverseOrder(new NaturalComparator()));
    }

    @Override
    public final Result<R> sortDesc(String fieldName) {
        return sortAsc(fieldName, Collections.reverseOrder(new NaturalComparator()));
    }

    @Override
    public final Result<R> sortDesc(Name fieldName) {
        return sortAsc(fieldName, Collections.reverseOrder(new NaturalComparator()));
    }

    @Override
    public final <T> Result<R> sortDesc(Field<T> field, Comparator<? super T> comparator) {
        return sortAsc(field, Collections.reverseOrder(comparator));
    }

    @Override
    public final Result<R> sortDesc(int fieldIndex, Comparator<?> comparator) {
        return sortAsc(fieldIndex, Collections.reverseOrder(comparator));
    }

    @Override
    public final Result<R> sortDesc(String fieldName, Comparator<?> comparator) {
        return sortAsc(fieldName, Collections.reverseOrder(comparator));
    }

    @Override
    public final Result<R> sortDesc(Name fieldName, Comparator<?> comparator) {
        return sortAsc(fieldName, Collections.reverseOrder(comparator));
    }

    @Override
    public final Result<R> sortDesc(Comparator<? super R> comparator) {
        return sortAsc(Collections.reverseOrder(comparator));
    }

    @Override
    public final Result<R> intern(Field<?>... f) {
        return intern(fields.fields.indexesOf(f));
    }

    @Override
    public final Result<R> intern(int... fieldIndexes) {
        for (int fieldIndex : fieldIndexes)
            if (fields.field(fieldIndex).getType() == String.class)
                for (Record record : this)
                    ((AbstractRecord) record).intern0(fieldIndex);

        return this;
    }

    @Override
    public final Result<R> intern(String... fieldNames) {
        return intern(fields.fields.indexesOf(fieldNames));
    }

    @Override
    public final Result<R> intern(Name... fieldNames) {
        return intern(fields.fields.indexesOf(fieldNames));
    }

    /**
     * A comparator for records, wrapping another comparator for &lt;T&gt;
     */
    private static class RecordComparator<T, R extends Record> implements Comparator<R> {

        private final Comparator<? super T> comparator;
        private final int fieldIndex;

        RecordComparator(int fieldIndex, Comparator<? super T> comparator) {
            this.fieldIndex = fieldIndex;
            this.comparator = comparator;
        }

        @Override
        public final int compare(R record1, R record2) {
            return comparator.compare((T) record1.get(fieldIndex), (T) record2.get(fieldIndex));
        }
    }

    /**
     * A natural comparator
     */
    private static class NaturalComparator<T extends Comparable<? super T>> implements Comparator<T> {

        @Override
        public final int compare(T o1, T o2) {
            if (o1 == null && o2 == null)
                return 0;
            else if (o1 == null)
                return -1;
            else if (o2 == null)
                return 1;
            else
                return o1.compareTo(o2);
        }
    }

    private final int safeIndex(int index) {
        if (index >= 0 && index < fields.size())
            return index;

        throw new IllegalArgumentException("No field at index " + index + " in Record type " + fields);
    }

    // -------------------------------------------------------------------------
    // XXX Fetching of parents or children
    // -------------------------------------------------------------------------

    @Override
    public final <O extends UpdatableRecord<O>> Result<O> fetchParents(ForeignKey<R, O> key) {
        return key.fetchParents(this);
    }

    @Override
    public final <O extends TableRecord<O>> Result<O> fetchChildren(ForeignKey<O, R> key) {
        return key.fetchChildren(this);
    }

    @Override
    public final <O extends UpdatableRecord<O>> Table<O> parents(ForeignKey<R, O> key) {
        return key.parents(this);
    }

    @Override
    public final <O extends TableRecord<O>> Table<O> children(ForeignKey<O, R> key) {
        return key.children(this);
    }

    // -------------------------------------------------------------------------
    // XXX Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return format(TXTFormat.DEFAULT.maxRows(50).maxColWidth(50));
    }

    @Override
    public int hashCode() {
        return records.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ResultImpl) {
            ResultImpl<R> other = (ResultImpl<R>) obj;
            return records.equals(other.records);
        }

        return false;
    }

    // -------------------------------------------------------------------------
    // XXX: List API
    // -------------------------------------------------------------------------

    @Override
    public final int size() {
        return records.size();
    }

    @Override
    public final boolean contains(Object o) {
        return records.contains(o);
    }

    @Override
    public final Object[] toArray() {
        return records.toArray();
    }

    @Override
    public final <T> T[] toArray(T[] a) {
        return records.toArray(a);
    }

    @Override
    public final boolean add(R e) {
        return records.add(e);
    }

    @Override
    public final boolean remove(Object o) {
        return records.remove(o);
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        return records.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends R> c) {
        return records.addAll(c);
    }

    @Override
    public final boolean addAll(int index, Collection<? extends R> c) {
        return records.addAll(index, c);
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        return records.removeAll(c);
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        return records.retainAll(c);
    }

    @Override
    public final void clear() {
        records.clear();
    }

    @Override
    public final R get(int index) {
        return records.get(index);
    }

    @Override
    public final R set(int index, R element) {
        return records.set(index, element);
    }

    @Override
    public final void add(int index, R element) {
        records.add(index, element);
    }

    @Override
    public final R remove(int index) {
        return records.remove(index);
    }

    @Override
    public final int indexOf(Object o) {
        return records.indexOf(o);
    }

    @Override
    public final int lastIndexOf(Object o) {
        return records.lastIndexOf(o);
    }

    @Override
    public final Iterator<R> iterator() {
        return records.iterator();
    }

    @Override
    public final ListIterator<R> listIterator() {
        return records.listIterator();
    }

    @Override
    public final ListIterator<R> listIterator(int index) {
        return records.listIterator(index);
    }

    @Override
    public final List<R> subList(int fromIndex, int toIndex) {
        return records.subList(fromIndex, toIndex);
    }
}
