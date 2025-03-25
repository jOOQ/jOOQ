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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.jooq.ContextConverter.scoped;
import static org.jooq.conf.SettingsTools.updatablePrimaryKeys;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.converterOrFail;
import static org.jooq.impl.Tools.converterContext;
import static org.jooq.impl.Tools.embeddedFields;
import static org.jooq.impl.Tools.indexFail;
import static org.jooq.impl.Tools.indexOrFail;
import static org.jooq.impl.Tools.nonReplacingEmbeddable;
import static org.jooq.impl.Tools.settings;

import java.io.Writer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jooq.Attachable;
import org.jooq.CSVFormat;
import org.jooq.ChartFormat;
import org.jooq.Converter;
import org.jooq.ConverterContext;
import org.jooq.DataType;
import org.jooq.EmbeddableRecord;
import org.jooq.Field;
import org.jooq.Fields;
import org.jooq.JSONFormat;
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
import org.jooq.TXTFormat;
import org.jooq.Table;
import org.jooq.UDTRecord;
import org.jooq.UniqueKey;
import org.jooq.XMLFormat;
import org.jooq.exception.IOException;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.MappingException;
import org.jooq.tools.StringUtils;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * A general base class for all {@link Record} types
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
abstract class AbstractRecord
extends
    AbstractStore
implements
    Record,
    FieldsTrait
{

    final AbstractRow<? extends AbstractRecord> fields;
    final Object[]                              values;
    final Object[]                              originals;
    final BitSet                                touched;
    boolean                                     fetched;

    /**
     * @deprecated - 3.14.5 - [#8495] [#11058] - Re-use AbstractRow reference if possible
     */
    @Deprecated
    AbstractRecord(Collection<? extends Field<?>> fields) {
        this(Tools.row0(fields.toArray(EMPTY_FIELD)));
    }

    /**
     * @deprecated - 3.14.5 - [#8495] [#11058] - Re-use AbstractRow reference if possible
     */
    @Deprecated
    AbstractRecord(Field<?>... fields) {
        this(Tools.row0(fields));
    }

    AbstractRecord(AbstractRow<?> fields) {
        int size = fields.size();

        this.fields = (AbstractRow<? extends AbstractRecord>) fields;
        this.values = new Object[size];
        this.originals = new Object[size];
        this.touched = new BitSet(size);
    }

    // ------------------------------------------------------------------------
    // XXX: Attachable API
    // ------------------------------------------------------------------------

    @Override
    final List<Attachable> getAttachables() {
        List<Attachable> result = null;

        int size = size();
        for (int i = 0; i < size; i++) {
            if (values[i] instanceof Attachable a) {
                if (result == null)
                    result = new ArrayList<>();

                result.add(a);
            }
        }

        return result == null ? emptyList() : result;
    }

    // ------------------------------------------------------------------------
    // XXX: FieldProvider API
    // ------------------------------------------------------------------------

    @Override
    @Internal
    public final Fields internalFieldsRow() {
        return fields;
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
        int index = fields.indexOf(field);

        if (index >= 0)
            return (T) get(index);
        else if (nonReplacingEmbeddable(field))
            return (T) Tools
                .newRecord(fetched, configuration(), ((EmbeddableTableField<?, ?>) field).recordType)
                .operate(new TransferRecordState<>(embeddedFields(field)));
        else
            throw Tools.indexFail(fields, field);
    }

    @Override
    public final <U> U get(Field<?> field, Class<? extends U> type) {
        Object t = get(field);
        return (U) converterOrFail(this, t, (Class) field.getType(), type).from(t, converterContext(this));
    }

    @Override
    public final <T, U> U get(Field<T> field, Converter<? super T, ? extends U> converter) {
        return scoped(converter).from(get(field), converterContext(this));
    }

    @Override
    public final Object get(int index) {
        return values[safeIndex(index)];
    }

    @Override
    public final <U> U get(int index, Class<? extends U> type) {
        Object t = get(index);
        return (U) converterOrFail(this, t, (Class) field(safeIndex(index)).getType(), type).from(t, converterContext(this));
    }

    @Override
    public final <U> U get(int index, Converter<?, ? extends U> converter) {
        return Convert.convert(get(index), converter);
    }

    @Override
    public final Object get(String fieldName) {
        return get(indexOrFail(fields, fieldName));
    }

    @Override
    public final <T> T get(String fieldName, Class<? extends T> type) {
        return get(indexOrFail(fields, fieldName), type);
    }

    @Override
    public final <U> U get(String fieldName, Converter<?, ? extends U> converter) {
        return Convert.convert(get(fieldName), converter);
    }

    @Override
    public final Object get(Name fieldName) {
        return get(indexOrFail(fields, fieldName));
    }

    @Override
    public final <T> T get(Name fieldName, Class<? extends T> type) {
        return get(indexOrFail(fields, fieldName), type);
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
        set(index, field(index), value);
    }

    @Override
    public final <T> void set(Field<T> field, T value) {
        int index = fields.indexOf(field);

        set(field, index, value);
    }

    final <T> void set(Field<T> field, int index, T value) {
        if (index >= 0)
            set(index, field, value);
        else if (nonReplacingEmbeddable(field)) {
            Field<?>[] f = embeddedFields(field);
            Object[] v = value instanceof EmbeddableRecord e
                ? e.intoArray()
                : new Object[f.length];

            for (int i = 0; i < f.length; i++)
                set(indexOrFail(fields, f[i]), f[i], v[i]);
        }
        else
            throw indexFail(fields, field);
    }

    final void set(int index, Field<?> field, Object value) {
        // Relevant issues documenting this method's behaviour:
        // [#945] Avoid bugs resulting from setting the same value twice
        // [#948] To allow for controlling the number of hard-parses
        //        To allow for explicitly overriding default values
        // [#979] Avoid modifying chnaged flag on unchanged primary key values

        UniqueKey<?> key = getPrimaryKey();

        // Normal fields' changed flag is always set to true
        if (key == null || !key.getFields().contains(field)) {
            touched.set(index);
        }

        // The primary key's changed flag might've been set previously
        else if (touched.get(index)) {
            touched.set(index);
        }

        // [#2764] Users may override updatability of primary key values
        else if (updatablePrimaryKeys(settings(this))) {
            touched.set(index);
        }

        // [#2698] If the primary key has not yet been set
        else if (originals[index] == null) {
            touched.set(index);
        }

        // [#979] If the primary key is being touched, all other fields' flags
        // need to be set to true for in case this record is stored again, an
        // INSERT statement will thus be issued
        else {

            // [#945] Be sure that touched is never reset to false
            touched.set(index, touched.get(index) || !StringUtils.equals(values[index], value));

            if (touched.get(index))
                touched(true);
        }

        values[index] = value;
    }

    @Override
    public final <T, U> void set(Field<T> field, U value, Converter<? extends T, ? super U> converter) {
        set(field, scoped(converter).to(value, converterContext(this)));
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
            int targetIndex = indexOrFail(this.fields, field);
            int sourceIndex = indexOrFail(record.fields, field);

            values[targetIndex] = record.get(sourceIndex);
            originals[targetIndex] = record.original(sourceIndex);
            touched.set(targetIndex, record.touched(sourceIndex));
        }
    }

    final int safeIndex(int index) {
        if (index >= 0 && index < values.length)
            return index;

        throw indexFail(fields, index);
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
        return Tools.newRecord(fetched, configuration(), (Class<AbstractRecord>) getClass(), fields)
                    .operate(record -> {
                        for (int i = 0; i < originals.length; i++)
                            record.values[i] = record.originals[i] = originals[i];

                        return record;
                    });
    }

    @Override
    public final <T> T original(Field<T> field) {
        int index = fields.indexOf(field);

        if (index >= 0)
            return (T) original(index);
        else if (nonReplacingEmbeddable(field))
            return (T) Tools
                .newRecord(fetched, configuration(), ((EmbeddableTableField<?, ?>) field).recordType)
                .operate(((AbstractRecord) original()).new TransferRecordState<>(embeddedFields(field)));
        else
            throw Tools.indexFail(fields, field);
    }

    @Override
    public final Object original(int fieldIndex) {
        return originals[safeIndex(fieldIndex)];
    }

    @Override
    public final Object original(String fieldName) {
        return original(indexOrFail(fields, fieldName));
    }

    @Override
    public final Object original(Name fieldName) {
        return original(indexOrFail(fields, fieldName));
    }

    @Override
    @Deprecated
    public final boolean changed() {
        return touched();
    }

    @Override
    @Deprecated
    public final boolean changed(Field<?> field) {
        return touched(field);
    }

    @Override
    @Deprecated
    public final boolean changed(int fieldIndex) {
        return touched(fieldIndex);
    }

    @Override
    @Deprecated
    public final boolean changed(String fieldName) {
        return touched(fieldName);
    }

    @Override
    @Deprecated
    public final boolean changed(Name fieldName) {
        return touched(fieldName);
    }

    @Override
    @Deprecated
    public final void changed(boolean c) {
        touched(c);
    }

    @Override
    @Deprecated
    public final void changed(Field<?> field, boolean c) {
        touched(field, c);
    }

    @Override
    @Deprecated
    public final void changed(int fieldIndex, boolean c) {
        touched(fieldIndex, c);
    }

    @Override
    @Deprecated
    public final void changed(String fieldName, boolean c) {
        touched(fieldName, c);
    }

    @Override
    @Deprecated
    public final void changed(Name fieldName, boolean c) {
        touched(fieldName, c);
    }

    @Override
    public final boolean touched() {
        return !touched.isEmpty();
    }

    @Override
    public final boolean touched(Field<?> field) {
        int index = fields.indexOf(field);

        if (index >= 0)
            return touched(index);
        else if (nonReplacingEmbeddable(field))
            return anyMatch(embeddedFields(field), f -> touched(f));
        else
            throw Tools.indexFail(fields, field);
    }

    @Override
    public final boolean touched(int fieldIndex) {
        return touched.get(safeIndex(fieldIndex));
    }

    @Override
    public final boolean touched(String fieldName) {
        return touched(indexOrFail(fields, fieldName));
    }

    @Override
    public final boolean touched(Name fieldName) {
        return touched(indexOrFail(fields, fieldName));
    }

    @Override
    public final void touched(boolean c) {
        touched.set(0, values.length, c);

        // [#1995] If a value is meant to be "unchanged", the "original" should
        // match the supposedly "unchanged" value.
        if (!c)
            System.arraycopy(values, 0, originals, 0, values.length);
    }

    @Override
    public final void touched(Field<?> field, boolean c) {
        int index = fields.indexOf(field);

        if (index >= 0)
            touched(index, c);
        else if (nonReplacingEmbeddable(field))
            for (Field<?> f : embeddedFields(field))
                touched(f, c);
        else
            throw Tools.indexFail(fields, field);
    }

    @Override
    public final void touched(int fieldIndex, boolean c) {
        safeIndex(fieldIndex);

        touched.set(fieldIndex, c);

        // [#1995] If a value is meant to be "unchanged", the "original" should
        // match the supposedly "unchanged" value.
        if (!c)
            originals[fieldIndex] = values[fieldIndex];
    }

    @Override
    public final void touched(String fieldName, boolean c) {
        touched(indexOrFail(fields, fieldName), c);
    }

    @Override
    public final void touched(Name fieldName, boolean c) {
        touched(indexOrFail(fields, fieldName), c);
    }

    @Override
    public final boolean modified() {
        for (int i = 0; i < size(); i++)
            if (modified(i))
                return true;


        return false;
    }

    @Override
    public final boolean modified(Field<?> field) {
        int index = fields.indexOf(field);

        if (index >= 0)
            return modified(index);
        else if (nonReplacingEmbeddable(field))
            return anyMatch(embeddedFields(field), f -> modified(f));
        else
            throw Tools.indexFail(fields, field);
    }

    @Override
    public final boolean modified(int fieldIndex) {
        int i = safeIndex(fieldIndex);
        return touched.get(i) && !deepEqual(values[i], originals[i]);
    }

    @Override
    public final boolean modified(String fieldName) {
        return modified(indexOrFail(fields, fieldName));
    }

    @Override
    public final boolean modified(Name fieldName) {
        return modified(indexOrFail(fields, fieldName));
    }

    @Override
    public final void reset() {
        touched.clear();

        System.arraycopy(originals, 0, values, 0, originals.length);
    }

    @Override
    public final void reset(Field<?> field) {
        int index = fields.indexOf(field);

        if (index >= 0)
            reset(index);
        else if (nonReplacingEmbeddable(field))
            for (Field<?> f : embeddedFields(field))
                reset(f);
        else
            throw Tools.indexFail(fields, field);
    }

    @Override
    public final void reset(int fieldIndex) {
        safeIndex(fieldIndex);

        touched.clear(fieldIndex);
        values[fieldIndex] = originals[fieldIndex];
    }

    @Override
    public final void reset(String fieldName) {
        reset(indexOrFail(fields, fieldName));
    }

    @Override
    public final void reset(Name fieldName) {
        reset(indexOrFail(fields, fieldName));
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
        Map<String, Object> map = new LinkedHashMap<>();

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
        return Tools.newRecord(fetched, configuration(), Record.class, Tools.row0(f)).operate(new TransferRecordState<Record>(f));
    }



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



    @Override
    public final <E> E into(Class<? extends E> type) {
        return (E) ((FieldsImpl) fields.fields).mapper(Tools.configuration(this), type).map(this);
    }

    // [#10191] Java and Kotlin can produce overloads for this method despite
    // generic type erasure, but Scala cannot
    @Override
    public /* final */ <E> E into(E object) {
        if (object == null)
            throw new NullPointerException("Cannot copy Record into null");

        Class<E> type = (Class<E>) object.getClass();

        try {
            return (@NotNull E) new DefaultRecordMapper<Record, E>((FieldsImpl) fields.fields, type, object, configuration()).map(this);
        }

        // Pass MappingExceptions on to client code
        catch (MappingException e) {
            throw e;
        }

        // All other reflection exceptions are intercepted
        catch (Exception e) {
            throw new MappingException("An error occurred when mapping record to " + type, e);
        }
    }

    @Override
    public final <R extends Record> R into(Table<R> table) {
        return Tools.newRecord(fetched, configuration(), table).operate(new TransferRecordState<>(table.fields()));
    }

    final <R extends Record> R intoRecord(R record) {
        return Tools.newRecord(fetched, configuration(), () -> record).operate(new TransferRecordState<>(null));
    }

    final <R extends Record> R intoRecord(Class<R> type) {
        return (R) Tools.newRecord(fetched, configuration(), type, fields).operate(new TransferRecordState<>(null));
    }

    class TransferRecordState<R extends Record> implements ThrowingFunction<R, R, MappingException> {

        final ConverterContext converterContext;
        final Field<?>[]       targetFields;

        TransferRecordState(Field<?>[] targetFields) {
            this.targetFields = targetFields;
            this.converterContext = converterContext(AbstractRecord.this);
        }

        @Override
        public R apply(R target) throws MappingException {
            AbstractRecord source = AbstractRecord.this;

            try {

                // [#1522] [#2989] If possible the complete state of this record should be copied onto the other record
                if (target instanceof AbstractRecord t) {

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
                            t.touched.set(targetIndex, touched.get(sourceIndex));
                        }
                    }
                }

                else {
                    for (Field<?> targetField : target.fields()) {
                        Field<?> sourceField = field(targetField);

                        if (sourceField != null)
                            Tools.setValue(target, targetField, source, sourceField, converterContext);
                    }
                }

                return target;
            }

            // All reflection exceptions are intercepted
            catch (Exception e) {
                throw new MappingException("An error occurred when mapping record to " + target, e);
            }
        }
    }

    @Override
    public final ResultSet intoResultSet() {
        return asResult().intoResultSet();
    }

    @Override
    public final <E> E map(RecordMapper<Record, E> mapper) {
        return (@NotNull E) mapper.map(this);
    }

    private final void from0(Object source, int[] targetIndexMapping) {
        if (source == null) return;

        // [#2520] TODO: Benchmark this from() method. There's probably a better implementation
        from(
            Tools.configuration(this).recordUnmapperProvider().provide(source.getClass(), (FieldsImpl) fields.fields).unmap(prepareArrayOrIterableForUnmap(source, targetIndexMapping)),
            targetIndexMapping
        );

        // [#2700] [#3582] If a POJO attribute is NULL, but the column is NOT NULL
        // then we should let the database apply DEFAULT values
        Tools.resetTouchedOnNotNull(this);
    }

    /**
     * Generated subclasses may call this method.
     *
     * @deprecated - [#12494] - 3.20.0 - Please re-generate your code
     */
    @Deprecated
    protected /* non-final */ void resetChangedOnNotNull() {
        resetTouchedOnNotNull();
    }

    /**
     * Generated subclasses may call this method.
     */
    protected /* non-final */ void resetTouchedOnNotNull() {
        Tools.resetTouchedOnNotNull(this);
    }

    private final Object prepareArrayOrIterableForUnmap(Object source, int[] targetIndexMapping) {
        if (targetIndexMapping == null)
            return source;

        boolean array = source instanceof Object[];
        Iterable<?> iterable =
              array
            ? asList((Object[]) source)
            : source instanceof Iterable
            ? (Iterable<?>) source
            : null;

        if (iterable != null) {
            Object[] result = new Object[size()];
            Iterator<?> it = iterable.iterator();

            for (int i = 0; it.hasNext() && i < targetIndexMapping.length; i++) {
                int index = targetIndexMapping[i];
                Object o = it.next();

                if (index >= 0 && index < result.length)
                    result[index] = o;
            }

            return array ? result : asList(result);
        }
        else
            return source;
    }

    @Override
    public final void from(Object source) {
        from0(source, null);
    }

    @Override
    public final void from(Object source, Field<?>... f) {
        from0(source, fields.fields.indexesOf(f));
    }

    @Override
    public final void from(Object source, String... fieldNames) {
        from0(source, fields.fields.indexesOf(fieldNames));
    }

    @Override
    public final void from(Object source, Name... fieldNames) {
        from0(source, fields.fields.indexesOf(fieldNames));
    }

    @Override
    public final void from(Object source, int... fieldIndexes) {
        from0(source, fieldIndexes);
    }

    @Override
    public final void fromMap(Map<String, ?> map) {
        from(map);
    }

    @Override
    public final void fromMap(Map<String, ?> map, Field<?>... f) {
        from(map, f);
    }

    @Override
    public final void fromMap(Map<String, ?> map, String... fieldNames) {
        from(map, fieldNames);
    }

    @Override
    public final void fromMap(Map<String, ?> map, Name... fieldNames) {
        from(map, fieldNames);
    }

    @Override
    public final void fromMap(Map<String, ?> map, int... fieldIndexes) {
        from(map, fieldIndexes);
    }

    @Override
    public final void fromArray(Object... array) {
        from(array);
    }

    @Override
    public final void fromArray(Object[] array, Field<?>... f) {
        from(array, f);
    }

    @Override
    public final void fromArray(Object[] array, String... fieldNames) {
        from(array, fieldNames);
    }

    @Override
    public final void fromArray(Object[] array, Name... fieldNames) {
        from(array, fieldNames);
    }

    @Override
    public final void fromArray(Object[] array, int... fieldIndexes) {
        from(array, fieldIndexes);
    }

    /**
     * Load {@link UDTRecord} content into this record, e.g. after member procedure calls.
     */
    protected final void from(Record source) {
        ConverterContext cc = Tools.converterContext(this);

        for (Field<?> field : fields.fields.fields) {
            Field<?> sourceField = source.field(field);

            if (sourceField != null && source.touched(sourceField))
                Tools.setValue(this, field, source, sourceField, cc);
        }
    }

    final void from(Record source, int[] indexMapping) {
        int s = source.size();
        int t = indexMapping == null ? s : indexMapping.length;

        for (int i = 0; i < s && i < t; i++) {
            int j = indexMapping == null ? i : indexMapping[i];

            // [#12697] Don't re-apply data type conversion, assuming it already happened
            if (source.field(j) != null && source.touched(j))
                set((Field) field(j), j, source.get(j));
        }
    }

    // -------------------------------------------------------------------------
    // Formatting methods
    // -------------------------------------------------------------------------

    @Override
    public final void format(Writer writer, TXTFormat format) {
        asResult().format(writer, format);
    }

    @Override
    public final void formatCSV(Writer writer, CSVFormat format)  {
        asResult().formatCSV(writer, format);
    }

    @Override
    public final void formatJSON(Writer writer, JSONFormat format) {
        format = format.mutable(true);

        try {
            switch (format.recordFormat()) {
                case ARRAY:
                    AbstractResult.formatJSONArray0(this, fields, format, 0, writer);
                    break;
                case OBJECT:
                    AbstractResult.formatJSONMap0(this, fields, format, 0, writer);
                    break;
                default:
                    throw new IllegalArgumentException("Format not supported: " + format);
            }

            writer.flush();
        }
        catch (java.io.IOException e) {
            throw new IOException("Exception while writing JSON", e);
        }
    }

    @Override
    public final void formatXML(Writer writer, XMLFormat format) {
        format = format.mutable(true);

        try {
            AbstractResult.formatXMLRecord(writer, format, 0, this, fields);
            writer.flush();
        }
        catch (java.io.IOException e) {
            throw new IOException("Exception while writing XML", e);
        }
    }

    @Override
    public final void formatHTML(Writer writer) {
        Result<AbstractRecord> result = asResult();
        result.formatHTML(writer);
    }

    @Override
    public final void formatChart(Writer writer, ChartFormat format) {
        Result<AbstractRecord> result = asResult();
        result.formatChart(writer, format);
    }

    @Override
    public final void formatInsert(Writer writer) {
        formatInsert(writer, null, fields.fields.fields);
    }

    @Override
    public final void formatInsert(Writer writer, Table<?> table, Field<?>... f) {
        Result<AbstractRecord> result = asResult();
        result.formatInsert(writer, table, f);
    }

    @Override
    public final Document intoXML(XMLFormat format) {
        Result<AbstractRecord> result = asResult();
        return result.intoXML(format);
    }

    @Override
    public final <H extends ContentHandler> H intoXML(H handler, XMLFormat format) throws SAXException {
        Result<AbstractRecord> result = asResult();
        return result.intoXML(handler, format);
    }

    // ------------------------------------------------------------------------
    // XXX: Object and Comparable API
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        // [#3900] Nested records should generate different toString() behaviour
        return ThreadGuard.run(ThreadGuard.RECORD_TOSTRING, () -> asResult().toString(), () -> valuesRow().toString());
    }

    @Override
    public int compareTo(Record that) {
        // Note: keep this implementation in-sync with AbstractStore.equals()!

        if (that == this)
            return 0;
        if (that == null)
            throw new NullPointerException();
        if (size() != that.size())
            throw new ClassCastException(String.format("Trying to compare incomparable records (wrong degree):\n%s\n%s", this, that));

        Class<?>[] thisTypes = this.fieldsRow().types();
        Class<?>[] thatTypes = that.fieldsRow().types();

        if (!asList(thisTypes).equals(asList(thatTypes)))
            throw new ClassCastException(String.format("Trying to compare incomparable records (type mismatch):\n%s\n%s", this, that));

        for (int i = 0; i < size(); i++) {
            final Object thisValue = get(i);
            final Object thatValue = that.get(i);

            // [#1850] Only return -1/+1 early. In all other cases,
            // continue checking the remaining fields
            if (thisValue == null && thatValue == null)
                continue;

            // Order column values in a SQL NULLS LAST manner
            else if (thisValue == null)
                return 1;

            else if (thatValue == null)
                return -1;

            // [#985] Compare arrays too.
            else if (thisValue.getClass().isArray() && thatValue.getClass().isArray()) {

                // Might be byte[]
                if (thisValue.getClass() == byte[].class) {
                    int compare = compare((byte[]) thisValue, (byte[]) thatValue);

                    if (compare != 0)
                        return compare;
                }

                // Other primitive types are not expected
                else if (!thisValue.getClass().getComponentType().isPrimitive()) {
                    int compare = compare((Object[]) thisValue, (Object[]) thatValue);

                    if (compare != 0)
                        return compare;
                }

                else
                    throw new ClassCastException(String.format("Unsupported data type in natural ordering: %s", thisValue.getClass()));
            }
            else {
                int compare = compare0(thisValue, thatValue);

                if (compare != 0)
                    return compare;
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

            if (v1 != v2)
                return v1 < v2 ? -1 : 1;
        }

        return array1.length - array2.length;
    }

    /**
     * Compare two arrays
     */
    final int compare(Object[] array1, Object[] array2) {
        int length = Math.min(array1.length, array2.length);

        for (int i = 0; i < length; i++) {
            int compare = compare0(array1[i], array2[i]);

            if (compare != 0)
                return compare;
        }

        return array1.length - array2.length;
    }

    final int compare0(Object o1, Object o2) {
        return o1 == o2
             ? 0
             : o1 == null
             ? -1
             : o2 == null
             ? 1
             : o1 instanceof Comparable<?> && o1.getClass() == o2.getClass()
             ? ((Comparable) o1).compareTo(o2)
             : o1.hashCode() - o2.hashCode();
    }

    // -------------------------------------------------------------------------
    // XXX: Deprecated and discouraged methods
    // -------------------------------------------------------------------------

    @Override
    public final <T> T getValue(Field<T> field) {
        return get(field);
    }

    @Override
    public final <T> T getValue(Field<?> field, Class<? extends T> type) {
        return get(field, type);
    }

    @Override
    public final <T, U> U getValue(Field<T> field, Converter<? super T, ? extends U> converter) {
        return get(field, converter);
    }

    @Override
    public final Object getValue(int index) {
        return get(index);
    }

    @Override
    public final <T> T getValue(int index, Class<? extends T> type) {
        return get(index, type);
    }

    @Override
    public final <U> U getValue(int index, Converter<?, ? extends U> converter) {
        return get(index, converter);
    }

    @Override
    public final Object getValue(String fieldName) {
        return get(fieldName);
    }

    @Override
    public final <T> T getValue(String fieldName, Class<? extends T> type) {
        return get(fieldName, type);
    }

    @Override
    public final <U> U getValue(String fieldName, Converter<?, ? extends U> converter) {
        return get(fieldName, converter);
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

    final Result<AbstractRecord> asResult() {
        Result<AbstractRecord> result = new ResultImpl<>(configuration(), fields);
        result.add(this);
        return result;
    }
}
