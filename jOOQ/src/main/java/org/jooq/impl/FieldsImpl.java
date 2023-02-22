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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.converterOrFail;
import static org.jooq.impl.Tools.indexOrFail;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.newRecord;
import static org.jooq.impl.Tools.unaliasTable;

import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordType;
import org.jooq.Row;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.QOM.UTransient;
import org.jooq.tools.JooqLogger;


/**
 * A simple wrapper for <code>Field[]</code>, providing some useful lookup
 * methods
 *
 * @author Lukas Eder
 */
final class FieldsImpl<R extends Record> extends AbstractQueryPart implements RecordType<R>, Mappable<R>, UTransient {

    private static final JooqLogger log = JooqLogger.getLogger(FieldsImpl.class);
    Field<?>[]                      fields;

    FieldsImpl(SelectField<?>... fields) {
        this.fields = Tools.map(fields, toField(), Field<?>[]::new);
    }

    FieldsImpl(Collection<? extends SelectField<?>> fields) {
        this.fields = Tools.map(fields, toField(), Field<?>[]::new);
    }

    // -------------------------------------------------------------------------
    // Mappable API
    // -------------------------------------------------------------------------

    @Override
    public final RecordMapper<R, ?> mapper(int fieldIndex) {
        return r -> r.get(fieldIndex);
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(int fieldIndex, Configuration configuration, Class<? extends U> type) {
        return mapper(fieldIndex, converterOrFail(configuration, null, fields[safeIndex(fieldIndex)].getType(), type));
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(int fieldIndex, Converter<?, ? extends U> converter) {
        return r -> r.get(fieldIndex, converter);
    }

    @Override
    public final RecordMapper<R, Record> mapper(int[] fieldIndexes) {
        return mapper(fields(fieldIndexes));
    }

    @Override
    public final RecordMapper<R, ?> mapper(String fieldName) {
        return mapper(indexOrFail(this, fieldName));
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(String fieldName, Configuration configuration, Class<? extends U> type) {
        return mapper(fieldName, converterOrFail(configuration, null, field(indexOrFail(this, fieldName)).getType(), type));
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(String fieldName, Converter<?, ? extends U> converter) {
        return r -> r.get(fieldName, converter);
    }

    @Override
    public final RecordMapper<R, Record> mapper(String[] fieldNames) {
        return mapper(fields(fieldNames));
    }

    @Override
    public final RecordMapper<R, ?> mapper(Name fieldName) {
        return mapper(indexOrFail(this, fieldName));
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(Name fieldName, Configuration configuration, Class<? extends U> type) {
        return mapper(fieldName, converterOrFail(configuration, null, field(indexOrFail(this, fieldName)).getType(), type));
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(Name fieldName, Converter<?, ? extends U> converter) {
        return r -> r.get(fieldName, converter);
    }

    @Override
    public final RecordMapper<R, Record> mapper(Name[] fieldNames) {
        return mapper(fields(fieldNames));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> RecordMapper<R, T> mapper(Field<T> field) {
        return (RecordMapper<R, T>) mapper(indexOrFail(this, field));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final <U> RecordMapper<R, U> mapper(Field<?> field, Configuration configuration, Class<? extends U> type) {
        return mapper(field, (Converter) converterOrFail(configuration, null, field.getType(), type));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T, U> RecordMapper<R, U> mapper(Field<T> field, Converter<? super T, ? extends U> converter) {
        return (RecordMapper<R, U>) mapper(indexOrFail(this, field), converter);
    }

    @Override
    public final RecordMapper<R, Record> mapper(Field<?>[] f) {
        AbstractRow<?> row = Tools.row0(f == null ? EMPTY_FIELD : f);

        return r -> newRecord(false, AbstractRecord.class, row, r.configuration()).operate(x -> {
            for (Field<?> field : row.fields.fields)
                Tools.copyValue((AbstractRecord) x, field, r, field);

            return x;
        });
    }

    @Override
    public final <S extends Record> RecordMapper<R, S> mapper(Table<S> table) {
        return r -> r.into(table);
    }

    @Override
    public final <E> RecordMapper<R, E> mapper(Configuration configuration, Class<? extends E> type) {
        return configuration.recordMapperProvider().provide(this, type);
    }

    // -------------------------------------------------------------------------
    // RecordType API
    // -------------------------------------------------------------------------

    /**
     * [#13341] Prevent costly calls to Select.asTable() where not strictly
     * needed.
     */
    static final Row fieldsRow0(FieldsTrait fields) {
        return fields instanceof Select<?> s ? s.asTable("t").fieldsRow() : fields.fieldsRow();
    }

    private static final ThrowingFunction<SelectField<?>, Field<?>, RuntimeException> toField() {
        return f -> f instanceof Row r
                  ? new RowAsField<>(r)
                  : f instanceof Table<?> t
                  ? new TableAsField<>(t)
                  : (Field<?>) f;
    }

    @Override
    public final int size() {
        return fields.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> Field<T> field(Field<T> field) {
        return (Field<T>) field0(field, RETURN_FIELD);
    }

    private final <U> U field0(Field<?> field, FieldOrIndex<U> result) {
        if (field == null)
            return result.resultNull();

        // [#4540] Try finding a match by identity
        for (int i = 0; i < fields.length; i++) {
            Field<?> f = fields[i];

            if (f == field)
                return result.result(f, i);
        }

        // [#1802] Try finding an exact match (e.g. exact matching qualified name)
        for (int i = 0; i < fields.length; i++) {
            Field<?> f = fields[i];

            if (f.equals(field))
                return result.result(f, i);
        }

        // [#4283] table / column matches are better than column only matches
        Field<?> columnOnlyMatch = null;
        Field<?> columnOnlyMatch2 = null;
        int columnOnlyIndexMatch = -1;

        // [#14671] column only matches might still match on the unaliased table
        Field<?> unaliased = null;
        Field<?> aliasMatch = null;
        Field<?> aliasMatch2 = null;
        int aliasIndexMatch = -1;

        String tableName = tableName(field);
        String fieldName = field.getName();

        for (int i = 0; i < fields.length; i++) {
            Field<?> f = fields[i];
            String fName = f.getName();

            if (tableName != null) {
                String tName = tableName(f);

                if (tName != null && tableName.equals(tName) && fName.equals(fieldName))
                    return result.result(f, i);
            }

            // In case no exact match was found, return the first field with matching name
            if (fName.equals(fieldName)) {

                // [#14671] Prefer matches by unaliased tables, if applicable
                if (unaliased == null)
                    unaliased = unaliasTable(field);

                if (unaliased != null && unaliased.equals(unaliasTable(f))) {
                    if (aliasMatch == null) {
                        aliasMatch = f;
                        aliasIndexMatch = i;
                    }
                    else
                        aliasMatch2 = f;
                }

                if (columnOnlyMatch == null) {
                    columnOnlyMatch = f;
                    columnOnlyIndexMatch = i;
                }

                // [#4476] [#4477] This might be unintentional from a user
                //                 perspective, e.g. when ambiguous ID columns are present.
                // [#5578] Finish the loop, though, as we might have an exact match
                //         despite some ambiguity
                else
                    columnOnlyMatch2 = f;
            }
        }

        if (aliasMatch2 != null && log.isInfoEnabled())
            log.info("Ambiguous match found for " + fieldName + ". Both " + aliasMatch + " and " + aliasMatch2 + " match.", new SQLWarning());

        if (aliasMatch != null)
            return result.result(aliasMatch, aliasIndexMatch);

        if (columnOnlyMatch2 != null && log.isInfoEnabled())
            log.info("Ambiguous match found for " + fieldName + ". Both " + columnOnlyMatch + " and " + columnOnlyMatch2 + " match.", new SQLWarning());

        return result.result(columnOnlyMatch, columnOnlyIndexMatch);
    }

    private final String tableName(Field<?> field) {
        if (field instanceof TableField<?, ?> f) {
            Table<?> table = f.getTable();

            if (table != null)
                return table.getName();
        }

        return null;
    }

    @Override
    public final Field<?> field(String fieldName) {
        return field0(fieldName, RETURN_FIELD);
    }

    private final <U> U field0(String fieldName, FieldOrIndex<U> result) {
        if (fieldName == null)
            return result.resultNull();

        Field<?> columnMatch = null;
        int indexMatch = -1;

        for (int i = 0; i < fields.length; i++) {
            Field<?> f = fields[i];

            if (f.getName().equals(fieldName)) {
                if (columnMatch == null) {
                    columnMatch = f;
                    indexMatch = i;
                }

                // [#4476] [#4477] [#5046] This might be unintentional from a user
                // perspective, e.g. when ambiguous ID columns are present.
                else {
                    log.info("Ambiguous match found for " + fieldName + ". Both " + columnMatch + " and " + f + " match.", new SQLWarning());
                }
            }
        }

        return result.result(columnMatch, indexMatch);
    }

    @Override
    public final <T> Field<T> field(String fieldName, Class<T> type) {
        Field<?> result = field(fieldName);
        return result == null ? null : result.coerce(type);
    }

    @Override
    public final <T> Field<T> field(String fieldName, DataType<T> dataType) {
        Field<?> result = field(fieldName);
        return result == null ? null : result.coerce(dataType);
    }

    @Override
    public final Field<?> field(Name name) {
        return field0(name, RETURN_FIELD);
    }

    private final <U> U field0(Name name, FieldOrIndex<U> result) {
        if (name == null)
            return result.resultNull();

        return field0(DSL.field(name), result);
    }

    @Override
    public final <T> Field<T> field(Name fieldName, Class<T> type) {
        Field<?> result = field(fieldName);
        return result == null ? null : result.coerce(type);
    }

    @Override
    public final <T> Field<T> field(Name fieldName, DataType<T> dataType) {
        Field<?> result = field(fieldName);
        return result == null ? null : result.coerce(dataType);
    }

    @Override
    public final Field<?> field(int index) {
        if (index >= 0 && index < fields.length)
            return fields[index];

        return null;
    }

    final int safeIndex(int index) {
        if (index >= 0 && index < fields.length)
            return index;

        throw new IllegalArgumentException("No field at index " + index + " in Record type " + fields);
    }

    @Override
    public final <T> Field<T> field(int fieldIndex, Class<T> type) {
        Field<?> result = field(fieldIndex);
        return result == null ? null : result.coerce(type);
    }

    @Override
    public final <T> Field<T> field(int fieldIndex, DataType<T> dataType) {
        Field<?> result = field(fieldIndex);
        return result == null ? null : result.coerce(dataType);
    }

    @Override
    public final Field<?>[] fields() {
        return fields;
    }

    @Override
    public final Row fieldsRow() {
        return new RowImplN(fields);
    }

    @Override
    public final Stream<Field<?>> fieldStream() {
        return Stream.of(fields);
    }

    @Override
    public final Field<?>[] fields(Field<?>... f) {
        return map(f, i -> field(i), Field[]::new);
    }

    @Override
    public final Field<?>[] fields(String... f) {
        return map(f, i -> field(i), Field[]::new);
    }

    @Override
    public final Field<?>[] fields(Name... f) {
        return map(f, i -> field(i), Field[]::new);
    }

    @Override
    public final Field<?>[] fields(int... f) {
        return map(f, i -> field(i), Field[]::new);
    }

    @Override
    public final int indexOf(Field<?> field) {
        return field0(field, RETURN_INDEX);
    }

    @Override
    public final int indexOf(String fieldName) {
        return field0(fieldName, RETURN_INDEX);
    }

    @Override
    public final int indexOf(Name fieldName) {
        return field0(fieldName, RETURN_INDEX);
    }

    @Override
    public final Class<?>[] types() {
        return Tools.map(fields, f -> f.getType(), Class[]::new);
    }

    @Override
    public final Class<?> type(int fieldIndex) {
        return fieldIndex >= 0 && fieldIndex < size() ? field(fieldIndex).getType() : null;
    }

    @Override
    public final Class<?> type(String fieldName) {
        return type(indexOrFail(this, fieldName));
    }

    @Override
    public final Class<?> type(Name fieldName) {
        return type(indexOrFail(this, fieldName));
    }

    @Override
    public final DataType<?>[] dataTypes() {
        return map(fields, f -> f.getDataType(), DataType[]::new);
    }

    @Override
    public final DataType<?> dataType(int fieldIndex) {
        return fieldIndex >= 0 && fieldIndex < size() ? field(fieldIndex).getDataType() : null;
    }

    @Override
    public final DataType<?> dataType(String fieldName) {
        return dataType(indexOrFail(this, fieldName));
    }

    @Override
    public final DataType<?> dataType(Name fieldName) {
        return dataType(indexOrFail(this, fieldName));
    }

    final int[] indexesOf(Field<?>... f) {
        int[] result = new int[f.length];

        for (int i = 0; i < f.length; i++)
            result[i] = indexOrFail(this, f[i]);

        return result;
    }

    final int[] indexesOf(String... fieldNames) {
        int[] result = new int[fieldNames.length];

        for (int i = 0; i < fieldNames.length; i++)
            result[i] = indexOrFail(this, fieldNames[i]);

        return result;
    }

    final int[] indexesOf(Name... fieldNames) {
        int[] result = new int[fieldNames.length];

        for (int i = 0; i < fieldNames.length; i++)
            result[i] = indexOrFail(this, fieldNames[i]);

        return result;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(wrap(fields));
    }

    // -------------------------------------------------------------------------
    // XXX: List-like API
    // -------------------------------------------------------------------------

    final void add(Field<?> f) {

        // TODO: [#10481] Can we replace our internal Field<?>[] by an ArrayList<Field<?>>?
        Field<?>[] result = new Field[fields.length + 1];

        System.arraycopy(fields, 0, result, 0, fields.length);
        result[fields.length] = f;

        fields = result;
    }

























    // -------------------------------------------------------------------------
    // XXX: [#8040] An abstraction over two possible return types.
    // -------------------------------------------------------------------------

    private static interface FieldOrIndex<U> {
        U result(Field<?> field, int index);
        U resultNull();
    }

    private static final FieldOrIndex<Field<?>> RETURN_FIELD = new FieldOrIndex<Field<?>>() {
        @Override
        public Field<?> result(Field<?> field, int index) {
            return field;
        }

        @Override
        public Field<?> resultNull() {
            return null;
        }
    };

    private static final FieldOrIndex<Integer> RETURN_INDEX = new FieldOrIndex<Integer>() {
        @Override
        public Integer result(Field<?> field, int index) {
            return index;
        }

        @Override
        public Integer resultNull() {
            return -1;
        }
    };

    // -------------------------------------------------------------------------
    // XXX: Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (that instanceof FieldsImpl<?> f)
            return Arrays.equals(fields, f.fields);

        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(fields);
    }
}
