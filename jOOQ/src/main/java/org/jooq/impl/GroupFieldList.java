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
import static org.jooq.SQLDialect.*;
import static org.jooq.impl.DSL.groupingSets;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.Tools.EMPTY_FIELD;

import java.util.EnumSet;
import java.util.Set;

import org.jooq.Context;
import org.jooq.GroupField;
// ...
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.UniqueKey;

/**
 * @author Lukas Eder
 */
final class GroupFieldList extends QueryPartList<GroupField> {

    static final Set<SQLDialect> EMULATE_EMPTY_GROUP_BY_CONSTANT = SQLDialect.supportedUntil(DERBY, HSQLDB, IGNITE);
    static final Set<SQLDialect> EMULATE_EMPTY_GROUP_BY_OTHER    = SQLDialect.supportedUntil(FIREBIRD, MARIADB, MYSQL, SQLITE, YUGABYTE);
    static final Set<SQLDialect> NO_SUPPORT_GROUP_BY_TABLE       = SQLDialect.supportedBy(AURORA_MYSQL, AURORA_POSTGRES, COCKROACHDB, DB2, DERBY, FIREBIRD, HANA, H2, HSQLDB, INFORMIX, MARIADB, MEMSQL, MYSQL, ORACLE, POSTGRES, REDSHIFT, SQLDATAWAREHOUSE, SQLITE, SQLSERVER, VERTICA);
    static final Set<SQLDialect> NO_SUPPORT_GROUP_FUNCTIONAL_DEP = SQLDialect.supportedBy(DERBY, FIREBIRD);















    GroupFieldList() {
        super();
    }

    GroupFieldList(Iterable<? extends GroupField> wrappedList) {
        super(wrappedList);
    }

    GroupFieldList(GroupField[] wrappedList) {
        super(wrappedList);
    }

    @Override
    public final boolean rendersContent(Context<?> ctx) {
        return true;
    }

    @Override
    protected final void toSQLEmptyList(Context<?> ctx) {
        // [#1665] Empty GROUP BY () clauses need parentheses

        ctx.sql(' ');

        // [#4292] Some dialects accept constant expressions in GROUP BY
        // Note that dialects may consider constants as indexed field
        // references, as in the ORDER BY clause!
        if (EMULATE_EMPTY_GROUP_BY_CONSTANT.contains(ctx.dialect()))
            ctx.sql('0');

        // [#4447] CUBRID can't handle subqueries in GROUP BY
        else if (ctx.family() == CUBRID)
            ctx.sql("1 + 0");











        // [#4292] Some dialects don't support empty GROUP BY () clauses
        else if (EMULATE_EMPTY_GROUP_BY_OTHER.contains(ctx.dialect()))
            ctx.sql('(').visit(DSL.select(one())).sql(')');

        // Few dialects support the SQL standard "grand total" (i.e. empty grouping set)
        else
            ctx.sql("()");
    }

    @Override
    protected void acceptElement(Context<?> ctx, GroupField part) {
        if (part instanceof Table) { Table<?> t = (Table<?>) part;
            if (NO_SUPPORT_GROUP_BY_TABLE.contains(ctx.dialect())) {
                UniqueKey<?> pk = t.getPrimaryKey();

                if (pk == null || NO_SUPPORT_GROUP_FUNCTIONAL_DEP.contains(ctx.dialect()))
                    if (t.fields().length > 0)
                        ctx.visit(QueryPartListView.wrap(t.fields()));
                    else
                        super.acceptElement(ctx, part);
                else if (pk.getFields().size() == 1)
                    ctx.visit(pk.getFields().get(0));
                else
                    ctx.visit(QueryPartListView.wrap(pk.getFields()));
            }
            else
                super.acceptElement(ctx, part);
        }
        else
            super.acceptElement(ctx, part);
    }
}
