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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.Condition;
import org.jooq.ConditionProvider;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.LockProvider;
import org.jooq.Operator;
import org.jooq.OrderProvider;
import org.jooq.Record;
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
    public final int bindReference(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException {
        int result = initialIndex;

        result = getSelect0().bindDeclaration(configuration, stmt, result);
        result = getFrom().bindDeclaration(configuration, stmt, result);
        result = getJoin().bindDeclaration(configuration, stmt, result);
        result = getWhere().bindReference(configuration, stmt, result);
        result = getConnectBy().bindReference(configuration, stmt, result);
        result = getConnectByStartWith().bindReference(configuration, stmt, result);
        result = getGroupBy().bindReference(configuration, stmt, result);
        result = getHaving().bindReference(configuration, stmt, result);
        result = getOrderBy().bindReference(configuration, stmt, result);

        if (getLimit().isApplicable()) {
            result = getLimit().bindReference(configuration, stmt, result);
        }

        result = forUpdateOf.bindReference(configuration, stmt, result);
        result = forUpdateOfTables.bindReference(configuration, stmt, result);

        return result;
    }

    @Override
    public final String toSQLReference(Configuration configuration, boolean inlineParameters) {
        StringBuilder sb = new StringBuilder();

        // If a limit applies
        if (getLimit().isApplicable()) {
            switch (configuration.getDialect()) {

                // Oracle knows the ROWNUM pseudo-column. That makes things simple
                case ORACLE:
                    sb.append(toSQLReferenceLimitOracle(configuration, inlineParameters));
                    break;

                // With DB2, there are two possibilities
                case DB2:

                    // DB2 natively supports a "FIRST ROWS" clause, without offset
                    if (getLimit().getOffset() == 0) {
                        sb.append(toSQLReferenceLimitDefault(configuration, inlineParameters));
                    }

                    // "OFFSET" has to be simulated
                    else {
                        sb.append(toSQLReferenceLimitDB2SQLServerSybase(configuration, inlineParameters));
                    }

                    break;

                case SQLSERVER:
                case SYBASE: {

                    // SQL Server and Sybase natively support a "TOP" clause,
                    // without offset
                    if (getLimit().getOffset() == 0) {
                        sb.append(toSQLReference0(configuration, inlineParameters));
                    }

                    // "OFFSET" has to be simulated
                    else {
                        sb.append(toSQLReferenceLimitDB2SQLServerSybase(configuration, inlineParameters));
                    }

                    break;
                }

                // By default, render the dialect's limit clause
                default: {
                    sb.append(toSQLReferenceLimitDefault(configuration, inlineParameters));
                }
            }
        }

        // If no limit applies, just render the rest of the query
        else {
            sb.append(toSQLReference0(configuration, inlineParameters));
        }

        if (forUpdate) {
            sb.append(" for update");

            if (!forUpdateOf.isEmpty()) {
                sb.append(" of ");
                sb.append(forUpdateOf.toSQLNames(configuration));
            }
            else if (!forUpdateOfTables.isEmpty()) {
                sb.append(" of ");

                switch (configuration.getDialect()) {

                    // Some dialects don't allow for an OF [table-names] clause
                    // It can be simulated by listing the table's fields, though
                    case DB2:
                    case DERBY:
                    case INGRES:
                    case ORACLE: {
                        sb.append(forUpdateOfTables.toSQLFieldNames(configuration));
                        break;
                    }

                    // Render the OF [table-names] clause
                    default:
                        sb.append(forUpdateOfTables.toSQLNames(configuration));
                        break;
                }
            }

            if (forUpdateMode != null) {
                sb.append(" ");
                sb.append(forUpdateMode.toSQL());

                if (forUpdateMode == ForUpdateMode.WAIT) {
                    sb.append(" ");
                    sb.append(forUpdateWait);
                }
            }
        }
        else if (forShare) {
            switch (configuration.getDialect()) {

                // MySQL has a non-standard implementation for the "FOR SHARE" clause
                case MYSQL:
                    sb.append(" lock in share mode");
                    break;

                // Postgres is known to implement the "FOR SHARE" clause like this
                default:
                    sb.append(" for share");
                    break;
            }
        }

        return sb.toString();
    }

    /**
     * The default LIMIT / OFFSET clause in most dialects
     */
    private String toSQLReferenceLimitDefault(Configuration configuration, boolean inlineParameters) {
        StringBuilder sb = new StringBuilder();
        String enclosed = toSQLReference0(configuration, inlineParameters);

        sb.append(enclosed);
        sb.append(" ");
        sb.append(getLimit().toSQLReference(configuration, inlineParameters));

        return sb.toString();
    }

    /**
     * Simulate the LIMIT / OFFSET clause in the {@link SQLDialect#DB2} and
     * {@link SQLDialect#SQLSERVER} dialects
     */
    private final String toSQLReferenceLimitDB2SQLServerSybase(Configuration configuration, boolean inlineParameters) {
        StringBuilder sb = new StringBuilder();
        String enclosed = toSQLReference0(configuration, inlineParameters);

        String subqueryName = "limit_" + Math.abs(enclosed.hashCode());
        String rownumName = "rownum_" + Math.abs(enclosed.hashCode());

        sb.append("select * from (select ");
        sb.append(subqueryName);
        sb.append(".*, row_number() over (order by ");

        if (getOrderBy().isEmpty()) {
            sb.append(getSelect().get(0).getName());
        }
        else {
            String separator = "";
            for (SortField<?> field : getOrderBy()) {
                sb.append(separator);
                sb.append(field.getName());
                sb.append(" ");
                sb.append(field.getOrder().toSQL());

                separator = ", ";
            }
        }

        sb.append(") as ");
        sb.append(rownumName);
        sb.append(" from (");
        sb.append(enclosed);
        sb.append(") as ");
        sb.append(subqueryName);
        sb.append(") as outer_");
        sb.append(subqueryName);
        sb.append(" where ");
        sb.append(rownumName);
        sb.append(" >= ");

        if (inlineParameters) {
            sb.append(getLimit().getLowerRownum());
        }
        else {
            sb.append("?");
        }

        sb.append(" and ");
        sb.append(rownumName);
        sb.append(" < ");

        if (inlineParameters) {
            sb.append(getLimit().getUpperRownum());
        }
        else {
            sb.append("?");
        }

        return sb.toString();
    }

    /**
     * Simulate the LIMIT / OFFSET clause in the {@link SQLDialect#ORACLE}
     * dialect
     */
    private final String toSQLReferenceLimitOracle(Configuration configuration, boolean inlineParameters) {
        StringBuilder sb = new StringBuilder();

        String enclosed = toSQLReference0(configuration, inlineParameters);

        String subqueryName = "limit_" + Math.abs(enclosed.hashCode());
        String rownumName = "rownum_" + Math.abs(enclosed.hashCode());

        sb.append("select * from (select ");
        sb.append(subqueryName);
        sb.append(".*, rownum as ");
        sb.append(rownumName);
        sb.append(" from (");
        sb.append(enclosed);
        sb.append(") ");
        sb.append(subqueryName);
        sb.append(") where ");
        sb.append(rownumName);
        sb.append(" >= ");

        if (inlineParameters) {
            sb.append(getLimit().getLowerRownum());
        }
        else {
            sb.append("?");
        }

        sb.append(" and ");
        sb.append(rownumName);
        sb.append(" < ");

        if (inlineParameters) {
            sb.append(getLimit().getUpperRownum());
        }
        else {
            sb.append("?");
        }

        return sb.toString();
    }

    /**
     * This method renders the main part of a query without the LIMIT clause.
     * This part is common to any type of limited query
     */
    private final String toSQLReference0(Configuration configuration, boolean inlineParameters) {
        StringBuilder sb = new StringBuilder();

        sb.append("select ");
        if (distinct) {
            sb.append("distinct ");
        }

        if (!StringUtils.isBlank(hint)) {
            sb.append(hint);
            sb.append(" ");
        }

        // SQL Server is a bit different from the other dialects
        switch (configuration.getDialect()) {
            case SQLSERVER: // No break
            case SYBASE:

                // If we have a limit, it needs to be rendered here
                if (getLimit().isApplicable() && getLimit().getOffset() == 0) {
                    sb.append(getLimit().toSQLReference(configuration, inlineParameters));
                    sb.append(" ");
                }

                // If we don't have a limit, some subqueries still need a "TOP" clause
                else if (configuration.getDialect() == SQLSERVER && !getOrderBy().isEmpty()) {
                    sb.append("top 100 percent ");
                }
        }

        sb.append(getSelect().toSQLDeclaration(configuration, inlineParameters));
        if (!getFrom().toSQLDeclaration(configuration, inlineParameters).isEmpty()) {
            sb.append(" from ");
            sb.append(getFrom().toSQLDeclaration(configuration, inlineParameters));
        }

        if (!getJoin().isEmpty()) {
            sb.append(" ");
            sb.append(getJoin().toSQLDeclaration(configuration, inlineParameters));
        }

        if (!(getWhere().getWhere() instanceof TrueCondition)) {
            sb.append(" where ");
            sb.append(getWhere().toSQLReference(configuration, inlineParameters));
        }

        if (!(getConnectBy().getWhere() instanceof TrueCondition)) {
            sb.append(" connect by");

            if (connectByNoCycle) {
                sb.append(" nocycle");
            }

            sb.append(" ");
            sb.append(getConnectBy().toSQLReference(configuration, inlineParameters));

            if (!(getConnectByStartWith().getWhere() instanceof TrueCondition)) {
                sb.append(" start with ");
                sb.append(getConnectByStartWith().toSQLReference(configuration, inlineParameters));
            }
        }

        if (!getGroupBy().isEmpty()) {
            sb.append(" group by ");
            sb.append(getGroupBy().toSQLReference(configuration, inlineParameters));
        }

        if (!(getHaving().getWhere() instanceof TrueCondition)) {
            sb.append(" having ");
            sb.append(getHaving().toSQLReference(configuration, inlineParameters));
        }

        if (!getOrderBy().isEmpty()) {
            sb.append(" order by ");
            sb.append(getOrderBy().toSQLReference(configuration, inlineParameters));
        }

        return sb.toString();
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
        // #109 : Don't allow empty select lists to render select *
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
    @SuppressWarnings("unchecked")
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
