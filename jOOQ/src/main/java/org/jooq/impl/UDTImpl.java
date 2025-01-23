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

import static java.util.Collections.emptyList;

import java.util.List;

import org.jooq.Binding;
import org.jooq.Catalog;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Name;
import org.jooq.Named;
import org.jooq.Package;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.Schema;
import org.jooq.UDT;
import org.jooq.UDTField;
import org.jooq.UDTRecord;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * A common base type for UDT's
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public /* non-final */ class UDTImpl<R extends UDTRecord<R>>
extends
    AbstractNamed
implements
    UDT<R>,
    FieldsTrait,
    UNotYetImplemented
{

    private final Schema          schema;
    private final FieldsImpl<R>   fields;
    private final Package         pkg;
    private final boolean         synthetic;
    private transient DataType<R> type;

    /**
     * @deprecated - [#15583] - 3.19.0 - Please re-generate your code.
     */
    @Deprecated
    public UDTImpl(String name, Schema schema) {
        this(DSL.name(name), schema);
    }

    /**
     * @deprecated - [#15583] - 3.19.0 - Please re-generate your code.
     */
    @Deprecated
    public UDTImpl(String name, Schema schema, Package pkg) {
        this(DSL.name(name), schema, pkg);
    }

    /**
     * @deprecated - [#15583] - 3.19.0 - Please re-generate your code.
     */
    @Deprecated
    public UDTImpl(String name, Schema schema, Package pkg, boolean synthetic) {
        this(DSL.name(name), schema, pkg, synthetic);
    }

    public UDTImpl(Name name, Schema schema) {
        this(name, schema, null);
    }

    public UDTImpl(Name name, Schema schema, Package pkg) {
        this(name, schema, pkg, false);
    }

    public UDTImpl(Name name, Schema schema, Package pkg, boolean synthetic) {
        super(qualify(pkg != null ? pkg : schema, name), CommentImpl.NO_COMMENT);

        this.fields = new FieldsImpl<>();
        this.schema = schema;
        this.pkg = pkg;
        this.synthetic = synthetic;
    }

    @Override
    public final Catalog getCatalog() {
        return getSchema() == null ? null : getSchema().getCatalog();
    }

    @Override
    public /* non-final */ Schema getSchema() {
        return schema;
    }

    @Override
    public /* non-final */ Name getQualifiedName() {
        Named q = getPackage() != null ? getPackage() : getSchema();
        return q == null ? super.getQualifiedName() : q.getQualifiedName().append(getUnqualifiedName());
    }

    @Override
    public final Package getPackage() {
        return pkg;
    }

    @Override
    public final Row fieldsRow() {
        return Tools.row0(fields);
    }

    final FieldsImpl<R> fields0() {
        return fields;
    }

    /**
     * Subclasses must override this method if they use the generic type
     * parameter <code>R</code> for other types than {@link Record}
     */
    @Override
    public Class<R> getRecordType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isSQLUsable() {
        return true ;
    }

    @Override
    public final boolean isSynthetic() {
        return synthetic;
    }

    @Override
    public final R newRecord() {
        return DSL.using(new DefaultConfiguration()).newRecord(this);
    }

    @Override
    public final DataType<R> getDataType() {
        if (type == null)
            type = new UDTDataType<>(this);

        return type;
    }

    @Override
    public final void accept(Context<?> ctx) {
        QualifiedImpl.acceptMappedSchemaPrefix(ctx, getSchema());






        ctx.visit(DSL.name(getName()));
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link #createField(Name, DataType, UDT)}
     *             instead.
     */
    @Deprecated
    protected static final <R extends UDTRecord<R>, T> UDTField<R, T> createField(String name, DataType<T> type, UDT<R> udt) {
        return createField(DSL.name(name), type, udt, "", null, null);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link #createField(Name, DataType, UDT, String)}
     *             instead.
     */
    @Deprecated
    protected static final <R extends UDTRecord<R>, T> UDTField<R, T> createField(String name, DataType<T> type, UDT<R> udt, String comment) {
        return createField(DSL.name(name), type, udt, comment, null, null);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link #createField(Name, DataType, UDT, String, Converter)}
     *             instead.
     */
    @Deprecated
    protected static final <R extends UDTRecord<R>, T, U> UDTField<R, U> createField(String name, DataType<T> type, UDT<R> udt, String comment, Converter<T, U> converter) {
        return createField(DSL.name(name), type, udt, comment, converter, null);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link #createField(Name, DataType, UDT, String, Binding)}
     *             instead.
     */
    @Deprecated
    protected static final <R extends UDTRecord<R>, T, U> UDTField<R, U> createField(String name, DataType<T> type, UDT<R> udt, String comment, Binding<T, U> binding) {
        return createField(DSL.name(name), type, udt, comment, null, binding);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link #createField(Name, DataType, UDT, String, Converter, Binding)}
     *             instead.
     */
    @Deprecated
    protected static final <R extends UDTRecord<R>, T, X, U> UDTField<R, U> createField(String name, DataType<T> type, UDT<R> udt, String comment, Converter<X, U> converter, Binding<T, X> binding) {
        return createField(DSL.name(name), type, udt, comment, converter, binding);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends UDTRecord<R>, T> UDTField<R, T> createField(Name name, DataType<T> type, UDT<R> udt) {
        return createField(name, type, udt, "", null, null);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends UDTRecord<R>, T> UDTField<R, T> createField(Name name, DataType<T> type, UDT<R> udt, String comment) {
        return createField(name, type, udt, comment, null, null);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends UDTRecord<R>, T, U> UDTField<R, U> createField(Name name, DataType<T> type, UDT<R> udt, String comment, Converter<T, U> converter) {
        return createField(name, type, udt, comment, converter, null);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends UDTRecord<R>, T, U> UDTField<R, U> createField(Name name, DataType<T> type, UDT<R> udt, String comment, Binding<T, U> binding) {
        return createField(name, type, udt, comment, null, binding);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    @SuppressWarnings("unchecked")
    protected static final <R extends UDTRecord<R>, T, X, U> UDTField<R, U> createField(Name name, DataType<T> type, UDT<R> udt, String comment, Converter<X, U> converter, Binding<T, X> binding) {
        final Binding<T, U> actualBinding = DefaultBinding.newBinding(converter, type, binding);
        final DataType<U> actualType = converter == null && binding == null
            ? (DataType<U>) type
            : type.asConvertedDataType(actualBinding);

        // [#5999] TODO: Allow for user-defined Names
        final UDTFieldImpl<R, U> udtField = new UDTFieldImpl<>(name, actualType, udt, DSL.comment(comment), actualBinding);

        return udtField;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Schema $schema() {
        return schema;
    }
}
