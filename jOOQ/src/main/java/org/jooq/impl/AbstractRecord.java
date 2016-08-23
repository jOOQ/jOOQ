/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static java.util.Arrays.asList;
import static org.jooq.conf.SettingsTools.updatablePrimaryKeys;
import static org.jooq.impl.Tools.getAnnotatedGetter;
import static org.jooq.impl.Tools.getAnnotatedMembers;
import static org.jooq.impl.Tools.getMatchingGetter;
import static org.jooq.impl.Tools.getMatchingMembers;
import static org.jooq.impl.Tools.hasColumnAnnotations;
import static org.jooq.impl.Tools.indexOrFail;
import static org.jooq.impl.Tools.resetChangedOnNotNull;
import static org.jooq.impl.Tools.settings;
import static org.jooq.impl.Tools.ThreadGuard.Guard.RECORD_TOSTRING;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.Stream;

import org.jooq.Attachable;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Field;
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
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.MappingException;
import org.jooq.impl.Tools.ThreadGuard;
import org.jooq.impl.Tools.ThreadGuard.GuardedOperation;
import org.jooq.tools.Convert;
import org.jooq.tools.StringUtils;

/**
 * A general base class for all {@link Record} types
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
abstract class AbstractRecord extends AbstractStore implements Record {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6052512608911220404L;

    final RowImpl             fields;
    final Object[]            values;
    final Object[]            originals;
    final BitSet              changed;
    boolean                   fetched;

    AbstractRecord(Collection<? extends Field<?>> fields) {
        this(new RowImpl(fields));
    }

    AbstractRecord(Field<?>... fields) {
        this(new RowImpl(fields));
    }

    AbstractRecord(RowImpl fields) {
        int size = fields.size();

        this.fields = fields;
        this.values = new Object[size];
        this.originals = new Object[size];
        this.changed = new BitSet(size);
    }

    // ------------------------------------------------------------------------
    // XXX: Attachable API
    // ------------------------------------------------------------------------

    @Override
    final List<Attachable> getAttachables() {
        List<Attachable> result = null;

        int size = size();
        for (int i = 0; i < size; i++) {
            if (values[i] instanceof Attachable) {
                if (result == null)
                    result = new ArrayList<Attachable>();

                result.add((Attachable) values[i]);
            }
        }

        return result == null ? Collections.<Attachable>emptyList() : result;
    }

    // ------------------------------------------------------------------------
    // XXX: FieldProvider API
    // ------------------------------------------------------------------------

    @Override
    public final <T> Field<T> field(Field<T> field) {
        return fieldsRow().field(field);
    }

    @Override
    public final Field<?> field(String name) {
        return fieldsRow().field(name);
    }

    @Override
    public final Field<?> field(Name name) {
        return fieldsRow().field(name);
    }

    @Override
    public final Field<?> field(int index) {
        return index >= 0 && index < fields.size() ? fields.field(index) : null;
    }

    @Override
    public final Field<?>[] fields() {
        return fields.fields();
    }

    @Override
    public final Field<?>[] fields(Field<?>... f) {
        return fields.fields(f);
    }

    @Override
    public final Field<?>[] fields(String... fieldNames) {
        return fields.fields(fieldNames);
    }

    @Override
    public final Field<?>[] fields(Name... fieldNames) {
        return fields.fields(fieldNames);
    }

    @Override
    public final Field<?>[] fields(int... fieldIndexes) {
        return fields.fields(fieldIndexes);
    }

    // ------------------------------------------------------------------------
    // XXX: Record API
    // ------------------------------------------------------------------------

    @Override
    public final int size() {
        return fields.size();
    }

    @Override
    public final <T> T get(Field<T> field) {
        return (T) get(indexOrFail(fieldsRow(), field));
    }

    @Override
    public final <T> T get(Field<?> field, Class<? extends T> type) {
        return Convert.convert(get(field), type);
    }

    @Override
    public final <T, U> U get(Field<T> field, Converter<? super T, ? extends U> converter) {
        return converter.from(get(field));
    }

    @Override
    public final Object get(int index) {
        return values[safeIndex(index)];
    }

    @Override
    public final <T> T get(int index, Class<? extends T> type) {
        return Convert.convert(get(index), type);
    }

    @Override
    public final <U> U get(int index, Converter<?, ? extends U> converter) {
        return Convert.convert(get(index), converter);
    }

    @Override
    public final Object get(String fieldName) {
        return get(indexOrFail(fieldsRow(), fieldName));
    }

    @Override
    public final <T> T get(String fieldName, Class<? extends T> type) {
        return Convert.convert(get(fieldName), type);
    }

    @Override
    public final <U> U get(String fieldName, Converter<?, ? extends U> converter) {
        return Convert.convert(get(fieldName), converter);
    }

    @Override
    public final Object get(Name fieldName) {
        return get(indexOrFail(fieldsRow(), fieldName));
    }

    @Override
    public final <T> T get(Name fieldName, Class<? extends T> type) {
        return Convert.convert(get(fieldName), type);
    }

    @Override
    public final <U> U get(Name fieldName, Converter<?, ? extends U> converter) {
        return Convert.convert(get(fieldName), converter);
    }

    /**
     * Subclasses may type-unsafely set a value to a record index. This method
     * takes care of converting the value to the appropriate type.
     *
     * @deprecated - Use {@link AbstractRecord#set(int, Object)} instead
     */
    @Deprecated
    protected final void setValue(int index, Object value) {
        set(index, value);
    }

    protected final void set(int index, Object value) {
        set(index, (Field) field(index), value);
    }

    @Override
    public final <T> void set(Field<T> field, T value) {
        set(indexOrFail(fields, field), field, value);
    }

    private final <T> void set(int index, Field<T> field, T value) {
        // Relevant issues documenting this method's behaviour:
        // [#945] Avoid bugs resulting from setting the same value twice
        // [#948] To allow for controlling the number of hard-parses
        //        To allow for explicitly overriding default values
        // [#979] Avoid modifying chnaged flag on unchanged primary key values

        UniqueKey<?> key = getPrimaryKey();

        // Normal fields' changed flag is always set to true
        if (key == null || !key.getFields().contains(field)) {
            changed.set(index);
        }

        // The primary key's changed flag might've been set previously
        else if (changed.get(index)) {
            changed.set(index);
        }

        // [#2764] Users may override updatability of primary key values
        else if (updatablePrimaryKeys(settings(this))) {
            changed.set(index);
        }

        // [#2698] If the primary key has not yet been set
        else if (originals[index] == null) {
            changed.set(index);
        }

        // [#979] If the primary key is being changed, all other fields' flags
        // need to be set to true for in case this record is stored again, an
        // INSERT statement will thus be issued
        else {

            // [#945] Be sure that changed is never reset to false
            changed.set(index, changed.get(index) || !StringUtils.equals(values[index], value));

            if (changed.get(index)) {
                changed(true);
            }
        }

        values[index] = value;
    }

    @Override
    public final <T, U> void set(Field<T> field, U value, Converter<? extends T, ? super U> converter) {
        set(field, converter.to(value));
    }

    @Override
    public /* non-final */ <T> Record with(Field<T> field, T value) {
        set(field, value);
        return this;
    }

    @Override
    public <T, U> Record with(Field<T> field, U value, Converter<? extends T, ? super U> converter) {
        set(field, value, converter);
        return this;
    }

    final void setValues(Field<?>[] fields, AbstractRecord record) {
        fetched = record.fetched;

        for (Field<?> field : fields) {
            int targetIndex = indexOrFail(fieldsRow(), field);
            int sourceIndex = indexOrFail(record.fieldsRow(), field);

            values[targetIndex] = record.get(sourceIndex);
            originals[targetIndex] = record.original(sourceIndex);
            changed.set(targetIndex, record.changed(sourceIndex));
        }
    }

    final void intern0(int fieldIndex) {
        safeIndex(fieldIndex);

        if (field(fieldIndex).getType() == String.class) {
            values[fieldIndex] = ((String) values[fieldIndex]).intern();
            originals[fieldIndex] = ((String) originals[fieldIndex]).intern();
        }
    }

    final int safeIndex(int index) {
        if (index >= 0 && index < values.length)
            return index;

        throw new IllegalArgumentException("No field at index " + index + " in Record type " + fieldsRow());
    }

    /**
     * Subclasses may override this
     */
    UniqueKey<?> getPrimaryKey() {
        return null;
    }

    /*
     * This method is overridden covariantly by TableRecordImpl
     */
    @Override
    public Record original() {
        return Tools.newRecord(fetched, (Class<AbstractRecord>) getClass(), fields.fields.fields, configuration())
                    .operate(new RecordOperation<AbstractRecord, RuntimeException>() {

            @Override
            public AbstractRecord operate(AbstractRecord record) throws RuntimeException {
                for (int i = 0; i < originals.length; i++) {
                    record.values[i] = originals[i];
                    record.originals[i] = originals[i];
                }

                return record;
            }
        });
    }

    @Override
    public final <T> T original(Field<T> field) {
        return (T) original(indexOrFail(fieldsRow(), field));
    }

    @Override
    public final Object original(int fieldIndex) {
        return originals[safeIndex(fieldIndex)];
    }

    @Override
    public final Object original(String fieldName) {
        return original(indexOrFail(fieldsRow(), fieldName));
    }

    @Override
    public final Object original(Name fieldName) {
        return original(indexOrFail(fieldsRow(), fieldName));
    }

    @Override
    public final boolean changed() {
        return !changed.isEmpty();
    }

    @Override
    public final boolean changed(Field<?> field) {
        return changed(indexOrFail(fieldsRow(), field));
    }

    @Override
    public final boolean changed(int fieldIndex) {
        return changed.get(safeIndex(fieldIndex));
    }

    @Override
    public final boolean changed(String fieldName) {
        return changed(indexOrFail(fieldsRow(), fieldName));
    }

    @Override
    public final boolean changed(Name fieldName) {
        return changed(indexOrFail(fieldsRow(), fieldName));
    }

    @Override
    public final void changed(boolean c) {
        changed.set(0, values.length, c);

        // [#1995] If a value is meant to be "unchanged", the "original" should
        // match the supposedly "unchanged" value.
        if (!c) {
            System.arraycopy(values, 0, originals, 0, values.length);
        }
    }

    @Override
    public final void changed(Field<?> field, boolean c) {
        changed(indexOrFail(fieldsRow(), field), c);
    }

    @Override
    public final void changed(int fieldIndex, boolean c) {
        safeIndex(fieldIndex);

        changed.set(fieldIndex, c);

        // [#1995] If a value is meant to be "unchanged", the "original" should
        // match the supposedly "unchanged" value.
        if (!c)
            originals[fieldIndex] = values[fieldIndex];
    }

    @Override
    public final void changed(String fieldName, boolean c) {
        changed(indexOrFail(fieldsRow(), fieldName), c);
    }

    @Override
    public final void changed(Name fieldName, boolean c) {
        changed(indexOrFail(fieldsRow(), fieldName), c);
    }

    @Override
    public final void reset() {
        changed.clear();

        System.arraycopy(originals, 0, values, 0, originals.length);
    }

    @Override
    public final void reset(Field<?> field) {
        reset(indexOrFail(fieldsRow(), field));
    }

    @Override
    public final void reset(int fieldIndex) {
        safeIndex(fieldIndex);

        changed.clear(fieldIndex);
        values[fieldIndex] = originals[fieldIndex];
    }

    @Override
    public final void reset(String fieldName) {
        reset(indexOrFail(fieldsRow(), fieldName));
    }

    @Override
    public final void reset(Name fieldName) {
        reset(indexOrFail(fieldsRow(), fieldName));
    }

    @Override
    public final Object[] intoArray() {
        return into(Object[].class);
    }

    @Override
    public final List<Object> intoList() {
        return Arrays.asList(intoArray());
    }


    @Override
    public final Stream<Object> intoStream() {
        return into(Stream.class);
    }


    @Override
    public final Map<String, Object> intoMap() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        int size = fields.size();
        for (int i = 0; i < size; i++) {
            Field<?> field = fields.field(i);

            if (map.put(field.getName(), get(i)) != null) {
                throw new InvalidResultException("Field " + field.getName() + " is not unique in Record : " + this);
            }
        }

        return map;
    }

    @Override
    public final Record into(Field<?>... f) {
        return Tools.newRecord(fetched, Record.class, f, configuration()).operate(new TransferRecordState<Record>(f));
    }

    // [jooq-tools] START [into-fields]

    @Override
    public final <T1> Record1<T1> into(Field<T1> field1) {
        return (Record1) into(new Field[] { field1 });
    }

    @Override
    public final <T1, T2> Record2<T1, T2> into(Field<T1> field1, Field<T2> field2) {
        return (Record2) into(new Field[] { field1, field2 });
    }

    @Override
    public final <T1, T2, T3> Record3<T1, T2, T3> into(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return (Record3) into(new Field[] { field1, field2, field3 });
    }

    @Override
    public final <T1, T2, T3, T4> Record4<T1, T2, T3, T4> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return (Record4) into(new Field[] { field1, field2, field3, field4 });
    }

    @Override
    public final <T1, T2, T3, T4, T5> Record5<T1, T2, T3, T4, T5> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return (Record5) into(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6> Record6<T1, T2, T3, T4, T5, T6> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return (Record6) into(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7> Record7<T1, T2, T3, T4, T5, T6, T7> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return (Record7) into(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8> Record8<T1, T2, T3, T4, T5, T6, T7, T8> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return (Record8) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return (Record9) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return (Record10) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return (Record11) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return (Record12) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return (Record13) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return (Record14) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return (Record15) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return (Record16) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return (Record17) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return (Record18) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return (Record19) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return (Record20) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return (Record21) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return (Record22) into(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }

// [jooq-tools] END [into-fields]

    @Override
    public final <E> E into(Class<? extends E> type) {
        return (E) Tools.configuration(this).recordMapperProvider().provide(fields.fields, type).map(this);
    }

    @Override
    public final <E> E into(E object) {
        if (object == null) {
            throw new NullPointerException("Cannot copy Record into null");
        }

        Class<E> type = (Class<E>) object.getClass();

        try {
            return new DefaultRecordMapper<Record, E>(fields.fields, type, object, configuration()).map(this);
        }

        // Pass MappingExceptions on to client code
        catch (MappingException e) {
            throw e;
        }

        // All other reflection exceptions are intercepted
        catch (Exception e) {
            throw new MappingException("An error ocurred when mapping record to " + type, e);
        }
    }

    @Override
    public final <R extends Record> R into(Table<R> table) {
        return Tools.newRecord(fetched, table, configuration()).operate(new TransferRecordState<R>(table.fields()));
    }

    final <R extends Record> R intoRecord(Class<R> type) {
        return Tools.newRecord(fetched, type, fields(), configuration()).operate(new TransferRecordState<R>(null));
    }

    private class TransferRecordState<R extends Record> implements RecordOperation<R, MappingException> {

        private final Field<?>[] targetFields;

        TransferRecordState(Field<?>[] targetFields) {
            this.targetFields = targetFields;
        }

        @Override
        public R operate(R target) throws MappingException {
            AbstractRecord source = AbstractRecord.this;

            try {

                // [#1522] [#2989] If possible the complete state of this record should be copied onto the other record
                if (target instanceof AbstractRecord) {
                    AbstractRecord t = (AbstractRecord) target;

                    // Iterate over target fields, to avoid ambiguities when two source fields share the same name.
                    // [#3634] If external targetFields are provided, use those instead of the target record's fields.
                    //         The record doesn't know about aliased tables, for instance.
                    for (int targetIndex = 0; targetIndex < (targetFields != null ? targetFields.length : t.size()); targetIndex++) {
                        Field<?> targetField = (targetFields != null ? targetFields[targetIndex] : t.field(targetIndex));
                        int sourceIndex = fields.indexOf(targetField);

                        if (sourceIndex >= 0) {
                            DataType<?> targetType = targetField.getDataType();

                            t.values[targetIndex] = targetType.convert(values[sourceIndex]);
                            t.originals[targetIndex] = targetType.convert(originals[sourceIndex]);
                            t.changed.set(targetIndex, changed.get(sourceIndex));
                        }
                    }
                }

                else {
                    for (Field<?> targetField : target.fields()) {
                        Field<?> sourceField = field(targetField);

                        if (sourceField != null) {
                            Tools.setValue(target, targetField, source, sourceField);
                        }
                    }
                }

                return target;
            }

            // All reflection exceptions are intercepted
            catch (Exception e) {
                throw new MappingException("An error ocurred when mapping record to " + target, e);
            }
        }
    }

    @Override
    public final ResultSet intoResultSet() {
        ResultImpl<Record> result = new ResultImpl<Record>(configuration(), fields.fields.fields);
        result.add(this);
        return result.intoResultSet();
    }

    @Override
    public final <E> E map(RecordMapper<Record, E> mapper) {
        return mapper.map(this);
    }

    @Override
    public final void from(Object source) {
        if (source == null) return;

        // [#1987] Distinguish between various types to load data from
        // Maps are loaded using a {field-name -> value} convention
        if (source instanceof Map) {
            fromMap((Map<String, ?>) source);
        }

        // Arrays are loaded through index mapping
        else if (source instanceof Object[]) {
            fromArray((Object[]) source);
        }

        // All other types are expected to be POJOs
        else {
            from(source, fields());
        }
    }

    @Override
    public final void from(Object source, Field<?>... f) {
        if (source == null) return;

        // [#1987] Distinguish between various types to load data from
        // Maps are loaded using a {field-name -> value} convention
        if (source instanceof Map) {
            fromMap((Map<String, ?>) source, f);
        }

        // Arrays are loaded through index mapping
        else if (source instanceof Object[]) {
            fromArray((Object[]) source, f);
        }

        // All other types are expected to be POJOs
        else {
            Class<?> type = source.getClass();

            try {
                boolean useAnnotations = hasColumnAnnotations(configuration(), type);

                for (Field<?> field : f) {
                    List<java.lang.reflect.Field> members;
                    Method method;

                    // Annotations are available and present
                    if (useAnnotations) {
                        members = getAnnotatedMembers(configuration(), type, field.getName());
                        method = getAnnotatedGetter(configuration(), type, field.getName());
                    }

                    // No annotations are present
                    else {
                        members = getMatchingMembers(configuration(), type, field.getName());
                        method = getMatchingGetter(configuration(), type, field.getName());
                    }

                    // Use only the first applicable method or member
                    if (method != null) {
                        Tools.setValue(this, field, method.invoke(source));
                    }
                    else if (members.size() > 0) {
                        from(source, members.get(0), field);
                    }
                }
            }

            // All reflection exceptions are intercepted
            catch (Exception e) {
                throw new MappingException("An error ocurred when mapping record from " + type, e);
            }
        }

        // [#2700] [#3582] If a POJO attribute is NULL, but the column is NOT NULL
        // then we should let the database apply DEFAULT values
        resetChangedOnNotNull(this);
    }

    @Override
    public final void from(Object source, String... fieldNames) {
        from(source, fields(fieldNames));
    }

    @Override
    public final void from(Object source, Name... fieldNames) {
        from(source, fields(fieldNames));
    }

    @Override
    public final void from(Object source, int... fieldIndexes) {
        from(source, fields(fieldIndexes));
    }

    @Override
    public final void fromMap(Map<String, ?> map) {
        from(map, fields());
    }

    @Override
    public final void fromMap(Map<String, ?> map, Field<?>... f) {
        for (int i = 0; i < f.length; i++) {
            String name = f[i].getName();

            // Set only those values contained in the map
            if (map.containsKey(name)) {
                Tools.setValue(this, f[i], map.get(name));
            }
        }
    }

    @Override
    public final void fromMap(Map<String, ?> map, String... fieldNames) {
        fromMap(map, fields(fieldNames));
    }

    @Override
    public final void fromMap(Map<String, ?> map, Name... fieldNames) {
        fromMap(map, fields(fieldNames));
    }

    @Override
    public final void fromMap(Map<String, ?> map, int... fieldIndexes) {
        fromMap(map, fields(fieldIndexes));
    }

    @Override
    public final void fromArray(Object... array) {
        fromArray(array, fields());
    }

    @Override
    public final void fromArray(Object[] array, Field<?>... f) {
        Fields accept = new Fields(f);
        int size = fields.size();

        for (int i = 0; i < size && i < array.length; i++) {
            Field field = fields.field(i);

            if (accept.field(field) != null) {
                Tools.setValue(this, field, array[i]);
            }
        }
    }

    @Override
    public final void fromArray(Object[] array, String... fieldNames) {
        fromArray(array, fields(fieldNames));
    }

    @Override
    public final void fromArray(Object[] array, Name... fieldNames) {
        fromArray(array, fields(fieldNames));
    }

    @Override
    public final void fromArray(Object[] array, int... fieldIndexes) {
        fromArray(array, fields(fieldIndexes));
    }

    /**
     * This method was implemented with [#799]. It may be useful to make it
     * public for broader use...?
     */
    protected final void from(Record source) {
        for (Field<?> field : fields.fields.fields) {
            Field<?> sourceField = source.field(field);

            if (sourceField != null) {
                Tools.setValue(this, field, source, sourceField);
            }
        }
    }

    private final void from(Object source, java.lang.reflect.Field member, Field<?> field)
        throws IllegalAccessException {

        Class<?> mType = member.getType();

        if (mType.isPrimitive()) {
            if (mType == byte.class) {
                Tools.setValue(this, field, member.getByte(source));
            }
            else if (mType == short.class) {
                Tools.setValue(this, field, member.getShort(source));
            }
            else if (mType == int.class) {
                Tools.setValue(this, field, member.getInt(source));
            }
            else if (mType == long.class) {
                Tools.setValue(this, field, member.getLong(source));
            }
            else if (mType == float.class) {
                Tools.setValue(this, field, member.getFloat(source));
            }
            else if (mType == double.class) {
                Tools.setValue(this, field, member.getDouble(source));
            }
            else if (mType == boolean.class) {
                Tools.setValue(this, field, member.getBoolean(source));
            }
            else if (mType == char.class) {
                Tools.setValue(this, field, member.getChar(source));
            }
        }
        else {
            Tools.setValue(this, field, member.get(source));
        }
    }

    // ------------------------------------------------------------------------
    // XXX: Object and Comparable API
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        // [#3900] Nested records should generate different toString() behaviour
        return ThreadGuard.run(RECORD_TOSTRING, new GuardedOperation<String>() {
            @Override
            public String unguarded() {
                Result<AbstractRecord> result = new ResultImpl<AbstractRecord>(configuration(), fields.fields.fields);
                result.add(AbstractRecord.this);
                return result.toString();
            }

            @Override
            public String guarded() {
                return valuesRow().toString();
            }
        });
    }

    @Override
    public int compareTo(Record that) {
        // Note: keep this implementation in-sync with AbstractStore.equals()!

        if (that == null) {
            throw new NullPointerException();
        }
        if (size() != that.size()) {
            throw new ClassCastException(String.format("Trying to compare incomparable records (wrong degree):\n%s\n%s", this, that));
        }

        Class<?>[] thisTypes = this.fieldsRow().types();
        Class<?>[] thatTypes = that.fieldsRow().types();

        if (!asList(thisTypes).equals(asList(thatTypes))) {
            throw new ClassCastException(String.format("Trying to compare incomparable records (type mismatch):\n%s\n%s", this, that));
        }

        for (int i = 0; i < size(); i++) {
            final Object thisValue = get(i);
            final Object thatValue = that.get(i);

            // [#1850] Only return -1/+1 early. In all other cases,
            // continue checking the remaining fields
            if (thisValue == null && thatValue == null) {
                continue;
            }

            // Order column values in a SQL NULLS LAST manner
            else if (thisValue == null) {
                return 1;
            }

            else if (thatValue == null) {
                return -1;
            }

            // [#985] Compare arrays too.
            else if (thisValue.getClass().isArray() && thatValue.getClass().isArray()) {

                // Might be byte[]
                if (thisValue.getClass() == byte[].class) {
                    int compare = compare((byte[]) thisValue, (byte[]) thatValue);

                    if (compare != 0) {
                        return compare;
                    }
                }

                // Other primitive types are not expected
                else if (!thisValue.getClass().getComponentType().isPrimitive()) {
                    int compare = compare((Object[]) thisValue, (Object[]) thatValue);

                    if (compare != 0) {
                        return compare;
                    }
                }

                else {
                    throw new ClassCastException(String.format("Unsupported data type in natural ordering: %s", thisValue.getClass()));
                }
            }
            else {
                int compare = ((Comparable) thisValue).compareTo(thatValue);

                if (compare != 0) {
                    return compare;
                }
            }
        }

        // If we got through the above loop, the two records are equal
        return 0;
    }

    /**
     * Compare two byte arrays
     */
    final int compare(byte[] array1, byte[] array2) {
        int length = Math.min(array1.length, array2.length);

        for (int i = 0; i < length; i++) {
            int v1 = (array1[i] & 0xff);
            int v2 = (array2[i] & 0xff);

            if (v1 != v2) {
                return v1 < v2 ? -1 : 1;
            }
        }

        return array1.length - array2.length;
    }

    /**
     * Compare two arrays
     */
    final int compare(Object[] array1, Object[] array2) {
        int length = Math.min(array1.length, array2.length);

        for (int i = 0; i < length; i++) {
            int compare = ((Comparable) array1[i]).compareTo(array2[i]);

            if (compare != 0) {
                return compare;
            }
        }

        return array1.length - array2.length;
    }

    // -------------------------------------------------------------------------
    // XXX: Deprecated and discouraged methods
    // -------------------------------------------------------------------------

    @Override
    public final <T> T getValue(Field<T> field) {
        return get(field);
    }

    @Override
    @Deprecated
    public final <T> T getValue(Field<T> field, T defaultValue) {
        T result = getValue(field);
        return result != null ? result : defaultValue;
    }

    @Override
    public final <T> T getValue(Field<?> field, Class<? extends T> type) {
        return get(field, type);
    }

    @Override
    @Deprecated
    public final <T> T getValue(Field<?> field, Class<? extends T> type, T defaultValue) {
        final T result = get(field, type);
        return result == null ? defaultValue : result;
    }

    @Override
    public final <T, U> U getValue(Field<T> field, Converter<? super T, ? extends U> converter) {
        return get(field, converter);
    }

    @Override
    @Deprecated
    public final <T, U> U getValue(Field<T> field, Converter<? super T, ? extends U> converter, U defaultValue) {
        final U result = get(field, converter);
        return result == null ? defaultValue : result;
    }

    @Override
    public final Object getValue(int index) {
        return get(index);
    }

    @Override
    @Deprecated
    public final Object getValue(int index, Object defaultValue) {
        final Object result = get(index);
        return result == null ? defaultValue : result;
    }

    @Override
    public final <T> T getValue(int index, Class<? extends T> type) {
        return get(index, type);
    }

    @Override
    @Deprecated
    public final <T> T getValue(int index, Class<? extends T> type, T defaultValue) {
        final T result = get(index, type);
        return result == null ? defaultValue : result;
    }

    @Override
    public final <U> U getValue(int index, Converter<?, ? extends U> converter) {
        return get(index, converter);
    }

    @Override
    @Deprecated
    public final <U> U getValue(int index, Converter<?, ? extends U> converter, U defaultValue) {
        final U result = get(index, converter);
        return result == null ? defaultValue : result;
    }

    @Override
    public final Object getValue(String fieldName) {
        return get(fieldName);
    }

    @Override
    @Deprecated
    public final Object getValue(String fieldName, Object defaultValue) {
        return getValue(indexOrFail(fieldsRow(), fieldName), defaultValue);
    }

    @Override
    public final <T> T getValue(String fieldName, Class<? extends T> type) {
        return get(fieldName, type);
    }

    @Override
    @Deprecated
    public final <T> T getValue(String fieldName, Class<? extends T> type, T defaultValue) {
        final T result = get(fieldName, type);
        return result == null ? defaultValue : result;
    }

    @Override
    public final <U> U getValue(String fieldName, Converter<?, ? extends U> converter) {
        return get(fieldName, converter);
    }

    @Override
    @Deprecated
    public final <U> U getValue(String fieldName, Converter<?, ? extends U> converter, U defaultValue) {
        final U result = get(fieldName, converter);
        return result == null ? defaultValue : result;
    }

    @Override
    public final Object getValue(Name fieldName) {
        return get(fieldName);
    }

    @Override
    public final <T> T getValue(Name fieldName, Class<? extends T> type) {
        return get(fieldName, type);
    }

    @Override
    public final <U> U getValue(Name fieldName, Converter<?, ? extends U> converter) {
        return get(fieldName, converter);
    }

    @Override
    public final <T> void setValue(Field<T> field, T value) {
        set(field, value);
    }

    @Override
    public final <T, U> void setValue(Field<T> field, U value, Converter<? extends T, ? super U> converter) {
        set(field, value, converter);
    }

}
