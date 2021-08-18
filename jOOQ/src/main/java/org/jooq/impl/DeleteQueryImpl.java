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

import static org.jooq.Clause.DELETE;
import static org.jooq.Clause.DELETE_DELETE;
import static org.jooq.Clause.DELETE_RETURNING;
import static org.jooq.Clause.DELETE_WHERE;
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.conf.SettingsTools.getExecuteDeleteWithoutWhere;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.Keywords.K_DELETE;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Keywords.K_LIMIT;
import static org.jooq.impl.Keywords.K_ORDER_BY;
import static org.jooq.impl.Keywords.K_USING;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.Tools.containsDeclaredTable;
import static org.jooq.impl.Tools.traverseJoins;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DeleteQuery;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.OrderField;
import org.jooq.Param;
// ...
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
final class DeleteQueryImpl<R extends Record> extends AbstractDMLQuery<R> implements DeleteQuery<R> {

    private static final Clause[]        CLAUSES                         = { DELETE };
    private static final Set<SQLDialect> SPECIAL_DELETE_AS_SYNTAX        = SQLDialect.supportedBy(MARIADB, MYSQL);

    // LIMIT is not supported at all
    private static final Set<SQLDialect> NO_SUPPORT_LIMIT                = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES, SQLITE);

    // LIMIT is supported but not ORDER BY
    private static final Set<SQLDialect> NO_SUPPORT_ORDER_BY_LIMIT       = SQLDialect.supportedBy(IGNITE);
    private static final Set<SQLDialect> SUPPORT_MULTITABLE_DELETE       = SQLDialect.supportedBy(MARIADB, MYSQL);
    private static final Set<SQLDialect> REQUIRE_REPEAT_FROM_IN_USING    = SQLDialect.supportedBy(MARIADB, MYSQL);
    private static final Set<SQLDialect> NO_SUPPORT_REPEAT_FROM_IN_USING = SQLDialect.supportedBy(POSTGRES);







    private final TableList              using;
    private final ConditionProviderImpl  condition;
    private final SortFieldList          orderBy;
    private Param<? extends Number>      limit;

    DeleteQueryImpl(Configuration configuration, WithImpl with, Table<R> table) {
        super(configuration, with, table);

        this.using = new TableList();
        this.condition = new ConditionProviderImpl();
        this.orderBy = new SortFieldList();
    }

    final Condition getWhere() {
        return condition.getWhere();
    }

    final boolean hasWhere() {
        return condition.hasWhere();
    }

    final TableList getUsing() {
        return using;
    }

    @Override
    public final void addUsing(Collection<? extends TableLike<?>> f) {
        for (TableLike<?> provider : f)
            using.add(provider.asTable());
    }

    @Override
    public final void addUsing(TableLike<?> f) {
        using.add(f.asTable());
    }

    @Override
    public final void addUsing(TableLike<?>... f) {
        for (TableLike<?> provider : f)
            using.add(provider.asTable());
    }

    @Override
    public final void addConditions(Collection<? extends Condition> conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Condition conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Condition... conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition conditions) {
        condition.addConditions(operator, conditions);
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
    public final void addOrderBy(OrderField<?>... fields) {
        addOrderBy(Arrays.asList(fields));
    }

    @Override
    public final void addOrderBy(Collection<? extends OrderField<?>> fields) {
        orderBy.addAll(Tools.sortFields(fields));
    }

    @Override
    public final void addLimit(Number numberOfRows) {
        addLimit(DSL.val(numberOfRows));
    }

    @Override
    public final void addLimit(Param<? extends Number> numberOfRows) {
        limit = numberOfRows;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    final void accept0(Context<?> ctx) {
        ctx.start(DELETE_DELETE)
           .visit(K_DELETE).sql(' ');

        Table<?> t = table(ctx);

        boolean multiTableJoin = (SUPPORT_MULTITABLE_DELETE.contains(ctx.dialect()) && t instanceof JoinTable);
        boolean specialDeleteAsSyntax = SPECIAL_DELETE_AS_SYNTAX.contains(ctx.dialect());

        // [#11924] Multiple tables listed in the FROM clause mean this is a
        //          MySQL style multi table DELETE
        if (multiTableJoin)

            // No table declarations in this case, but references
            ctx.visit(K_FROM).sql(' ').visit(traverseJoins(t, new TableList(), null, (r, x) -> { r.add(x); return r; })).formatSeparator();








        // [#2464] Use the USING clause to declare aliases in MySQL
        else
            ctx.visit(K_FROM).sql(' ').declareTables(!specialDeleteAsSyntax, c -> c.visit(t));

        // [#11925] In MySQL, the tables in FROM must be repeated in USING
        if (!using.isEmpty() || multiTableJoin || specialDeleteAsSyntax && Tools.alias(t) != null) {
            TableList u;


            if (REQUIRE_REPEAT_FROM_IN_USING.contains(ctx.dialect()) && !containsDeclaredTable(using, t)) {
                u = new TableList(t);
                u.addAll(using);
            }
            else if (NO_SUPPORT_REPEAT_FROM_IN_USING.contains(ctx.dialect()) && containsDeclaredTable(using, t)) {
                u = new TableList(using);
                u.remove(t);
            }
            else
                u = using;

            ctx.formatSeparator()
               .visit(K_USING)
               .sql(' ')
               .declareTables(true, c -> c.visit(u));
        }

        ctx.end(DELETE_DELETE);





        boolean noSupportParametersInWhere = false;

        if (limit != null && NO_SUPPORT_LIMIT.contains(ctx.dialect()) || !orderBy.isEmpty() && NO_SUPPORT_ORDER_BY_LIMIT.contains(ctx.dialect())) {
            Field<?>[] keyFields =
                  table().getKeys().isEmpty()
                ? new Field[] { table().rowid() }
                : (table().getPrimaryKey() != null
                    ? table().getPrimaryKey()
                    : table().getKeys().get(0)).getFieldsArray();

            ctx.start(DELETE_WHERE)
               .formatSeparator()
               .visit(K_WHERE).sql(' ');

            ctx.paramTypeIf(ParamType.INLINED, noSupportParametersInWhere, c -> {
                if (keyFields.length == 1)
                    c.visit(keyFields[0].in(select((Field) keyFields[0]).from(table()).where(getWhere()).orderBy(orderBy).limit(limit)));
                else
                    c.visit(row(keyFields).in(select(keyFields).from(table()).where(getWhere()).orderBy(orderBy).limit(limit)));
            });

            ctx.end(DELETE_WHERE);
        }
        else {
            ctx.start(DELETE_WHERE);

            if (hasWhere())
                ctx.paramTypeIf(ParamType.INLINED, noSupportParametersInWhere, c ->
                    c.formatSeparator()
                       .visit(K_WHERE).sql(' ')
                       .visit(getWhere())
                );







            ctx.end(DELETE_WHERE);

            if (!orderBy.isEmpty())
                ctx.formatSeparator()
                   .visit(K_ORDER_BY).sql(' ')
                   .visit(orderBy);

            if (limit != null)
                ctx.formatSeparator()
                   .visit(K_LIMIT).sql(' ')
                   .visit(limit);
        }

        ctx.start(DELETE_RETURNING);
        toSQLReturning(ctx);
        ctx.end(DELETE_RETURNING);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final boolean isExecutable() {

        // [#6771] Take action when DELETE query has no WHERE clause
        if (!condition.hasWhere())
            executeWithoutWhere("DELETE without WHERE", getExecuteDeleteWithoutWhere(configuration().settings()));

        return super.isExecutable();
    }










}
