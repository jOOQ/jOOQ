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
import static org.jooq.Clause.DELETE;
import static org.jooq.Clause.DELETE_DELETE;
import static org.jooq.Clause.DELETE_RETURNING;
import static org.jooq.Clause.DELETE_WHERE;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;

import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DeleteQuery;
import org.jooq.Operator;
import org.jooq.Record;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class DeleteQueryImpl<R extends Record> extends AbstractDMLQuery<R> implements DeleteQuery<R> {

    private static final long           serialVersionUID = -1943687511774150929L;
    private static final Clause[]       CLAUSES          = { DELETE };

    private final ConditionProviderImpl condition;

    DeleteQueryImpl(Configuration configuration, WithImpl with, Table<R> table) {
        super(configuration, with, table);

        this.condition = new ConditionProviderImpl();
    }

    final Condition getWhere() {
        return condition.getWhere();
    }

    @Override
    public final void addConditions(Collection<? extends Condition> conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Condition... conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition... conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Collection<? extends Condition> conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    final void accept0(Context<?> ctx) {
        boolean declare = ctx.declareTables();

        ctx.start(DELETE_DELETE)
           .keyword("delete").sql(' ');

        // [#2464] MySQL supports a peculiar multi-table DELETE syntax for aliased tables:
        // DELETE t1 FROM my_table AS t1
        if (asList(MARIADB, MYSQL).contains(ctx.configuration().dialect())) {

            // [#2579] TODO: Improve Table API to discover aliased tables more
            // reliably instead of resorting to instanceof:
            if (table instanceof TableAlias ||
               (table instanceof TableImpl && ((TableImpl<R>) table).getAliasedTable() != null)) {
                ctx.visit(table)
                   .sql(' ');
            }
        }

        ctx.keyword("from").sql(' ')
           .declareTables(true)
           .visit(table)
           .declareTables(declare)
           .end(DELETE_DELETE)
           .start(DELETE_WHERE);

        if (!(getWhere() instanceof TrueCondition)) {
            ctx.formatSeparator()
               .keyword("where").sql(' ')
               .visit(getWhere());
        }

        ctx.end(DELETE_WHERE)
           .start(DELETE_RETURNING);

        toSQLReturning(ctx);

        ctx.end(DELETE_RETURNING);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
