/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SQLSERVER2008;
import static org.jooq.SQLDialect.SQLSERVER2012;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.Utils.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.GroupField;
import org.jooq.JoinType;
import org.jooq.Operator;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.SelectQuery;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.conf.ParamType;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.StringUtils;

/**
 * A sub-select is a <code>SELECT</code> statement that can be combined with
 * other <code>SELECT</code> statement in <code>UNION</code>s and similar
 * operations.
 *
 * @author Lukas Eder
 */
class SelectQueryImpl<R extends Record> extends AbstractSelect<R> implements SelectQuery<R> {

    /**
     * Generated UID
     */
    private static final long               serialVersionUID = 1646393178384872967L;

    private final SelectFieldList           select;
    private String                          hint;
    private String                          option;
    private boolean                         distinct;
    private boolean                         forUpdate;
    private final QueryPartList<Field<?>>   forUpdateOf;
    private final TableList                 forUpdateOfTables;
    private ForUpdateMode                   forUpdateMode;
    private int                             forUpdateWait;
    private boolean                         forShare;
    private final TableList                 from;
    private final ConditionProviderImpl     condition;
    private final ConditionProviderImpl     connectBy;
    private boolean                         connectByNoCycle;
    private final ConditionProviderImpl     connectByStartWith;
    private boolean                         grouping;
    private final QueryPartList<GroupField> groupBy;
    private final ConditionProviderImpl     having;
    private final SortFieldList             orderBy;
    private boolean                         orderBySiblings;
    private final Limit                     limit;

    SelectQueryImpl(Configuration configuration) {
        this(configuration, null);
    }

    SelectQueryImpl(Configuration configuration, boolean distinct) {
        this(configuration, null, distinct);
    }

    SelectQueryImpl(Configuration configuration, TableLike<? extends R> from) {
        this(configuration, from, false);
    }

    SelectQueryImpl(Configuration configuration, TableLike<? extends R> from, boolean distinct) {
        super(configuration);

        this.distinct = distinct;
        this.select = new SelectFieldList();
        this.from = new TableList();
        this.condition = new ConditionProviderImpl();
        this.connectBy = new ConditionProviderImpl();
        this.connectByStartWith = new ConditionProviderImpl();
        this.groupBy = new QueryPartList<GroupField>();
        this.having = new ConditionProviderImpl();
        this.orderBy = new SortFieldList();
        this.limit = new Limit();

        if (from != null) {
            this.from.add(from.asTable());
        }

        this.forUpdateOf = new QueryPartList<Field<?>>();
        this.forUpdateOfTables = new TableList();
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

        // If a limit applies
        if (getLimit().isApplicable()) {
            switch (context.configuration().dialect()) {

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
                        toSQLReferenceLimitDB2SQLServer2008Sybase(context);
                    }

                    break;
                }

                // Sybase ASE and SQL Server support a TOP clause without OFFSET
                // OFFSET can be simulated in SQL Server, not in ASE
                case ASE:
                case SQLSERVER2008: {

                    // Native TOP support, without OFFSET and without bind values
                    if (getLimit().offsetZero() && !getLimit().rendersParams()) {
                        toSQLReference0(context);
                    }

                    // OFFSET simulation
                    else {
                        toSQLReferenceLimitDB2SQLServer2008Sybase(context);
                    }

                    break;
                }

                // Sybase has TOP .. START AT support (no bind values)
                // Firebird has FIRST .. SKIP support (no bind values)
                case SYBASE: {

                    // Native TOP support, without OFFSET and without bind values
                    if (!getLimit().rendersParams()) {
                        toSQLReference0(context);
                    }

                    // OFFSET simulation
                    else {
                        toSQLReferenceLimitDB2SQLServer2008Sybase(context);
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
        if (forUpdate && !asList(CUBRID, SQLSERVER).contains(context.configuration().dialect().family())) {
            context.formatSeparator()
                   .keyword("for update");

            if (!forUpdateOf.isEmpty()) {
                context.keyword(" of ");
                Utils.fieldNames(context, forUpdateOf);
            }
            else if (!forUpdateOfTables.isEmpty()) {
                context.keyword(" of ");

                switch (context.configuration().dialect()) {

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
                        Utils.tableNames(context, forUpdateOfTables);
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
            switch (context.configuration().dialect()) {

                // MySQL has a non-standard implementation for the "FOR SHARE" clause
                case MARIADB:
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

        // [#1952] SQL Server OPTION() clauses as well as many other optional
        // end-of-query clauses are appended to the end of a query
        if (!StringUtils.isBlank(option)) {
            context.formatSeparator()
                   .sql(option);
        }

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
     * {@link SQLDialect#SQLSERVER2008} and {@link SQLDialect#SYBASE} dialects
     */
    private final void toSQLReferenceLimitDB2SQLServer2008Sybase(RenderContext context) {

        // [#1954] Render enclosed SELECT first to obtain a "unique" hash code
        RenderContext tmpLocal = new DefaultRenderContext(context);
        tmpLocal.subquery(true);
        toSQLReference0(tmpLocal);
        String tmpEnclosed = tmpLocal.render();

        String subqueryName = "limit_" + Utils.hash(tmpEnclosed);
        String rownumName = "rownum_" + Utils.hash(tmpEnclosed);

        // Render enclosed SELECT again, adding an additional ROW_NUMBER() OVER()
        // window function, calculating row numbers for the LIMIT .. OFFSET clause
        RenderContext local = new DefaultRenderContext(context);
        local.subquery(true);
        toSQLReference0(local, rowNumber().over().orderBy(getNonEmptyOrderBy()).as(rownumName));
        String enclosed = local.render();

        context.keyword("select * from (")
               .formatIndentStart()
               .formatNewLine()
               .sql(enclosed)
               .formatIndentEnd()
               .formatNewLine()
               .keyword(") as ")
               .sql(name(subqueryName))
               .formatSeparator()
               .keyword("where ")
               .sql(name(rownumName))
               .sql(" > ")
               .sql(getLimit().getLowerRownum())
               .formatSeparator()
               .keyword("and ")
               .sql(name(rownumName))
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

        String subqueryName = "limit_" + Utils.hash(enclosed);
        String rownumName = "rownum_" + Utils.hash(enclosed);

        context.keyword("select * from (")
               .formatIndentStart()
               .formatNewLine()
                 .keyword("select ")
                 .sql(name(subqueryName))
                 .keyword(".*, rownum as ")
                 .sql(name(rownumName))
                 .formatSeparator()
                 .keyword("from (")
                 .formatIndentStart()
                 .formatNewLine()
                   .sql(enclosed)
                 .formatIndentEnd()
                 .formatNewLine()
                 .sql(") ")
                 .sql(name(subqueryName))
                 .formatSeparator()
                 .keyword("where rownum <= ")
                 .sql(getLimit().getUpperRownum())
               .formatIndentEnd()
               .formatNewLine()
               .sql(") ")
               .formatSeparator()
               .keyword("where ")
               .sql(name(rownumName))
               .sql(" > ")
               .sql(getLimit().getLowerRownum());
    }

    /**
     * This method renders the main part of a query without the LIMIT clause.
     * This part is common to any type of limited query
     */
    private final void toSQLReference0(RenderContext context) {
        toSQLReference0(context, null);
    }

    /**
     * This method renders the main part of a query without the LIMIT clause.
     * This part is common to any type of limited query
     */
    private final void toSQLReference0(RenderContext context, QueryPart limitOffsetRownumber) {
        SQLDialect dialect = context.configuration().dialect();

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
        switch (dialect.family()) {
            case ASE:
            case SQLSERVER: {

                // If we have a TOP clause, it needs to be rendered here
                if (asList(ASE, SQLSERVER2008).contains(dialect)
                        && getLimit().isApplicable()
                        && getLimit().offsetZero()
                        && !getLimit().rendersParams()) {

                    context.sql(getLimit()).sql(" ");
                }

                // [#759] SQL Server needs a TOP clause in ordered subqueries
                else if (dialect.family() == SQLSERVER
                        && context.subquery()
                        && !getOrderBy().isEmpty()) {

                    // [#2423] SQL Server 2012 will render an OFFSET .. FETCH
                    // clause if there is an applicable limit
                    if (dialect == SQLSERVER2008 || !getLimit().isApplicable()) {
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

        // [#1905] H2 only knows arrays, no row value expressions. Subqueries
        // in the context of a row value expression predicate have to render
        // arrays explicitly, as the subquery doesn't form an implicit RVE
        if (context.subquery() && dialect == H2 && context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY) != null) {
            Object data = context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY);

            try {
                context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, null);
                context.sql("(")
                       .sql(getSelect1())
                       .sql(")");
            }
            finally {
                context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, data);
            }
        }

        // The default behaviour
        else {
            context.sql(getSelect1());
        }

        if (limitOffsetRownumber != null) {

            // [#1724] Inlining is necessary to avoid further complexity between
            // toSQL()'s LIMIT .. OFFSET rendering and bind()'s "obliviousness"
            // thereof. This should be improved by delegating to composed Select
            // objects.
            ParamType paramType = context.paramType();
            context.paramType(INLINED)
                   .sql(",")
                   .formatIndentStart()
                   .formatSeparator()
                   .sql(limitOffsetRownumber)
                   .formatIndentEnd()
                   .paramType(paramType);
        }

        context.declareFields(false);

        // FROM and JOIN clauses
        // ---------------------
        context.declareTables(true);

        if (!context.render(getFrom()).isEmpty()) {
            context.formatSeparator()
                   .keyword("from ")
                   .sql(getFrom());

            // [#1681] Sybase ASE and Ingres need a cross-joined dummy table
            // To be able to GROUP BY () empty sets
            if (grouping && getGroupBy().isEmpty() && asList(ASE, INGRES).contains(dialect)) {
                context.sql(", (select 1 as x) as empty_grouping_dummy_table");
            }
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
        if (grouping) {
            context.formatSeparator()
                   .keyword("group by ");

            // [#1665] Empty GROUP BY () clauses need parentheses
            if (getGroupBy().isEmpty()) {

                // [#1681] Use the constant field from the dummy table Sybase ASE, Ingres
                if (asList(ASE, INGRES).contains(dialect)) {
                    context.sql("empty_grouping_dummy_table.x");
                }

                // Some dialects don't support empty GROUP BY () clauses
                else if (asList(CUBRID, DERBY, FIREBIRD, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE).contains(dialect)) {
                    context.sql("1");
                }

                // Few dialects support the SQL standard empty grouping set
                else {
                    context.sql("()");
                }
            }
            else {
                context.sql(getGroupBy());
            }
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
                   .keyword("order ")
                   .keyword(orderBySiblings ? "siblings " : "")
                   .keyword("by ")
                   .sql(getOrderBy());
        }

        // [#2423] SQL Server 2012 requires an ORDER BY clause, along with
        // OFFSET .. FETCH
        else if (getLimit().isApplicable() && asList(SQLSERVER, SQLSERVER2012).contains(dialect)){
            context.formatSeparator()
                   .keyword("order by 1");
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

    @Override
    public final void addSelect(Collection<? extends Field<?>> fields) {
        getSelect0().addAll(fields);
    }

    @Override
    public final void addSelect(Field<?>... fields) {
        addSelect(Arrays.asList(fields));
    }

    @Override
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

    final SelectFieldList getSelect0() {
        return select;
    }

    final SelectFieldList getSelect1() {
        if (getSelect0().isEmpty()) {
            SelectFieldList result = new SelectFieldList();

            // [#109] [#489]: SELECT * is only applied when at least one table
            // from the table source is "unknown", i.e. not generated from a
            // physical table. Otherwise, the fields are selected explicitly
            if (knownTableSource()) {
                for (TableLike<?> table : getFrom()) {
                    for (Field<?> field : table.asTable().fields()) {
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
        return table.fieldsRow().size() > 0;
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

    final void setGrouping() {
        grouping = true;
    }

    final QueryPartList<GroupField> getGroupBy() {
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

    final SortFieldList getNonEmptyOrderBy() {
        if (getOrderBy().isEmpty()) {
            SortFieldList result = new SortFieldList();
            result.add(getSelect().get(0).asc());
            return result;
        }

        return getOrderBy();
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
            fields[i] = inline(fieldIndexes[i]);
        }

        addOrderBy(fields);
    }

    @Override
    public final void setOrderBySiblings(boolean orderBySiblings) {
        this.orderBySiblings = orderBySiblings;
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

    final void setOption(String option) {
        this.option = option;
    }

    @Override
    final boolean isForUpdate() {
        return forUpdate;
    }

    @Override
    public final void addFrom(Collection<? extends TableLike<?>> f) {
        for (TableLike<?> provider : f) {
            getFrom().add(provider.asTable());
        }
    }

    @Override
    public final void addFrom(TableLike<?>... f) {
        addFrom(Arrays.asList(f));
    }

    @Override
    public final void addConnectBy(Condition c) {
        getConnectBy().addConditions(c);
    }

    @Override
    public final void addConnectByNoCycle(Condition c) {
        getConnectBy().addConditions(c);
        setConnectByNoCycle(true);
    }

    @Override
    public final void setConnectByStartWith(Condition c) {
        setStartWith(c);
    }

    @Override
    public final void addGroupBy(Collection<? extends GroupField> fields) {
        setGrouping();
        getGroupBy().addAll(fields);
    }

    @Override
    public final void addGroupBy(GroupField... fields) {
        addGroupBy(Arrays.asList(fields));
    }

    @Override
    public final void addHaving(Condition... conditions) {
        addHaving(Arrays.asList(conditions));
    }

    @Override
    public final void addHaving(Collection<Condition> conditions) {
        getHaving().addConditions(conditions);
    }

    @Override
    public final void addHaving(Operator operator, Condition... conditions) {
        getHaving().addConditions(operator, conditions);
    }

    @Override
    public final void addHaving(Operator operator, Collection<Condition> conditions) {
        getHaving().addConditions(operator, conditions);
    }

    @Override
    public final void addJoin(TableLike<?> table, Condition... conditions) {
        addJoin(table, JoinType.JOIN, conditions);
    }

    @Override
    public final void addJoin(TableLike<?> table, JoinType type, Condition... conditions) {
        addJoin(table, type, conditions, null);
    }

    @Override
    public final void addJoin(TableLike<?> table, JoinType type, Condition[] conditions, Field<?>[] partitionBy) {
        // TODO: This and similar methods should be refactored, patterns extracted...

        int index = getFrom().size() - 1;
        Table<?> joined = null;

        switch (type) {
            case JOIN:
                joined = getFrom().get(index).join(table).on(conditions);
                break;
            case LEFT_OUTER_JOIN:
                joined = getFrom().get(index).leftOuterJoin(table).partitionBy(partitionBy).on(conditions);
                break;
            case RIGHT_OUTER_JOIN:
                joined = getFrom().get(index).rightOuterJoin(table).partitionBy(partitionBy).on(conditions);
                break;
            case FULL_OUTER_JOIN:
                joined = getFrom().get(index).fullOuterJoin(table).on(conditions);
                break;

            // These join types don't take any ON clause. Ignore conditions.
            case CROSS_JOIN:
                joined = getFrom().get(index).crossJoin(table);
                break;
            case NATURAL_JOIN:
                joined = getFrom().get(index).naturalJoin(table);
                break;
            case NATURAL_LEFT_OUTER_JOIN:
                joined = getFrom().get(index).naturalLeftOuterJoin(table);
                break;
            case NATURAL_RIGHT_OUTER_JOIN:
                joined = getFrom().get(index).naturalRightOuterJoin(table);
                break;
        }

        getFrom().set(index, joined);
    }

    @Override
    public final void addJoinOnKey(TableLike<?> table, JoinType type) throws DataAccessException {
        // TODO: This and similar methods should be refactored, patterns extracted...

        int index = getFrom().size() - 1;
        Table<?> joined = null;

        switch (type) {
            case JOIN:
                joined = getFrom().get(index).join(table).onKey();
                break;
            case LEFT_OUTER_JOIN:
                joined = getFrom().get(index).leftOuterJoin(table).onKey();
                break;
            case RIGHT_OUTER_JOIN:
                joined = getFrom().get(index).rightOuterJoin(table).onKey();
                break;
            case FULL_OUTER_JOIN:
                joined = getFrom().get(index).fullOuterJoin(table).onKey();
                break;

            // These join types don't take any ON clause. Ignore conditions.
            case CROSS_JOIN:
                joined = getFrom().get(index).crossJoin(table);
                break;
            case NATURAL_JOIN:
                joined = getFrom().get(index).naturalJoin(table);
                break;
            case NATURAL_LEFT_OUTER_JOIN:
                joined = getFrom().get(index).naturalLeftOuterJoin(table);
                break;
            case NATURAL_RIGHT_OUTER_JOIN:
                joined = getFrom().get(index).naturalRightOuterJoin(table);
                break;
        }

        getFrom().set(index, joined);
    }

    @Override
    public final void addJoinOnKey(TableLike<?> table, JoinType type, TableField<?, ?>... keyFields) throws DataAccessException {
        // TODO: This and similar methods should be refactored, patterns extracted...

        int index = getFrom().size() - 1;
        Table<?> joined = null;

        switch (type) {
            case JOIN:
                joined = getFrom().get(index).join(table).onKey(keyFields);
                break;
            case LEFT_OUTER_JOIN:
                joined = getFrom().get(index).leftOuterJoin(table).onKey(keyFields);
                break;
            case RIGHT_OUTER_JOIN:
                joined = getFrom().get(index).rightOuterJoin(table).onKey(keyFields);
                break;
            case FULL_OUTER_JOIN:
                joined = getFrom().get(index).fullOuterJoin(table).onKey(keyFields);
                break;

            // These join types don't take any ON clause. Ignore conditions.
            case CROSS_JOIN:
                joined = getFrom().get(index).crossJoin(table);
                break;
            case NATURAL_JOIN:
                joined = getFrom().get(index).naturalJoin(table);
                break;
            case NATURAL_LEFT_OUTER_JOIN:
                joined = getFrom().get(index).naturalLeftOuterJoin(table);
                break;
            case NATURAL_RIGHT_OUTER_JOIN:
                joined = getFrom().get(index).naturalRightOuterJoin(table);
                break;
        }

        getFrom().set(index, joined);
    }

    @Override
    public final void addJoinOnKey(TableLike<?> table, JoinType type, ForeignKey<?, ?> key) {
        // TODO: This and similar methods should be refactored, patterns extracted...

        int index = getFrom().size() - 1;
        Table<?> joined = null;

        switch (type) {
            case JOIN:
                joined = getFrom().get(index).join(table).onKey(key);
                break;
            case LEFT_OUTER_JOIN:
                joined = getFrom().get(index).leftOuterJoin(table).onKey(key);
                break;
            case RIGHT_OUTER_JOIN:
                joined = getFrom().get(index).rightOuterJoin(table).onKey(key);
                break;
            case FULL_OUTER_JOIN:
                joined = getFrom().get(index).fullOuterJoin(table).onKey(key);
                break;

            // These join types don't take any ON clause. Ignore conditions.
            case CROSS_JOIN:
                joined = getFrom().get(index).crossJoin(table);
                break;
            case NATURAL_JOIN:
                joined = getFrom().get(index).naturalJoin(table);
                break;
            case NATURAL_LEFT_OUTER_JOIN:
                joined = getFrom().get(index).naturalLeftOuterJoin(table);
                break;
            case NATURAL_RIGHT_OUTER_JOIN:
                joined = getFrom().get(index).naturalRightOuterJoin(table);
                break;
        }

        getFrom().set(index, joined);
    }

    @Override
    public final void addJoinUsing(TableLike<?> table, Collection<? extends Field<?>> fields) {
        addJoinUsing(table, JoinType.JOIN, fields);
    }

    @Override
    public final void addJoinUsing(TableLike<?> table, JoinType type, Collection<? extends Field<?>> fields) {
        // TODO: This and similar methods should be refactored, patterns extracted...

        int index = getFrom().size() - 1;
        Table<?> joined = null;

        switch (type) {
            case JOIN:
                joined = getFrom().get(index).join(table).using(fields);
                break;
            case LEFT_OUTER_JOIN:
                joined = getFrom().get(index).leftOuterJoin(table).using(fields);
                break;
            case RIGHT_OUTER_JOIN:
                joined = getFrom().get(index).rightOuterJoin(table).using(fields);
                break;
            case FULL_OUTER_JOIN:
                joined = getFrom().get(index).fullOuterJoin(table).using(fields);
                break;

            // These join types don't take any USING clause. Ignore fields
            case CROSS_JOIN:
                joined = getFrom().get(index).crossJoin(table);
                break;
            case NATURAL_JOIN:
                joined = getFrom().get(index).naturalJoin(table);
                break;
            case NATURAL_LEFT_OUTER_JOIN:
                joined = getFrom().get(index).naturalLeftOuterJoin(table);
                break;
            case NATURAL_RIGHT_OUTER_JOIN:
                joined = getFrom().get(index).naturalRightOuterJoin(table);
                break;
        }

        getFrom().set(index, joined);
    }

    @Override
    public final void addHint(String h) {
        setHint(h);
    }

    @Override
    public final void addOption(String o) {
        setOption(o);
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
