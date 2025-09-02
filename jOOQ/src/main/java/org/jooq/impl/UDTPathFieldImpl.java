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

import static java.lang.Boolean.TRUE;
// ...
import static org.jooq.impl.QualifiedName.hashCode0;
import static org.jooq.impl.SchemaImpl.DEFAULT_SCHEMA;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_STORE_ASSIGNMENT;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.Objects;
import java.util.function.BiFunction;

import org.jooq.Binding;
import org.jooq.Catalog;
import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.LanguageContext;
import org.jooq.Name;
import org.jooq.Package;
import org.jooq.Record;
import org.jooq.RecordQualifier;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDT;
import org.jooq.UDTPathField;
import org.jooq.UDTPathTableField;
import org.jooq.UDTRecord;
import org.jooq.exception.MetaDataUnavailableException;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.tools.StringUtils;

/**
 * A common base type for UDT path fields.
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public /* non-final */ class UDTPathFieldImpl<R extends Record, U extends UDTRecord<U>, T>
extends
    AbstractField<T>
implements
    UDTPathField<R, U, T>,
    SimpleQueryPart,
    TypedReference<T>,
    NamedField<T>,
    ScopeMappable,
    UEmpty
{

    private final RecordQualifier<R> qualifier;
    private final UDT<U>             udt;

    UDTPathFieldImpl(Name name, DataType<T> type, RecordQualifier<R> qualifier, UDT<U> udt, Comment comment) {
        this(name, type, qualifier, udt, comment, type.getBinding());
    }

    UDTPathFieldImpl(Name name, DataType<T> type, RecordQualifier<R> qualifier, UDT<U> udt, Comment comment, Binding<?, T> binding) {
        super(qualify(qualifier, name), type, comment, binding);

        this.qualifier = qualifier;
        this.udt = udt;
    }

    @Override
    public final UDTPathTableField<?, ?, ?> getTableField() {
        if (getQualifier() instanceof UDTPathTableFieldImpl<?, ?, ?>.UDTPathFieldImplAsQualifier u)
            return u.getTableField();
        else if (this instanceof UDTPathTableField<?, ?, ?> tf)
            return tf;
        else
            throw new IllegalStateException();
    }

    static final <R extends UDTRecord<R>> Field<R> construct(
        UDT<R> udt,
        BiFunction<? super UDT<R>, ? super Field<?>, ? extends Field<?>> init
    ) {

        // [#15506] We do not recurse into nested UDTs here, because u1(null, null) is not the same thing as u1(u2(null, null), null)
        return new UDTConstructor<>(udt, map(
            udt.fields(),
            f -> init.apply(udt, f)
        ));
    }

    static final void patchUDTConstructor(
        UDTPathField<?, ?, ?> u,
        UDTConstructor<?> udtConstructor,
        Field<?> value,
        BiFunction<? super UDT<?>, ? super Field<?>, ? extends Field<?>> init
    ) {
        while (!u.getUDT().equals(udtConstructor.udt)) {
            UDTPathField<?, ?, ?> p = getPathFieldFor(udtConstructor.udt, u);

            int i = udtConstructor.udt.indexOf(p);
            Field<?> e = udtConstructor.args.get(i);

            if (!(e instanceof UDTConstructor))
                udtConstructor.args.set(i, e = construct(p.getUDT(), init));

            udtConstructor = (UDTConstructor<?>) e;
        }

        udtConstructor.args.set(udtConstructor.udt.indexOf(u), value);
    }

    static final UDTPathField<?, ?, ?> getPathFieldFor(UDT<?> udt, UDTPathField<?, ?, ?> f) {
        while (udt.field(f) == null) {
            if (f.getQualifier() instanceof UDTPathTableFieldImpl<?, ?, ?>.UDTPathFieldImplAsQualifier u)
                f = u.getPathField();
            else
                throw new IllegalStateException();
        }

        return f;
    }

    @Override
    public final RecordQualifier<R> getQualifier() {
        return qualifier;
    }

    @Override
    public final RecordQualifier<U> asQualifier() {
        return new UDTPathFieldImplAsQualifier();
    }

    // [#228] TODO Refactor this logic into UDTImpl
    final class UDTPathFieldImplAsQualifier
    extends AbstractNamed
    implements
        RecordQualifier<U>,
        FieldsTrait,
        UNotYetImplemented
    {
        UDTPathFieldImplAsQualifier() {
            super(UDTPathFieldImpl.this.getQualifiedName(), UDTPathFieldImpl.this.getCommentPart());
        }

        UDTPathField<?, ?, ?> getPathField() {
            return UDTPathFieldImpl.this;
        }

        UDTPathTableField<?, ?, ?> getTableField() {
            return UDTPathFieldImpl.this.getTableField();
        }

        RecordQualifier<R> getQualifier() {
            return UDTPathFieldImpl.this.getQualifier();
        }

        @Override
        public final Catalog getCatalog() {
            return getQualifier() == null ? null : getQualifier().getCatalog();
        }

        @Override
        public final Schema getSchema() {
            return getQualifier() == null ? null : getQualifier().getSchema();
        }

        @Override
        public final Schema $schema() {
            return getSchema();
        }

        @Override
        public final Row fieldsRow() {
            return getNonNullUDT().fieldsRow();
        }

        @Override
        public final Package getPackage() {
            return getUDT() == null ? null : getUDT().getPackage();
        }

        @Override
        public final Class<? extends U> getRecordType() {
            return getNonNullUDT().getRecordType();
        }

        @Override
        public final DataType<U> getDataType() {
            return getNonNullUDT().getDataType();
        }

        @Override
        public final U newRecord() {
            return getNonNullUDT().newRecord();
        }

        @Override
        public final void accept(Context<?> ctx) {
            UDTPathFieldImpl.this.accept(ctx);
        }

        final UDT<U> getNonNullUDT() {
            UDT<U> r = UDTPathFieldImpl.this.getUDT();

            if (r == null)
                throw new MetaDataUnavailableException("UDT meta data is unavailable for UDTPathField: " + this);

            return r;
        }
    }

    @Override
    public final UDT<U> getUDT() {
        return udt;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        return true;
    }

    @Override
    public boolean declaresFields() {
        return  super.declaresFields();
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final void accept(Context<?> ctx) {
        RecordQualifier<R> q = getQualifier();

        // [#228] The disambiguating wrapping in parentheses is only required in references to
        //        a UDT path identifier expression, not in assignments (where it is disallowed)
        if (!TRUE.equals(ctx.data(DATA_STORE_ASSIGNMENT)) && isTableFieldOrUnqualified(q)) {





            ctx.sql('(').visit(q).sql(").").visit(getUnqualifiedName());
        }
        else if (q instanceof Table<?> t)
            TableFieldImpl.accept2(ctx, t, getUnqualifiedName(), getDataType());
        else if (q != null)
            ctx.visit(q).sql('.').visit(getUnqualifiedName());
        else
            ctx.visit(getUnqualifiedName());
    }

    private final boolean isTableFieldOrUnqualified(RecordQualifier<R> q) {
        if (q instanceof UDTPathFieldImpl<?, ?, ?>.UDTPathFieldImplAsQualifier u) {
            return u.getQualifier() instanceof Table || u.getQualifier() == null;
        }
        else
            return false;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {
        if (getQualifier() instanceof Table)
            return hashCode0(
                defaultIfNull(getQualifier().getSchema(), DEFAULT_SCHEMA.get()).getQualifiedName(),
                getQualifier().getUnqualifiedName(),
                getUnqualifiedName()
            );
        else if (getQualifier() != null && getUDT() != null)
            return hashCode0(
                defaultIfNull(getQualifier().getSchema(), DEFAULT_SCHEMA.get()).getQualifiedName(),
                getQualifier().getUnqualifiedName(),
                getUDT().getUnqualifiedName(),
                getUnqualifiedName()
            );
        else
            return hashCode0(
                getUnqualifiedName()
            );
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (getQualifier() instanceof Table && that instanceof TableField<?, ?> other) {
            return
                Objects.equals(getQualifier(), other.getTable()) &&
                Objects.equals(getName(), other.getName());
        }

        // [#2144] UDTPathFieldImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        else if (that instanceof UDTPathField<?, ?, ?> other) {
            return
                Objects.equals(getQualifier(), other.getQualifier()) &&
                Objects.equals(getUDT(), other.getUDT()) &&
                Objects.equals(getName(), other.getName());
        }

        return super.equals(that);
    }
}
