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

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;

import java.util.List;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class TableList extends QueryPartList<Table<?>> {

    private static final long serialVersionUID = -8545559185481762229L;

    TableList() {
        super();
    }

    TableList(List<? extends Table<?>> wrappedList) {
        super(wrappedList);
    }

    @Override
    protected void toSQLEmptyList(Context<?> ctx) {
        ctx.visit(new Dual());
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    /**
     * Get a list of names of the <code>NamedQueryParts</code> contained in this
     * list.
     */
    final void toSQLFields(Context<?> ctx) {
        String separator = "";

        // [#4151] Some databases don't allow for qualifying column
        // names here. Copy also to SelectQueryImpl
        boolean unqualified = asList(DERBY, FIREBIRD, H2, HSQLDB).contains(ctx.family());
        boolean qualify = ctx.qualify();

        if (unqualified)
            ctx.qualify(false);

        for (Table<?> table : this) {
            for (Field<?> field : table.fieldsRow().fields()) {
                ctx.sql(separator);
                ctx.visit(field);

                separator = ", ";
            }
        }

        if (unqualified)
            ctx.qualify(qualify);
    }
}
