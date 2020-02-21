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

import static org.jooq.Clause.FIELD_ROW;
// ...
import static org.jooq.impl.Keywords.K_ROW;

import java.util.Collection;
import java.util.stream.Stream;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Row;
import org.jooq.Row1;
import org.jooq.Row2;

/**
 * A common base class for the various degrees of {@link Row1}, {@link Row2},
 * etc.
 */
abstract class AbstractRow extends AbstractQueryPart implements Row {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 2175082265665049629L;
    private static final Clause[] CLAUSES          = { FIELD_ROW };

    final Fields<?>               fields;

    AbstractRow(Field<?>... fields) {
        this(new Fields<>(fields));
    }

    AbstractRow(Collection<? extends Field<?>> fields) {
        this(new Fields<>(fields));
    }

    AbstractRow(Fields<?> fields) {
        super();

        this.fields = fields;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> context) {






        context.sql("(");

        String separator = "";
        for (Field<?> field : fields.fields) {
            context.sql(separator);
            context.visit(field);

            separator = ", ";
        }

        context.sql(")");
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    // ------------------------------------------------------------------------
    // XXX: Row accessor API
    // ------------------------------------------------------------------------

    @Override
    public final int size() {
        return fields.size();
    }


    @Override
    public final Stream<Field<?>> fieldStream() {
        return Stream.of(fields());
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

    @Override
    public final int indexOf(Field<?> field) {
        return fields.indexOf(field);
    }

    @Override
    public final int indexOf(String fieldName) {
        return fields.indexOf(fieldName);
    }

    @Override
    public final int indexOf(Name fieldName) {
        return fields.indexOf(fieldName);
    }

    @Override
    public final Class<?>[] types() {
        return fields.types();
    }

    @Override
    public final Class<?> type(int fieldIndex) {
        return fields.type(fieldIndex);
    }

    @Override
    public final Class<?> type(String fieldName) {
        return fields.type(fieldName);
    }

    @Override
    public final Class<?> type(Name fieldName) {
        return fields.type(fieldName);
    }

    @Override
    public final DataType<?>[] dataTypes() {
        return fields.dataTypes();
    }

    @Override
    public final DataType<?> dataType(int fieldIndex) {
        return fields.dataType(fieldIndex);
    }

    @Override
    public final DataType<?> dataType(String fieldName) {
        return fields.dataType(fieldName);
    }

    @Override
    public final DataType<?> dataType(Name fieldName) {
        return fields.dataType(fieldName);
    }

    // ------------------------------------------------------------------------
    // [NOT] NULL predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition isNull() {
        return new RowIsNull(this, true);
    }

    @Override
    public final Condition isNotNull() {
        return new RowIsNull(this, false);
    }

}
