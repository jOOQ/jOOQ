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



import static org.jooq.impl.Keywords.K_FINAL;
import static org.jooq.impl.Keywords.K_NEW;
import static org.jooq.impl.Keywords.K_OLD;
import static org.jooq.impl.Keywords.K_TABLE;
import static org.jooq.impl.Tools.abstractDMLQuery;

import org.jooq.Context;
import org.jooq.DMLQuery;
import org.jooq.Delete;
import org.jooq.Insert;
import org.jooq.Merge;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.TableOptions;
import org.jooq.Update;


/**
 * @author Lukas Eder
 */
final class DataChangeDeltaTable<R extends Record> extends AbstractTable<R> implements AutoAliasTable<R> {

    private final ResultOption result;
    private final DMLQuery<R>  query;
    private final Table<R>     table;
    private final Name         alias;

    DataChangeDeltaTable(ResultOption result, DMLQuery<R> query) {
        this(result, query, table(query));
    }

    private DataChangeDeltaTable(ResultOption result, DMLQuery<R> query, Table<R> table) {
        this(result, query, table, table.getUnqualifiedName());
    }

    private DataChangeDeltaTable(ResultOption result, DMLQuery<R> query, Table<R> table, Name alias) {
        super(TableOptions.expression(), alias);

        this.result = result;
        this.query = query;
        this.table = table;
        this.alias = alias;
    }

    enum ResultOption {
        FINAL, OLD, NEW
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static final <R extends Record> Table<R> table(DMLQuery<R> query) {
        if (query instanceof Insert || query instanceof Update || query instanceof Delete)
            return (Table<R>) abstractDMLQuery(query).table();
        else if (query instanceof Merge)
            return ((MergeImpl) query).table();
        else
            throw new IllegalStateException("Unsupported query type: " + query);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (result) {
            case FINAL: ctx.visit(K_FINAL); break;
            case OLD: ctx.visit(K_OLD); break;
            case NEW: ctx.visit(K_NEW); break;
            default: throw new IllegalStateException("Unsupported result option: " + result);
        }

        ctx.sql(' ').visit(K_TABLE)
           .sqlIndentStart(" (")
           .visit(query)
           .sqlIndentEnd(')');
    }

    // -------------------------------------------------------------------------
    // XXX: Table API
    // -------------------------------------------------------------------------

    @Override
    public Table<R> as(Name as) {
        return new TableAlias<>(new DataChangeDeltaTable<>(result, query, table, as), as);
    }

    @Override
    public Table<R> as(Name as, Name... fieldAliases) {
        return new TableAlias<>(new DataChangeDeltaTable<>(result, query, table, as), as, fieldAliases);
    }

    @Override
    public final Table<R> autoAlias(Context<?> ctx) {
        return as(alias);
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return table.getRecordType();
    }

    @SuppressWarnings("unchecked")
    @Override
    final FieldsImpl<R> fields0() {
        return ((AbstractRow<R>) table.as(alias).fieldsRow()).fields;
    }
}
