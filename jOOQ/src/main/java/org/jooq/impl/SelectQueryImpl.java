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
import static org.jooq.Clause.SELECT_ORDER_BY;
import static org.jooq.Clause.SELECT_SELECT;
import static org.jooq.Clause.SELECT_START_WITH;
import static org.jooq.Clause.SELECT_WHERE;
import static org.jooq.Clause.SELECT_WINDOW;
import static org.jooq.Operator.OR;
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SortOrder.ASC;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.denseRank;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.Dual.DUAL_ACCESS;
import static org.jooq.impl.Utils.DATA_LOCALLY_SCOPED_DATA_MAP;
import static org.jooq.impl.Utils.DATA_RENDERING_DB2_FINAL_TABLE_CLAUSE;
import static org.jooq.impl.Utils.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY;
import static org.jooq.impl.Utils.DATA_WINDOW_DEFINITIONS;
import static org.jooq.impl.Utils.DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jooq.BindContext;
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
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.SelectQuery;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnStep;
import org.jooq.TablePartitionByStep;
import org.jooq.WindowDefinition;
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
    private static final Clause[]           CLAUSES          = { SELECT };

    private final WithImpl                  with;
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
    public final void bind(BindContext context) {
        if (with != null)
            context.visit(with);

        pushWindow(context);

        context.declareFields(true)
               .visit(getSelect0())
               .declareFields(false)
               .declareTables(true)
               .visit(getFrom())
               .declareTables(false)
               .visit(getWhere())
               .visit(getConnectByStartWith())
               .visit(getConnectBy())
               .visit(getGroupBy())
               .visit(getHaving());

        if (asList(POSTGRES).contains(context.configuration().dialect().family())) {
            context.declareWindows(true)
                   .visit(getWindow())
                   .declareWindows(false);
        }

        context.visit(getOrderBy());

        // TOP clauses never bind values. So this can be safely applied at the
        // end for LIMIT .. OFFSET clauses, or ROW_NUMBER() filtering
        if (getLimit().isApplicable()) {
            context.visit(getLimit());
        }

        context.visit(forUpdateOf)
               .visit(forUpdateOfTables);
    }

    @Override
    public final void toSQL(RenderContext context) {
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
            switch (context.configuration().dialect()) {

                /* [pro] xx
                xx xxxxxx xxxxx xxx xxxxxx xxxxxxxxxxxxxx xxxx xxxxx xxxxxx xxxxxx
                xxxx xxxxxxx
                xxxx xxxxxxxxxx
                xxxx xxxxxxxxxx
                xxxx xxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxx

                xx xxxx xxxx xxxxx xxx xxx xxxxxxxxxxxxx
                xxxx xxxx
                xxxx xxxxxx
                xxxx xxxxxxx x

                    xx xxx xxxxxxxx xxxxxxxx x xxxxxx xxxxx xxxxxxx xxxxxxx
                    xx xxxxxx xxx xxxxxxx xxxx xxxxxx
                    xx xxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    x

                    xx xxxxxxxx xxx xx xx xxxxxxxxx
                    xxxx x
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    x

                    xxxxxx
                x

                xx xxxxxx xxx xxx xxx xxxxxx xxxxxxx x xxx xxxxxx xxxxxxx xxxxxx
                xx xxxxxx xxx xx xxxxxxxxx xx xxx xxxxxxx xxx xx xxx
                xxxx xxxxxxx
                xxxx xxxxxxxxxxx
                xxxx xxxx
                xxxx xxxxxxxxxxxxxx x

                    xx xxxxxx xxx xxxxxxxx xxxxxxx xxxxxx xxx xxxxxxx xxxx xxxxxx
                    xx xxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                        xxxxxxxxxxxxxxxxxxxxxxxxx
                    x

                    xx xxxxxx xxxxxxxxxx
                    xxxx x
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    x

                    xxxxxx
                x

                xx xxxxxx xxx xxx xx xxxxx xx xxxxxxx xxx xxxx xxxxxxx
                xx xxxxxxxx xxx xxxxx xx xxxx xxxxxxx xxx xxxx xxxxxxx
                xxxx xxxxxxx x

                    xx xxxxxx xxx xxxxxxxx xxxxxxx xxxxxx xxx xxxxxxx xxxx xxxxxx
                    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                        xxxxxxxxxxxxxxxxxxxxxxxxx
                    x

                    xx xxxxxx xxxxxxxxxx
                    xxxx x
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    x

                    xxxxxx
                x

                xx [/pro] */
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
        if (forUpdate && !asList(CUBRID).contains(context.configuration().dialect().family())) {
            context.formatSeparator()
                   .keyword("for update");

            if (!forUpdateOf.isEmpty()) {
                context.sql(" ").keyword("of").sql(" ");
                Utils.fieldNames(context, forUpdateOf);
            }
            else if (!forUpdateOfTables.isEmpty()) {
                context.sql(" ").keyword("of").sql(" ");

                switch (context.configuration().dialect().family()) {

                    // Some dialects don't allow for an OF [table-names] clause
                    // It can be simulated by listing the table's fields, though
                    /* [pro] xx
                    xxxx xxxx
                    xxxx xxxxxxx
                    xxxx xxxxxxx
                    xx [/pro] */
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
            if (context.configuration().dialect().family() == FIREBIRD) {
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

        if (TRUE.equals(wrapDerivedTables)) {
            context.sql(")")
                   .data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, true);
        }
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
    private void toSQLReferenceLimitDefault(RenderContext context) {
        toSQLReference0(context);
        context.visit(getLimit());
    }

    /* [pro] xx
    xxx
     x xxxxxxxx xxx xxxxx x xxxxxx xxxxxx xx xxx xxxxxx xxxxxxxxxxxxxxxx
     x xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxx
     xx
    xxxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx x

        xx xxxxxxx xxxxxx xxxxxxxx xxxxxx xxxxx xx xxxxxx x xxxxxxxx xxxx xxxx
        xxxxxxxxxxxxx xxxxxxxx x xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxxxxxxxx x xxxxxxxxxxxxxxxxxx

        xxxxxx xxxxxxxxxxxx x xxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxxxxxxx x xxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxx

        xx xxxxxx xxxxxxxx xxxxxx xxxxxx xxxxxx xx xxxxxxxxxx xxxxxxxxxxxx xxxxxx
        xx xxxxxx xxxxxxxxx xxxxxxxxxxx xxx xxxxxxx xxx xxx xxxxx xx xxxxxx xxxxxx
        xxxxxxxxxxxxx xxxxx x xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxx

        xx xxxxxxx xxxx xxxxxxxx xx xxxxxxxx xx xxxxxxx xxx xxxxxxxxxxxx xxxxxxx
        xx xxxxx xxxxxxx xxx xxxxxxxx xxxxxxxxxx xxxxxxxx xxx xxxxxxxxxxxx xxxxxxx
        xx xxxxxxxx xx xxx xxxxxxxx xxxxx xx xxxxxx xxx xxx xxx xxxxxxxxxxx xxxx
        xx xxx xxxxxxxxxx
        xx xxxxxxxxxx x
            xxxxxxxxxxxxxxxxxx xxxxx x xxx xxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

            xx xxxxx xxxxxxxxx xxxx xxxx xxxx xx xxxxxxxxxx xxxxxx xxxxxxx xxxxxxxxxxx
            xxx xxxxxxxxx xxxxx x xxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x
        xxxx x
            xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x
        xxxxxx xxxxxxxx x xxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxxxxx x xxxx xxx
               xxxxxxxxxxxxxxxxxxxx
               xxxxxxxxxxxxxxxx
               xxxxxxxxxxxxxx
               xxxxxxxxxxxxxxxxxx
               xxxxxxxxxxxxxxxx
               xxxxxxx xxxxxxxxxxxxxxxxxxxxxx xx
               xxxxxxxxxxxxxxxxxxxxxxxxxx
               xxxxxxxxxxxxxxxxxx
               xxxxxxxxxxxxxxxxxxxxxxx xx
               xxxxxxxxxxxxxxxxxxxxxxxx
               xxxxxx x xx
               xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               xxxxxxxxxxxxxxxxxx
               xxxxxxxxxxxxxxxxxxxxx xx
               xxxxxxxxxxxxxxxxxxxxxxxx
               xxxxxx xx xx
               xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xxx
     x xxxxxxxx xxx xxxxx x xxxxxx xxxxxx xx xxx xxxxxx xxxxxxxxxxxxxxxxxx
     x xxxxxxx
     xx
    xxxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx x
        xxxxxxxxxxxxx xxxxx x xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxxxxx x xxxxxxxxxxxxxxx

        xxxxxx xxxxxxxxxxxx x xxxxxxxx x xxxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxxxxxxx x xxxxxxxxx x xxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxx xxx
               xxxxxxxxxxxxxxxxxxxx
               xxxxxxxxxxxxxxxx
                 xxxxxxxxxxxxxxxxxxxxxxxx xx
                 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx
                 xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx xx
                 xxxxxxxxxxxxxxxxxxxxxxxx
                 xxxxxxxxxxxxxxxxxx
                 xxxxxxxxxxxxxxxxxxxxxx xxx
                 xxxxxxxxxxxxxxxxxxxx
                 xxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxx
                 xxxxxxxxxxxxxxxxxx
                 xxxxxxxxxxxxxxxx
                 xxxxxxx xx
                 xxxxxxxxxxxxxxxxxxxxxxxxxx
                 xxxxxxxxxxxxxxxxxx
                 xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxx xx xx
                 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               xxxxxxxxxxxxxxxxxx
               xxxxxxxxxxxxxxxx
               xxxxxxx xx
               xxxxxxxxxxxxxxxxxx
               xxxxxxxxxxxxxxxxxxxxxxx xx
               xxxxxxxxxxxxxxxxxxxxxxxx
               xxxxxx x xx
               xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    x
    xx [/pro] */

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
        context.start(SELECT_SELECT)
               .keyword("select")
               .sql(" ");

        // [#1493] Oracle hints come directly after the SELECT keyword
        if (!StringUtils.isBlank(hint)) {
            context.sql(hint).sql(" ");
        }

        if (distinct) {
            context.keyword("distinct").sql(" ");
        }

        /* [pro] xx
        xx xxxxxx xxx xxx xxxxxx xxxx xxxxxxx xxx xxxxxxx
        xxxxxx xxxxxxxxxxxxxxxxxx x
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxxxxxxxx x

                xx xx xx xxxx x xxx xxxxxxx xx xxxxx xx xx xxxxxxxx xxxx
                xx xxxxxxxxxxxxxxx xxxxxxxxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xx xxxxxxxxxxxxxxxxxxxxxxxxx
                        xx xxxxxxxxxxxxxxxxxxxxxxx
                        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxx x

                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx
                x

                xx xxxxxx xxx xxxxxx xxxxx x xxx xxxxxx xx xxxxxxx xxxxxxxxxx
                xxxx xx xxxxxxxxxxxxxxxxx xx xxxxxxxxx
                        xx xxxxxxxxxxxxxxxxxx
                        xx xxxxxxxxxxxxxxxxxxxxxxxx x

                    xx xxxxxxx xxx xxxxxx xxxx xxxx xxxxxx xx xxxxxx xx xxxxx
                    xx xxxxxx xx xxxxx xx xx xxxxxxxxxx xxxxx
                    xx xxxxxxxx xx xxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxx x
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxx xxx
                    x
                x

                xxxxxx
            x

            xxxx xxxxxxx x
                xx xxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx
                x

                xxxxxx
            x

            xx xxxxxx xxxxxxx xxxxxxxxxx xxxxxx xx xxxxxxx xxx xxxxxx xxx xxx xx xxxx
            xxxx xxxxxxx x
            x
        x
        xx [/pro] */

        context.declareFields(true);

        // [#1905] H2 only knows arrays, no row value expressions. Subqueries
        // in the context of a row value expression predicate have to render
        // arrays explicitly, as the subquery doesn't form an implicit RVE
        if (context.subquery() && dialect == H2 && context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY) != null) {
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
                   .visit(limitOffsetRownumber)
                   .formatIndentEnd()
                   .paramType(paramType);
        }

        context.declareFields(false)
               .end(SELECT_SELECT);

        // FROM and JOIN clauses
        // ---------------------
        context.start(SELECT_FROM)
               .declareTables(true);

        // The simplest way to see if no FROM clause needs to be rendered is to
        // render it. But use a new RenderContext (without any VisitListeners)
        // for that purpose!
        boolean hasFrom = (context.data(DATA_RENDERING_DB2_FINAL_TABLE_CLAUSE) != null);

        if (!hasFrom) {
            DefaultConfiguration c = new DefaultConfiguration(context.configuration().dialect());
            String renderedFrom = new DefaultRenderContext(c).render(getFrom());
            hasFrom = !renderedFrom.isEmpty();
        }

        if (hasFrom) {
            context.formatSeparator()
                   .keyword("from")
                   .sql(" ")
                   .visit(getFrom());

            /* [pro] xx
            xx xxxxxxx xxxxxx xxx xxx xxxxxx xxxx x xxxxxxxxxxxx xxxxx xxxxx
            xx xx xx xxxx xx xxxxx xx xx xxxxx xxxx
            xx xxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxx x
                xx xxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxxxxxxxxxx xxxxxxx x xx xx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                x
                xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                x
            x
            xx [/pro] */
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
                if (asList().contains(dialect)) {
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

        if (!getWindow().isEmpty() && asList(POSTGRES).contains(dialect.family())) {
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
                   .sql(" ")
                   .visit(getOrderBy());
        }

        // [#2423] SQL Server 2012 requires an ORDER BY clause, along with
        // OFFSET .. FETCH
        else if (getLimit().isApplicable() && asList().contains(dialect)){
            context.formatSeparator()
                   .keyword("order by")
                   .sql(" 1");
        }

        context.end(SELECT_ORDER_BY);
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

    /* [pro] xx
    xxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxx x
        xxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx x xxxxxxxx
    x
    xx [/pro] */

    @Override
    public final void setForUpdateNoWait() {
        setForUpdate(true);
        forUpdateMode = ForUpdateMode.NOWAIT;
        forUpdateWait = 0;
    }

    /* [pro] xx
    xxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxx x
        xxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx x xx
    x
    xx [/pro] */

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

    final SortFieldList getNonEmptyOrderBy() {
        if (getOrderBy().isEmpty()) {
            SortFieldList result = new SortFieldList();
            result.add(getSelect().get(0).asc());
            return result;
        }

        return getOrderBy();
    }

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

    /* [pro] xx
    xxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxx xxxxx xxxxxxxxxxx xxxxxxxxxxx xxxxxxxxxx xxxxxxxxxxxx x
        xxxxxxxxxxxxxxx xxxxx xxxxxxxxxxx xxxxxxxxxxxxx
    x
    xx [/pro] */

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
                /* [pro] xx
                x x xxxxxxxxxxxxxxxxxxxxxxxxxxx
                xx [/pro] */
                joined = o.on(conditions);
                break;
            }
            case RIGHT_OUTER_JOIN: {
                TablePartitionByStep p = getFrom().get(index).rightOuterJoin(table);
                TableOnStep o = p;
                /* [pro] xx
                x x xxxxxxxxxxxxxxxxxxxxxxxxxxx
                xx [/pro] */
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

            /* [pro] xx
            xxxx xxxxxxxxxxxx
                xxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxx
            xxxx xxxxxxxxxxxx
                xxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxx
            xx [/pro] */
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
