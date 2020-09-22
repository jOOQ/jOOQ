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

import static org.jooq.impl.DSL.nullSafe;
import static org.jooq.impl.ExpressionOperator.*;

import org.jooq.Binding;
import org.jooq.Check;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Domain;
import org.jooq.EmbeddableRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.Parameter;
// ...
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Support;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDT;
import org.jooq.UDTRecord;
import org.jooq.UniqueKey;
// ...
// ...

import org.jetbrains.annotations.NotNull;

/**
 * A utility class that grants access to internal API, to be used only by
 * generated code.
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly.
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public final class Internal {

    /**
     * Factory method for embeddable types.
     */
    @SafeVarargs
    @NotNull
    public static final <R extends Record, E extends EmbeddableRecord<E>> TableField<R, E> createEmbeddable(Name name, Class<E> recordType, Table<R> table, TableField<R, ?>... fields) {
        return createEmbeddable(name, recordType, false, table, fields);
    }

    /**
     * Factory method for embeddable types.
     */
    @SafeVarargs
    @NotNull
    public static final <R extends Record, E extends EmbeddableRecord<E>> TableField<R, E> createEmbeddable(Name name, Class<E> recordType, boolean replacesFields, Table<R> table, TableField<R, ?>... fields) {
        return new EmbeddableTableField<>(name, recordType, replacesFields, table, fields);
    }

    /**
     * Factory method for indexes.
     */
    @NotNull
    public static final Index createIndex(Name name, Table<?> table, OrderField<?>[] sortFields, boolean unique) {
        return new IndexImpl(name, table, sortFields, null, unique);
    }

    /**
     * Factory method for identities.
     */
    @NotNull
    public static final <R extends Record, T> Identity<R, T> createIdentity(Table<R> table, TableField<R, T> field) {
        return new IdentityImpl<>(table, field);
    }

    /**
     * Factory method for unique keys.
     */
    @NotNull
    @SafeVarargs
    public static final <R extends Record> UniqueKey<R> createUniqueKey(Table<R> table, TableField<R, ?>... fields) {
        return createUniqueKey(table, (Name) null, fields, true);
    }

    /**
     * Factory method for unique keys.
     */
    @NotNull
    @SafeVarargs
    public static final <R extends Record> UniqueKey<R> createUniqueKey(Table<R> table, Name name, TableField<R, ?>... fields) {
        return createUniqueKey(table, name, fields, true);
    }

    /**
     * Factory method for unique keys.
     */
    @NotNull
    public static final <R extends Record> UniqueKey<R> createUniqueKey(Table<R> table, Name name, TableField<R, ?>[] fields, boolean enforced) {
        return new UniqueKeyImpl<>(table, name, fields, enforced);
    }

    /**
     * Factory method for unique keys.
     */
    @NotNull
    public static final <R extends Record, ER extends EmbeddableRecord<ER>> UniqueKey<R> createUniqueKey(Table<R> table, Name name, TableField<R, ER> embeddableField, boolean enforced) {
        return createUniqueKey(table, name, fields(embeddableField), enforced);
    }

    /**
     * Factory method for foreign keys.
     *
     * @deprecated - 3.14.0 - [#9404] - Please re-generate your code.
     */
    @Deprecated
    @NotNull
    @SafeVarargs
    public static final <R extends Record, U extends Record> ForeignKey<R, U> createForeignKey(UniqueKey<U> key, Table<R> table, TableField<R, ?>... fields) {
        return createForeignKey(table, (Name) null, fields, key, key.getFieldsArray(), true);
    }

    /**
     * Factory method for foreign keys.
     */
    @NotNull
    public static final <R extends Record, U extends Record> ForeignKey<R, U> createForeignKey(Table<R> table, Name name, TableField<R, ?>[] fkFields, UniqueKey<U> uk, TableField<U, ?>[] ukFields, boolean enforced) {
        ForeignKey<R, U> result = new ReferenceImpl<>(table, name, fkFields, uk, ukFields == null ? uk.getFieldsArray() : ukFields, enforced);

        if (uk instanceof UniqueKeyImpl)
            ((UniqueKeyImpl<U>) uk).references.add(result);

        return result;
    }

    /**
     * Factory method for foreign keys.
     */
    @NotNull
    public static final <R extends Record, U extends Record, ER extends EmbeddableRecord<ER>> ForeignKey<R, U> createForeignKey(Table<R> table, Name name, TableField<R, ER> fkEmbeddableField, UniqueKey<U> uk, TableField<U, ER> ukEmbeddableField, boolean enforced) {
        return createForeignKey(table, name, fields(fkEmbeddableField), uk, fields(ukEmbeddableField), enforced);
    }

    /**
     * Factory method for sequences.
     */
    @NotNull
    public static final <T extends Number> Sequence<T> createSequence(String name, Schema schema, DataType<T> type) {
        return new SequenceImpl<>(name, schema, type, false);
    }

    /**
     * Factory method for sequences.
     */
    @NotNull
    public static final <T extends Number> Sequence<T> createSequence(String name, Schema schema, DataType<T> type, Number startWith, Number incrementBy, Number minvalue, Number maxvalue, boolean cycle, Number cache) {
        return new SequenceImpl<>(
            DSL.name(name),
            schema,
            type,
            false,
            startWith != null ? Tools.field(startWith, type) : null,
            incrementBy != null ? Tools.field(incrementBy, type) : null,
            minvalue != null ? Tools.field(minvalue, type) : null,
            maxvalue != null ? Tools.field(maxvalue, type) : null,
            cycle,
            cache != null ? Tools.field(cache, type) : null
        );
    }

    /**
     * Factory method for check constraints.
     */
    @NotNull
    public static final <R extends Record> Check<R> createCheck(Table<R> table, Name name, String condition) {
        return createCheck(table, name, condition, true);
    }

    /**
     * Factory method for check constraints.
     */
    @NotNull
    public static final <R extends Record> Check<R> createCheck(Table<R> table, Name name, String condition, boolean enforced) {
        return new CheckImpl<>(table, name, DSL.condition(condition), enforced);
    }

    /**
     * Factory method for domain specifications.
     */
    @NotNull
    public static final <T> Domain<T> createDomain(Schema schema, Name name, DataType<T> type, Check<?>... checks) {
        return new DomainImpl<>(schema, name, type, checks);
    }

    /**
     * Factory method for path aliases.
     */
    @NotNull
    public static final Name createPathAlias(Table<?> child, ForeignKey<?, ?> path) {
        Name name = DSL.name(path.getName());

        if (child instanceof TableImpl) {
            Table<?> ancestor = ((TableImpl<?>) child).child;

            if (ancestor != null)
                name = createPathAlias(ancestor, ((TableImpl<?>) child).childPath).append(name);
            else
                name = child.getQualifiedName().append(name);
        }

        return DSL.name("alias_" + Tools.hash(name));
    }

    /**
     * Factory method for parameters.
     */
    @NotNull
    public static final <T> Parameter<T> createParameter(String name, DataType<T> type, boolean isDefaulted, boolean isUnnamed) {
        return createParameter(name, type, isDefaulted, isUnnamed, null, null);
    }

    /**
     * Factory method for parameters.
     */
    @NotNull
    public static final <T, U> Parameter<U> createParameter(String name, DataType<T> type, boolean isDefaulted, boolean isUnnamed, Converter<T, U> converter) {
        return createParameter(name, type, isDefaulted, isUnnamed, converter, null);
    }

    /**
     * Factory method for parameters.
     */
    @NotNull
    public static final <T, U> Parameter<U> createParameter(String name, DataType<T> type, boolean isDefaulted, boolean isUnnamed, Binding<T, U> binding) {
        return createParameter(name, type, isDefaulted, isUnnamed, null, binding);
    }

    /**
     * Factory method for parameters.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static final <T, X, U> Parameter<U> createParameter(String name, DataType<T> type, boolean isDefaulted, boolean isUnnamed, Converter<X, U> converter, Binding<T, X> binding) {
        final Binding<T, U> actualBinding = DefaultBinding.newBinding(converter, type, binding);
        final DataType<U> actualType = converter == null && binding == null
            ? (DataType<U>) type
            : type.asConvertedDataType(actualBinding);

        return new ParameterImpl<>(name, actualType, actualBinding, isDefaulted, isUnnamed);
    }









    private Internal() {}

    /**
     * Factory method for indexes.
     *
     * @deprecated - 3.14.0 - [#9404] - Please re-generate your code.
     */
    @NotNull
    @Deprecated
    public static final Index createIndex(String name, Table<?> table, OrderField<?>[] sortFields, boolean unique) {
        return createIndex(DSL.name(name), table, sortFields, unique);
    }

    /**
     * Factory method for unique keys.
     *
     * @deprecated - 3.14.0 - [#9404] - Please re-generate your code.
     */
    @NotNull
    @Deprecated
    @SafeVarargs
    public static final <R extends Record> UniqueKey<R> createUniqueKey(Table<R> table, String name, TableField<R, ?>... fields) {
        return createUniqueKey(table, name, fields, true);
    }

    /**
     * Factory method for unique keys.
     *
     * @deprecated - 3.14.0 - [#9404] - Please re-generate your code.
     */
    @NotNull
    @Deprecated
    public static final <R extends Record> UniqueKey<R> createUniqueKey(Table<R> table, String name, TableField<R, ?>[] fields, boolean enforced) {
        return createUniqueKey(table, DSL.name(name), fields, enforced);
    }

    /**
     * Factory method for foreign keys.
     *
     * @deprecated - 3.14.0 - [#9404] - Please re-generate your code.
     */
    @NotNull
    @Deprecated
    @SafeVarargs
    public static final <R extends Record, U extends Record> ForeignKey<R, U> createForeignKey(UniqueKey<U> key, Table<R> table, String name, TableField<R, ?>... fields) {
        return createForeignKey(key, table, name, fields, true);
    }

    /**
     * Factory method for foreign keys.
     *
     * @deprecated - 3.14.0 - [#9404] - Please re-generate your code.
     */
    @NotNull
    @Deprecated
    public static final <R extends Record, U extends Record> ForeignKey<R, U> createForeignKey(UniqueKey<U> key, Table<R> table, String name, TableField<R, ?>[] fields, boolean enforced) {
        return createForeignKey(table, DSL.name(name), fields, key, key.getFieldsArray(), enforced);
    }

    /**
     * Get the fields of an embeddable type.
     */
    @NotNull
    public static final <R extends Record, ER extends EmbeddableRecord<ER>> TableField<R, ?>[] fields(TableField<R, ER> embeddableField) {
        return ((EmbeddableTableField<R, ER>) embeddableField).fields;
    }

    @Support
    static final <T> Field<T> add(Field<T> lhs, Field<T> rhs) {
        return new Expression<>(ADD, true, lhs, nullSafe(rhs, lhs.getDataType()));
    }

    @Support
    static final <T> Field<T> sub(Field<T> lhs, Field<T> rhs) {
        return new Expression<>(SUBTRACT, true, lhs, nullSafe(rhs, lhs.getDataType()));
    }

    @Support
    static final <T> Field<T> mul(Field<T> lhs, Field<T> rhs) {
        return new Expression<>(MULTIPLY, true, lhs, nullSafe(rhs, lhs.getDataType()));
    }

    @Support
    static final <T> Field<T> div(Field<T> lhs, Field<T> rhs) {
        return new Expression<>(DIVIDE, true, lhs, nullSafe(rhs, lhs.getDataType()));
    }
}
