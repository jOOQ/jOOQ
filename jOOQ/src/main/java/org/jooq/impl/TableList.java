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

import static java.util.Arrays.asList;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.impl.Tools.flatMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class TableList extends QueryPartList<Table<?>> {

    private static final Set<SQLDialect> UNQUALIFY_FIELDS = SQLDialect.supportedBy(DERBY, FIREBIRD, H2, HSQLDB);

    TableList() {
        super();
    }

    TableList(List<? extends Table<?>> wrappedList) {
        super(wrappedList);
    }

    @SafeVarargs
    TableList(Table<?>... wrappedList) {
        super(wrappedList);
    }

    @Override
    public final boolean rendersContent(Context<?> ctx) {
        return true;
    }

    @Override
    protected void acceptElement(Context<?> ctx, Table<?> part) {
        Table<?> alternative;

        if (ctx.declareTables() && part instanceof AutoAliasTable && (alternative = ((AutoAliasTable<?>) part).autoAlias(ctx)) != null)
            super.acceptElement(ctx, alternative);
        else
            super.acceptElement(ctx, part);
    }

    @Override
    protected void toSQLEmptyList(Context<?> ctx) {
        ctx.visit(new Dual());
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    final List<Field<?>> fields() {
        return flatMap(this, t -> asList(t.fieldsRow().fields()));
    }

    /**
     * Get a list of names of the <code>NamedQueryParts</code> contained in this
     * list.
     */
    final void toSQLFields(Context<?> ctx) {

        // [#4151] [#6117] Some databases don't allow for qualifying column
        // names here. Copy also to SelectQueryImpl
        ctx.qualify(!UNQUALIFY_FIELDS.contains(ctx.dialect()) && ctx.qualify(), c -> {
            String sep = "";

            for (Table<?> table : this) {
                for (Field<?> field : table.fieldsRow().fields()) {
                    ctx.sql(sep);
                    ctx.visit(field);

                    sep = ", ";
                }
            }
        });
    }
}
