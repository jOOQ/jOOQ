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

import org.jooq.Binding;
import org.jooq.Check;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.Parameter;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;

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
    public static final <R extends Record, T extends Record> TableField<R, T> createEmbeddable(Name name, Class<T> recordType, Table<R> table, TableField<R, ?>... fields) {
        return new EmbeddableTableField<>(name, recordType, table, fields);
    }

    /**
     * Factory method for indexes.
     */
    public static final Index createIndex(String name, Table<?> table, OrderField<?>[] sortFields, boolean unique) {
        return createIndex(DSL.name(name), table, sortFields, unique);
    }

    /**
     * Factory method for indexes.
     */
    public static final Index createIndex(Name name, Table<?> table, OrderField<?>[] sortFields, boolean unique) {
        return new IndexImpl(name, table, sortFields, null, unique);
    }

    /**
     * Factory method for identities.
     */
    public static final <R extends Record, T> Identity<R, T> createIdentity(Table<R> table, TableField<R, T> field) {
        return new IdentityImpl<>(table, field);
    }

    /**
     * Factory method for unique keys.
     */

    @SafeVarargs

    public static final <R extends Record> UniqueKey<R> createUniqueKey(Table<R> table, TableField<R, ?>... fields) {
        return new UniqueKeyImpl<>(table, fields, true);
    }

    /**
     * Factory method for unique keys.
     */

    @SafeVarargs

    public static final <R extends Record> UniqueKey<R> createUniqueKey(Table<R> table, String name, TableField<R, ?>... fields) {
        return new UniqueKeyImpl<>(table, name, fields, true);
    }

    /**
     * Factory method for foreign keys.
     */

    @SafeVarargs

    public static final <R extends Record, U extends Record> ForeignKey<R, U> createForeignKey(UniqueKey<U> key, Table<R> table, TableField<R, ?>... fields) {
        return createForeignKey(key, table, null, fields);
    }

    /**
     * Factory method for foreign keys.
     */

    @SafeVarargs

    public static final <R extends Record, U extends Record> ForeignKey<R, U> createForeignKey(UniqueKey<U> key, Table<R> table, String name, TableField<R, ?>... fields) {
        ForeignKey<R, U> result = new ReferenceImpl<>(key, table, name, fields, true);

        if (key instanceof UniqueKeyImpl)
            ((UniqueKeyImpl<U>) key).references.add(result);

        return result;
    }

    /**
     * Factory method for sequences.
     */
    public static final <T extends Number> Sequence<T> createSequence(String name, Schema schema, DataType<T> type) {
        return new SequenceImpl<>(name, schema, type, false);
    }

    /**
     * Factory method for sequences.
     */
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
    public static final <R extends Record> Check<R> createCheck(Table<R> table, Name name, String condition) {
        return new CheckImpl<>(table, name, DSL.condition(condition), true);
    }

    /**
     * Factory method for path aliases.
     */
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
    public static final <T> Parameter<T> createParameter(String name, DataType<T> type, boolean isDefaulted, boolean isUnnamed) {
        return createParameter(name, type, isDefaulted, isUnnamed, null, null);
    }

    /**
     * Factory method for parameters.
     */
    public static final <T, U> Parameter<U> createParameter(String name, DataType<T> type, boolean isDefaulted, boolean isUnnamed, Converter<T, U> converter) {
        return createParameter(name, type, isDefaulted, isUnnamed, converter, null);
    }

    /**
     * Factory method for parameters.
     */
    public static final <T, U> Parameter<U> createParameter(String name, DataType<T> type, boolean isDefaulted, boolean isUnnamed, Binding<T, U> binding) {
        return createParameter(name, type, isDefaulted, isUnnamed, null, binding);
    }

    /**
     * Factory method for parameters.
     */
    @SuppressWarnings("unchecked")
    public static final <T, X, U> Parameter<U> createParameter(String name, DataType<T> type, boolean isDefaulted, boolean isUnnamed, Converter<X, U> converter, Binding<T, X> binding) {
        final Binding<T, U> actualBinding = DefaultBinding.newBinding(converter, type, binding);
        final DataType<U> actualType = converter == null && binding == null
            ? (DataType<U>) type
            : type.asConvertedDataType(actualBinding);

        return new ParameterImpl<>(name, actualType, actualBinding, isDefaulted, isUnnamed);
    }

    private Internal() {}
}
