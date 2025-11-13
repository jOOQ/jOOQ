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

import static org.jooq.TableOptions.expression;
import static org.jooq.impl.Names.N_DUAL;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Path;
// ...
// ...
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Table;
import org.jooq.impl.HintedTable.HintType;
import org.jooq.impl.QOM.UEmptyTable;
import org.jooq.impl.QOM.UNoQueryPart;

/**
 * @author Lukas Eder
 */
final class NoTable
extends
    AbstractTable<Record>
implements
    UEmptyTable<Record>,
    Path<Record>,
    UNoQueryPart
{

    static final NoTable INSTANCE = new NoTable();

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(new Dual());
    }

    private NoTable() {
        super(expression(), N_DUAL);
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return Record.class;
    }

    @Override
    final FieldsImpl<Record> fields0() {
        return new FieldsImpl<>();
    }

    // ------------------------------------------------------------------------
    // XXX: Table API
    // ------------------------------------------------------------------------

    @Override
    public final Table<Record> as(Name alias) {
        return this;
    }

    @Override
    public final Table<Record> as(Name alias, Name... fieldAliases) {
        return this;
    }

    @Override
    public final Table<Record> where(Condition condition) {
        return this;
    }

    @Override
    public final Table<Record> withOrdinality() {
        return this;
    }

    @Override
    final Table<Record> hintedTable(HintType hintType, String... indexes) {
        return this;
    }
















}
