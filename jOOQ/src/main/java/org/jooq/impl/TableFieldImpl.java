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
 */

package org.jooq.impl;

import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_REFERENCE;
import static org.jooq.impl.Tools.DataKey.DATA_OMIT_CLAUSE_EVENT_EMISSION;

import org.jooq.Binding;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.tools.StringUtils;

/**
 * A common base type for table fields.
 *
 * @author Lukas Eder
 */
final class TableFieldImpl<R extends Record, T> extends AbstractField<T> implements TableField<R, T> {

    private static final long     serialVersionUID = -2211214195583539735L;
    private static final Clause[] CLAUSES          = { FIELD, FIELD_REFERENCE };

    private final Table<R>    table;

    TableFieldImpl(String name, DataType<T> type, Table<R> table, String comment, Binding<?, T> binding) {
        super(name, type, comment, binding);

        this.table = table;
    }

    @Override
    public final Table<R> getTable() {
        return table;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.data(DATA_OMIT_CLAUSE_EVENT_EMISSION, true);

        if (ctx.qualify()) {
            ctx.visit(table);
            ctx.sql('.');
        }

        ctx.literal(getName());
        ctx.data(DATA_OMIT_CLAUSE_EVENT_EMISSION, null);
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

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
