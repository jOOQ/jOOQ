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

import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_REFERENCE;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;

/**
 * A <code>QualifiedTable</code> is a {@link Table} that always renders a table
 * name or alias as a literal using {@link RenderContext#literal(String)}
 *
 * @author Lukas Eder
 */
final class QualifiedTable extends AbstractTable<Record> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 6937002867156868761L;
    private static final Clause[] CLAUSES          = { TABLE, TABLE_REFERENCE };

    private final Name            name;

    QualifiedTable(Name name) {
        super(name.getName()[name.getName().length - 1]);

        this.name = name;
    }

    // ------------------------------------------------------------------------
    // Table API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(name);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    public final Table<Record> as(String alias) {
        return new TableAlias<Record>(this, alias);
    }

    @Override
    public final Table<Record> as(String alias, String... fieldAliases) {
        return new TableAlias<Record>(this, alias, fieldAliases);
    }

    @Override
    final Fields<Record> fields0() {
        return new Fields<Record>();
    }
}
