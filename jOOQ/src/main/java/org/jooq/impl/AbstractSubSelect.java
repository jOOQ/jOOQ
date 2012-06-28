/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.impl.Factory.literal;
import static org.jooq.impl.Factory.one;

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
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.tools.StringUtils;

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
            return getAttachables(select, from, condition, groupBy, having, orderBy, limit, forUpdateOf, forUpdateOfTables);
        }
        else {
            return getAttachables(select, from, condition, groupBy, having, orderBy, forUpdateOf, forUpdateOfTables);
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.declareFields(true)
               .bind((QueryPart) getSelect0())
               .declareFields(false)
               .declareTables(true)
               .bind((QueryPart) getFrom())
               .declareTables(false)
               .bind(getWhere())
               .bind(getConnectByStartWith())
               .bind(getConnectBy())
               .bind((QueryPart) getGroupBy())
               .bind(getHaving())
               .bind((QueryPart) getOrderBy());

        // TOP clauses never bind values. So this can be safely applied at the
        // end for LIMIT .. OFFSET clauses, or ROW_NUMBER() filtering
        if (getLimit().isApplicable()) {
            context.bind(getLimit());
        }

        context.bind((QueryPart) forUpdateOf)
               .bind((QueryPart) forUpdateOfTables);
    }

    @Override
    public final void toSQL(RenderContext context) {

        // If this is a nested select, be sure that indentation will stay on
        // the same level for the whole nested select
        context.formatIndentLockStart();

        // If a limit applies
        if (getLimit().isApplicable()) {
            switch (context.getDialect()) {

                // Oracle knows the ROWNUM pseudo-column. That makes things simple
                case ORACLE:
                    toSQLReferenceLimitOracle(context);
                    break;

                // With DB2, there are two possibilities
                case DB2: {

                    // DB2 natively supports a "FIRST ROWS" clause, without
                    // offset and without bind values
                    if (getLimit().offsetZero() && !getLimit().rendersParams()) {
                        toSQLReferenceLimitDefault(context);
                    }

                    // "OFFSET" has to be simulated
                    else {
                        toSQLReferenceLimitDB2SQLServerSybase(context);
                    }

                    break;
                }

                // Sybase ASE and SQL Server support a TOP clause without OFFSET
                // OFFSET can be simulated in SQL Server, not in ASE
                case ASE:
                case SQLSERVER: {

                    // Native TOP support, without OFFSET and without bind values
                    if (getLimit().offsetZero() && !getLimit().rendersParams()) {
                        toSQLReference0(context);
                    }

                    // OFFSET simulation
                    else {
                        toSQLReferenceLimitDB2SQLServerSybase(context);
                    }

                    break;
                }

                // Sybase has TOP .. START AT support, but only without bind
                // variables
                case SYBASE: {

                    // Native TOP support, without OFFSET and without bind values
                    if (!getLimit().rendersParams()) {
                        toSQLReference0(context);
                    }

                    // OFFSET simulation
                    else {
                        toSQLReferenceLimitDB2SQLServerSybase(context);
                    }

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

        // [#1296] FOR UPDATE is simulated in some dialects using ResultSet.CONCUR_UPDATABLE
        if (forUpdate && !asList(CUBRID, SQLSERVER).contains(context.getDialect())) {
            context.formatSeparator()
                   .keyword("for update");

            if (!forUpdateOf.isEmpty()) {
                context.keyword(" of ");
                Util.toSQLNames(context, forUpdateOf);
            }
            else if (!forUpdateOfTables.isEmpty()) {
                context.keyword(" of ");

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
                        Util.toSQLNames(context, forUpdateOfTables);
                        break;
                }
            }

            if (forUpdateMode != null) {
                context.sql(" ");
                context.keyword(forUpdateMode.toSQL());

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
                    context.formatSeparator()
                           .keyword("lock in share mode");
                    break;

                // Postgres is known to implement the "FOR SHARE" clause like this
                default:
                    context.formatSeparator()
                           .keyword("for share");
                    break;
            }
        }

        // See start of method: Indent locking for nested selects
        context.formatIndentLockEnd();
    }

    /**
     * The default LIMIT / OFFSET clause in most dialects
     */
    private void toSQLReferenceLimitDefault(RenderContext context) {
        toSQLReference0(context);
        context.sql(getLimit());
    }

    /**
     * Simulate the LIMIT / OFFSET clause in the {@link SQLDialect#DB2},
     * {@link SQLDialect#SQLSERVER} and {@link SQLDialect#SYBASE} dialects
     */
    private final void toSQLReferenceLimitDB2SQLServerSybase(RenderContext context) {
        RenderContext local = new DefaultRenderContext(context);
        toSQLReference0(local);
        String enclosed = local.render();

        String subqueryName = "limit_" + Util.hash(enclosed);
        String rownumName = "rownum_" + Util.hash(enclosed);

        context.keyword("select * from (")
               .formatIndentStart()
               .formatNewLine()
               .keyword("select ")
               .sql(subqueryName)
               .sql(".*, row_number() ")
               .keyword("over (order by ");

        if (getOrderBy().isEmpty()) {
            context.literal(getSelect().get(0).getName());
        }
        else {
            String separator = "";

            for (SortField<?> field : getOrderBy()) {
                context.sql(separator)
                       .literal(field.getName())
                       .sql(" ")
                       .keyword(field.getOrder().toSQL());

                separator = ", ";
            }
        }

        context.keyword(") as ")
               .sql(rownumName)
               .formatSeparator()
               .keyword("from (")
               .formatIndentStart()
               .formatNewLine()
               .sql(enclosed)
               .formatIndentEnd()
               .formatNewLine()
               .keyword(") as ")
               .sql(subqueryName)
               .formatIndentEnd()
               .formatNewLine()
               .keyword(") as ")
               .sql("outer_")
               .sql(subqueryName)
               .formatSeparator()
               .keyword("where ")
               .sql(rownumName)
               .sql(" > ")
               .sql(getLimit().getLowerRownum())
               .formatSeparator()
               .keyword("and ")
               .sql(rownumName)
               .sql(" <= ")
               .sql(getLimit().getUpperRownum());
    }

    /**
     * Simulate the LIMIT / OFFSET clause in the {@link SQLDialect#ORACLE}
     * dialect
     */
    private final void toSQLReferenceLimitOracle(RenderContext context) {
        RenderContext local = new DefaultRenderContext(context);
        toSQLReference0(local);
        String enclosed = local.render();

        String subqueryName = "limit_" + Util.hash(enclosed);
        String rownumName = "rownum_" + Util.hash(enclosed);

        context.keyword("select * from (")
               .formatIndentStart()
               .formatNewLine()
               .keyword("select ")
               .sql(subqueryName)
               .keyword(".*, rownum as ")
               .sql(rownumName)
               .formatSeparator()
               .keyword("from (")
               .formatIndentStart()
               .formatNewLine()
               .sql(enclosed)
               .formatIndentEnd()
               .formatNewLine()
               .sql(") ")
               .sql(subqueryName)
               .formatIndentEnd()
               .formatNewLine()
               .keyword(")")
               .formatSeparator()
               .keyword("where ")
               .sql(rownumName)
               .sql(" > ")
               .sql(getLimit().getLowerRownum())
               .formatSeparator()
               .keyword("and ")
               .sql(rownumName)
               .sql(" <= ")
               .sql(getLimit().getUpperRownum());
    }

    /**
     * This method renders the main part of a query without the LIMIT clause.
     * This part is common to any type of limited query
     */
    private final void toSQLReference0(RenderContext context) {

        // SELECT clause
        // -------------
        context.keyword("select ");

        // [#1493] Oracle hints come directly after the SELECT keyword
        if (!StringUtils.isBlank(hint)) {
            context.sql(hint).sql(" ");
        }

        if (distinct) {
            context.keyword("distinct ");
        }

        // Sybase and SQL Server have leading TOP clauses
        switch (context.getDialect()) {
            case ASE:
            case SQLSERVER: {

                // If we have a TOP clause, it needs to be rendered here
                if (getLimit().isApplicable() && getLimit().offsetZero() && !getLimit().rendersParams()) {
                    context.sql(getLimit()).sql(" ");
                }

                // If we don't have a limit, some subqueries still need a "TOP" clause
                else if (context.getDialect() == SQLSERVER && !getOrderBy().isEmpty()) {

                    // [#759] The TOP 100% is only rendered in subqueries
                    if (context.subquery() || getLimit().isApplicable()) {
                        context.keyword("top 100 percent ");
                    }
                }

                break;
            }

            case SYBASE: {
                if (getLimit().isApplicable() && !getLimit().rendersParams()) {
                    context.sql(getLimit()).sql(" ");
                }

                break;
            }

            // [#780] Ordered subqueries should be handled for Ingres and ASE as well
            case INGRES: {
            }
        }

        context.declareFields(true);
        context.sql(getSelect1());
        context.declareFields(false);

        // FROM and JOIN clauses
        // ---------------------
        context.declareTables(true);

        if (!context.render(getFrom()).isEmpty()) {
            context.formatSeparator()
                   .keyword("from ")
                   .sql(getFrom());
        }

        context.declareTables(false);

        // WHERE clause
        // ------------
        if (!(getWhere().getWhere() instanceof TrueCondition)) {
            context.formatSeparator()
                   .keyword("where ")
                   .sql(getWhere());
        }

        // CONNECT BY clause
        // -----------------
        if (!(getConnectBy().getWhere() instanceof TrueCondition)) {

            // CUBRID supports this clause only as [ START WITH .. ] CONNECT BY
            // Oracle also knows the CONNECT BY .. [ START WITH ] alternative
            // syntax
            toSQLStartWith(context);
            toSQLConnectBy(context);
        }

        // GROUP BY and HAVING clause
        // --------------------------
        if (!getGroupBy().isEmpty()) {
            context.formatSeparator()
                   .keyword("group by ")
                   .sql(getGroupBy());
        }

        if (!(getHaving().getWhere() instanceof TrueCondition)) {
            context.formatSeparator()
                   .keyword("having ")
                   .sql(getHaving());
        }

        // ORDER BY clause
        // ---------------
        if (!getOrderBy().isEmpty()) {
            context.formatSeparator()
                   .keyword("order by ")
                   .sql(getOrderBy());
        }
    }

    private void toSQLStartWith(RenderContext context) {
        if (!(getConnectByStartWith().getWhere() instanceof TrueCondition)) {
            context.formatSeparator()
                   .keyword("start with ")
                   .sql(getConnectByStartWith());
        }
    }

    private void toSQLConnectBy(RenderContext context) {
        context.formatSeparator()
               .keyword("connect by");

        if (connectByNoCycle) {
            context.keyword(" nocycle");
        }

        context.sql(" ").sql(getConnectBy());
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
    public final void addLimit(Param<Integer> numberOfRows) {
        addLimit(0, numberOfRows);
    }

    @Override
    public final void addLimit(int offset, int numberOfRows) {
        limit.setOffset(offset);
        limit.setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(int offset, Param<Integer> numberOfRows) {
        limit.setOffset(offset);
        limit.setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(Param<Integer> offset, int numberOfRows) {
        limit.setOffset(offset);
        limit.setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(Param<Integer> offset, Param<Integer> numberOfRows) {
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

    @Override
    public final List<Field<?>> getSelect() {
        return getSelect1();
    }

    final FieldList getSelect0() {
        return select;
    }

    final FieldList getSelect1() {
        if (getSelect0().isEmpty()) {
            FieldList result = new SelectFieldList();

            // [#109] [#489]: SELECT * is only applied when at least one table
            // from the table source is "unknown", i.e. not generated from a
            // physical table. Otherwise, the fields are selected explicitly
            if (knownTableSource()) {
                for (TableLike<?> table : getFrom()) {
                    for (Field<?> field : table.asTable().getFields()) {
                        result.add(field);
                    }
                }
            }

            // The default is SELECT 1, when projections and table sources are
            // both empty
            if (getFrom().isEmpty()) {
                result.add(one());
            }

            return result;
        }

        return getSelect0();
    }

    private final boolean knownTableSource() {
        for (Table<?> table : getFrom()) {
            if (!knownTable(table)) {
                return false;
            }
        }

        return true;
    }

    private final boolean knownTable(Table<?> table) {
        return table.getFields().size() > 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class<? extends R> getRecordType() {
        // Generated record classes only come into play, when the select is
        // - on a single table
        // - a select *

        if (getFrom().size() == 1 && getSelect0().isEmpty()) {
            return (Class<? extends R>) getFrom().get(0).asTable().getRecordType();
        }
        else {
            return (Class<? extends R>) RecordImpl.class;
        }
    }

    final TableList getFrom() {
        return from;
    }

    final FieldList getGroupBy() {
        return groupBy;
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
    public final void addOrderBy(int... fieldIndexes) {
        Field<?>[] fields = new Field[fieldIndexes.length];

        for (int i = 0; i < fieldIndexes.length; i++) {
            fields[i] = literal(fieldIndexes[i]);
        }

        addOrderBy(fields);
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

    @Override
    final boolean isForUpdate() {
        return forUpdate;
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
