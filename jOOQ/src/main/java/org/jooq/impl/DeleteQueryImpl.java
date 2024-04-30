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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static org.jooq.Clause.DELETE;
import static org.jooq.Clause.DELETE_DELETE;
import static org.jooq.Clause.DELETE_RETURNING;
import static org.jooq.Clause.DELETE_WHERE;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
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
import static org.jooq.SQLDialect.YUGABYTEDB;
// ...
import static org.jooq.conf.SettingsTools.getExecuteDeleteWithoutWhere;
import static org.jooq.impl.ConditionProviderImpl.extractCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.mergeInto;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.systemName;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.InlineDerivedTable.hasInlineDerivedTables;
import static org.jooq.impl.InlineDerivedTable.transformInlineDerivedTables;
import static org.jooq.impl.InlineDerivedTable.transformInlineDerivedTables0;
import static org.jooq.impl.Internal.hash;
import static org.jooq.impl.Keywords.K_DELETE;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Keywords.K_LIMIT;
import static org.jooq.impl.Keywords.K_ORDER_BY;
import static org.jooq.impl.Keywords.K_ROWS;
import static org.jooq.impl.Keywords.K_USING;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.Tools.containsDeclaredTable;
import static org.jooq.impl.Tools.traverseJoins;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_UNQUALIFY_LOCAL_SCOPE;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DeleteQuery;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.OrderField;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
// ...
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
// ...
import org.jooq.conf.ParamType;
import org.jooq.impl.QOM.Delete;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * @author Lukas Eder
 */
final class DeleteQueryImpl<R extends Record>
extends
    AbstractDMLQuery<R>
implements
    DeleteQuery<R>,
    QOM.Delete<R>
{

    private static final Clause[]       CLAUSES                          = { DELETE };
    static final Set<SQLDialect>        SPECIAL_DELETE_AS_SYNTAX         = SQLDialect.supportedBy(MARIADB, MYSQL);

    // LIMIT is not supported at all
    static final Set<SQLDialect>        NO_SUPPORT_LIMIT                 = SQLDialect.supportedUntil(CLICKHOUSE, CUBRID, DERBY, DUCKDB, H2, HSQLDB, POSTGRES, SQLITE, YUGABYTEDB);

    // LIMIT is supported but not ORDER BY
    static final Set<SQLDialect>        NO_SUPPORT_ORDER_BY_LIMIT        = SQLDialect.supportedBy(IGNITE);
    static final Set<SQLDialect>        SUPPORT_MULTITABLE_DELETE        = SQLDialect.supportedBy(MARIADB, MYSQL);
    static final Set<SQLDialect>        REQUIRE_REPEAT_FROM_IN_USING     = SQLDialect.supportedBy(MARIADB, MYSQL);
    static final Set<SQLDialect>        NO_SUPPORT_REPEAT_FROM_IN_USING  = SQLDialect.supportedBy(DUCKDB, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>        REQUIRES_WHERE                   = SQLDialect.supportedBy(CLICKHOUSE);
    static final Set<SQLDialect>        EMULATE_USING_WITH_MERGE         = SQLDialect.supportedBy(DERBY, FIREBIRD, H2, HSQLDB);









    // https://github.com/ClickHouse/ClickHouse/issues/61020
    static final Set<SQLDialect>        NO_SUPPORT_QUALIFY_IN_WHERE      = SQLDialect.supportedBy(CLICKHOUSE);

    private final TableList             using;
    private final ConditionProviderImpl condition;
    private final SortFieldList         orderBy;
    private Field<? extends Number>     limit;

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
    public final void addLimit(Field<? extends Number> numberOfRows) {
        if (numberOfRows instanceof NoField)
            return;

        limit = numberOfRows;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.scopeStart(this);

        // [#2682] [#15632] Apply inline derived tables to the target table
        // [#15632] TODO: Refactor this logic with UpdateQueryImpl
        Table<?> t = table(ctx);
        if (hasInlineDerivedTables(ctx, t) || hasInlineDerivedTables(ctx, using)) {
            ConditionProviderImpl where = new ConditionProviderImpl();
            TableList u = transformInlineDerivedTables(ctx, using, where);

            copy(
                d -> {
                    if (u != using) {
                        d.using.clear();
                        d.using.addAll(u);
                    }

                    if (where.hasWhere())
                        d.addConditions(where);
                },
                transformInlineDerivedTables0(ctx, t, where, false)
            ).accept0(ctx);
        }
        else
            accept0(ctx);

        ctx.scopeEnd();
    }

    static final Field<?>[] keyFields(Context<?> ctx, Table<?> table) {

        // [#16569] [#16571] The PostgreSQL ctid is not unique in a logically partitioned table
        return table.getKeys().isEmpty()
            ? ctx.family() == POSTGRES
                ? new Field[] { field(systemName("tableoid")), table.rowid() }
                : new Field[] { table.rowid() }
            : (table.getPrimaryKey() != null
                ? table.getPrimaryKey()
                : table.getKeys().get(0)).getFieldsArray();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    final void accept1(Context<?> ctx) {
        if (!using.isEmpty() && EMULATE_USING_WITH_MERGE.contains(ctx.dialect())) {
            acceptUsingAsMerge(ctx);
            return;
        }

        ctx.start(DELETE_DELETE)
           .visit(K_DELETE).sql(' ');

        Table<?> t = table(ctx);

        boolean multiTableJoin = (SUPPORT_MULTITABLE_DELETE.contains(ctx.dialect()) && t instanceof JoinTable);
        boolean specialDeleteAsSyntax = SPECIAL_DELETE_AS_SYNTAX.contains(ctx.dialect());

        // [#11925] In MySQL, the tables in FROM must be repeated in USING
        boolean hasUsing = !using.isEmpty() || multiTableJoin || specialDeleteAsSyntax && Tools.alias(t) != null;

        // [#11924] Multiple tables listed in the FROM clause mean this is a
        //          MySQL style multi table DELETE
        if (multiTableJoin)

            // No table declarations in this case, but references
            ctx.visit(K_FROM).sql(' ').visit(traverseJoins(t, new TableList(), null, (r, x) -> { r.add(x); return r; })).formatSeparator();












        // [#2464] Use the USING clause to declare aliases in MySQL
        else
            ctx.visit(K_FROM).sql(' ').declareTables(!specialDeleteAsSyntax, c -> c.visit(t));

        // [#14011] Additional predicates that are added for various reasons
        Condition moreWhere = noCondition();
        if (hasUsing) {
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















            TableList u0 = u;
            ctx.formatSeparator()
               .visit(K_USING)
               .sql(' ')
               .declareTables(true, c -> c.visit(u0));
        }

        ctx.end(DELETE_DELETE);





        boolean noSupportParametersInWhere = false;
        if (moreWhere instanceof NoCondition && REQUIRES_WHERE.contains(ctx.dialect()))
            moreWhere = trueCondition();

        Condition where = DSL.and(getWhere(), moreWhere);
        boolean noQualifyInWhere = NO_SUPPORT_QUALIFY_IN_WHERE.contains(ctx.dialect());

        if (limit != null && NO_SUPPORT_LIMIT.contains(ctx.dialect()) || !orderBy.isEmpty() && NO_SUPPORT_ORDER_BY_LIMIT.contains(ctx.dialect())) {
            Field<?>[] keyFields = keyFields(ctx, table());

            ctx.start(DELETE_WHERE)
               .formatSeparator()
               .visit(K_WHERE).sql(' ');

            ctx.paramTypeIf(ParamType.INLINED, noSupportParametersInWhere, c -> {
                if (noQualifyInWhere)
                    ctx.data(DATA_UNQUALIFY_LOCAL_SCOPE, true);

                if (keyFields.length == 1)
                    c.visit(keyFields[0].in(select((Field) keyFields[0]).from(table()).where(where).orderBy(orderBy).limit(limit)));
                else
                    c.visit(row(keyFields).in(select(keyFields).from(table()).where(where).orderBy(orderBy).limit(limit)));

                if (noQualifyInWhere)
                    ctx.data(DATA_UNQUALIFY_LOCAL_SCOPE, false);
            });

            ctx.end(DELETE_WHERE);
        }
        else {
            ctx.start(DELETE_WHERE);

            if (!(where instanceof NoCondition))
                ctx.paramTypeIf(ParamType.INLINED, noSupportParametersInWhere, c -> {
                    c.formatSeparator().visit(K_WHERE).sql(' ');

                    if (noQualifyInWhere)
                        ctx.data(DATA_UNQUALIFY_LOCAL_SCOPE, true);

                    ctx.visit(where);

                    if (noQualifyInWhere)
                        ctx.data(DATA_UNQUALIFY_LOCAL_SCOPE, false);
                });

            ctx.end(DELETE_WHERE);

            if (!orderBy.isEmpty())
                ctx.formatSeparator()
                   .visit(K_ORDER_BY).sql(' ')
                   .visit(orderBy);

            acceptLimit(ctx, limit);
        }

        ctx.start(DELETE_RETURNING);
        toSQLReturning(ctx);
        ctx.end(DELETE_RETURNING);
    }

    static final record MergeUsing(Table<?> table, boolean patchSource) {

    }

    static final MergeUsing mergeUsing(
        TableList tables,
        Table<?> table,
        Condition condition,
        SortFieldList orderBy,
        Field<? extends Number> limit
    ) {
        boolean patchSource = true;

        if (orderBy.isEmpty() && limit == null) {
            if (tables.size() == 1 && tables.get(0) instanceof TableImpl && !(patchSource = false))
                return new MergeUsing(tables.get(0), patchSource);
            else
                return new MergeUsing(select().from(tables).asTable("s"), patchSource);
        }

        // TODO [#13326]: Avoid the JOIN if it isn't strictly necessary
        //                (i.e. if ORDER BY references only from, not table)
        else
            return new MergeUsing(
                select(tables.fields())
                .from(tables)
                .join(table).on(condition)
                .orderBy(orderBy)
                .limit(limit)
                .asTable("s"),
                patchSource
            );

    }

    private final void acceptUsingAsMerge(Context<?> ctx) {
        // TODO: What about RETURNING?
        // TODO: What if there are multiple FROM tables?
        // TODO: What if there are SET ROW = ROW assignment(s)?
        // TODO: What if there are SET ROW = (SELECT ..) assignment(s)?

        Condition c = condition;
        Table<?> t = table(ctx);
        TableList u;

        // [#15637] Same semantics as NO_SUPPORT_REPEAT_FROM_IN_USING
        if (containsDeclaredTable(using, t)) {
            u = new TableList(using);
            u.remove(t);
        }
        else
            u = using;

        MergeUsing mu = mergeUsing(u, t, c, orderBy, limit);

        if (mu.patchSource() && ctx.configuration().requireCommercial(() -> "The DELETE .. USING to MERGE transformation requires commercial only logic for non-trivial USING clauses. Please upgrade to the jOOQ Professional Edition or jOOQ Enterprise Edition")) {











        }

        ctx.visit(mergeInto(table).using(mu.table()).on(c).whenMatchedThenDelete());
    }

    static final void acceptLimit(Context<?> ctx, Field<? extends Number> limit) {
        if (limit != null)
            if (ctx.family() == FIREBIRD)
                ctx.formatSeparator()
                   .visit(K_ROWS).sql(' ')
                   .visit(limit);
            else
                ctx.formatSeparator()
                   .visit(K_LIMIT).sql(' ')
                   .visit(limit);
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











    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    final DeleteQueryImpl<R> copy(Consumer<? super DeleteQueryImpl<?>> finisher) {
        return copy(finisher, table);
    }

    final <O extends Record> DeleteQueryImpl<O> copy(Consumer<? super DeleteQueryImpl<?>> finisher, Table<O> t) {
        DeleteQueryImpl<O> r = new DeleteQueryImpl<>(configuration(), with, t);
        r.using.addAll(using);
        r.condition.addConditions(extractCondition(condition));
        r.orderBy.addAll(orderBy);
        r.limit = limit;

        if (!returning.isEmpty())
            r.setReturning(returning);

        finisher.accept(r);
        return r;
    }

    @Override
    public final WithImpl $with() {
        return with;
    }

    @Override
    public final Table<R> $from() {
        return table;
    }

    @Override
    public final Delete<?> $from(Table<?> newFrom) {
        if ($from() == newFrom)
            return this;
        else
            return copy(d -> {}, newFrom);
    }

    @Override
    public final UnmodifiableList<? extends Table<?>> $using() {
        return QOM.unmodifiable(using);
    }

    @Override
    public final Delete<R> $using(Collection<? extends Table<?>> using) {
        return copy(d -> {
            d.using.clear();
            d.using.addAll(using);
        });
    }

    @Override
    public final Condition $where() {
        return condition.getWhereOrNull();
    }

    @Override
    public final Delete<R> $where(Condition newWhere) {
        if ($where() == newWhere)
            return this;
        else
            return copy(d -> d.condition.setWhere(newWhere));
    }

    @Override
    public final UnmodifiableList<? extends SortField<?>> $orderBy() {
        return QOM.unmodifiable(orderBy);
    }

    @Override
    public final Delete<R> $orderBy(Collection<? extends SortField<?>> newOrderBy) {
        return copy(d -> {
            d.orderBy.clear();
            d.orderBy.addAll(newOrderBy);
        });
    }

    @Override
    public final Field<? extends Number> $limit() {
        return limit;
    }

    @Override
    public final Delete<R> $limit(Field<? extends Number> newLimit) {
        if ($limit() == newLimit)
            return this;
        else
            return copy(d -> d.limit = newLimit);
    }









































}
