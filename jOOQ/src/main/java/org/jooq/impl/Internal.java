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

import static org.jooq.impl.DSL.field;
// ...
import static org.jooq.impl.Tools.CONFIG;
import static org.jooq.impl.Tools.CTX;
import static org.jooq.impl.Tools.configuration;
import static org.jooq.impl.Tools.nullSafe;
import static org.jooq.impl.Tools.qualify;
import static org.jooq.tools.StringUtils.isBlank;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jooq.Binding;
import org.jooq.Check;
import org.jooq.Comment;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.ConverterContext;
import org.jooq.DDLExportConfiguration;
import org.jooq.DataType;
import org.jooq.Domain;
import org.jooq.EmbeddableRecord;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Generator;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.ParamMode;
import org.jooq.Parameter;
// ...
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RecordQualifier;
// ...
// ...
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Statement;
import org.jooq.Support;
import org.jooq.Table;
import org.jooq.TableElement;
import org.jooq.TableField;
// ...
// ...
// ...
// ...
import org.jooq.UDT;
import org.jooq.UDTField;
import org.jooq.UDTPathField;
import org.jooq.UDTPathTableField;
import org.jooq.UDTRecord;
import org.jooq.UniqueKey;
// ...
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.MappingException;
import org.jooq.impl.QOM.CreateTable;
import org.jooq.impl.QOM.GenerationLocation;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;
// ...
// ...

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

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

    public static final <R extends Record, T, P extends UDTPathTableField<R, ?, T>> P createUDTPathTableField(
        Name name,
        DataType<T> type,
        Table<R> table,
        Class<P> returnType
    ) {
        return createUDTPathTableField(name, type, table, null, returnType, null, null, null);
    }

    public static final <R extends Record, T, P extends UDTPathTableField<R, ?, T>> P createUDTPathTableField(
        Name name,
        DataType<T> type,
        Table<R> table,
        String comment,
        Class<P> returnType
    ) {
        return createUDTPathTableField(name, type, table, comment, returnType, null, null, null);
    }

    public static final <R extends Record, T, U, P extends UDTPathTableField<R, ?, U>> P createUDTPathTableField(
        Name name,
        DataType<T> type,
        Table<R> table,
        String comment,
        Class<P> returnType,
        Converter<T, U> converter
    ) {
        return createUDTPathTableField(name, type, table, comment, returnType, converter, null, null);
    }

    public static final <R extends Record, T, U, P extends UDTPathTableField<R, ?, U>> P createUDTPathTableField(
        Name name,
        DataType<T> type,
        Table<R> table,
        String comment,
        Class<P> returnType,
        Binding<T, U> binding
    ) {
        return createUDTPathTableField(name, type, table, comment, returnType, null, binding, null);
    }

    public static final <R extends Record, T, X, U, P extends UDTPathTableField<R, ?, U>> P createUDTPathTableField(
        Name name,
        DataType<T> type,
        Table<R> table,
        String comment,
        Class<P> returnType,
        Converter<X, U> converter,
        Binding<T, X> binding
    ) {
        return createUDTPathTableField(name, type, table, comment, returnType, converter, binding, null);
    }

    public static final <R extends Record, TR extends Table<R>, T, P extends UDTPathTableField<R, ?, T>> P createUDTPathTableField(
        Name name,
        DataType<T> type,
        TR table,
        String comment,
        Class<P> returnType,
        Generator<R, TR, T> generator
    ) {
        return createUDTPathTableField(name, type, table, comment, returnType, null, null, generator);
    }

    public static final <R extends Record, TR extends Table<R>, T, U, P extends UDTPathTableField<R, ?, U>> P createUDTPathTableField(
        Name name,
        DataType<T> type,
        TR table,
        String comment,
        Class<P> returnType,
        Converter<T, U> converter,
        Generator<R, TR, U> generator
    ) {
        return createUDTPathTableField(name, type, table, comment, returnType, converter, null, generator);
    }

    public static final <R extends Record, TR extends Table<R>, T, U, P extends UDTPathTableField<R, ?, U>> P createUDTPathTableField(
        Name name,
        DataType<T> type,
        TR table,
        String comment,
        Class<P> returnType,
        Binding<T, U> binding,
        Generator<R, TR, U> generator
    ) {
        return createUDTPathTableField(name, type, table, comment, returnType, null, binding, generator);
    }

    @SuppressWarnings("unchecked")
    public static final <R extends Record, TR extends Table<R>, T, X, U, P extends UDTPathTableField<R, ?, U>> P createUDTPathTableField(
        Name name,
        DataType<T> type,
        TR table,
        String comment,
        Class<P> returnType,
        Converter<X, U> converter,
        Binding<T, X> binding,
        Generator<R, TR, U> generator
    ) {
        Binding<T, U> actualBinding = DefaultBinding.newBinding(converter, type, binding);
        DataType<U> actualType =
            converter == null && binding == null
          ? (DataType<U>) type
          : type.asConvertedDataType(actualBinding);

        if (generator != null)
            actualType = actualType.generatedAlwaysAs(generator).generationLocation(GenerationLocation.CLIENT);

        // [#5999] TODO: Allow for user-defined Names
        try {
            P tableField = newInstance(name, table, null, comment, returnType, actualBinding, actualType);

            // [#1199] The public API of Table returns immutable field lists
            if (table instanceof TableImpl<?> t)
                t.fields.add(tableField);

            return tableField;
        }
        catch (Exception e) {
            throw new DataTypeException("Cannot instantiate " + returnType + ".", e);
        }
    }

    public static final <T, P extends UDTField<?, T>> P createUDTPathField(
        Name name,
        DataType<T> type,
        UDTPathField<?, ?, ?> qualifier,
        Class<P> returnType
    ) {
        return createUDTPathField(name, type, qualifier, null, returnType, null, null);
    }

    public static final <T, P extends UDTField<?, T>> P createUDTPathField(
        Name name,
        DataType<T> type,
        UDTPathField<?, ?, ?> qualifier,
        String comment,
        Class<P> returnType
    ) {
        return createUDTPathField(name, type, qualifier, comment, returnType, null, null);
    }

    public static final <T, U, P extends UDTField<?, U>> P createUDTPathField(
        Name name,
        DataType<T> type,
        UDTPathField<?, ?, ?> qualifier,
        String comment,
        Class<P> returnType,
        Converter<T, U> converter
    ) {
        return createUDTPathField(name, type, qualifier, comment, returnType, converter, null);
    }

    public static final <T, U, P extends UDTField<?, U>> P createUDTPathField(
        Name name,
        DataType<T> type,
        UDTPathField<?, ?, ?> qualifier,
        String comment,
        Class<P> returnType,
        Binding<T, U> binding
    ) {
        return createUDTPathField(name, type, qualifier, comment, returnType, null, binding);
    }

    public static final <T, X, U, P extends UDTField<?, U>> P createUDTPathField(
        Name name,
        DataType<T> type,
        UDTPathField<?, ?, ?> qualifier,
        String comment,
        Class<P> returnType,
        Converter<X, U> converter,
        Binding<T, X> binding
    ) {
        Binding<T, U> actualBinding = DefaultBinding.newBinding(converter, type, binding);
        DataType<U> actualType =
            converter == null && binding == null
          ? (DataType<U>) type
          : type.asConvertedDataType(actualBinding);

        // [#5999] TODO: Allow for user-defined Names
        try {

            // [#228] While it would be cleaner to pass around a Function5 constructor reference,
            //        chances are that the cost on compilation speed is significantly higher than
            //        if we just use reflection
            return newInstance(name, null, qualifier, comment, returnType, actualBinding, actualType);
        }
        catch (Exception e) {
            throw new DataTypeException("Cannot instantiate " + returnType + ".", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T, X, U, P extends UDTField<?, U>> P newInstance(
        Name name,
        RecordQualifier<?> qualifier,
        UDTPathField<?, ?, ?> path,
        String comment,
        Class<P> returnType,
        Binding<T, U> actualBinding,
        DataType<U> actualType
    ) throws Exception {

        // [#228] This case only happens in generated UDTPath types, never in generated Table types
        if (returnType == (Class) UDTField.class)
            return (P) new UDTPathFieldImpl<>(name, actualType, path.asQualifier(), path.getUDT(), DSL.comment(comment), actualBinding);

        // [#228] While it would be cleaner to pass around a Function5 constructor reference,
        //        chances are that the cost on compilation speed is significantly higher than
        //        if we just use reflection
        else
            return returnType
                .getConstructor(Name.class, DataType.class, RecordQualifier.class, Comment.class, Binding.class)
                .newInstance(name, actualType, qualifier == null ? path.asQualifier() : qualifier, DSL.comment(comment), actualBinding);
    }

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

        if (uk instanceof UniqueKeyImpl<U> u)
            u.references.add(result);

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
        return createDomain(schema, name, type, null, null, checks);
    }

    /**
     * Factory method for domain specifications.
     */
    @NotNull
    public static final <T, U> Domain<U> createDomain(Schema schema, Name name, DataType<T> type, Converter<T, U> converter, Check<?>... checks) {
        return createDomain(schema, name, type, converter, null, checks);
    }

    /**
     * Factory method for domain specifications.
     */
    @NotNull
    public static final <T, U> Domain<U> createDomain(Schema schema, Name name, DataType<T> type, Binding<T, U> binding, Check<?>... checks) {
        return createDomain(schema, name, type, null, binding, checks);
    }

    /**
     * Factory method for domain specifications.
     */
    @NotNull
    public static final <T, X, U> Domain<U> createDomain(Schema schema, Name name, DataType<T> type, Converter<X, U> converter, Binding<T, X> binding, Check<?>... checks) {
        Binding<T, U> actualBinding = DefaultBinding.newBinding(converter, type, binding);
        DataType<U> actualType =
            converter == null && binding == null
          ? (DataType<U>) type
          : type.asConvertedDataType(actualBinding);

        return new DomainImpl<>(schema, name, actualType, checks);
    }


























    /**
     * Factory method for path aliases.
     */
    @NotNull
    public static final Name createPathAlias(Table<?> path, ForeignKey<?, ?> childPath, InverseForeignKey<?, ?> parentPath) {
        Name name = childPath != null
            ? DSL.name(childPath.getName())
            : DSL.name(parentPath.getName() + ".inverse." + parentPath.getForeignKey().getTable().getName());

        if (path instanceof TableImpl<?> t) {
            if (t.path != null)
                name = createPathAlias(t.path, t.childPath, t.parentPath).append(name);
            else
                name = path.getQualifiedName().append(name);
        }

        return DSL.name("alias_" + hash(name));
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

        // TODO: [#11327] Get the ParamMode right
        return new ParameterImpl<>(ParamMode.IN, DSL.name(name), actualType, isDefaulted, isUnnamed);
    }









    private Internal() {}

    /**
     * Factory method for indexes.
     *
     * @deprecated - 3.14.0 - [#9404] - Please re-generate your code.
     */
    @NotNull
    @Deprecated(since = "3.14", forRemoval = true)
    public static final Index createIndex(String name, Table<?> table, OrderField<?>[] sortFields, boolean unique) {
        return createIndex(DSL.name(name), table, sortFields, unique);
    }

    /**
     * Factory method for unique keys.
     *
     * @deprecated - 3.14.0 - [#9404] - Please re-generate your code.
     */
    @NotNull
    @SafeVarargs
    @Deprecated(since = "3.14", forRemoval = true)
    public static final <R extends Record> UniqueKey<R> createUniqueKey(Table<R> table, String name, TableField<R, ?>... fields) {
        return createUniqueKey(table, name, fields, true);
    }

    /**
     * Factory method for unique keys.
     *
     * @deprecated - 3.14.0 - [#9404] - Please re-generate your code.
     */
    @NotNull
    @Deprecated(since = "3.14", forRemoval = true)
    public static final <R extends Record> UniqueKey<R> createUniqueKey(Table<R> table, String name, TableField<R, ?>[] fields, boolean enforced) {
        return createUniqueKey(table, DSL.name(name), fields, enforced);
    }

    /**
     * Factory method for foreign keys.
     *
     * @deprecated - 3.14.0 - [#9404] - Please re-generate your code.
     */
    @NotNull
    @SafeVarargs
    @Deprecated(since = "3.14", forRemoval = true)
    public static final <R extends Record, U extends Record> ForeignKey<R, U> createForeignKey(UniqueKey<U> key, Table<R> table, String name, TableField<R, ?>... fields) {
        return createForeignKey(key, table, name, fields, true);
    }

    /**
     * Factory method for foreign keys.
     *
     * @deprecated - 3.14.0 - [#9404] - Please re-generate your code.
     */
    @NotNull
    @Deprecated(since = "3.14", forRemoval = true)
    public static final <R extends Record, U extends Record> ForeignKey<R, U> createForeignKey(UniqueKey<U> key, Table<R> table, String name, TableField<R, ?>[] fields, boolean enforced) {
        return createForeignKey(table, DSL.name(name), fields, key, key.getFieldsArray(), enforced);
    }

    /**
     * Get the fields of an embeddable type.
     *
     * @deprecated - [#11058] - 3.14.5 - Please re-generate your code.
     */
    @NotNull
    @Deprecated(since = "3.14", forRemoval = true)
    public static final <R extends Record, ER extends EmbeddableRecord<ER>> TableField<R, ?>[] fields(TableField<R, ER> embeddableField) {
        return ((EmbeddableTableField<R, ER>) embeddableField).fields;
    }

    /**
     * Get the fields row of an embeddable type.
     *
     * @deprecated - [#12238] - 3.16.0 - Please re-generate your code.
     */
    @NotNull
    @Deprecated(since = "3.16", forRemoval = true)
    public static final <R extends Record, ER extends EmbeddableRecord<ER>> Row fieldsRow(TableField<R, ER> embeddableField) {
        return (@NotNull Row) embeddableField.getDataType().getRow();
    }

    @Support
    static final <T> Field<T> ineg(Field<T> field) {
        return new Neg<>(field, true);
    }

    @SuppressWarnings("unchecked")
    @Support
    static final <T> Field<T> iadd(Field<T> lhs, Field<?> rhs) {
        return new IAdd<>(lhs, (Field<T>) nullSafe(rhs, lhs.getDataType()));
    }

    @SuppressWarnings("unchecked")
    @Support
    static final <T> Field<T> isub(Field<T> lhs, Field<?> rhs) {
        return new ISub<>(lhs, (Field<T>) nullSafe(rhs, lhs.getDataType()));
    }

    @SuppressWarnings("unchecked")
    @Support
    static final <T> Field<T> imul(Field<T> lhs, Field<?> rhs) {
        return new IMul<>(lhs, (Field<T>) nullSafe(rhs, lhs.getDataType()));
    }

    @SuppressWarnings("unchecked")
    @Support
    static final <T> Field<T> idiv(Field<T> lhs, Field<?> rhs) {
        return new IDiv<>(lhs, (Field<T>) nullSafe(rhs, lhs.getDataType()));
    }

    /**
     * Create a {@link Subscriber} from a set of lambdas.
     * <p>
     * This is used for internal purposes and thus subject for change.
     */
    public static final <T> Subscriber<T> subscriber(
        Consumer<? super Subscription> subscription,
        Consumer<? super T> onNext,
        Consumer<? super Throwable> onError,
        Runnable onComplete
    ) {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscription.accept(s);
            }

            @Override
            public void onNext(T t) {
                onNext.accept(t);
            }

            @Override
            public void onError(Throwable t) {
                onError.accept(t);
            }

            @Override
            public void onComplete() {
                onComplete.run();
            }
        };
    }

    /**
     * JDK agnostic abstraction over {@link Class#arrayType()} and
     * {@link Array#newInstance(Class, int)}.
     */
    @SuppressWarnings({ "unchecked", "unused" })
    public static final <T> Class<T[]> arrayType(Class<T> type) {

        if (true)
            return (Class<T[]>) type.arrayType();
        else

        return (Class<T[]>) Array.newInstance(type, 0).getClass();
    }

    /**
     * Create an empty result from a {@link Record} using its row type.
     */
    public static final <R extends Record> Result<R> result(R record) {
        return new ResultImpl<>(Tools.configuration(record), ((AbstractRecord) record).fields);
    }

    /**
     * Whether this is a commercial edition of jOOQ.
     */
    public static final boolean commercial() {
        return CONFIG.get().commercial();
    }

    /**
     * Whether this is a commercial edition of jOOQ, logging a warning message,
     * if not.
     */
    public static final boolean commercial(Supplier<String> logMessage) {
        return CONFIG.get().commercial(logMessage);
    }

    /**
     * Whether this is a commercial edition of jOOQ, throwing an exception with
     * a message, if not.
     */
    public static final void requireCommercial(Supplier<String> logMessage) throws DataAccessException {
        CONFIG.get().requireCommercial(logMessage);
    }












































































































































































    /**
     * Return a non-negative hash code for a {@link QueryPart}, taking into
     * account FindBugs' <code>RV_ABSOLUTE_VALUE_OF_HASHCODE</code> pattern
     */
    public static final int hash(QueryPart part) {
        return hash0(CTX.get().render(part));
    }

    static final int hash0(Object object) {
        if (object == null)
            return 0;

        // [#6025] Prevent unstable alias generation for derived tables due to
        //         inlined bind variables in hashCode() calculation
        // [#6175] TODO: Speed this up with a faster way to calculate a hash code
        else
            return 0x7FFFFFF & object.hashCode();
    }

    private static final Lazy<ConverterContext> CONVERTER_SCOPE = Lazy.of(() -> new DefaultConverterContext(CONFIG.get()));

    public static final ConverterContext converterContext() {
        return CONVERTER_SCOPE.get();
    }

    private static final Lazy<Integer> JAVA_VERSION = Lazy.of(() -> {
        try {

            return Reflect.onClass(Runtime.class)

                // Since Java 9
                .call("version")

                // Since Java 10
                .call("feature")
                .get();
        }
        catch (ReflectException e) {
            return 8;
        }
    });

    /**
     * Get the Java version (relevant to jOOQ) as an int.
     * <p>
     * Supported versions are:
     * <ul>
     * <li>8</li>
     * <li>11</li>
     * <li>17</li>
     * <li>21</li>
     * </ul>
     */
    public static final int javaVersion() {
        return JAVA_VERSION.get();
    }

    /**
     * [#11898] [#16044] This method just acts as a bridge to internal API from
     * the deprecated-for-removal {@link org.jooq.tools.Convert} utility. Do not
     * reuse these methods.
     */
    @Deprecated(forRemoval = true)
    public static final Object[] convert(Object[] values, Field<?>[] fields) {
        return Convert.convert(values, fields);
    }

    /**
     * [#11898] [#16044] This method just acts as a bridge to internal API from
     * the deprecated-for-removal {@link org.jooq.tools.Convert} utility. Do not
     * reuse these methods.
     */
    @Deprecated(forRemoval = true)
    public static final Object[] convert(Object[] values, Class<?>[] types) {
        return Convert.convert(values, types);
    }

    /**
     * [#11898] [#16044] This method just acts as a bridge to internal API from
     * the deprecated-for-removal {@link org.jooq.tools.Convert} utility. Do not
     * reuse these methods.
     */
    @Deprecated(forRemoval = true)
    public static final <U> U[] convertArray(Object[] from, Converter<?, ? extends U> converter) throws DataTypeException {
        return Convert.convertArray(from, converter);
    }

    /**
     * [#11898] [#16044] This method just acts as a bridge to internal API from
     * the deprecated-for-removal {@link org.jooq.tools.Convert} utility. Do not
     * reuse these methods.
     */
    @Deprecated(forRemoval = true)
    public static final Object[] convertArray(Object[] from, Class<?> toClass) throws DataTypeException {
        return Convert.convertArray(from, toClass);
    }

    /**
     * [#11898] [#16044] This method just acts as a bridge to internal API from
     * the deprecated-for-removal {@link org.jooq.tools.Convert} utility. Do not
     * reuse these methods.
     */
    @Deprecated(forRemoval = true)
    public static final <U> U[] convertCollection(Collection from, Class<? extends U[]> to) {
        return Convert.convertCollection(from, to);
    }

    /**
     * [#11898] [#16044] This method just acts as a bridge to internal API from
     * the deprecated-for-removal {@link org.jooq.tools.Convert} utility. Do not
     * reuse these methods.
     */
    @Deprecated(forRemoval = true)
    public static final <U> U convert(Object from, Converter<?, ? extends U> converter) throws DataTypeException {
        return Convert.convert(from, converter);
    }

    /**
     * [#11898] [#16044] This method just acts as a bridge to internal API from
     * the deprecated-for-removal {@link org.jooq.tools.Convert} utility. Do not
     * reuse these methods.
     */
    @Deprecated(forRemoval = true)
    public static final <T> T convert(Object from, Class<? extends T> toClass) throws DataTypeException {
        return Convert.convert(from, toClass);
    }

    /**
     * [#11898] [#16044] This method just acts as a bridge to internal API from
     * the deprecated-for-removal {@link org.jooq.tools.Convert} utility. Do not
     * reuse these methods.
     */
    @Deprecated(forRemoval = true)
    public static final <T> List<T> convert(Collection<?> collection, Class<? extends T> type) throws DataTypeException {
        return Convert.convert(collection, type);
    }

    /**
     * [#11898] [#16044] This method just acts as a bridge to internal API from
     * the deprecated-for-removal {@link org.jooq.tools.Convert} utility. Do not
     * reuse these methods.
     */
    @Deprecated(forRemoval = true)
    public static final <U> List<U> convert(Collection<?> collection, Converter<?, ? extends U> converter) throws DataTypeException {
        return Convert.convert(collection, converter);
    }

    /**
     * A utility to list enum literals from a {@link Class}, independently of
     * language implementation.
     */
    @SuppressWarnings("unchecked")
    public static final <E> E[] enums(Class<? extends E> type) {

        // Java implementation
        if (Enum.class.isAssignableFrom(type)) {
            E[] result = type.getEnumConstants();

            // [#18568] Enums may be subclasses of the declaring enum type, e.g. enum E { a {} } or Scala 3 enums.
            if (result == null && type.getSuperclass() != Enum.class)
                result = (E[]) type.getSuperclass().getEnumConstants();

            return result;
        }

        // [#4427] Scala implementation
        else {
            try {

                // There's probably a better way to do this:
                // http://stackoverflow.com/q/36068089/521799
                Class<?> companionClass = Thread.currentThread().getContextClassLoader().loadClass(type.getName() + "$");
                java.lang.reflect.Field module = companionClass.getField("MODULE$");
                Object companion = module.get(companionClass);
                return (E[]) companionClass.getMethod("values").invoke(companion);
            }
            catch (Exception e) {
                throw new MappingException("Error while looking up Scala enum", e);
            }
        }
    }
}
