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

import static java.util.stream.Collectors.joining;
import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_REFERENCE;
// ...
import static org.jooq.impl.DefaultMetaProvider.meta;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_OMIT_CLAUSE_EVENT_EMISSION;

import java.util.stream.Stream;

import org.jooq.Binding;
import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.tools.StringUtils;

/**
 * A common base type for table fields.
 *
 * @author Lukas Eder
 */
class TableFieldImpl<R extends Record, T> extends AbstractField<T> implements TableField<R, T>, SimpleQueryPart {

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
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final void accept(Context<?> ctx) {













        ctx.data(DATA_OMIT_CLAUSE_EVENT_EMISSION, true, c -> {
            if (c.qualify() && getTable() != null)
                c.visit(getTable()).sql('.');

            c.visit(getUnqualifiedName());
        });
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

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
