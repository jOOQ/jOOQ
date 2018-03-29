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

import static org.jooq.impl.Tools.indexOrFail;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.DataType;
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
import org.jooq.RecordType;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.TXTFormat;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.InvalidResultException;
import org.jooq.tools.Convert;
import org.jooq.tools.jdbc.MockResultSet;

/**
 * @author Lukas Eder
 * @author Ivan Dugic
 */
@SuppressWarnings({"rawtypes", "unchecked" })
final class ResultImpl<R extends Record> extends AbstractCursor<R> implements Result<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 6416154375799578362L;

    private final List<R>     records;

    ResultImpl(Configuration configuration, Collection<? extends Field<?>> fields) {
        this(configuration, new Fields<R>(fields));
    }

    ResultImpl(Configuration configuration, Field<?>... fields) {
        this(configuration, new Fields<R>(fields));
    }

    ResultImpl(Configuration configuration, Fields<R> fields) {
        super(configuration, fields);

        this.records = new ArrayList<R>();
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
    public final RecordType<R> recordType() {
        return fields;
    }

    @Override
    public final Row fieldsRow() {
        return new RowImpl(fields);
    }

    @Override
    public final <T> Field<T> field(Field<T> field) {
        return fields.field(field);
    }

    @Override
    public final Field<?> field(String name) {
        return fields.field(name);
    }

    @Override
    public final <T> Field<T> field(String name, Class<T> type) {
        return fields.field(name, type);
    }

    @Override
    public final <T> Field<T> field(String name, DataType<T> dataType) {
        return fields.field(name, dataType);
    }

    @Override
    public final Field<?> field(Name name) {
        return fields.field(name);
    }

    @Override
    public final <T> Field<T> field(Name name, Class<T> type) {
        return fields.field(name, type);
    }

    @Override
    public final <T> Field<T> field(Name name, DataType<T> dataType) {
        return fields.field(name, dataType);
    }

    @Override
    public final Field<?> field(int index) {
        return fields.field(index);
    }

    @Override
    public final <T> Field<T> field(int index, Class<T> type) {
        return fields.field(index, type);
    }

    @Override
    public final <T> Field<T> field(int index, DataType<T> dataType) {
        return fields.field(index, dataType);
    }

    @Override
    public final Field<?>[] fields() {
        return fields.fields().clone();
    }

    @Override
    public final Field<?>[] fields(Field<?>... f) {
        return fields.fields(f);
    }

    @Override
    public final Field<?>[] fields(int... indexes) {
        return fields.fields(indexes);
    }

    @Override
    public final Field<?>[] fields(String... names) {
        return fields.fields(names);
    }

    @Override
    public final Field<?>[] fields(Name... names) {
        return fields.fields(names);
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
    @Deprecated
    public final <T> T getValue(int index, Field<T> field, T defaultValue) {
        return get(index).getValue(field, defaultValue);
    }

    @Override
    public final Object getValue(int index, int fieldIndex) {
        return get(index).get(fieldIndex);
    }

    @Override
    @Deprecated
    public final Object getValue(int index, int fieldIndex, Object defaultValue) {
        return get(index).getValue(fieldIndex, defaultValue);
    }

    @Override
    public final Object getValue(int index, String fieldName) {
        return get(index).get(fieldName);
    }

    @Override
    @Deprecated
    public final Object getValue(int index, String fieldName, Object defaultValue) {
        return get(index).getValue(fieldName, defaultValue);
    }

    @Override
    public final <T> List<T> getValues(Field<T> field) {
        return (List<T>) getValues(indexOrFail(fieldsRow(), field));
    }

    @Override
    public final <T> List<T> getValues(Field<?> field, Class<? extends T> type) {
        return Convert.convert(getValues(field), type);
    }

    @Override
    public final <T, U> List<U> getValues(Field<T> field, Converter<? super T, ? extends U> converter) {
        return Convert.convert(getValues(field), converter);
    }

    @Override
    public final List<?> getValues(int fieldIndex) {
        List<Object> result = new ArrayList<Object>(size());

        for (R record : this)
            result.add(record.get(fieldIndex));

        return result;
    }

    @Override
    public final <T> List<T> getValues(int fieldIndex, Class<? extends T> type) {
        return Convert.convert(getValues(fieldIndex), type);
    }

    @Override
    public final <U> List<U> getValues(int fieldIndex, Converter<?, ? extends U> converter) {
        return Convert.convert(getValues(fieldIndex), converter);
    }

    @Override
    public final List<?> getValues(String fieldName) {
        return getValues(field(fieldName));
    }

    @Override
    public final <T> List<T> getValues(String fieldName, Class<? extends T> type) {
        return Convert.convert(getValues(fieldName), type);
    }

    @Override
    public final <U> List<U> getValues(String fieldName, Converter<?, ? extends U> converter) {
        return Convert.convert(getValues(fieldName), converter);
    }

    @Override
    public final List<?> getValues(Name fieldName) {
        return getValues(field(fieldName));
    }

    @Override
    public final <T> List<T> getValues(Name fieldName, Class<? extends T> type) {
        return Convert.convert(getValues(fieldName), type);
    }

    @Override
    public final <U> List<U> getValues(Name fieldName, Converter<?, ? extends U> converter) {
        return Convert.convert(getValues(fieldName), converter);
    }

    final void addRecord(R record) {
        records.add(record);
    }

    @Override
    public final List<Map<String, Object>> intoMaps() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (R record : this)
            list.add(record.intoMap());

        return list;
    }

    @Override
    public final <K> Map<K, R> intoMap(Field<K> key) {
        return intoMap0(indexOrFail(fieldsRow(), key));
    }

    @Override
    public final Map<?, R> intoMap(int keyFieldIndex) {
        return intoMap0(keyFieldIndex);
    }

    @Override
    public final Map<?, R> intoMap(String keyFieldName) {
        return intoMap(field(keyFieldName));
    }

    @Override
    public final Map<?, R> intoMap(Name keyFieldName) {
        return intoMap(field(keyFieldName));
    }

    private final <K> Map<K, R> intoMap0(int keyFieldIndex) {
        Map<K, R> map = new LinkedHashMap<K, R>();

        for (R record : this)
            if (map.put((K) record.get(keyFieldIndex), record) != null)
                throw new InvalidResultException("Key " + keyFieldIndex + " is not unique in Result for " + this);

        return map;
    }

    @Override
    public final <K, V> Map<K, V> intoMap(Field<K> key, Field<V> value) {
        int kIndex = indexOrFail(fieldsRow(), key);
        int vIndex = indexOrFail(fieldsRow(), value);

        return intoMap0(kIndex, vIndex);
    }

    @Override
    public final Map<?, ?> intoMap(int keyFieldIndex, int valueFieldIndex) {
        return intoMap0(keyFieldIndex, valueFieldIndex);
    }

    @Override
    public final Map<?, ?> intoMap(String keyFieldName, String valueFieldName) {
        return intoMap(field(keyFieldName), field(valueFieldName));
    }

    @Override
    public final Map<?, ?> intoMap(Name keyFieldName, Name valueFieldName) {
        return intoMap(field(keyFieldName), field(valueFieldName));
    }

    private final <K, V> Map<K, V> intoMap0(int kIndex, int vIndex) {
        Map<K, V> map = new LinkedHashMap<K, V>();

        for (R record : this)
            if (map.put((K) record.get(kIndex), (V) record.get(vIndex)) != null)
                throw new InvalidResultException("Key " + record.get(kIndex) + " is not unique in Result for " + this);

        return map;
    }

    @Override
    public final Map<Record, R> intoMap(int[] keyFieldIndexes) {
        return intoMap(fields(keyFieldIndexes));
    }

    @Override
    public final Map<Record, R> intoMap(String[] keyFieldNames) {
        return intoMap(fields(keyFieldNames));
    }

    @Override
    public final Map<Record, R> intoMap(Name[] keyFieldNames) {
        return intoMap(fields(keyFieldNames));
    }

    @Override
    public final Map<Record, R> intoMap(Field<?>[] keys) {
        if (keys == null)
            keys = new Field[0];

        Map<Record, R> map = new LinkedHashMap<Record, R>();
        for (R record : this) {
            RecordImpl key = new RecordImpl(keys);

            for (Field<?> field : keys)
                Tools.copyValue(key, field, record, field);

            if (map.put(key, record) != null)
                throw new InvalidResultException("Key list " + Arrays.asList(keys) + " is not unique in Result for " + this);
        }

        return map;
    }

    @Override
    public final Map<Record, Record> intoMap(int[] keyFieldIndexes, int[] valueFieldIndexes) {
        return intoMap(fields(keyFieldIndexes), fields(valueFieldIndexes));
    }

    @Override
    public final Map<Record, Record> intoMap(String[] keyFieldNames, String[] valueFieldNames) {
        return intoMap(fields(keyFieldNames), fields(valueFieldNames));
    }

    @Override
    public final Map<Record, Record> intoMap(Name[] keyFieldNames, Name[] valueFieldNames) {
        return intoMap(fields(keyFieldNames), fields(valueFieldNames));
    }

    @Override
    public final Map<Record, Record> intoMap(Field<?>[] keys, Field<?>[] values) {
        if (keys == null)
            keys = new Field[0];

        Map<Record, Record> map = new LinkedHashMap<Record, Record>();
        for (R record : this) {
            RecordImpl key = new RecordImpl(keys);
            RecordImpl value = new RecordImpl(values);

            for (Field<?> field : keys)
                Tools.copyValue(key, field, record, field);

            for (Field<?> field : values)
                Tools.copyValue(value, field, record, field);

            if (map.put(key, value) != null)
                throw new InvalidResultException("Key list " + Arrays.asList(keys) + " is not unique in Result for " + this);
        }

        return map;
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(int[] keyFieldIndexes, Class<? extends E> type) {
        return intoMap(fields(keyFieldIndexes), type);
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(String[] keyFieldNames, Class<? extends E> type) {
        return intoMap(fields(keyFieldNames), type);
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(Name[] keyFieldNames, Class<? extends E> type) {
        return intoMap(fields(keyFieldNames), type);
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(Field<?>[] keys, Class<? extends E> type) {
        return intoMap(keys, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper) {
        return intoMap(fields(keyFieldIndexes), mapper);
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(String[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return intoMap(fields(keyFieldNames), mapper);
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(Name[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return intoMap(fields(keyFieldNames), mapper);
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        if (keys == null)
            keys = new Field[0];

        Map<List<?>, E> map = new LinkedHashMap<List<?>, E>();

        for (R record : this) {
            List<Object> keyValueList = new ArrayList<Object>();

            for (Field<?> key : keys)
                keyValueList.add(record.get(key));

            if (map.put(keyValueList, mapper.map(record)) != null)
                throw new InvalidResultException("Key list " + keyValueList + " is not unique in Result for " + this);
        }

        return map;
    }

    @Override
    public final <K> Map<K, R> intoMap(Class<? extends K> keyType) {
        return intoMap(Tools.configuration(this).recordMapperProvider().provide(fields, keyType));
    }

    @Override
    public final <K, V> Map<K, V> intoMap(Class<? extends K> keyType, Class<? extends V> valueType) {
        return intoMap(
            Tools.configuration(this).recordMapperProvider().provide(fields, keyType),
            Tools.configuration(this).recordMapperProvider().provide(fields, valueType)
        );
    }

    @Override
    public final <K, V> Map<K, V> intoMap(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper) {
        return intoMap(Tools.configuration(this).recordMapperProvider().provide(fields, keyType), valueMapper);
    }

    @Override
    public final <K> Map<K, R> intoMap(RecordMapper<? super R, K> keyMapper) {
        Map<K, R> map = new LinkedHashMap<K, R>();

        for (R record : this) {
            K key = keyMapper.map(record);

            if (map.put(key, record) != null)
                throw new InvalidResultException("Key list " + key + " is not unique in Result for " + this);
        }

        return map;
    }

    @Override
    public final <K, V> Map<K, V> intoMap(RecordMapper<? super R, K> keyMapper, Class<V> valueType) {
        return intoMap(keyMapper, Tools.configuration(this).recordMapperProvider().provide(fields, valueType));
    }

    @Override
    public final <K, V> Map<K, V> intoMap(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper) {
        Map<K, V> map = new LinkedHashMap<K, V>();

        for (R record : this) {
            K key = keyMapper.map(record);
            V value = valueMapper.map(record);

            if (map.put(key, value) != null)
                throw new InvalidResultException("Key list " + key + " is not unique in Result for " + this);
        }

        return map;
    }

    @Override
    public final <S extends Record> Map<S, R> intoMap(Table<S> table) {
        Map<S, R> map = new LinkedHashMap<S, R>();

        for (R record : this) {
            S key = record.into(table);

            if (map.put(key, record) != null)
                throw new InvalidResultException("Key list " + key + " is not unique in Result for " + this);
        }

        return map;
    }

    @Override
    public final <S extends Record, T extends Record> Map<S, T> intoMap(Table<S> keyTable, Table<T> valueTable) {
        Map<S, T> map = new LinkedHashMap<S, T>();

        for (R record : this) {
            S key = record.into(keyTable);
            T value = record.into(valueTable);

            if (map.put(key, value) != null)
                throw new InvalidResultException("Key list " + key + " is not unique in Result for " + this);
        }

        return map;
    }

    @Override
    public final <E, S extends Record> Map<S, E> intoMap(Table<S> table, Class<? extends E> type) {
        return intoMap(table, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E, S extends Record> Map<S, E> intoMap(Table<S> table, RecordMapper<? super R, E> mapper) {
        Map<S, E> map = new LinkedHashMap<S, E>();

        for (R record : this) {
            S key = record.into(table);

            if (map.put(key, mapper.map(record)) != null)
                throw new InvalidResultException("Key list " + key + " is not unique in Result for " + this);
        }

        return map;
    }

    @Override
    public final <E> Map<?, E> intoMap(int keyFieldIndex, Class<? extends E> type) {
        return intoMap(keyFieldIndex, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E> Map<?, E> intoMap(String keyFieldName, Class<? extends E> type) {
        return intoMap(keyFieldName, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E> Map<?, E> intoMap(Name keyFieldName, Class<? extends E> type) {
        return intoMap(keyFieldName, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <K, E> Map<K, E> intoMap(Field<K> key, Class<? extends E> type) {
        return intoMap(key, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E> Map<?, E> intoMap(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        return intoMap0(keyFieldIndex, mapper);
    }

    @Override
    public final <E> Map<?, E> intoMap(String keyFieldName, RecordMapper<? super R, E> mapper) {
        return intoMap(field(keyFieldName), mapper);
    }

    @Override
    public final <E> Map<?, E> intoMap(Name keyFieldName, RecordMapper<? super R, E> mapper) {
        return intoMap(field(keyFieldName), mapper);
    }

    @Override
    public final <K, E> Map<K, E> intoMap(Field<K> key, RecordMapper<? super R, E> mapper) {
        return intoMap0(indexOrFail(fieldsRow(), key), mapper);
    }

    private final <K, E> Map<K, E> intoMap0(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        Map<K, E> map = new LinkedHashMap<K, E>();

        for (R record : this)
            if (map.put((K) record.get(keyFieldIndex), mapper.map(record)) != null)
                throw new InvalidResultException("Key " + keyFieldIndex + " is not unique in Result for " + this);

        return map;
    }

    @Override
    public final <K> Map<K, Result<R>> intoGroups(Field<K> key) {
        return intoGroups0(indexOrFail(fieldsRow(), key));
    }

    @Override
    public final Map<?, Result<R>> intoGroups(int keyFieldIndex) {
        return intoGroups0(keyFieldIndex);
    }

    @Override
    public final Map<?, Result<R>> intoGroups(String keyFieldName) {
        return intoGroups(field(keyFieldName));
    }

    @Override
    public final Map<?, Result<R>> intoGroups(Name keyFieldName) {
        return intoGroups(field(keyFieldName));
    }

    private final <K> Map<K, Result<R>> intoGroups0(int keyFieldIndex) {
        Map<K, Result<R>> map = new LinkedHashMap<K, Result<R>>();

        for (R record : this) {
            K val = (K) record.get(keyFieldIndex);
            Result<R> result = map.get(val);

            if (result == null)
                map.put(val, result = new ResultImpl<R>(configuration, fields));

            result.add(record);
        }

        return map;
    }

    @Override
    public final <K, V> Map<K, List<V>> intoGroups(Field<K> key, Field<V> value) {
        int kIndex = indexOrFail(fieldsRow(), key);
        int vIndex = indexOrFail(fieldsRow(), value);

        return intoGroups0(kIndex, vIndex);
    }

    @Override
    public final Map<?, List<?>> intoGroups(int keyFieldIndex, int valueFieldIndex) {
        return (Map) intoGroups0(keyFieldIndex, valueFieldIndex);
    }

    @Override
    public final Map<?, List<?>> intoGroups(String keyFieldName, String valueFieldName) {
        return (Map) intoGroups(field(keyFieldName), field(valueFieldName));
    }

    @Override
    public final Map<?, List<?>> intoGroups(Name keyFieldName, Name valueFieldName) {
        return (Map) intoGroups(field(keyFieldName), field(valueFieldName));
    }

    private final <K, V> Map<K, List<V>> intoGroups0(int kIndex, int vIndex) {
        Map<K, List<V>> map = new LinkedHashMap<K, List<V>>();

        for (R record : this) {
            K k = (K) record.get(kIndex);
            V v = (V) record.get(vIndex);

            List<V> result = map.get(k);
            if (result == null)
                map.put(k, result = new ArrayList<V>());

            result.add(v);
        }

        return map;
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(int keyFieldIndex, Class<? extends E> type) {
        return intoGroups(keyFieldIndex, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(String keyFieldName, Class<? extends E> type) {
        return intoGroups(keyFieldName, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(Name keyFieldName, Class<? extends E> type) {
        return intoGroups(keyFieldName, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <K, E> Map<K, List<E>> intoGroups(Field<K> key, Class<? extends E> type) {
        return intoGroups(key, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <K, E> Map<K, List<E>> intoGroups(Field<K> key, RecordMapper<? super R, E> mapper) {
        return intoGroups0(indexOrFail(fieldsRow(), key), mapper);
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        return intoGroups0(keyFieldIndex, mapper);
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(String keyFieldName, RecordMapper<? super R, E> mapper) {
        return intoGroups(field(keyFieldName), mapper);
    }

    @Override
    public final <E> Map<?, List<E>> intoGroups(Name keyFieldName, RecordMapper<? super R, E> mapper) {
        return intoGroups(field(keyFieldName), mapper);
    }

    private final <K, E> Map<K, List<E>> intoGroups0(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        Map<K, List<E>> map = new LinkedHashMap<K, List<E>>();

        for (R record : this) {
            K keyVal = (K) record.get(keyFieldIndex);

            List<E> list = map.get(keyVal);
            if (list == null)
                map.put(keyVal, list = new ArrayList<E>());

            list.add(mapper.map(record));
        }

        return map;
    }

    @Override
    public final Map<Record, Result<R>> intoGroups(int[] keyFieldIndexes) {
        return intoGroups(fields(keyFieldIndexes));
    }

    @Override
    public final Map<Record, Result<R>> intoGroups(String[] keyFieldNames) {
        return intoGroups(fields(keyFieldNames));
    }

    @Override
    public final Map<Record, Result<R>> intoGroups(Name[] keyFieldNames) {
        return intoGroups(fields(keyFieldNames));
    }

    @Override
    public final Map<Record, Result<R>> intoGroups(Field<?>[] keys) {
        if (keys == null)
            keys = new Field[0];

        Map<Record, Result<R>> map = new LinkedHashMap<Record, Result<R>>();
        for (R record : this) {
            RecordImpl key = new RecordImpl(keys);

            for (Field<?> field : keys)
                Tools.copyValue(key, field, record, field);

            Result<R> result = map.get(key);
            if (result == null)
                map.put(key, result = new ResultImpl<R>(configuration(), this.fields));

            result.add(record);
        }

        return map;
    }

    @Override
    public final Map<Record, Result<Record>> intoGroups(int[] keyFieldIndexes, int[] valueFieldIndexes) {
        return intoGroups(fields(keyFieldIndexes), fields(valueFieldIndexes));
    }

    @Override
    public final Map<Record, Result<Record>> intoGroups(String[] keyFieldNames, String[] valueFieldNames) {
        return intoGroups(fields(keyFieldNames), fields(valueFieldNames));
    }

    @Override
    public final Map<Record, Result<Record>> intoGroups(Name[] keyFieldNames, Name[] valueFieldNames) {
        return intoGroups(fields(keyFieldNames), fields(keyFieldNames));
    }

    @Override
    public final Map<Record, Result<Record>> intoGroups(Field<?>[] keys, Field<?>[] values) {
        if (keys == null)
            keys = new Field[0];

        if (values == null)
            values = new Field[0];

        Map<Record, Result<Record>> map = new LinkedHashMap<Record, Result<Record>>();
        for (R record : this) {
            RecordImpl key = new RecordImpl(keys);
            RecordImpl value = new RecordImpl(values);

            for (Field<?> field : keys)
                Tools.copyValue(key, field, record, field);

            for (Field<?> field : values)
                Tools.copyValue(value, field, record, field);

            Result<Record> result = map.get(key);
            if (result == null)
                map.put(key, result = new ResultImpl<Record>(configuration(), values));

            result.add(value);
        }

        return map;
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(int[] keyFieldIndexes, Class<? extends E> type) {
        return intoGroups(keyFieldIndexes, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(String[] keyFieldNames, Class<? extends E> type) {
        return intoGroups(keyFieldNames, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(Name[] keyFieldNames, Class<? extends E> type) {
        return intoGroups(keyFieldNames, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(Field<?>[] keys, Class<? extends E> type) {
        return intoGroups(keys, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper) {
        return intoGroups(fields(keyFieldIndexes), mapper);
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(String[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return intoGroups(fields(keyFieldNames), mapper);
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(Name[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return intoGroups(fields(keyFieldNames), mapper);
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        if (keys == null)
            keys = new Field[0];

        Map<Record, List<E>> map = new LinkedHashMap<Record, List<E>>();
        for (R record : this) {
            RecordImpl key = new RecordImpl(keys);

            for (Field<?> field : keys)
                Tools.copyValue(key, field, record, field);

            List<E> list = map.get(key);
            if (list == null)
                map.put(key, list = new ArrayList<E>());

            list.add(mapper.map(record));
        }

        return map;
    }

    @Override
    public final <K> Map<K, Result<R>> intoGroups(Class<? extends K> keyType) {
        return intoGroups(Tools.configuration(this).recordMapperProvider().provide(fields, keyType));
    }

    @Override
    public final <K, V> Map<K, List<V>> intoGroups(Class<? extends K> keyType, Class<? extends V> valueType) {
        return intoGroups(
            Tools.configuration(this).recordMapperProvider().provide(fields, keyType),
            Tools.configuration(this).recordMapperProvider().provide(fields, valueType)
        );
    }

    @Override
    public final <K, V> Map<K, List<V>> intoGroups(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper) {
        return intoGroups(Tools.configuration(this).recordMapperProvider().provide(fields, keyType), valueMapper);
    }

    @Override
    public final <K> Map<K, Result<R>> intoGroups(RecordMapper<? super R, K> keyMapper) {
        Map<K, Result<R>> map = new LinkedHashMap<K, Result<R>>();

        for (R record : this) {
            K key = keyMapper.map(record);

            Result<R> result = map.get(key);
            if (result == null)
                map.put(key, result = new ResultImpl(configuration(), fields()));

            result.add(record);
        }

        return map;
    }

    @Override
    public final <K, V> Map<K, List<V>> intoGroups(RecordMapper<? super R, K> keyMapper, Class<V> valueType) {
        return intoGroups(keyMapper, Tools.configuration(this).recordMapperProvider().provide(fields, valueType));
    }

    @Override
    public final <K, V> Map<K, List<V>> intoGroups(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper) {
        Map<K, List<V>> map = new LinkedHashMap<K, List<V>>();

        for (R record : this) {
            K key = keyMapper.map(record);

            List<V> list = map.get(key);
            if (list == null)
                map.put(key, list = new ArrayList<V>());

            list.add(valueMapper.map(record));
        }

        return map;
    }

    @Override
    public final <S extends Record> Map<S, Result<R>> intoGroups(Table<S> table) {
        Map<S, Result<R>> map = new LinkedHashMap<S, Result<R>>();

        for (R record : this) {
            S key = record.into(table);

            Result<R> result = map.get(key);
            if (result == null)
                map.put(key, result = new ResultImpl<R>(configuration(), this.fields));

            result.add(record);
        }

        return map;
    }

    @Override
    public final <S extends Record, T extends Record> Map<S, Result<T>> intoGroups(Table<S> keyTable, Table<T> valueTable) {
        Map<S, Result<T>> map = new LinkedHashMap<S, Result<T>>();

        for (R record : this) {
            S key = record.into(keyTable);
            T value = record.into(valueTable);

            Result<T> result = map.get(key);
            if (result == null)
                map.put(key, result = DSL.using(configuration()).newResult(valueTable));

            result.add(value);
        }

        return map;
    }

    @Override
    public final <E, S extends Record> Map<S, List<E>> intoGroups(Table<S> table, Class<? extends E> type) {
        return intoGroups(table, Tools.configuration(this).recordMapperProvider().provide(fields, type));
    }

    @Override
    public final <E, S extends Record> Map<S, List<E>> intoGroups(Table<S> table, RecordMapper<? super R, E> mapper) {
        Map<S, List<E>> map = new LinkedHashMap<S, List<E>>();

        for (R record : this) {
            S key = record.into(table);

            List<E> list = map.get(key);
            if (list == null) {
                list = new ArrayList<E>();
                map.put(key, list);
            }

            list.add(mapper.map(record));
        }

        return map;
    }

    @Override
    @Deprecated
    public final Object[][] intoArray() {
        return intoArrays();
    }

    @Override
    public final Object[][] intoArrays() {
        int size = size();
        Object[][] array = new Object[size][];

        for (int i = 0; i < size; i++) {
            array[i] = get(i).intoArray();
        }

        return array;
    }

    @Override
    public final Object[] intoArray(int fieldIndex) {
        Class<?> type = fields.fields[fieldIndex].getType();
        List<?> list = getValues(fieldIndex);
        return list.toArray((Object[]) Array.newInstance(type, list.size()));
    }

    @Override
    public final <T> T[] intoArray(int fieldIndex, Class<? extends T> type) {
        return (T[]) Convert.convertArray(intoArray(fieldIndex), type);
    }

    @Override
    public final <U> U[] intoArray(int fieldIndex, Converter<?, ? extends U> converter) {
        return Convert.convertArray(intoArray(fieldIndex), converter);
    }

    @Override
    public final Object[] intoArray(String fieldName) {
        Class<?> type = field(fieldName).getType();
        List<?> list = getValues(fieldName);
        return list.toArray((Object[]) Array.newInstance(type, list.size()));
    }

    @Override
    public final <T> T[] intoArray(String fieldName, Class<? extends T> type) {
        return (T[]) Convert.convertArray(intoArray(fieldName), type);
    }

    @Override
    public final <U> U[] intoArray(String fieldName, Converter<?, ? extends U> converter) {
        return Convert.convertArray(intoArray(fieldName), converter);
    }

    @Override
    public final Object[] intoArray(Name fieldName) {
        Class<?> type = field(fieldName).getType();
        List<?> list = getValues(fieldName);
        return list.toArray((Object[]) Array.newInstance(type, list.size()));
    }

    @Override
    public final <T> T[] intoArray(Name fieldName, Class<? extends T> type) {
        return (T[]) Convert.convertArray(intoArray(fieldName), type);
    }

    @Override
    public final <U> U[] intoArray(Name fieldName, Converter<?, ? extends U> converter) {
        return Convert.convertArray(intoArray(fieldName), converter);
    }

    @Override
    public final <T> T[] intoArray(Field<T> field) {
        return getValues(field).toArray((T[]) Array.newInstance(field.getType(), 0));
    }

    @Override
    public final <T> T[] intoArray(Field<?> field, Class<? extends T> type) {
        return (T[]) Convert.convertArray(intoArray(field), type);
    }

    @Override
    public final <T, U> U[] intoArray(Field<T> field, Converter<? super T, ? extends U> converter) {
        return Convert.convertArray(intoArray(field), converter);
    }

    @Override
    public final <E> Set<E> intoSet(RecordMapper<? super R, E> mapper) {
        Set<E> result = new LinkedHashSet<E>();

        for (R record : this)
            result.add(mapper.map(record));

        return result;
    }

    @Override
    public final Set<?> intoSet(int fieldIndex) {
        return new LinkedHashSet<Object>(getValues(fieldIndex));
    }

    @Override
    public final <T> Set<T> intoSet(int fieldIndex, Class<? extends T> type) {
        return new LinkedHashSet<T>(getValues(fieldIndex, type));
    }

    @Override
    public final <U> Set<U> intoSet(int fieldIndex, Converter<?, ? extends U> converter) {
        return new LinkedHashSet<U>(getValues(fieldIndex, converter));
    }

    @Override
    public final Set<?> intoSet(String fieldName) {
        return new LinkedHashSet<Object>(getValues(fieldName));
    }

    @Override
    public final <T> Set<T> intoSet(String fieldName, Class<? extends T> type) {
        return new LinkedHashSet<T>(getValues(fieldName, type));
    }

    @Override
    public final <U> Set<U> intoSet(String fieldName, Converter<?, ? extends U> converter) {
        return new LinkedHashSet<U>(getValues(fieldName, converter));
    }

    @Override
    public final Set<?> intoSet(Name fieldName) {
        return new LinkedHashSet<Object>(getValues(fieldName));
    }

    @Override
    public final <T> Set<T> intoSet(Name fieldName, Class<? extends T> type) {
        return new LinkedHashSet<T>(getValues(fieldName, type));
    }

    @Override
    public final <U> Set<U> intoSet(Name fieldName, Converter<?, ? extends U> converter) {
        return new LinkedHashSet<U>(getValues(fieldName, converter));
    }

    @Override
    public final <T> Set<T> intoSet(Field<T> field) {
        return new LinkedHashSet<T>(getValues(field));
    }

    @Override
    public final <T> Set<T> intoSet(Field<?> field, Class<? extends T> type) {
        return new LinkedHashSet<T>(getValues(field, type));
    }

    @Override
    public final <T, U> Set<U> intoSet(Field<T> field, Converter<? super T, ? extends U> converter) {
        return new LinkedHashSet<U>(getValues(field, converter));
    }

    @Override
    public final Result<Record> into(Field<?>... f) {
        Result<Record> result = new ResultImpl<Record>(Tools.configuration(this), f);

        for (Record record : this)
            result.add(record.into(f));

        return result;
    }

    // [jooq-tools] START [into-fields]

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

// [jooq-tools] END [into-fields]

    @Override
    public final <E> List<E> into(Class<? extends E> type) {
        List<E> list = new ArrayList<E>(size());
        RecordMapper<R, E> mapper = Tools.configuration(this).recordMapperProvider().provide(fields, type);

        for (R record : this)
            list.add(mapper.map(record));

        return list;
    }

    @Override
    public final <Z extends Record> Result<Z> into(Table<Z> table) {
        Result<Z> list = new ResultImpl<Z>(configuration(), table.fields());

        for (R record : this)
            list.add(record.into(table));

        return list;
    }

    @Override
    public final <H extends RecordHandler<? super R>> H into(H handler) {
        for (R record : this)
            handler.next(record);

        return handler;
    }

    @Override
    public final ResultSet intoResultSet() {
        return new MockResultSet(this);
    }

    @Override
    public final <E> List<E> map(RecordMapper<? super R, E> mapper) {
        List<E> result = new ArrayList<E>();

        for (R record : this)
            result.add(mapper.map(record));

        return result;
    }

    @Override
    public final <T extends Comparable<? super T>> Result<R> sortAsc(Field<T> field) {
        return sortAsc(field, new NaturalComparator<T>());
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
        Collections.sort(this, comparator);
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
        return intern(fields.indexesOf(f));
    }

    @Override
    public final Result<R> intern(int... fieldIndexes) {
        for (int fieldIndex : fieldIndexes)
            if (fields.fields[fieldIndex].getType() == String.class)
                for (Record record : this)
                    ((AbstractRecord) record).intern0(fieldIndex);

        return this;
    }

    @Override
    public final Result<R> intern(String... fieldNames) {
        return intern(fields.indexesOf(fieldNames));
    }

    @Override
    public final Result<R> intern(Name... fieldNames) {
        return intern(fields.indexesOf(fieldNames));
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
            if (o1 == null && o2 == null) {
                return 0;
            }
            else if (o1 == null) {
                return -1;
            }
            else if (o2 == null) {
                return 1;
            }
            return o1.compareTo(o2);
        }
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
