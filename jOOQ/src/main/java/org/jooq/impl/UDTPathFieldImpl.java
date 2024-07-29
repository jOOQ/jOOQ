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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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

import static java.lang.Boolean.TRUE;
import static org.jooq.impl.QualifiedName.hashCode0;
import static org.jooq.impl.SchemaImpl.DEFAULT_SCHEMA;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_STORE_ASSIGNMENT;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.stream.Stream;

import org.jooq.Binding;
import org.jooq.Catalog;
import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Package;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RecordQualifier;
// ...
import org.jooq.Row;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
// ...
import org.jooq.UDT;
import org.jooq.UDTPathField;
import org.jooq.UDTRecord;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.Tools.BooleanDataKey;
import org.jooq.tools.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

/**
 * A common base type for UDT path fields.
 *
 * @author Lukas Eder
 */
@Internal
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

    UDTPathFieldImpl(Name name, DataType<T> type, RecordQualifier<R> qualifier, UDT<U> udt, Comment comment, Binding<?, T> binding) {
        super(qualify(qualifier, name), type, comment, binding);

        this.qualifier = qualifier;
        this.udt = udt;

        qualifier.$name();
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
    private class UDTPathFieldImplAsQualifier
    extends AbstractNamed
    implements
        RecordQualifier<U>,
        FieldsTrait,
        UNotYetImplemented
    {
        UDTPathFieldImplAsQualifier() {
            super(UDTPathFieldImpl.this.getQualifiedName(), UDTPathFieldImpl.this.getCommentPart());
        }

        RecordQualifier<R> getQualifier() {
            return UDTPathFieldImpl.this.getQualifier();
        }

        @Override
        public final Catalog getCatalog() {
            return null;
        }

        @Override
        public final Schema getSchema() {
            return null;
        }

        @Override
        public final Schema $schema() {
            return null;
        }

        @Override
        public final Row fieldsRow() {
            return getUDT().fieldsRow();
        }

        @Override
        public final Package getPackage() {
            return getUDT().getPackage();
        }

        @Override
        public final Class<? extends U> getRecordType() {
            return getUDT().getRecordType();
        }

        @Override
        public final DataType<U> getDataType() {
            return getUDT().getDataType();
        }

        @Override
        public final U newRecord() {
            return getUDT().newRecord();
        }

        @Override
        public final void accept(Context<?> ctx) {
            UDTPathFieldImpl.this.accept(ctx);
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
        if (!TRUE.equals(ctx.data(DATA_STORE_ASSIGNMENT)) && q instanceof UDTPathFieldImpl.UDTPathFieldImplAsQualifier && ((UDTPathFieldImpl<?, ?, ?>.UDTPathFieldImplAsQualifier) q).getQualifier() instanceof Table)
            ctx.sql('(').visit(q).sql(").").visit(getUnqualifiedName());
        else if (q instanceof Table<?> t)
            TableFieldImpl.accept2(ctx, t, getUnqualifiedName());
        else
            ctx.visit(q).sql('.').visit(getUnqualifiedName());
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
        else
            return hashCode0(
                defaultIfNull(getQualifier().getSchema(), DEFAULT_SCHEMA.get()).getQualifiedName(),
                getQualifier().getUnqualifiedName(),
                getUDT().getUnqualifiedName(),
                getUnqualifiedName()
            );
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (getQualifier() instanceof Table && that instanceof TableField<?, ?> other) {
            return
                StringUtils.equals(getQualifier(), other.getTable()) &&
                StringUtils.equals(getName(), other.getName());
        }

        // [#2144] UDTPathFieldImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        else if (that instanceof UDTPathField<?, ?, ?> other) {
            return
                StringUtils.equals(getQualifier(), other.getQualifier()) &&
                StringUtils.equals(getUDT(), other.getUDT()) &&
                StringUtils.equals(getName(), other.getName());
        }

        return super.equals(that);
    }
}
