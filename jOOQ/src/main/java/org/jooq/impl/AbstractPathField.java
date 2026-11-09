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
import static org.jooq.impl.Tools.BooleanDataKey.DATA_STORE_ASSIGNMENT;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.Objects;

import org.jooq.Binding;
import org.jooq.Catalog;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.LanguageContext;
import org.jooq.Name;
import org.jooq.Package;
import org.jooq.QualifiedRecord;
import org.jooq.Record;
import org.jooq.RecordQualifier;
import org.jooq.Row;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDT;
import org.jooq.exception.MetaDataUnavailableException;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * [#19262] A common base type for UDT path fields and table path fields.
 *
 * @author Lukas Eder
 */
abstract class AbstractPathField<R extends Record, U extends QualifiedRecord<U>, T>
extends
    AbstractField<T>
implements
    SimpleQueryPart,
    TypedReference<T>,
    NamedField<T>,
    ScopeMappable,
    UEmpty
{

    private final RecordQualifier<R> qualifier;

    /**
     * The {@link UDT} or {@link Table} describing the path's record type.
     */
    final RecordQualifier<U>         pathType;

    AbstractPathField(Name name, DataType<T> type, RecordQualifier<R> qualifier, RecordQualifier<U> pathType, Comment comment, Binding<?, T> binding) {
        super(qualify(qualifier, name), type, comment, binding);

        this.qualifier = qualifier;
        this.pathType = pathType;
    }

    /**
     * The path field that this path field is contained in, if any.
     */
    final AbstractPathField<?, ?, ?> getParentPathField() {
        return getQualifier() instanceof AbstractPathField<?, ?, ?>.PathFieldAsQualifier u ? u.getPathField() : null;
    }

    public final RecordQualifier<R> getQualifier() {
        return qualifier;
    }

    public final RecordQualifier<U> asQualifier() {
        return new PathFieldAsQualifier();
    }

    // [#228] TODO Refactor this logic into UDTImpl
    final class PathFieldAsQualifier
    extends AbstractNamed
    implements
        RecordQualifier<U>,
        FieldsTrait,
        UNotYetImplemented
    {
        PathFieldAsQualifier() {
            super(AbstractPathField.this.getQualifiedName(), AbstractPathField.this.getCommentPart());
        }

        AbstractPathField<R, U, T> getPathField() {
            return AbstractPathField.this;
        }

        RecordQualifier<R> getQualifier() {
            return AbstractPathField.this.getQualifier();
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
            return pathType instanceof UDT<?> u ? u.getPackage() : null;
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
            AbstractPathField.this.accept(ctx);
        }

        final RecordQualifier<U> getNonNullUDT() {
            RecordQualifier<U> r = pathType;

            if (r == null)
                throw new MetaDataUnavailableException("UDT meta data is unavailable for UDTPathField: " + this);

            return r;
        }
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

    final boolean isTableFieldOrUnqualified(RecordQualifier<R> q) {
        if (q instanceof AbstractPathField<?, ?, ?>.PathFieldAsQualifier u) {
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
        else if (getQualifier() != null && pathType != null)
            return hashCode0(
                defaultIfNull(getQualifier().getSchema(), DEFAULT_SCHEMA.get()).getQualifiedName(),
                getQualifier().getUnqualifiedName(),
                pathType.getUnqualifiedName(),
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

        // [#2144] AbstractPathField equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        else if (that instanceof AbstractPathField<?, ?, ?> other) {
            return
                Objects.equals(getQualifier(), other.getQualifier()) &&
                Objects.equals(pathType, other.pathType) &&
                Objects.equals(getName(), other.getName());
        }

        return super.equals(that);
    }
}
