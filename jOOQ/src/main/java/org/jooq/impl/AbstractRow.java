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

import static org.jooq.Clause.FIELD_ROW;
// ...
// ...
import static org.jooq.impl.Keywords.K_ROW;
import static org.jooq.impl.Names.N_ROW;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.Tools.extractVal;
import static org.jooq.impl.Tools.isVal;
import static org.jooq.impl.Tools.nullSafe;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jooq.Binding;
import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
// ...
import org.jooq.Row;
import org.jooq.Row1;
import org.jooq.Row2;
import org.jooq.Select;
import org.jooq.ContextConverter;
import org.jooq.SelectField;
// ...
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * A common base class for the various degrees of {@link Row1}, {@link Row2},
 * etc.
 */
abstract class AbstractRow<R extends Record> extends AbstractQueryPart implements Row, SelectField<R> {

    private static final Clause[] CLAUSES = { FIELD_ROW };

    final FieldsImpl<R>           fields;

    AbstractRow(SelectField<?>... fields) {
        this(new FieldsImpl<>(fields));
    }

    AbstractRow(Collection<? extends SelectField<?>> fields) {
        this(new FieldsImpl<>(fields));
    }

    AbstractRow(FieldsImpl<R> fields) {
        super();

        this.fields = fields;
    }

    // ------------------------------------------------------------------------
    // XXX: SelectField API
    // ------------------------------------------------------------------------

    final RowAsField<Row, R> rf() {
        return new RowAsField<Row, R>(this);
    }

    @Override
    public final <U> SelectField<U> convert(Binding<R, U> binding) {
        return rf().convert(binding);
    }

    @Override
    public final <U> SelectField<U> convert(Converter<R, U> converter) {
        return rf().convert(converter);
    }

    @Override
    public final <U> SelectField<U> convert(Class<U> toType, Function<? super R, ? extends U> from, Function<? super U, ? extends R> to) {
        return rf().convert(toType, from, to);
    }

    @Override
    public final <U> SelectField<U> convertFrom(Class<U> toType, Function<? super R, ? extends U> from) {
        return rf().convertFrom(toType, from);
    }

    @Override
    public final <U> SelectField<U> convertFrom(Function<? super R, ? extends U> from) {
        return rf().convertFrom(from);
    }

    @Override
    public final <U> SelectField<U> convertTo(Class<U> toType, Function<? super U, ? extends R> to) {
        return rf().convertTo(toType, to);
    }

    @Override
    public final <U> SelectField<U> convertTo(Function<? super U, ? extends R> to) {
        return rf().convertTo(to);
    }

    @Override
    public final Field<R> as(String alias) {
        return rf().as(alias);
    }

    @Override
    public final Field<R> as(Name alias) {
        return rf().as(alias);
    }

    @Override
    public final Field<R> as(Field<?> otherField) {
        return rf().as(otherField);
    }

    @Override
    public final ContextConverter<?, R> getConverter() {
        return rf().getConverter();
    }

    @Override
    public final Binding<?, R> getBinding() {
        return rf().getBinding();
    }

    @Override
    public final Class<R> getType() {
        return rf().getType();
    }

    @Override
    public final DataType<R> getDataType() {
        return rf().getDataType();
    }

    @Override
    public final DataType<R> getDataType(Configuration configuration) {
        return rf().getDataType(configuration);
    }

    @Override
    public final String getName() {
        return rf().getName();
    }

    @Override
    public final Name getQualifiedName() {
        return rf().getQualifiedName();
    }

    @Override
    public final Name getUnqualifiedName() {
        return rf().getUnqualifiedName();
    }

    @Override
    public final String getComment() {
        return rf().getComment();
    }

    @Override
    public final Comment getCommentPart() {
        return rf().getCommentPart();
    }

    /**
     * [#8517] Convert the bind values in this row to the types of columns of another row
     */
    @SuppressWarnings("rawtypes")
    final AbstractRow convertTo(Row row) {
        int size = fields.size();

        findConversionCandidates: {
            for (int i = 0; i < size; i++)
                if (isVal(fields.field(i)) && !isVal(row.field(i)))
                    break findConversionCandidates;

            return this;
        }

        Field<?>[] result = new Field[size];
        for (int i = 0; i < size; i++) {
            Field<?> f = fields.field(i);
            Val<?> v;

            if ((v = extractVal(f)) != null)
                result[i] = v.convertTo(row.field(i).getDataType());
            else
                result[i] = f;
        }

        return Tools.row0(result);
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {











        ctx.sql("(")
           .visit(wrap(fields.fields))
           .sql(")");
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    // ------------------------------------------------------------------------
    // XXX: Row accessor API
    // ------------------------------------------------------------------------

    static final Condition compare(Row arg1, Comparator comparator, Row arg2) {
        switch (comparator) {
            case EQUALS:
                return new RowEq(arg1, arg2);
            case GREATER:
                return new RowGt(arg1, arg2);
            case GREATER_OR_EQUAL:
                return new RowGe(arg1, arg2);
            case LESS:
                return new RowLt(arg1, arg2);
            case LESS_OR_EQUAL:
                return new RowLe(arg1, arg2);
            case NOT_EQUALS:
                return new RowNe(arg1, arg2);

            case IS_DISTINCT_FROM:
                return new RowIsDistinctFrom(arg1, arg2, false);
            case IS_NOT_DISTINCT_FROM:
                return new RowIsDistinctFrom(arg1, arg2, true);
        }

        throw new IllegalArgumentException("Comparator not supported: " + comparator);
    }

    final Condition compare(Comparator comparator, Row row) {
        return compare(this, comparator, row);
    }

    @Override
    public final int size() {
        return fields.size();
    }

    @Override
    public final Row fieldsRow() {
        return this;
    }

    @Override
    public final Stream<Field<?>> fieldStream() {
        return Stream.of(fields());
    }

    @Override
    public final <T> Field<T> field(Field<T> field) {
        return fields.field(field);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(String)}.
     */
    @Deprecated
    @Override
    public final Field<?> field(String name) {
        return fields.field(name);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(String, Class)}.
     */
    @Deprecated
    @Override
    public final <T> Field<T> field(String name, Class<T> type) {
        return fields.field(name, type);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(String, DataType)}.
     */
    @Deprecated
    @Override
    public final <T> Field<T> field(String name, DataType<T> dataType) {
        return fields.field(name, dataType);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(Name)}.
     */
    @Deprecated
    @Override
    public final Field<?> field(Name name) {
        return fields.field(name);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(Name, Class)}.
     */
    @Deprecated
    @Override
    public final <T> Field<T> field(Name name, Class<T> type) {
        return fields.field(name, type);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(Name, DataType)}.
     */
    @Deprecated
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
        return new RowIsNull(this);
    }

    @Override
    public final Condition isNotNull() {
        return new RowIsNotNull(this);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Name $name() {
        return N_ROW;
    }

    @Override
    public final DataType<R> $dataType() {
        return getDataType();
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $fields() {
        return QOM.unmodifiable(fields());
    }















    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return fields.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (that instanceof AbstractRow<?> r)
            return fields.equals(r.fields);

        return super.equals(that);
    }
}
