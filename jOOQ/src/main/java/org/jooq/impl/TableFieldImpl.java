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

import static java.lang.Boolean.FALSE;
import static java.util.stream.Collectors.joining;
import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_REFERENCE;
// ...
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DefaultMetaProvider.meta;
import static org.jooq.impl.SchemaImpl.DEFAULT_SCHEMA;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_OMIT_CLAUSE_EVENT_EMISSION;
import static org.jooq.impl.Tools.ExtendedDataKey.DATA_RENDER_TABLE;
import static org.jooq.impl.UpdateQueryImpl.NO_SUPPORT_UPDATE_JOIN;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.stream.Stream;

import org.jooq.Binding;
import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.DMLQuery;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.GeneratorStatementType;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.RowId;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.Update;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.impl.Tools.SimpleDataKey;
import org.jooq.tools.StringUtils;

/**
 * A common base type for table fields.
 *
 * @author Lukas Eder
 */
final class TableFieldImpl<R extends Record, T>
extends
     AbstractField<T>
implements
    TableField<R, T>,
    SimpleQueryPart,
    TypedReference<T>,
    ScopeMappable,
    UEmpty
{

    private static final Clause[] CLAUSES = { FIELD, FIELD_REFERENCE };

    private final Table<R>        table;

    @SuppressWarnings("unchecked")
    TableFieldImpl(Name name, DataType<T> type, Comment comment) {
        this(name, type, (Table<R>) table(name), comment, type.getBinding());
    }

    TableFieldImpl(Name name, DataType<T> type, Table<R> table, Comment comment, Binding<?, T> binding) {
        super(qualify(table, name), type, comment, binding);

        this.table = table;
    }

    private static final Table<Record> table(Name name) {
        return name.qualified() ? DSL.table(name.qualifier()) : null;
    }

    @Override
    public final Table<R> getTable() {
        return table;
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
        return CLAUSES;
    }

    @Override
    public final void accept(Context<?> ctx) {
        Table<?> root;






































        // [#7508] Implicit join path references inside of DML queries have to
        //         be emulated differently
        if (ctx.topLevelForLanguageContext() instanceof DMLQuery
            && table instanceof TableImpl
            && (root = ((TableImpl<?>) table).pathRoot()) != null

            // [#7508] Apply the emulation only in the DML statement itself, or
            //         when the root child is the DML target table. Implicit
            //         joins declared in a subquery without correlation to the
            //         DML statement can be generated normally.
            && (!ctx.subquery() || root.equals(ctx.data(SimpleDataKey.DATA_DML_TARGET_TABLE)))
        ) {

            // [#7508] MySQL supports UPDATE .. JOIN, so the default implicit
            //         JOIN emulation works out of the box.
            if (ctx.topLevelForLanguageContext() instanceof Update && !NO_SUPPORT_UPDATE_JOIN.contains(ctx.dialect())) {
                accept1(ctx);
            }
            else {
                TableImpl<?> t = (TableImpl<?>) table;
                Table<?> parent = t.alias.wrapped;
                Field<T> parentField = parent.field(this);
                ctx.visit(DSL.field(select(parentField).from(parent).where(JoinTable.onKey0(t.childPath, t.path, parent))));
            }
        }
        else
            accept1(ctx);
    }

    private final void accept1(Context<?> ctx) {
        ctx.data(DATA_OMIT_CLAUSE_EVENT_EMISSION, true, c -> {
            if (c.qualify() && getTable() != null && !FALSE.equals(ctx.data(DATA_RENDER_TABLE)))
                c.visit(getTable()).sql('.');

            c.visit(getUnqualifiedName());
        });
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {

        // [#7172] [#10274] [#14875] Cannot use Table.getQualifiedName() based super implementation yet here
        if (getTable() == null)
            return getUnqualifiedName().hashCode();
        else
            return defaultIfNull(getTable().getSchema(), DEFAULT_SCHEMA.get()).getQualifiedName()
                .append(getTable().getUnqualifiedName())
                .append(getUnqualifiedName()).hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        // [#2144] TableFieldImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof TableField) {
            TableField<?, ?> other = (TableField<?, ?>) that;
            return
                StringUtils.equals(getTable(), other.getTable()) &&
                StringUtils.equals(getName(), other.getName());
        }

        return super.equals(that);
    }
}
