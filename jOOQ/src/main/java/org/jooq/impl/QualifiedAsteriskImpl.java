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

import static org.jooq.SQLDialect.DUCKDB;
import static org.jooq.impl.Keywords.K_EXCEPT;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.QualifiedAsterisk;
import org.jooq.QueryPart;
// ...
import org.jooq.SQLDialect;
import org.jooq.Table;
// ...
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * @author Lukas Eder
 */
final class QualifiedAsteriskImpl extends AbstractQueryPart implements QualifiedAsterisk {

    private static final Set<SQLDialect> NO_SUPPORT_FULLY_QUALIFIED_ASTERISKS = SQLDialect.supportedBy(DUCKDB);

    private final Table<?>               table;
    final QueryPartList<Field<?>>        fields;

    QualifiedAsteriskImpl(Table<?> table) {
        this(table, null);
    }

    QualifiedAsteriskImpl(Table<?> table, QueryPartList<Field<?>> fields) {
        this.table = table;
        this.fields = fields == null ? new QueryPartList<>() : fields;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {










            default:
                if (NO_SUPPORT_FULLY_QUALIFIED_ASTERISKS.contains(ctx.dialect()))
                    ctx.qualify(false, c -> c.visit(table));
                else
                    ctx.visit(table);

                ctx.sql('.').visit(AsteriskImpl.INSTANCE.get());

                // [#7921] H2 has native support for EXCEPT. Emulations are implemented
                //         in SelectQueryImpl
                if (!fields.isEmpty())





                        ctx.sql(' ').visit(AsteriskImpl.keyword(ctx)).sql(" (").visit(fields).sql(')');

                break;
        }
    }

    @Override
    public final Table<?> qualifier() {
        return table;
    }

    @Override
    public final QualifiedAsterisk except(String... fieldNames) {
        return except(Tools.fieldsByName(fieldNames));
    }

    @Override
    public final QualifiedAsterisk except(Name... fieldNames) {
        return except(Tools.fieldsByName(fieldNames));
    }

    @Override
    public final QualifiedAsterisk except(Field<?>... f) {
        return except(Arrays.asList(f));
    }

    @Override
    public final QualifiedAsterisk except(Collection<? extends Field<?>> f) {
        QueryPartList<Field<?>> list = new QueryPartList<>();

        list.addAll(fields);
        list.addAll(f);

        return new QualifiedAsteriskImpl(table, list);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<?> $table() {
        return table;
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $except() {
        return QOM.unmodifiable(fields);
    }














}
