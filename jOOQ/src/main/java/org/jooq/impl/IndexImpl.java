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

import static org.jooq.impl.Tools.EMPTY_SORTFIELD;

import java.util.Arrays;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.SortField;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class IndexImpl extends AbstractNamed implements Index {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -5253463940194393996L;

    private final Table<?>       table;
    private final SortField<?>[] fields;
    private final Condition      where;
    private final boolean        unique;

    IndexImpl(Name name) {
        this(name, null, EMPTY_SORTFIELD, null, false);
    }

    IndexImpl(Name name, Table<?> table, OrderField<?>[] fields, Condition where, boolean unique) {
        super(name, CommentImpl.NO_COMMENT);

        this.table = table;
        this.fields = Tools.sortFields(fields);
        this.where = where;
        this.unique = unique;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(getQualifiedName());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final Table<?> getTable() {
        return table;
    }

    @Override
    public final List<SortField<?>> getFields() {
        return Arrays.asList(fields);
    }

    @Override
    public final Condition getWhere() {
        return where;
    }

    @Override
    public boolean getUnique() {
        return unique;
    }
}
