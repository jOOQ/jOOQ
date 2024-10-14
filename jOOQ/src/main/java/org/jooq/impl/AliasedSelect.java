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

import static org.jooq.SQLDialect.DERBY;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.Names.NQ_SELECT;
import static org.jooq.impl.Names.N_T;
import static org.jooq.impl.SubqueryCharacteristics.DERIVED_TABLE;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.collect;
import static org.jooq.impl.Tools.fieldNames;
import static org.jooq.impl.Tools.flattenCollection;
import static org.jooq.impl.Tools.hasEmbeddedFields;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.impl.Tools.selectQueryImpl;
import static org.jooq.impl.Tools.visitSubquery;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_FORCE_LIMIT_WITH_ORDER_BY;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_SELECT_ALIASES;

import java.util.List;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableOptions;
import org.jooq.impl.QOM.UTransient;

/**
 * A {@link Select} query that re-aliases its projection, or produces a derived
 * table if re-aliasing is not possible (e.g. in the presence of
 * <code>ORDER BY</code>).
 *
 * @author Lukas Eder
 */
final class AliasedSelect<R extends Record> extends AbstractTable<R> implements UTransient {

    private final Select<R> query;
    private final boolean   subquery;
    private final boolean   ignoreOrderBy;
    private final boolean   forceLimit;
    private final Name[]    aliases;

    AliasedSelect(Select<R> query, boolean subquery, boolean ignoreOrderBy, boolean forceLimit, Name... aliases) {
        super(TableOptions.expression(), NQ_SELECT);

        this.query = query;
        this.subquery = subquery;
        this.ignoreOrderBy = ignoreOrderBy;
        this.forceLimit = forceLimit;
        this.aliases = aliases;
    }

    final Select<R> query() {
        return query;
    }

    @Override
    public final Table<R> as(Name alias) {
        SelectQueryImpl<R> q = selectQueryImpl(query);
        List<Field<?>> f = q.getSelect();

        // [#11473] In the presence of ORDER BY, AliasedSelect tends not to work
        //          correctly if ORDER BY references names available prior to the aliasing only
        // [#10521] TODO: Reuse avoidAliasPushdown here
        // [#16326] With embeddables, we might need to generate field names from a flattened collection
        if (q != null && (ignoreOrderBy && !q.getOrderBy().isEmpty() || hasEmbeddedFields(f) || isEmpty(aliases)))
            if (isEmpty(aliases))
                return query.asTable(alias, fieldNames(collect(flattenCollection(f)).size()));
            else
                return query.asTable(alias, aliases);
        else
            return new TableAlias<>(this, alias, true);
    }

    @Override
    public final Table<R> as(Name alias, Name... fieldAliases) {
        return new TableAlias<>(this, alias, fieldAliases, true);
    }

    @Override
    final FieldsImpl<R> fields0() {
        return new FieldsImpl<>(query.asTable(N_T, aliases).fields());
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return query.getRecordType();
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (forceLimit)
            ctx.data(DATA_FORCE_LIMIT_WITH_ORDER_BY, true, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        SelectQueryImpl<R> q = selectQueryImpl(query);

        // [#3679] [#10540] Without standardised UNION subquery column names,
        //                  Derby projects column indexes 1, 2, 3 as names, but
        //                  they cannot be referenced. In that case, revert to
        //                  actual derived table usage.
        // [#10521] TODO: Reuse avoidAliasPushdown here
        if (ctx.family() == DERBY && q != null && q.hasUnions())
            visitSubquery(ctx, selectFrom(query.asTable(N_T, aliases)), DERIVED_TABLE, false);
        else
            ctx.data(DATA_SELECT_ALIASES, aliases, subquery ? c -> visitSubquery(c, query, DERIVED_TABLE, false) : c -> c.visit(query));
    }

    static final boolean avoidAliasPushdown(Context<?> ctx, Select<?> query) {
        SelectQueryImpl<?> q = selectQueryImpl(query);
        return q != null && (

            // [#3679] [#10540] Without standardised UNION subquery column names,
            //                  Derby projects column indexes 1, 2, 3 as names, but
            //                  they cannot be referenced. In that case, revert to
            //                  actual derived table usage.
               ctx.family() == DERBY && q.hasUnions()
            || !q.getOrderBy().isEmpty()
            || Tools.hasEmbeddedFields(q.getSelect())
        );
    }

    @Override // Avoid AbstractTable implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
