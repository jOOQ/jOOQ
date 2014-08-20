/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.jooq.Clause.SELECT;
import static org.jooq.Clause.SELECT_CONNECT_BY;
import static org.jooq.Clause.SELECT_FROM;
import static org.jooq.Clause.SELECT_GROUP_BY;
import static org.jooq.Clause.SELECT_HAVING;
import static org.jooq.Clause.SELECT_INTO;
import static org.jooq.Clause.SELECT_ORDER_BY;
import static org.jooq.Clause.SELECT_SELECT;
import static org.jooq.Clause.SELECT_START_WITH;
import static org.jooq.Clause.SELECT_WHERE;
import static org.jooq.Clause.SELECT_WINDOW;
import static org.jooq.Operator.OR;
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.ACCESS2013;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INFORMIX;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SQLSERVER2008;
import static org.jooq.SQLDialect.SQLSERVER2012;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.SortOrder.ASC;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.orderBy;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.Dual.DUAL_ACCESS;
import static org.jooq.impl.Dual.DUAL_INFORMIX;
import static org.jooq.impl.Utils.DATA_LOCALLY_SCOPED_DATA_MAP;
import static org.jooq.impl.Utils.DATA_OMIT_INTO_CLAUSE;
import static org.jooq.impl.Utils.DATA_OVERRIDE_ALIASES_IN_ORDER_BY;
import static org.jooq.impl.Utils.DATA_RENDERING_DB2_FINAL_TABLE_CLAUSE;
import static org.jooq.impl.Utils.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY;
import static org.jooq.impl.Utils.DATA_SELECT_INTO_TABLE;
import static org.jooq.impl.Utils.DATA_UNALIAS_ALIASES_IN_ORDER_BY;
import static org.jooq.impl.Utils.DATA_WINDOW_DEFINITIONS;
import static org.jooq.impl.Utils.DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.GroupField;
import org.jooq.JoinType;
import org.jooq.Operator;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectQuery;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnStep;
import org.jooq.TablePartitionByStep;
import org.jooq.WindowDefinition;
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
    private static final Clause[]           CLAUSES          = { SELECT };

    private final WithImpl                  with;
    private final SelectFieldList           select;
    private Table<?>                        into;
    private String                          hint;
    private String                          option;
    private boolean                         distinct;
    private final QueryPartList<Field<?>>   distinctOn;
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
    private final WindowList                window;
    private final SortFieldList             orderBy;
    private boolean                         orderBySiblings;
    private final QueryPartList<Field<?>>   seek;
    private boolean                         seekBefore;
    private final Limit                     limit;

    SelectQueryImpl(WithImpl with, Configuration configuration) {
        this(with, configuration, null);
    }

    SelectQueryImpl(WithImpl with, Configuration configuration, boolean distinct) {
        this(with, configuration, null, distinct);
    }

    SelectQueryImpl(WithImpl with, Configuration configuration, TableLike<? extends R> from) {
        this(with, configuration, from, false);
    }

    SelectQueryImpl(WithImpl with, Configuration configuration, TableLike<? extends R> from, boolean distinct) {
        super(configuration);

        this.with = with;
        this.distinct = distinct;
        this.distinctOn = new QueryPartList<Field<?>>();
        this.select = new SelectFieldList();
        this.from = new TableList();
        this.condition = new ConditionProviderImpl();
        this.connectBy = new ConditionProviderImpl();
        this.connectByStartWith = new ConditionProviderImpl();
        this.groupBy = new QueryPartList<GroupField>();
        this.having = new ConditionProviderImpl();
        this.window = new WindowList();
        this.orderBy = new SortFieldList();
        this.seek = new QueryPartList<Field<?>>();
        this.limit = new Limit();

        if (from != null) {
            this.from.add(from.asTable());
        }

        this.forUpdateOf = new QueryPartList<Field<?>>();
        this.forUpdateOfTables = new TableList();
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final void accept(Context<?> context) {
        SQLDialect dialect = context.dialect();
        SQLDialect family = context.family();

        if (into != null
                && context.data(DATA_OMIT_INTO_CLAUSE) == null
                && asList(CUBRID, DB2, DERBY, FIREBIRD, H2, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLITE).contains(family)) {

            context.data(DATA_OMIT_INTO_CLAUSE, true);
            context.visit(DSL.createTable(into).as(this));
            context.data().remove(DATA_OMIT_INTO_CLAUSE);

            return;
        }

        if (with != null)
            context.visit(with).formatSeparator();

        pushWindow(context);

        Boolean wrapDerivedTables = (Boolean) context.data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES);
        if (TRUE.equals(wrapDerivedTables)) {
            context.sql("(")
                   .data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, null);
        }

        // If a limit applies
        if (getLimit().isApplicable()) {
            switch (dialect) {

                /* [pro] */
                // Oracle knows the ROWNUM pseudo-column. That makes things simple
                case ORACLE:
                case ORACLE10G:
                case ORACLE11G:
                case ORACLE12C:
                    toSQLReferenceLimitOracle(context);
                    break;

                // With DB2, there are two possibilities
                case DB2:
                case DB2_9:
                case DB2_10: {

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
                case ACCESS:
                case ACCESS2013:
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

                // Informix has SKIP .. FIRST support
                case INFORMIX:

                // Sybase has TOP .. START AT support (no bind values)
                case SYBASE: {

                    // Native TOP support, without OFFSET and without bind values
                    if (!getLimit().rendersParams() || dialect == INFORMIX) {
                        toSQLReference0(context);
                    }

                    // OFFSET simulation
                    else {
                        toSQLReferenceLimitDB2SQLServer2008Sybase(context);
                    }

                    break;
                }

                /* [/pro] */
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
        if (forUpdate && !asList(CUBRID, SQLSERVER).contains(family)) {
            context.formatSeparator()
                   .keyword("for update");

            if (!forUpdateOf.isEmpty()) {
                context.sql(" ").keyword("of").sql(" ");
                Utils.fieldNames(context, forUpdateOf);
            }
            else if (!forUpdateOfTables.isEmpty()) {
                context.sql(" ").keyword("of").sql(" ");

                switch (family) {

                    // Some dialects don't allow for an OF [table-names] clause
                    // It can be emulated by listing the table's fields, though
                    /* [pro] */
                    case DB2:
                    case INFORMIX:
                    case INGRES:
                    case ORACLE:
                    /* [/pro] */
                    case DERBY: {
                        forUpdateOfTables.toSQLFieldNames(context);
                        break;
                    }

                    // Render the OF [table-names] clause
                    default:
                        Utils.tableNames(context, forUpdateOfTables);
                        break;
                }
            }

            // [#3186] Firebird's FOR UPDATE clause has a different semantics. To achieve "regular"
            // FOR UPDATE semantics, we should use FOR UPDATE WITH LOCK
            if (family == FIREBIRD) {
                context.sql(" ").keyword("with lock");
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
            switch (dialect) {

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

        if (TRUE.equals(wrapDerivedTables)) {
            context.sql(")")
                   .data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, true);
        }

        /* [pro] */
        // INTO clauses
        // ------------
        if (asList(INFORMIX).contains(family)) {
            context.start(SELECT_INTO);

            Table<?> actualInto = (Table<?>) context.data(DATA_SELECT_INTO_TABLE);
            if (actualInto == null)
                actualInto = into;

            if (actualInto != null
                    && context.data(DATA_OMIT_INTO_CLAUSE) == null) {

                context.formatSeparator()
                       .keyword("into")
                       .sql(" ")
                       .visit(actualInto);
            }

            context.end(SELECT_INTO);
        }

        /* [/pro] */
    }

    @SuppressWarnings("unchecked")
    private final void pushWindow(Context<?> context) {
        // [#531] [#2790] Make the WINDOW clause available to the SELECT clause
        // to be able to inline window definitions if the WINDOW clause is not
        // supported.
        if (!getWindow().isEmpty()) {
            ((Map<Object, Object>) context.data(DATA_LOCALLY_SCOPED_DATA_MAP)).put(DATA_WINDOW_DEFINITIONS, getWindow());
        }
    }

    /**
     * The default LIMIT / OFFSET clause in most dialects
     */
    private void toSQLReferenceLimitDefault(Context<?> context) {
        toSQLReference0(context);
        context.visit(getLimit());
    }

    /* [pro] */
    /**
     * Simulate the LIMIT / OFFSET clause in the {@link SQLDialect#DB2},
     * {@link SQLDialect#SQLSERVER2008} and {@link SQLDialect#SYBASE} dialects
     */
    @SuppressWarnings("serial")
    private final void toSQLReferenceLimitDB2SQLServer2008Sybase(Context<?> ctx) {

        // AUTHOR.ID, BOOK.ID, BOOK.TITLE
        final Field<?>[] originalFields = Utils.fieldArray(getSelect());

        // ID, ID, TITLE
        final String[] originalNames = Utils.fieldNames(originalFields);

        // v1, v2, v3
        final String[] alternativeNames = Utils.fieldNames(originalFields.length);

        // AUTHOR.ID as v1, BOOK.ID as v2, BOOK.TITLE as v3
        // Enforce x.* or just * if we have no known field names (e.g. when plain SQL tables are involved)
        final Field<?>[] alternativeFields = Utils.combine(
            alternativeNames.length == 0
                ? new Field[] { DSL.field("*") }
                : Utils.aliasedFields(originalFields, alternativeNames),

            null
        );

        alternativeFields[alternativeFields.length - 1] =
            new CustomField<Integer>("rn", SQLDataType.INTEGER) {
                @Override
                public void accept(Context<?> c) {

                    // [#3575] Ensure that no column aliases from the surrounding SELECT clause
                    // are referenced from the below ranking functions' ORDER BY clause.
                    c.data(DATA_UNALIAS_ALIASES_IN_ORDER_BY, true);

                    // [#2580] When DISTINCT is applied, we mustn't use ROW_NUMBER() OVER(),
                    // which changes the DISTINCT semantics. Instead, use DENSE_RANK() OVER(),
                    // ordering by the SELECT's ORDER BY clause AND all the expressions from
                    // the projection
                    c.visit(distinct
                        ? DSL.denseRank().over(orderBy(getNonEmptyOrderByForDistinct(c.configuration())))
                        : DSL.rowNumber().over(orderBy(getNonEmptyOrderBy(c.configuration())))
                    );

                    c.data().remove(DATA_UNALIAS_ALIASES_IN_ORDER_BY);
                }
            }.as("rn");

        // v1 as ID, v2 as ID, v3 as TITLE
        final Field<?>[] unaliasedFields = Utils.aliasedFields(Utils.fields(originalFields.length), originalNames);

        boolean subquery = ctx.subquery();

        ctx.keyword("select").sql(" ")
           .declareFields(true)
           .visit(new SelectFieldList(unaliasedFields))
           .declareFields(false)
           .formatSeparator()
           .keyword("from").sql(" (")
           .formatIndentStart()
           .formatNewLine()
           .subquery(true);

        toSQLReference0(ctx, originalFields, alternativeFields);

        ctx.subquery(subquery)
           .formatIndentEnd()
           .formatNewLine()
           .sql(") ")
           .visit(name("x"))
           .formatSeparator()
           .keyword("where").sql(" ")
           .visit(name("rn"))
           .sql(" > ")
           .visit(getLimit().getLowerRownum())
           .formatSeparator()
           .keyword("and").sql(" ")
           .visit(name("rn"))
           .sql(" <= ")
           .visit(getLimit().getUpperRownum());
    }

    /**
     * Simulate the LIMIT / OFFSET clause in the {@link SQLDialect#ORACLE}
     * dialect
     */
    private final void toSQLReferenceLimitOracle(Context<?> ctx) {

        // AUTHOR.ID, BOOK.ID, BOOK.TITLE
        Field<?>[] originalFields = Utils.fieldArray(getSelect());

        // ID, ID, TITLE
        String[] originalNames = Utils.fieldNames(originalFields);

        // v1, v2, v3
        String[] alternativeNames = Utils.fieldNames(originalFields.length);

        // AUTHOR.ID as v1, BOOK.ID as v2, BOOK.TITLE as v3
        Field<?>[] alternativeFields = Utils.aliasedFields(originalFields, alternativeNames);

        // x.v1, x.v2, x.v3, rownum rn
        Field<?>[] qualifiedAlternativeFields = Utils.combine(

            // Enforce x.* or just * if we have no known field names (e.g. when plain SQL tables are involved)
            alternativeNames.length == 0
                ? new Field[] { DSL.field("{0}.*", name("x")) }
                : Utils.fieldsByName("x", alternativeNames),
            DSL.rownum().as("rn")
        );

        // v1 as ID, v2 as ID, v3 as TITLE
        Field<?>[] unaliasedFields = Utils.aliasedFields(Utils.fields(originalFields.length), originalNames);

        ctx.keyword("select").sql(" ")
           .declareFields(true)
           .visit(new SelectFieldList(unaliasedFields))
           .declareFields(false)
           .formatSeparator()
           .keyword("from").sql(" (")
           .formatIndentStart()
           .formatNewLine()
             .keyword("select").sql(" ")
             .declareFields(true)
             .visit(new SelectFieldList(qualifiedAlternativeFields))
             .declareFields(false)
             .formatSeparator()
             .keyword("from").sql(" (")
             .formatIndentStart()
             .formatNewLine();

        toSQLReference0(ctx, originalFields, alternativeFields);

        ctx  .formatIndentEnd()
             .formatNewLine()
             .sql(") ")
             .visit(name("x"))
             .formatSeparator()
             .keyword("where").sql(" ").visit(DSL.rownum()).sql(" <= ")
             .visit(getLimit().getUpperRownum())
           .formatIndentEnd()
           .formatNewLine()
           .sql(") ")
           .formatSeparator()
           .keyword("where").sql(" ")
           .visit(name("rn"))
           .sql(" > ")
           .visit(getLimit().getLowerRownum());
    }
    /* [/pro] */

    /**
     * This method renders the main part of a query without the LIMIT clause.
     * This part is common to any type of limited query
     */
    private final void toSQLReference0(Context<?> context) {
        toSQLReference0(context, null, null);
    }

    /**
     * This method renders the main part of a query without the LIMIT clause.
     * This part is common to any type of limited query
     */
    private final void toSQLReference0(Context<?> context, Field<?>[] originalFields, Field<?>[] alternativeFields) {
        SQLDialect dialect = context.dialect();
        SQLDialect family = dialect.family();

        /* [pro] */

        // Informix doesn't allow SKIP .. FIRST in correlated subqueries, but we can
        // transform the subquery into a derived table, where SKIP .. FIRST are permitted.
        boolean wrapInDerivedTable = family == INFORMIX && context.subquery() && (getLimit().isApplicable() || !getOrderBy().isEmpty());

        if (wrapInDerivedTable)
            context.keyword("select").sql(" *")
                   .formatSeparator()
                   .keyword("from").sql(" (")
                   .formatIndentStart()
                   .formatNewLine();

        /* [/pro] */

        // SELECT clause
        // -------------
        context.start(SELECT_SELECT)
               .keyword("select")
               .sql(" ");

        // [#1493] Oracle hints come directly after the SELECT keyword
        if (!StringUtils.isBlank(hint)) {
            context.sql(hint).sql(" ");
        }

        /* [pro] */
        // Informix requires SKIP .. FIRST .. to be placed before DISTINCT
        if (family == INFORMIX && getLimit().isApplicable()) {
            context.visit(getLimit()).sql(" ");
        }
        /* [/pro] */

        if (!distinctOn.isEmpty()) {
            context.keyword("distinct on").sql(" (").visit(distinctOn).sql(") ");
        }
        else if (distinct) {
            context.keyword("distinct").sql(" ");
        }

        /* [pro] */
        // Sybase and SQL Server have leading TOP clauses
        switch (family) {
            case ACCESS:
            case ASE:
            case SQLSERVER: {

                // If we have a TOP clause, it needs to be rendered here
                if (asList(ACCESS, ACCESS2013, ASE, SQLSERVER2008).contains(dialect)
                        && getLimit().isApplicable()
                        && getLimit().offsetZero()
                        && !getLimit().rendersParams()) {

                    context.visit(getLimit()).sql(" ");
                }

                // [#759] SQL Server needs a TOP clause in ordered subqueries
                else if (family == SQLSERVER
                        && context.subquery()
                        && !getOrderBy().isEmpty()) {

                    // [#2423] SQL Server 2012 will render an OFFSET .. FETCH
                    // clause if there is an applicable limit
                    if (dialect == SQLSERVER2008 || !getLimit().isApplicable()) {
                        context.keyword("top").sql(" 100 ").keyword("percent").sql(" ");
                    }
                }

                break;
            }

            case SYBASE: {
                if (getLimit().isApplicable() && !getLimit().rendersParams()) {
                    context.visit(getLimit()).sql(" ");
                }

                break;
            }

            // [#780] Ordered subqueries should be handled for Ingres and ASE as well
            case INGRES: {
            }
        }
        /* [/pro] */

        context.declareFields(true);

        // [#2335] When emulating LIMIT .. OFFSET, the SELECT clause needs to generate
        // non-ambiguous column names as ambiguous column names are not allowed in subqueries
        if (alternativeFields != null) {
            context.visit(new SelectFieldList(alternativeFields));
        }

        // [#1905] H2 only knows arrays, no row value expressions. Subqueries
        // in the context of a row value expression predicate have to render
        // arrays explicitly, as the subquery doesn't form an implicit RVE
        else if (context.subquery() && dialect == H2 && context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY) != null) {
            Object data = context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY);

            try {
                context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, null);
                context.sql("(")
                       .visit(getSelect1())
                       .sql(")");
            }
            finally {
                context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, data);
            }
        }

        // The default behaviour
        else {
            context.visit(getSelect1());
        }


        context.declareFields(false)
               .end(SELECT_SELECT);

        // INTO clauses
        // ------------
        if (!asList(INFORMIX).contains(family)) {
            context.start(SELECT_INTO);

            Table<?> actualInto = (Table<?>) context.data(DATA_SELECT_INTO_TABLE);
            if (actualInto == null)
                actualInto = into;

            if (actualInto != null
                    && context.data(DATA_OMIT_INTO_CLAUSE) == null
                    && asList(ACCESS, ASE, HSQLDB, POSTGRES, SQLSERVER, SYBASE).contains(family)) {

                context.formatSeparator()
                       .keyword("into")
                       .sql(" ")
                       .visit(actualInto);
            }

            context.end(SELECT_INTO);
        }

        // FROM and JOIN clauses
        // ---------------------
        context.start(SELECT_FROM)
               .declareTables(true);

        // The simplest way to see if no FROM clause needs to be rendered is to
        // render it. But use a new RenderContext (without any VisitListeners)
        // for that purpose!
        boolean hasFrom = false
            /* [pro] */
            || (context.data(DATA_RENDERING_DB2_FINAL_TABLE_CLAUSE) != null)
            /* [/pro] */
        ;

        if (!hasFrom) {
            DefaultConfiguration c = new DefaultConfiguration(dialect);
            String renderedFrom = new DefaultRenderContext(c).render(getFrom());
            hasFrom = !renderedFrom.isEmpty();
        }

        if (hasFrom) {
            context.formatSeparator()
                   .keyword("from")
                   .sql(" ")
                   .visit(getFrom());

            /* [pro] */
            // [#1665] [#1681] Sybase ASE and Ingres need a cross-joined dummy table
            // To be able to GROUP BY () empty sets
            if (grouping && getGroupBy().isEmpty())
                if (asList(ASE, INGRES).contains(dialect))
                    context.sql(", (select 1 as dual) as empty_grouping_dummy_table");
                else if (family == INFORMIX)
                    context.sql(", (").sql(DUAL_INFORMIX).sql(") as empty_grouping_dummy_table");
                else if (asList(ACCESS).contains(dialect))
                    context.sql(", (").sql(DUAL_ACCESS).sql(") as empty_grouping_dummy_table");
            /* [/pro] */
        }

        context.declareTables(false)
               .end(SELECT_FROM);

        // WHERE clause
        // ------------
        context.start(SELECT_WHERE);

        if (!(getWhere().getWhere() instanceof TrueCondition)) {
            context.formatSeparator()
                   .keyword("where")
                   .sql(" ")
                   .visit(getWhere());
        }

        context.end(SELECT_WHERE);

        // CONNECT BY clause
        // -----------------

        // CUBRID supports this clause only as [ START WITH .. ] CONNECT BY
        // Oracle also knows the CONNECT BY .. [ START WITH ] alternative
        // syntax
        context.start(SELECT_START_WITH);

        if (!(getConnectByStartWith().getWhere() instanceof TrueCondition)) {
            context.formatSeparator()
                   .keyword("start with")
                   .sql(" ")
                   .visit(getConnectByStartWith());
        }

        context.end(SELECT_START_WITH);
        context.start(SELECT_CONNECT_BY);

        if (!(getConnectBy().getWhere() instanceof TrueCondition)) {
            context.formatSeparator()
                   .keyword("connect by");

            if (connectByNoCycle) {
                context.sql(" ").keyword("nocycle");
            }

            context.sql(" ").visit(getConnectBy());
        }

        context.end(SELECT_CONNECT_BY);

        // GROUP BY and HAVING clause
        // --------------------------
        context.start(SELECT_GROUP_BY);

        if (grouping) {
            context.formatSeparator()
                   .keyword("group by")
                   .sql(" ");

            // [#1665] Empty GROUP BY () clauses need parentheses
            if (getGroupBy().isEmpty()) {

                // [#1681] Use the constant field from the dummy table Sybase ASE, Ingres
                if (asList(ACCESS, ASE, INFORMIX, INGRES).contains(dialect)) {
                    context.sql("empty_grouping_dummy_table.dual");
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
                context.visit(getGroupBy());
            }
        }

        context.end(SELECT_GROUP_BY);

        // HAVING clause
        // -------------
        context.start(SELECT_HAVING);

        if (!(getHaving().getWhere() instanceof TrueCondition)) {
            context.formatSeparator()
                   .keyword("having")
                   .sql(" ")
                   .visit(getHaving());
        }

        context.end(SELECT_HAVING);

        // WINDOW clause
        // -------------
        context.start(SELECT_WINDOW);

        if (!getWindow().isEmpty() && asList(POSTGRES, SYBASE).contains(family)) {
            context.formatSeparator()
                   .keyword("window")
                   .sql(" ")
                   .declareWindows(true)
                   .visit(getWindow())
                   .declareWindows(false);
        }

        context.end(SELECT_WINDOW);

        // ORDER BY clause
        // ---------------
        context.start(SELECT_ORDER_BY);

        if (!getOrderBy().isEmpty()) {
            context.formatSeparator()
                   .keyword("order")
                   .sql(orderBySiblings ? " " : "")
                   .keyword(orderBySiblings ? "siblings" : "")
                   .sql(" ")
                   .keyword("by")
                   .sql(" ");

            /* [pro] */

            // [#2080] DB2, Oracle, and Sybase can deal with column aliases from the SELECT clause
            // but in case the aliases have been overridden to emulate OFFSET pagination, the
            // overrides must also apply to the ORDER BY clause
            if (originalFields != null) {
                context.data(DATA_OVERRIDE_ALIASES_IN_ORDER_BY, new Object[] { originalFields, alternativeFields });
                context.visit(getOrderBy());
                context.data().remove(DATA_OVERRIDE_ALIASES_IN_ORDER_BY);
            }
            else
            /* [/pro] */
            {
                context.visit(getOrderBy());
            }
        }

        /* [pro] */
        // [#2423] SQL Server 2012 requires an ORDER BY clause, along with
        // OFFSET .. FETCH
        else if (getLimit().isApplicable() && asList(SQLSERVER, SQLSERVER2012).contains(dialect)) {
            context.formatSeparator()
                   .keyword("order by")
                   .sql(" (")
                   .keyword("select")
                   .sql(" 0)");
        }
        /* [/pro] */

        context.end(SELECT_ORDER_BY);

        /* [pro] */
        if (wrapInDerivedTable)
            context.formatIndentEnd()
                   .formatNewLine()
                   .sql(")");
        /* [/pro] */
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
    public final void addDistinctOn(Field<?>... fields) {
        addDistinctOn(Arrays.asList(fields));
    }

    @Override
    public final void addDistinctOn(Collection<? extends Field<?>> fields) {
        this.distinctOn.addAll(fields);
    }

    @Override
    public final void setInto(Table<?> into) {
        this.into = into;
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

    /* [pro] */
    @Override
    public final void setForUpdateWait(int seconds) {
        setForUpdate(true);
        forUpdateMode = ForUpdateMode.WAIT;
        forUpdateWait = seconds;
    }
    /* [/pro] */

    @Override
    public final void setForUpdateNoWait() {
        setForUpdate(true);
        forUpdateMode = ForUpdateMode.NOWAIT;
        forUpdateWait = 0;
    }

    /* [pro] */
    @Override
    public final void setForUpdateSkipLocked() {
        setForUpdate(true);
        forUpdateMode = ForUpdateMode.SKIP_LOCKED;
        forUpdateWait = 0;
    }
    /* [/pro] */

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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    final ConditionProviderImpl getWhere() {
        if (getOrderBy().isEmpty() || getSeek().isEmpty()) {
            return condition;
        }
        else {
            SortFieldList o = getOrderBy();
            Condition c = null;

            // [#2786] TODO: Check if NULLS FIRST | NULLS LAST clauses are
            // contained in the SortFieldList, in case of which, the below
            // predicates will become a lot more complicated.
            if (o.nulls()) {}

            // If we have uniform sorting, more efficient row value expression
            // predicates can be applied, which can be heavily optimised on some
            // databases.
            if (o.size() > 1 && o.uniform()) {
                if (o.get(0).getOrder() == ASC ^ seekBefore) {
                    c = row(o.fields()).gt(row(getSeek()));
                }
                else {
                    c = row(o.fields()).lt(row(getSeek()));
                }
            }

            // With alternating sorting, the SEEK clause has to be explicitly
            // phrased for each ORDER BY field.
            else {
                ConditionProviderImpl or = new ConditionProviderImpl();

                for (int i = 0; i < o.size(); i++) {
                    ConditionProviderImpl and = new ConditionProviderImpl();

                    for (int j = 0; j < i; j++) {
                        SortFieldImpl<?> s = (SortFieldImpl<?>) o.get(j);
                        and.addConditions(((Field) s.getField()).eq(getSeek().get(j)));
                    }

                    SortFieldImpl<?> s = (SortFieldImpl<?>) o.get(i);
                    if (s.getOrder() == ASC ^ seekBefore) {
                        and.addConditions(((Field) s.getField()).gt(getSeek().get(i)));
                    }
                    else {
                        and.addConditions(((Field) s.getField()).lt(getSeek().get(i)));
                    }

                    or.addConditions(OR, and);
                }

                c = or;
            }

            ConditionProviderImpl result = new ConditionProviderImpl();
            result.addConditions(condition, c);
            return result;
        }
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

    final QueryPartList<WindowDefinition> getWindow() {
        return window;
    }

    final SortFieldList getOrderBy() {
        return orderBy;
    }

    final QueryPartList<Field<?>> getSeek() {
        return seek;
    }

    /* [pro] */
    final SortFieldList getNonEmptyOrderBy(Configuration configuration) {
        if (getOrderBy().isEmpty()) {
            SortFieldList result = new SortFieldList();

            switch (configuration.dialect().family()) {
                case DB2:
                    result.add(DSL.one().asc());
                    break;

                case SYBASE:
                    result.add(DSL.field("@@version").asc());
                    break;

                case SQLSERVER:
                default:
                    result.add(DSL.field("({select} 0)").asc());
                    break;
            }
            return result;
        }

        return getOrderBy();
    }

    final SortFieldList getNonEmptyOrderByForDistinct(Configuration configuration) {
        SortFieldList order = new SortFieldList();
        order.addAll(getNonEmptyOrderBy(configuration));

        for (Field<?> field : getSelect())
            order.add(field.asc());

        return order;
    }
    /* [/pro] */

    @Override
    public final void addOrderBy(Collection<? extends SortField<?>> fields) {
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
    public final void addSeekAfter(Field<?>... fields) {
        addSeekAfter(Arrays.asList(fields));
    }

    @Override
    public final void addSeekAfter(Collection<? extends Field<?>> fields) {
        seekBefore = false;
        getSeek().addAll(fields);
    }

    @Override
    public final void addSeekBefore(Field<?>... fields) {
        addSeekBefore(Arrays.asList(fields));
    }

    @Override
    public final void addSeekBefore(Collection<? extends Field<?>> fields) {
        seekBefore = true;
        getSeek().addAll(fields);
    }

    @Override
    public final void addConditions(Condition... conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Collection<? extends Condition> conditions) {
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
    public final void addHaving(Collection<? extends Condition> conditions) {
        getHaving().addConditions(conditions);
    }

    @Override
    public final void addHaving(Operator operator, Condition... conditions) {
        getHaving().addConditions(operator, conditions);
    }

    @Override
    public final void addHaving(Operator operator, Collection<? extends Condition> conditions) {
        getHaving().addConditions(operator, conditions);
    }

    @Override
    public final void addWindow(WindowDefinition... definitions) {
        addWindow(Arrays.asList(definitions));
    }

    @Override
    public final void addWindow(Collection<? extends WindowDefinition> definitions) {
        getWindow().addAll(definitions);
    }

    @Override
    public final void addJoin(TableLike<?> table, Condition... conditions) {
        addJoin(table, JoinType.JOIN, conditions);
    }

    @Override
    public final void addJoin(TableLike<?> table, JoinType type, Condition... conditions) {
        addJoin0(table, type, conditions, null);
    }

    /* [pro] */
    @Override
    public final void addJoin(TableLike<?> table, JoinType type, Condition[] conditions, Field<?>[] partitionBy) {
        addJoin0(table, type, conditions, partitionBy);
    }
    /* [/pro] */

    private final void addJoin0(TableLike<?> table, JoinType type, Condition[] conditions, Field<?>[] partitionBy) {

        // TODO: This and similar methods should be refactored, patterns extracted...
        int index = getFrom().size() - 1;
        Table<?> joined = null;

        switch (type) {
            case JOIN:
                joined = getFrom().get(index).join(table).on(conditions);
                break;
            case LEFT_OUTER_JOIN: {
                TablePartitionByStep p = getFrom().get(index).leftOuterJoin(table);
                TableOnStep o = p;
                /* [pro] */
                o = p.partitionBy(partitionBy);
                /* [/pro] */
                joined = o.on(conditions);
                break;
            }
            case RIGHT_OUTER_JOIN: {
                TablePartitionByStep p = getFrom().get(index).rightOuterJoin(table);
                TableOnStep o = p;
                /* [pro] */
                o = p.partitionBy(partitionBy);
                /* [/pro] */
                joined = o.on(conditions);
                break;
            }
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

            /* [pro] */
            case CROSS_APPLY:
                joined = getFrom().get(index).crossApply(table);
                break;
            case OUTER_APPLY:
                joined = getFrom().get(index).outerApply(table);
                break;
            /* [/pro] */
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

            default:
                throw new IllegalArgumentException("JoinType " + type + " is not supported with the addJoinOnKey() method. Use INNER or OUTER JOINs only");
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

            default:
                throw new IllegalArgumentException("JoinType " + type + " is not supported with the addJoinOnKey() method. Use INNER or OUTER JOINs only");
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

            default:
                throw new IllegalArgumentException("JoinType " + type + " is not supported with the addJoinOnKey() method. Use INNER or OUTER JOINs only");
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

            default:
                throw new IllegalArgumentException("JoinType " + type + " is not supported with the addJoinUsing() method. Use INNER or OUTER JOINs only");
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
