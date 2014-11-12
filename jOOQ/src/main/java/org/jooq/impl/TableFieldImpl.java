/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq.impl;

import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_REFERENCE;
import static org.jooq.impl.Utils.DATA_OMIT_CLAUSE_EVENT_EMISSION;

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
class TableFieldImpl<R extends Record, T> extends AbstractField<T> implements TableField<R, T> {

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
            ctx.sql(".");
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
