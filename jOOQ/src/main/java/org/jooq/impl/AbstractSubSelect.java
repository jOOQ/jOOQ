/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import static org.jooq.SQLDialect.SQLSERVER;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.ConditionProvider;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.LockProvider;
import org.jooq.Operator;
import org.jooq.OrderProvider;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableLike;

/**
 * A sub-select is a <code>SELECT</code> statement that can be combined with
 * other <code>SELECT</code> statement in <code>UNION</code>s and similar
 * operations.
 *
 * @author Lukas Eder
 */
abstract class AbstractSubSelect<R extends Record>
extends AbstractSelect<R>
implements
    ConditionProvider,
    OrderProvider,
    LockProvider {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID = 1646393178384872967L;

    private final FieldList             select;
    private String                      hint;
    private boolean                     distinct;
    private boolean                     forUpdate;
    private final FieldList             forUpdateOf;
    private final TableList             forUpdateOfTables;
    private ForUpdateMode               forUpdateMode;
    private int                         forUpdateWait;
    private boolean                     forShare;
    private final TableList             from;
    private final JoinList              join;
    private final ConditionProviderImpl condition;
    private final ConditionProviderImpl connectBy;
    private boolean                     connectByNoCycle;
    private final ConditionProviderImpl connectByStartWith;
    private final FieldList             groupBy;
    private final ConditionProviderImpl having;
    private final SortFieldList         orderBy;
    private final Limit                 limit;



    AbstractSubSelect(Configuration configuration) {
        this(configuration, null);
    }

    AbstractSubSelect(Configuration configuration, TableLike<? extends R> from) {
        this(configuration, from, false);
    }

    AbstractSubSelect(Configuration configuration, TableLike<? extends R> from, boolean distinct) {
        super(configuration);

        this.distinct = distinct;
        this.select = new SelectFieldList();
        this.from = new TableList();
        this.join = new JoinList();
        this.condition = new ConditionProviderImpl();
        this.connectBy = new ConditionProviderImpl();
        this.connectByStartWith = new ConditionProviderImpl();
        this.groupBy = new FieldList();
        this.having = new ConditionProviderImpl();
        this.orderBy = new SortFieldList();
        this.limit = new Limit();

        if (from != null) {
            this.from.add(from.asTable());
        }

        this.forUpdateOf = new FieldList();
        this.forUpdateOfTables = new TableList();
    }

    @Override
    public final List<Attachable> getAttachables() {
        if (limit.isApplicable()) {
            return getAttachables(select, from, join, condition, groupBy, having, orderBy, limit, forUpdateOf, forUpdateOfTables);
        }
        else {
            return getAttachables(select, from, join, condition, groupBy, having, orderBy, forUpdateOf, forUpdateOfTables);
        }
    }

    @Override
    public final void bind(BindContext context) throws SQLException {
        context.declareFields(true)
               .bind((QueryPart) getSelect0())
               .declareFields(false)
               .declareTables(true)
               .bind((QueryPart) getFrom())
               .bind((QueryPart) getJoin())
               .declareTables(false)
               .bind(getWhere())
               .bind(getConnectBy())
               .bind(getConnectByStartWith())
               .bind((QueryPart) getGroupBy())
               .bind(getHaving())
               .bind((QueryPart) getOrderBy());

        if (getLimit().isApplicable()) {
            context.bind(getLimit());
        }

        context.bind((QueryPart) forUpdateOf)
               .bind((QueryPart) forUpdateOfTables);
    }

    @Override
    public final void toSQL(RenderContext context) {

        // If a limit applies
        if (getLimit().isApplicable()) {
            switch (context.getDialect()) {

                // Oracle knows the ROWNUM pseudo-column. That makes things simple
                case ORACLE:
                    toSQLReferenceLimitOracle(context);
                    break;

                // With DB2, there are two possibilities
                case DB2: {

                    // DB2 natively supports a "FIRST ROWS" clause, without offset
                    if (getLimit().getOffset() == 0) {
                        toSQLReferenceLimitDefault(context);
                    }

                    // "OFFSET" has to be simulated
                    else {
                        toSQLReferenceLimitDB2SQLServer(context);
                    }

                    break;
                }

                // Sybase ASE and SQL Server support a TOP clause without OFFSET
                // OFFSET can be simulated in SQL Server, not in ASE
                case ASE:
                case SQLSERVER: {

                    // Native TOP support, without OFFSET
                    if (getLimit().getOffset() == 0) {
                        toSQLReference0(context);
                    }

                    // OFFSET simulation
                    else {
                        toSQLReferenceLimitDB2SQLServer(context);
                    }

                    break;
                }

                case SYBASE: {
                    toSQLReference0(context);
                    break;
                }

                // By default, render the dialect's limit clause
                default: {
                    toSQLReferenceLimitDefault(context);
                    break;
                }
            }
        }

        // If no limit applies, just render the rest of the query
        else {
            toSQLReference0(context);
        }

        if (forUpdate) {
            context.sql(" for update");

            if (!forUpdateOf.isEmpty()) {
                context.sql(" of ");
                forUpdateOf.toSQLNames(context);
            }
            else if (!forUpdateOfTables.isEmpty()) {
                context.sql(" of ");

                switch (context.getDialect()) {

                    // Some dialects don't allow for an OF [table-names] clause
                    // It can be simulated by listing the table's fields, though
                    case DB2:
                    case DERBY:
                    case INGRES:
                    case ORACLE: {
                        forUpdateOfTables.toSQLFieldNames(context);
                        break;
                    }

                    // Render the OF [table-names] clause
                    default:
                        forUpdateOfTables.toSQLNames(context);
                        break;
                }
            }

            if (forUpdateMode != null) {
                context.sql(" ");
                context.sql(forUpdateMode.toSQL());

                if (forUpdateMode == ForUpdateMode.WAIT) {
                    context.sql(" ");
                    context.sql(forUpdateWait);
                }
            }
        }
        else if (forShare) {
            switch (context.getDialect()) {

                // MySQL has a non-standard implementation for the "FOR SHARE" clause
                case MYSQL:
                    context.sql(" lock in share mode");
                    break;

                // Postgres is known to implement the "FOR SHARE" clause like this
                default:
                    context.sql(" for share");
                    break;
            }
        }
    }

    /**
     * The default LIMIT / OFFSET clause in most dialects
     */
    private void toSQLReferenceLimitDefault(RenderContext context) {
        toSQLReference0(context);

        context.sql(" ");
        context.sql(getLimit());
    }

    /**
     * Simulate the LIMIT / OFFSET clause in the {@link SQLDialect#DB2} and
     * {@link SQLDialect#SQLSERVER} dialects
     */
    private final void toSQLReferenceLimitDB2SQLServer(RenderContext context) {
        RenderContext local = new DefaultRenderContext(context);
        toSQLReference0(local);
        String enclosed = local.render();

        String subqueryName = "limit_" + Math.abs(enclosed.hashCode());
        String rownumName = "rownum_" + Math.abs(enclosed.hashCode());

        context.sql("select * from (select ")
               .sql(subqueryName)
               .sql(".*, row_number() over (order by ");

        if (getOrderBy().isEmpty()) {
            context.literal(getSelect().get(0).getName());
        }
        else {
            String separator = "";

            for (SortField<?> field : getOrderBy()) {
                context.sql(separator)
                       .literal(field.getName())
                       .sql(" ")
                       .sql(field.getOrder().toSQL());

                separator = ", ";
            }
        }

        context.sql(") as ")
               .sql(rownumName)
               .sql(" from (")
               .sql(enclosed)
               .sql(") as ")
               .sql(subqueryName)
               .sql(") as outer_")
               .sql(subqueryName)
               .sql(" where ")
               .sql(rownumName)
               .sql(" >= ");

        if (context.inline()) {
            context.sql(getLimit().getLowerRownum());
        }
        else {
            context.sql("?");
        }

        context.sql(" and ")
               .sql(rownumName)
               .sql(" < ");

        if (context.inline()) {
            context.sql(getLimit().getUpperRownum());
        }
        else {
            context.sql("?");
        }
    }

    /**
     * Simulate the LIMIT / OFFSET clause in the {@link SQLDialect#ORACLE}
     * dialect
     */
    private final void toSQLReferenceLimitOracle(RenderContext context) {
        RenderContext local = new DefaultRenderContext(context);
        toSQLReference0(local);
        String enclosed = local.render();

        String subqueryName = "limit_" + Math.abs(enclosed.hashCode());
        String rownumName = "rownum_" + Math.abs(enclosed.hashCode());

        context.sql("select * from (select ")
               .sql(subqueryName)
               .sql(".*, rownum as ")
               .sql(rownumName)
               .sql(" from (")
               .sql(enclosed)
               .sql(") ")
               .sql(subqueryName)
               .sql(") where ")
               .sql(rownumName)
               .sql(" >= ");

        if (context.inline()) {
            context.sql(getLimit().getLowerRownum());
        }
        else {
            context.sql("?");
        }

        context.sql(" and ")
               .sql(rownumName)
               .sql(" < ");

        if (context.inline()) {
            context.sql(getLimit().getUpperRownum());
        }
        else {
            context.sql("?");
        }
    }

    /**
     * This method renders the main part of a query without the LIMIT clause.
     * This part is common to any type of limited query
     */
    private final void toSQLReference0(RenderContext context) {

        // SELECT clause
        // -------------
        context.sql("select ");
        if (distinct) {
            context.sql("distinct ");
        }

        if (!StringUtils.isBlank(hint)) {
            context.sql(hint).sql(" ");
        }

        // Sybase and SQL Server have leading TOP clauses
        switch (context.getDialect()) {
            case ASE:
            case SQLSERVER: {

                // If we have a TOP clause, it needs to be rendered here
                if (getLimit().isApplicable() && getLimit().getOffset() == 0) {
                    context.sql(getLimit()).sql(" ");
                }

                // If we don't have a limit, some subqueries still need a "TOP" clause
                else if (context.getDialect() == SQLSERVER && !getOrderBy().isEmpty()) {

                    // [#759] The TOP 100% is only rendered in subqueries
                    if (context.subquery() || getLimit().isApplicable()) {
                        context.sql("top 100 percent ");
                    }
                }

                break;
            }

            case SYBASE: {
                if (getLimit().isApplicable()) {
                    context.sql(getLimit()).sql(" ");
                }

                break;
            }

            // [#780] Ordered subqueries should be handled for Ingres and ASE as well
            case INGRES: {
            }
        }

        context.declareFields(true);
        context.sql(getSelect());
        context.declareFields(false);

        // FROM and JOIN clauses
        // ---------------------
        context.declareTables(true);

        if (!context.render(getFrom()).isEmpty()) {
            context.sql(" from ").sql(getFrom());
        }

        if (!getJoin().isEmpty()) {
            context.sql(" ").sql(getJoin());
        }

        context.declareTables(false);

        // WHERE clause
        // ------------
        if (!(getWhere().getWhere() instanceof TrueCondition)) {
            context.sql(" where ").sql(getWhere());
        }

        // CONNECT BY clause
        // -----------------
        if (!(getConnectBy().getWhere() instanceof TrueCondition)) {
            context.sql(" connect by");

            if (connectByNoCycle) {
                context.sql(" nocycle");
            }

            context.sql(" ").sql(getConnectBy());

            if (!(getConnectByStartWith().getWhere() instanceof TrueCondition)) {
                context.sql(" start with ").sql(getConnectByStartWith());
            }
        }

        // GROUP BY and HAVING clause
        // --------------------------
        if (!getGroupBy().isEmpty()) {
            context.sql(" group by ").sql(getGroupBy());
        }

        if (!(getHaving().getWhere() instanceof TrueCondition)) {
            context.sql(" having ").sql(getHaving());
        }

        // ORDER BY clause
        // ---------------
        if (!getOrderBy().isEmpty()) {
            context.sql(" order by ").sql(getOrderBy());
        }
    }

    // @Mixin - Declaration in SelectQuery
    public final void addSelect(Collection<? extends Field<?>> fields) {
        getSelect0().addAll(fields);
    }

    // @Mixin - Declaration in SelectQuery
    public final void addSelect(Field<?>... fields) {
        addSelect(Arrays.asList(fields));
    }

    // @Mixin - Declaration in SelectQuery
    public final void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public final void addLimit(int numberOfRows) {
        addLimit(0, numberOfRows);
    }

    @Override
    public final void addLimit(int offset, int numberOfRows) {
        limit.setOffset(offset);
        limit.setNumberOfRows(numberOfRows);
    }

    @Override
    public final void setForUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
        this.forShare = false;
    }

    @Override
    public final void setForUpdateOf(Field<?>... fields) {
        setForUpdateOf(Arrays.asList(fields));
    }

    @Override
    public final void setForUpdateOf(Collection<? extends Field<?>> fields) {
        setForUpdate(true);
        forUpdateOf.clear();
        forUpdateOfTables.clear();
        forUpdateOf.addAll(fields);
    }

    @Override
    public final void setForUpdateOf(Table<?>... tables) {
        setForUpdate(true);
        forUpdateOf.clear();
        forUpdateOfTables.clear();
        forUpdateOfTables.addAll(Arrays.asList(tables));
    }

    @Override
    public final void setForUpdateWait(int seconds) {
        setForUpdate(true);
        forUpdateMode = ForUpdateMode.WAIT;
        forUpdateWait = seconds;
    }

    @Override
    public final void setForUpdateNoWait() {
        setForUpdate(true);
        forUpdateMode = ForUpdateMode.NOWAIT;
        forUpdateWait = 0;
    }

    @Override
    public final void setForUpdateSkipLocked() {
        setForUpdate(true);
        forUpdateMode = ForUpdateMode.SKIP_LOCKED;
        forUpdateWait = 0;
    }

    @Override
    public final void setForShare(boolean forShare) {
        this.forUpdate = false;
        this.forShare = forShare;
        this.forUpdateOf.clear();
        this.forUpdateOfTables.clear();
        this.forUpdateMode = null;
        this.forUpdateWait = 0;
    }

    final FieldList getSelect0() {
        return select;
    }

    @Override
    public final FieldList getSelect() {
        // [#109] : Don't allow empty select lists to render select *
        // Even if select * would be useful, generated client code
        // would be required to be in sync with the database schema

        if (getSelect0().isEmpty()) {
            FieldList result = new SelectFieldList();

            for (TableLike<?> table : getFrom()) {
                for (Field<?> field : table.asTable().getFields()) {
                    result.add(field);
                }
            }

            for (Join j : getJoin()) {
                for (Field<?> field : j.getTable().asTable().getFields()) {
                    result.add(field);
                }
            }

            return result;
        }

        return getSelect0();
    }

    @Override
    @SuppressWarnings({ "unchecked", "deprecation" })
    public final Class<? extends R> getRecordType() {
        // Generated record classes only come into play, when the select is
        // - on a single table
        // - a select *

        if (getTables().size() == 1 && getSelect0().isEmpty()) {
            return (Class<? extends R>) getTables().get(0).asTable().getRecordType();
        }
        else {
            return (Class<? extends R>) RecordImpl.class;
        }
    }

    final TableList getTables() {
        TableList result = new TableList(getFrom());

        for (Join j : getJoin()) {
            result.add(j.getTable().asTable());
        }

        return result;
    }

    final TableList getFrom() {
        return from;
    }

    final FieldList getGroupBy() {
        return groupBy;
    }

    final JoinList getJoin() {
        return join;
    }

    final Limit getLimit() {
        return limit;
    }

    final ConditionProviderImpl getWhere() {
        return condition;
    }

    final ConditionProviderImpl getConnectBy() {
        return connectBy;
    }

    final ConditionProviderImpl getConnectByStartWith() {
        return connectByStartWith;
    }

    final ConditionProviderImpl getHaving() {
        return having;
    }

    final SortFieldList getOrderBy() {
        return orderBy;
    }

    @Override
    public final void addOrderBy(Collection<SortField<?>> fields) {
        getOrderBy().addAll(fields);
    }

    @Override
    public final void addOrderBy(Field<?>... fields) {
        getOrderBy().addAll(fields);
    }

    @Override
    public final void addOrderBy(SortField<?>... fields) {
        addOrderBy(Arrays.asList(fields));
    }

    @Override
    public final void addConditions(Condition... conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Collection<Condition> conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition... conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Collection<Condition> conditions) {
        condition.addConditions(operator, conditions);
    }

    final void setConnectByNoCycle(boolean connectByNoCycle) {
        this.connectByNoCycle = connectByNoCycle;
    }

    final void setStartWith(Condition condition) {
        connectByStartWith.addConditions(condition);
    }

    final void setHint(String hint) {
        this.hint = hint;
    }

    // -------------------------------------------------------------------------
    // Utility classes
    // -------------------------------------------------------------------------

    /**
     * The lock mode for the <code>FOR UPDATE</code> clause, if set.
     */
    private static enum ForUpdateMode {
        WAIT("wait"),
        NOWAIT("nowait"),
        SKIP_LOCKED("skip locked"),

        ;

        private final String sql;

        private ForUpdateMode(String sql) {
            this.sql = sql;
        }

        public final String toSQL() {
            return sql;
        }
    }
}
