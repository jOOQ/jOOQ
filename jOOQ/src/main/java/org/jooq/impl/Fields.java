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

import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.indexOrFail;

import java.sql.SQLWarning;
import java.util.Arrays;
import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.RecordType;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.tools.JooqLogger;

/**
 * A simple wrapper for <code>Field[]</code>, providing some useful lookup
 * methods
 *
 * @author Lukas Eder
 */
final class Fields<R extends Record> extends AbstractQueryPart implements RecordType<R> {

    private static final long       serialVersionUID = -6911012275707591576L;
    private static final JooqLogger log              = JooqLogger.getLogger(Fields.class);
    Field<?>[]                      fields;

    Fields(Field<?>... fields) {
        this.fields = fields;
    }

    Fields(Collection<? extends Field<?>> fields) {
        this.fields = fields.toArray(EMPTY_FIELD);
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

        // [#4283] table / column matches are better than only column matches
        Field<?> columnMatch = null;
        Field<?> columnMatch2 = null;
        int indexMatch = -1;

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
                if (columnMatch == null) {
                    columnMatch = f;
                    indexMatch = i;
                }

                // [#4476] [#4477] This might be unintentional from a user
                //                 perspective, e.g. when ambiguous ID columns are present.
                // [#5578] Finish the loop, though, as we might have an exact match
                //         despite some ambiguity
                else {
                    columnMatch2 = f;
                }
            }
        }

        if (columnMatch2 != null)
            if (log.isInfoEnabled())
                log.info("Ambiguous match found for " + fieldName + ". Both " + columnMatch + " and " + columnMatch2 + " match.", new SQLWarning());

        return result.result(columnMatch, indexMatch);
    }

    private final String tableName(Field<?> field) {
        if (field instanceof TableField) {
            Table<?> table = ((TableField<?, ?>) field).getTable();

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
    public final Field<?>[] fields(Field<?>... f) {
        Field<?>[] result = new Field[f.length];

        for (int i = 0; i < result.length; i++)
            result[i] = field(f[i]);

        return result;
    }

    @Override
    public final Field<?>[] fields(String... f) {
        Field<?>[] result = new Field[f.length];

        for (int i = 0; i < result.length; i++)
            result[i] = field(f[i]);

        return result;
    }

    @Override
    public final Field<?>[] fields(Name... f) {
        Field<?>[] result = new Field[f.length];

        for (int i = 0; i < result.length; i++)
            result[i] = field(f[i]);

        return result;
    }

    @Override
    public final Field<?>[] fields(int... f) {
        Field<?>[] result = new Field[f.length];

        for (int i = 0; i < result.length; i++)
            result[i] = field(f[i]);

        return result;
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
        int size = fields.length;
        Class<?>[] result = new Class[size];

        for (int i = 0; i < size; i++)
            result[i] = field(i).getType();

        return result;
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
        int size = fields.length;
        DataType<?>[] result = new DataType[size];

        for (int i = 0; i < size; i++)
            result[i] = field(i).getDataType();

        return result;
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

        for (int i = 0; i < fieldNames.length; i++) {
            result[i] = indexOrFail(this, fieldNames[i]);
        }

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
        ctx.visit(new QueryPartList<Field<?>>(fields));
    }

    // -------------------------------------------------------------------------
    // XXX: List-like API
    // -------------------------------------------------------------------------

    final void add(Field<?> f) {
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

        if (that instanceof Fields)
            return Arrays.equals(fields, ((Fields<?>) that).fields);

        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(fields);
    }
}
