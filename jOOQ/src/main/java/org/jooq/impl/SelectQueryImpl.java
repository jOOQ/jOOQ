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
import static org.jooq.Clause.SELECT_EXCEPT;
import static org.jooq.Clause.SELECT_FROM;
import static org.jooq.Clause.SELECT_GROUP_BY;
import static org.jooq.Clause.SELECT_HAVING;
import static org.jooq.Clause.SELECT_INTERSECT;
import static org.jooq.Clause.SELECT_INTO;
import static org.jooq.Clause.SELECT_ORDER_BY;
import static org.jooq.Clause.SELECT_SELECT;
import static org.jooq.Clause.SELECT_START_WITH;
import static org.jooq.Clause.SELECT_UNION;
import static org.jooq.Clause.SELECT_UNION_ALL;
import static org.jooq.Clause.SELECT_WHERE;
import static org.jooq.Clause.SELECT_WINDOW;
import static org.jooq.Operator.OR;
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SortOrder.ASC;
import static org.jooq.impl.CombineOperator.EXCEPT;
import static org.jooq.impl.CombineOperator.INTERSECT;
import static org.jooq.impl.CombineOperator.UNION;
import static org.jooq.impl.CombineOperator.UNION_ALL;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.orderBy;
import static org.jooq.impl.DSL.row;
// ...
// ...
import static org.jooq.impl.Utils.DATA_LOCALLY_SCOPED_DATA_MAP;
import static org.jooq.impl.Utils.DATA_OMIT_INTO_CLAUSE;
import static org.jooq.impl.Utils.DATA_OVERRIDE_ALIASES_IN_ORDER_BY;
// ...
import static org.jooq.impl.Utils.DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE;
import static org.jooq.impl.Utils.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY;
import static org.jooq.impl.Utils.DATA_SELECT_INTO_TABLE;
import static org.jooq.impl.Utils.DATA_UNALIAS_ALIASES_IN_ORDER_BY;
import static org.jooq.impl.Utils.DATA_WINDOW_DEFINITIONS;
import static org.jooq.impl.Utils.DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES;
import static org.jooq.impl.Utils.fieldArray;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.GroupField;
import org.jooq.JoinType;
import org.jooq.Operator;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Select;
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
class SelectQueryImpl<R extends Record> extends AbstractResultQuery<R> implements SelectQuery<R> {

    /**
     * Generated UID
     */
    private static final long                    serialVersionUID = 1646393178384872967L;
    private static final Clause[]                CLAUSES          = { SELECT };

    private final WithImpl                       with;
    private final SelectFieldList                select;
    private Table<?>                             into;
    private String                               hint;
    private String                               option;
    private boolean                              distinct;
    private final QueryPartList<Field<?>>        distinctOn;
    private boolean                              forUpdate;
    private final QueryPartList<Field<?>>        forUpdateOf;
    private final TableList                      forUpdateOfTables;
    private ForUpdateMode                        forUpdateMode;
    private int                                  forUpdateWait;
    private boolean                              forShare;
    /* [pro] xx
    xxxxxxx xxxxxxx                              xxxxxxxxxxxxxxxx
    xxxxxxx xxxxxxx                              xxxxxxxxxxxxx
    xx [/pro] */
    private final TableList                      from;
    private final ConditionProviderImpl          condition;
    private final ConditionProviderImpl          connectBy;
    private boolean                              connectByNoCycle;
    private final ConditionProviderImpl          connectByStartWith;
    private boolean                              grouping;
    private final QueryPartList<GroupField>      groupBy;
    private final ConditionProviderImpl          having;
    private final WindowList                     window;
    private final SortFieldList                  orderBy;
    private boolean                              orderBySiblings;
    private final QueryPartList<Field<?>>        seek;
    private boolean                              seekBefore;
    private final Limit                          limit;
    private final List<CombineOperator>          unionOp;
    private final List<QueryPartList<Select<?>>> union;
    private final SortFieldList                  unionOrderBy;
    private boolean                              unionOrderBySiblings; // [#3579] TODO
    private final QueryPartList<Field<?>>        unionSeek;
    private boolean                              unionSeekBefore;      // [#3579] TODO
    private final Limit                          unionLimit;

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
        this.unionOp = new ArrayList<CombineOperator>();
        this.union = new ArrayList<QueryPartList<Select<?>>>();
        this.unionOrderBy = new SortFieldList();
        this.unionSeek = new QueryPartList<Field<?>>();
        this.unionLimit = new Limit();

        if (from != null) {
            this.from.add(from.asTable());
        }

        this.forUpdateOf = new QueryPartList<Field<?>>();
        this.forUpdateOfTables = new TableList();
    }

    @Override
    public final int fetchCount() throws DataAccessException {
        return DSL.using(configuration()).fetchCount(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> Field<T> asField() {
        if (getSelect().size() != 1) {
            throw new IllegalStateException("Can only use single-column ResultProviderQuery as a field");
        }

        return new ScalarSubquery<T>(this, (DataType<T>) getSelect().get(0).getDataType());
    }

    @Override
    public final <T> Field<T> asField(String alias) {
        return this.<T> asField().as(alias);
    }

    @Override
    public final Row fieldsRow() {
        return asTable().fieldsRow();
    }

    @Override
    public final <T> Field<T> field(Field<T> field) {
        return asTable().field(field);
    }

    @Override
    public final Field<?> field(String string) {
        return asTable().field(string);
    }

    @Override
    public final Field<?> field(int index) {
        return asTable().field(index);
    }

    @Override
    public final Field<?>[] fields() {
        return asTable().fields();
    }

    @Override
    public final Table<R> asTable() {
        // Its usually better to alias nested selects that are used in
        // the FROM clause of a query
        return new DerivedTable<R>(this).as("alias_" + Utils.hash(this));
    }

    @Override
    public final Table<R> asTable(String alias) {
        return new DerivedTable<R>(this).as(alias);
    }

    @Override
    public final Table<R> asTable(String alias, String... fieldAliases) {
        return new DerivedTable<R>(this).as(alias, fieldAliases);
    }

    @Override
    protected final Field<?>[] getFields(ResultSetMetaData meta) {

        // [#1808] TODO: Restrict this field list, in case a restricting fetch()
        // method was called to get here
        List<Field<?>> fields = getSelect();

        // If no projection was specified explicitly, create fields from result
        // set meta data instead. This is typically the case for SELECT * ...
        if (fields.isEmpty()) {
            Configuration configuration = configuration();
            return new MetaDataFieldProvider(configuration, meta).getFields();
        }

        return fieldArray(fields);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final void accept(Context<?> context) {
        SQLDialect dialect = context.dialect();
        SQLDialect family = context.family();

        // [#2791] TODO: Instead of explicitly manipulating these data() objects, future versions
        // of jOOQ should implement a push / pop semantics to clearly delimit such scope.
        Object renderTrailingLimit = context.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE);
        try {
            if (renderTrailingLimit != null)
                context.data().remove(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE);

            if (into != null
                    && context.data(DATA_OMIT_INTO_CLAUSE) == null
                    && asList(CUBRID, DERBY, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE).contains(family)) {

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
                       .formatIndentStart()
                       .formatNewLine()
                       .data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, null);
            }

            switch (dialect) {

                /* [pro] xx
                xx xxxxxx xxxxx xxx xxxxxx xxxxxxxxxxxxxx xxxx xxxxx xxxxxx xxxxxx
                xxxx xxxxxxx
                xxxx xxxxxxxxxx
                xxxx xxxxxxxxxx
                xxxx xxxxxxxxxx
                    xx xxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxx

                    xxxxxx

                xx xxxx xxxx xxxxx xxx xxx xxxxxxxxxxxxx
                xxxx xxxx
                xxxx xxxxxx
                xxxx xxxxxxx x

                    xx xxx xxxxxxxx xxxxxxxx x xxxxxx xxxxx xxxxxxx xxxxxxx
                    xx xxxxxx xxx xxxxxxx xxxx xxxxxx
                    xx xxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                    xx xxxxxxxx xxx xx xx xxxxxxxxx
                    xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                    xxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxx

                    xxxxxx
                x

                xx xxxxxx xxx xxx xxx xxxxxx xxxxxxx x xxx xxxxxx xxxxxxx xxxxxx
                xx xxxxxx xxx xx xxxxxxxxx xx xxx xxxxxxx xxx xx xxx
                xxxx xxxxxxx
                xxxx xxxxxxxxxxx
                xxxx xxxx
                xxxx xxxxxxxxxxxxxx x

                    xx xxxxxx xxx xxxxxxxx xxxxxxx xxxxxx xxx xxxxxxx xxxx xxxxxx
                    xx xxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxx

                    xx xxxxxx xxxxxxxxxx
                    xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                    xxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxx

                    xxxxxx
                x

                xx xxxxxxxx xxx xxxx xx xxxxx xxxxxxx
                xxxx xxxxxxxxx

                xx xxxxxx xxx xxx xx xxxxx xx xxxxxxx xxx xxxx xxxxxxx
                xxxx xxxxxxx x

                    xx xxxxxx xxx xxxxxxxx xxxxxxx xxxxxx xxx xxxxxxx xxxx xxxxxx
                    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxx xx xxxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxx

                    xx xxxxxx xxxxxxxxxx
                    xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                    xxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxx

                    xxxxxx
                x

                xx [/pro] */
                // By default, render the dialect's limit clause
                default: {
                    toSQLReferenceLimitDefault(context);

                    break;
                }
            }

            // [#1296] FOR UPDATE is simulated in some dialects using ResultSet.CONCUR_UPDATABLE
            if (forUpdate && !asList(CUBRID).contains(family)) {
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
                        /* [pro] xx
                        xxxx xxxx
                        xxxx xxxxxxxxx
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

            /* [pro] xx
            xx xxxxxxx xxx xxxxxx x xxx xxxxxx xxxx xxxxx xxxxxx x xxxx xxxx xxxx xxxxxxx
            xxxx xx xxxxxxxxxxxxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxx xxxxx xxxxxxxxx
            x
            xxxx xx xxxxxxxxxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxx xxxx xxxxxxx
            x
            xx [/pro] */

            // [#1952] SQL Server OPTION() clauses as well as many other optional
            // end-of-query clauses are appended to the end of a query
            if (!StringUtils.isBlank(option)) {
                context.formatSeparator()
                       .sql(option);
            }

            if (TRUE.equals(wrapDerivedTables)) {
                context.formatIndentEnd()
                       .formatNewLine()
                       .sql(")")
                       .data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, true);
            }

            /* [pro] xx
            xx xxxx xxxxxxx
            xx xxxxxxxxxxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxxxx

                xxxxxxxx xxxxxxxxxx x xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xx xxxxxxxxxxx xx xxxxx
                    xxxxxxxxxx x xxxxx

                xx xxxxxxxxxxx xx xxxx
                        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxx x

                    xxxxxxxxxxxxxxxxxxxxxxxxx
                           xxxxxxxxxxxxxxxx
                           xxxxxx xx
                           xxxxxxxxxxxxxxxxxxx
                x

                xxxxxxxxxxxxxxxxxxxxxxxxx
            x

            xx [/pro] */
        }
        finally {
            if (renderTrailingLimit != null)
                context.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE, renderTrailingLimit);
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
    private void toSQLReferenceLimitDefault(Context<?> context) {
        Object data = context.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE);

        context.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE, true);
        toSQLReference0(context);

        if (data == null)
            context.data().remove(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE);
        else
            context.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE, data);
    }

    /* [pro] xx
    xxx
     x xxxxxxxx xxx xxxxx x xxxxxx xxxxxx xx xxx xxxxxx xxxxxxxxxxxxxxxx
     x xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxx
     xx
    xxxxxxxxxxxxxxxxxxxxxxxxxxx
    xxxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxx x

        xx xxxxxxxxxx xxxxxxxx xxxxxxxxxx
        xxxxx xxxxxxxxxx xxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xx xxx xxx xxxxx
        xxxxx xxxxxxxx xxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xx xxx xxx xx
        xxxxx xxxxxxxx xxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xx xxxxxxxxx xx xxx xxxxxxx xx xxx xxxxxxxxxx xx xx
        xx xxxxxxx xxx xx xxxx x xx xx xxxx xx xxxxx xxxxx xxxxx xxxxx xxxx xxxxx xxx xxxxxx xxx xxxxxxxxx
        xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxx x xxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxx xx x
                x xxx xxxxxxx x xxxxxxxxxxxxxx x
                x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx

            xxxx
        xx

        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x xx x
            xxx xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx x
                xxxxxxxxx
                xxxxxx xxxx xxxxxxxxxxxxxxxxx xx x
                    xxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                    xx xxxxxxx xxxxxx xxxx xx xxxxxx xxxxxxx xxxx xxx xxxxxxxxxxx xxxxxx xxxxxx
                    xx xxx xxxxxxxxxx xxxx xxx xxxxx xxxxxxx xxxxxxxxxx xxxxx xx xxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                    xxxxxxx xxxxxxx x xxxxxxxxxxxx

                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxxxx x xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxx
                    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxxx

                    xx xxxxxxx xxxx xxxxxxxx xx xxxxxxxx xx xxxxxxx xxx xxxxxxxxxxxx xxxxxxx
                    xx xxxxx xxxxxxx xxx xxxxxxxx xxxxxxxxxx xxxxxxxx xxx xxxxxxxxxxxx xxxxxxx
                    xx xxxxxxxx xx xxx xxxxxxxx xxxxx xx xxxxxx xxx xxx xxx xxxxxxxxxxx xxxx
                    xx xxx xxxxxxxxxx
                    xxxxxxxxxxxxxxxx
                        x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xx

                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxxxxx

                x
            xxxxxxxxxxx

        xx xx xx xxx xx xx xxx xx xx xxxxx
        xxxxx xxxxxxxxxx xxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx

        xxxxxxx xxxxxxxx x xxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxxxxxxxxx xx
           xxxxxxxxxxxxxxxxxxxx
           xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxxxxxx xxx
           xxxxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxx
           xxxxxxx xx
           xxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxxxxxxx xx
           xxxxxxxxxxxxxxxxxx
           xxxxxx x xx
           xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxxxxx xx
           xxxxxxxxxxxxxxxxxx
           xxxxxx xx xx
           xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xxx
     x xxxxxxxx xxx xxxxx x xxxxxx xxxxxx xx xxx xxxxxx xxxxxxxxxxxxxxxxxx
     x xxxxxxx
     xx
    xxxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxx x

        xx xxxxxxxxxx xxxxxxxx xxxxxxxxxx
        xxxxxxxxxx xxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xx xxx xxx xxxxx
        xxxxxxxx xxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xx xxx xxx xx
        xxxxxxxx xxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xx xxxxxxxxx xx xxx xxxxxxx xx xxx xxxxxxxxxx xx xx
        xxxxxxxxxx xxxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx

        xx xxxxx xxxxx xxxxx xxxxxx xx
        xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxx x xxxxxxxxxxxxxx

            xx xxxxxxx xxx xx xxxx x xx xx xxxx xx xxxxx xxxxx xxxxx xxxxx xxxx xxxxx xxx xxxxxx xxx xxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxx xx x
                x xxx xxxxxxx x xxxxxxxxxxxxxxxxxx xxxxxxxxxx x
                x xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxx
        xx

        xx xx xx xxx xx xx xxx xx xx xxxxx
        xxxxxxxxxx xxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxxxxxxxxx xx
           xxxxxxxxxxxxxxxxxxxx
           xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxxxxxx xxx
           xxxxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxx
             xxxxxxxxxxxxxxxxxxxxxxxx xx
             xxxxxxxxxxxxxxxxxxxx
             xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
             xxxxxxxxxxxxxxxxxxxxx
             xxxxxxxxxxxxxxxxxx
             xxxxxxxxxxxxxxxxxxxxxx xxx
             xxxxxxxxxxxxxxxxxxxx
             xxxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxx

        xxx  xxxxxxxxxxxxxxxxxx
             xxxxxxxxxxxxxxxx
             xxxxxxx xx
             xxxxxxxxxxxxxxxxx
             xxxxxxxxxxxxxxxxxx
             xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xx
             xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxx
           xxxxxxx xx
           xxxxxxxxxxxxxxxxxx
           xxxxxxxxxxxxxxxxxxxxxxx xx
           xxxxxxxxxxxxxxxxxx
           xxxxxx x xx
           xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    x
    xx [/pro] */

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

        int unionOpSize = unionOp.size();

        // The SQL standard specifies:
        //
        // <query expression> ::=
        //    [ <with clause> ] <query expression body>
        //    [ <order by clause> ] [ <result offset clause> ] [ <fetch first clause> ]
        //
        // Depending on the dialect and on various syntax elements, parts of the above must be wrapped in
        // synthetic parentheses
        boolean wrapQueryExpressionInDerivedTable = false;
        boolean wrapQueryExpressionBodyInDerivedTable = false;

        /* [pro] xx

        xx xxxxxxxx xxxxxxx xxxxx xxxx xx xxxxx xx xxxxxxxxxx xxxxxxxxxxx xxx xx xxx
        xx xxxxxxxxx xxx xxxxxxxx xxxx x xxxxxxx xxxxxx xxxxx xxxx xx xxxxx xxx xxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxxx xx xxxxxxxx xx xxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxx

        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx
                   xxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxxxxxxxx xxx
                   xxxxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxxx

        xx xxxxxxx
        xx xxxxxxxxxxxx        x xxx xx xxxxx xx xxxxxxx xxxxx xxxx xx x xxxxxx xxxxx xxxxxxxxxxxxx
        xx                       xxxxx xxxxx xxxxxxxxxx xxx xx xxx xxxxx xxxxx xxxxxxxxxx xxxxx xx xxxxx
        xx                       xx xxxxxxx xxxxx xx xxxxxx xxxxxxxxx xxxxxxxx xx xxx xxxxx xxxxxxxxxxx
        xx                       xxx xxxxx xxxxxxxxxx xxxx xxxx xx xxxxxxx xx x xxxxxxx xxxxxx
        xx xxxx xxxxxxxxxx xxxxx xxxxxxxxxxxx x xxxxx xxxxxxxxx xxxx xx xxxxxxx xxxxx xxxxxxxxxx xxx xxxxx
        xx                       xxxx xxx xx xxxx xxxx xxx x xxxxxxx xxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx

            xxxxxxxxxxxxxxxxxxxxxxxx

            xxxxxxxxxxxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxx
                   xxxxxxxxxxxx

            xx xxxxxxxxxxxxxxxxxx xx xxxx xx xxxxxxxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxx xx
                       xxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x xxx
                       xxxxxxxxxxxxxxxxxxxxxx

            xxxxxxxxxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxxxxxxxx xxx
                   xxxxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxxx
        x

        xx [/pro] */

        // [#1658] jOOQ applies left-associativity to set operators. In order to enforce that across
        // all databases, we need to wrap relevant subqueries in parentheses.
        if (unionOpSize > 0) {
            for (int i = unionOpSize - 1; i >= 0; i--) {
                switch (unionOp.get(i)) {
                    case EXCEPT:    context.start(SELECT_EXCEPT);    break;
                    case INTERSECT: context.start(SELECT_INTERSECT); break;
                    case UNION:     context.start(SELECT_UNION);     break;
                    case UNION_ALL: context.start(SELECT_UNION_ALL); break;
                }

                unionParenthesis(context, "(");
            }
        }

        // SELECT clause
        // -------------
        context.start(SELECT_SELECT)
               .keyword("select")
               .sql(" ");

        // [#1493] Oracle hints come directly after the SELECT keyword
        if (!StringUtils.isBlank(hint)) {
            context.sql(hint).sql(" ");
        }

        /* [pro] xx
        xx xxxxxxxx xxxxxxxx xxxx xx xxxxx xx xx xx xxxxxx xxxxxx xxxxxxxx
        xx xxxxxxx xx xxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx
        x
        xx [/pro] */

        if (!distinctOn.isEmpty()) {
            context.keyword("distinct on").sql(" (").visit(distinctOn).sql(") ");
        }
        else if (distinct) {
            context.keyword("distinct").sql(" ");
        }

        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxx
        xx [/pro] */

        context.declareFields(true);

        // [#2335] When emulating LIMIT .. OFFSET, the SELECT clause needs to generate
        // non-ambiguous column names as ambiguous column names are not allowed in subqueries
        if (alternativeFields != null) {
            if (wrapQueryExpressionBodyInDerivedTable && originalFields.length < alternativeFields.length)
                context.visit(new SelectFieldList(Arrays.copyOf(alternativeFields, alternativeFields.length - 1)));
            else
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
        if (!asList().contains(family)) {
            context.start(SELECT_INTO);

            Table<?> actualInto = (Table<?>) context.data(DATA_SELECT_INTO_TABLE);
            if (actualInto == null)
                actualInto = into;

            if (actualInto != null
                    && context.data(DATA_OMIT_INTO_CLAUSE) == null
                    && asList(HSQLDB, POSTGRES).contains(family)) {

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
            /* [pro] xx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxx
            xx [/pro] */
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

            /* [pro] xx
            xx xxxxxxx xxxxxxx xxxxxx xxx xxx xxxxxx xxxx x xxxxxxxxxxxx xxxxx xxxxx
            xx xx xx xxxx xx xxxxx xx xx xxxxx xxxx
            xx xxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxx
                xx xxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxx xxxxxxx x xx xxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxx xx xxxxxxx xx xxxxxxxxx
                    xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
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

        if (!getWindow().isEmpty() && asList(POSTGRES).contains(family)) {
            context.formatSeparator()
                   .keyword("window")
                   .sql(" ")
                   .declareWindows(true)
                   .visit(getWindow())
                   .declareWindows(false);
        }

        context.end(SELECT_WINDOW);

        // ORDER BY clause for local subselect
        // -----------------------------------
        toSQLOrderBy(
            context,
            originalFields, alternativeFields,
            false, wrapQueryExpressionBodyInDerivedTable,
            orderBy, limit
        );

        // SET operations like UNION, EXCEPT, INTERSECT
        // --------------------------------------------
        if (unionOpSize > 0) {
            unionParenthesis(context, ")");

            for (int i = 0; i < unionOpSize; i++) {
                CombineOperator op = unionOp.get(i);

                for (Select<?> other : union.get(i)) {
                    context.formatSeparator()
                           .keyword(op.toSQL(dialect))
                           .sql(" ");

                    unionParenthesis(context, "(");
                    context.visit(other);
                    unionParenthesis(context, ")");
                }

                // [#1658] Close parentheses opened previously
                if (i < unionOpSize - 1)
                    unionParenthesis(context, ")");

                switch (unionOp.get(i)) {
                    case EXCEPT:    context.end(SELECT_EXCEPT);    break;
                    case INTERSECT: context.end(SELECT_INTERSECT); break;
                    case UNION:     context.end(SELECT_UNION);     break;
                    case UNION_ALL: context.end(SELECT_UNION_ALL); break;
                }
            }
        }

        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxx
                   xxxxxxx xxxx
        xx [/pro] */

        // ORDER BY clause for UNION
        // -------------------------
        boolean qualify = context.qualify();
        try {
            context.qualify(false);
            toSQLOrderBy(
                context,
                originalFields, alternativeFields,
                wrapQueryExpressionInDerivedTable, wrapQueryExpressionBodyInDerivedTable,
                unionOrderBy, unionLimit
            );
        }
        finally {
            context.qualify(qualify);
        }
    }

    private final void toSQLOrderBy(
            Context<?> context,
            Field<?>[] originalFields, Field<?>[] alternativeFields,
            boolean wrapQueryExpressionInDerivedTable, boolean wrapQueryExpressionBodyInDerivedTable,
            SortFieldList actualOrderBy,
            Limit actualLimit
    ) {

        context.start(SELECT_ORDER_BY);

        if (!actualOrderBy.isEmpty()) {
            context.formatSeparator()
                   .keyword("order")
                   .sql(orderBySiblings ? " " : "")
                   .keyword(orderBySiblings ? "siblings" : "")
                   .sql(" ")
                   .keyword("by")
                   .sql(" ");

            /* [pro] xx

            xx xxxxxxx xxxx xxxxxxx xxx xxxxxx xxx xxxx xxxx xxxxxx xxxxxxx xxxx xxx xxxxxx xxxxxx
            xx xxx xx xxxx xxx xxxxxxx xxxx xxxx xxxxxxxxxx xx xxxxxxx xxxxxx xxxxxxxxxxx xxx
            xx xxxxxxxxx xxxx xxxx xxxxx xx xxx xxxxx xx xxxxxx
            xx xxxxxxxxxxxxxxx xx xxxxx x
                xxxxxxx xxxxxxx x xxxxxxxxxxxxxxxxxx

                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxxxx x xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxx
                xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxx

                xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxx
            x
            xxxx
            xx [/pro] */
            {
                context.visit(actualOrderBy);
            }
        }

        /* [pro] xx
        xx xxxxxxx xxx xxxxxx xxxx xxxxxxxx xx xxxxx xx xxxxxxx xxxxx xxxx
        xx xxxxxx xx xxxxx
        xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxx xxxx
                   xxxxxx xxx
                   xxxxxxxxxxxxxxxxxx
                   xxxxxx xxxxx
        x
        xx [/pro] */

        context.end(SELECT_ORDER_BY);

        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxx
                   xxxxxxxxxx
        xx [/pro] */

        if (context.data().containsKey(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE) && actualLimit.isApplicable())
            context.visit(actualLimit);
    }

    /* [pro] xx
    xxxxxxx xxxxx xxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxx x
        xxxxxx xxxxxxxxxxxxxxx xxxxxxxxxxx xxxx xxxx xxxxxx xxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxx
            xx xxxxxxxxxxxxxx x xxx
    x

    xxxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx x
        xxxxxxxxxx xxxxxx x xxxxxxxxxxxxxxxxx
        xxxxxxxxxx xxxxxxx x xxxxxxxxxxxxxxxxxx

        xx xxxxxx xxx xxx xxxxxx xxxx xxxxxxx xxx xxxxxxx
        xxxxxx xxxxxxxx x
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
                xxxx xx xxxxxxx xx xxxxxxxxx
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
    x
    xx [/pro] */

    private final void unionParenthesis(Context<?> ctx, String parenthesis) {
        if (")".equals(parenthesis)) {
            ctx.formatIndentEnd()
               .formatNewLine();
        }

        // [#3579] Nested set operators aren't supported in some databases. Emulate them via derived tables...
        else if ("(".equals(parenthesis)) {
            switch (ctx.family()) {
                /* [pro] xx
                xxxx xxxxxxx
                xxxx xxxx
                xx [/pro] */
                case DERBY:
                case SQLITE:
                case MARIADB:
                case MYSQL:
                    ctx.formatNewLine()
                       .keyword("select")
                       .sql(" *")
                       .formatSeparator()
                       .keyword("from")
                       .sql(" ");
                    break;
            }
        }

        // [#3579] ... but don't use derived tables to emulate nested set operators for Firebird, as that
        // only causes many more issues in various contexts where they are not allowed:
        // - Recursive CTE
        // - INSERT SELECT
        // - Derived tables with undefined column names (see also [#3679])

        switch (ctx.family()) {
            case FIREBIRD:
                break;

            default:
                ctx.sql(parenthesis);
                break;
        }

        if ("(".equals(parenthesis)) {
            ctx.formatIndentStart()
               .formatNewLine();
        }

        else if (")".equals(parenthesis)) {
            switch (ctx.family()) {
                /* [pro] xx
                xxxx xxxxxxx
                xxxx xxxx
                xx [/pro] */
                case DERBY:
                case SQLITE:
                case MARIADB:
                case MYSQL:
                    ctx.sql(" x");
                    break;
            }
        }
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
        getLimit().setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(Param<Integer> numberOfRows) {
        getLimit().setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(int offset, int numberOfRows) {
        getLimit().setOffset(offset);
        getLimit().setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(int offset, Param<Integer> numberOfRows) {
        getLimit().setOffset(offset);
        getLimit().setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(Param<Integer> offset, int numberOfRows) {
        getLimit().setOffset(offset);
        getLimit().setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(Param<Integer> offset, Param<Integer> numberOfRows) {
        getLimit().setOffset(offset);
        getLimit().setNumberOfRows(numberOfRows);
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

    /* [pro] xx
    xxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxxxx x
        xxxxxxxxxxxxxxxxxxxx x xxxxx
        xxxxxxxxxxxxxxxxx x xxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxx x
        xxxxxxxxxxxxxxxxxxxx x xxxxxx
        xxxxxxxxxxxxxxxxx x xxxxx
    x
    xx [/pro] */

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
        return (unionOp.size() == 0) ? orderBy : unionOrderBy;
    }

    final QueryPartList<Field<?>> getSeek() {
        return (unionOp.size() == 0) ? seek : unionSeek;
    }

    final Limit getLimit() {
        return (unionOp.size() == 0) ? limit : unionLimit;
    }

    /* [pro] xx
    xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx x
        xx xxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxxxxxxxxx xxxxxx x xxx xxxxxxxxxxxxxxxx

            xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                xxxx xxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxx

                xxxx xxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxx

                xxxx xxxxxxxxxx
                xxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxx
                    xxxxxx
            x
            xxxxxx xxxxxxx
        x

        xxxxxx xxxxxxxxxxxxx
    x

    xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx x
        xxxxxxxxxxxxx xxxxx x xxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xxx xxxxxxxxx xxxxx x xxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxx

        xxxxxx xxxxxx
    x
    xx [/pro] */

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
        if (unionOp.size() == 0)
            this.orderBySiblings = orderBySiblings;
        else
            this.unionOrderBySiblings = orderBySiblings;
    }

    @Override
    public final void addSeekAfter(Field<?>... fields) {
        addSeekAfter(Arrays.asList(fields));
    }

    @Override
    public final void addSeekAfter(Collection<? extends Field<?>> fields) {
        if (unionOp.size() == 0)
            seekBefore = false;
        else
            unionSeekBefore = false;

        getSeek().addAll(fields);
    }

    @Override
    public final void addSeekBefore(Field<?>... fields) {
        addSeekBefore(Arrays.asList(fields));
    }

    @Override
    public final void addSeekBefore(Collection<? extends Field<?>> fields) {
        if (unionOp.size() == 0)
            seekBefore = true;
        else
            unionSeekBefore = true;

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
    public final void addFrom(TableLike<?> f) {
        addFrom(Arrays.asList(f));
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

    private final Select<R> combine(CombineOperator op, Select<? extends R> other) {
        int index = unionOp.size() - 1;

        if (index == -1 || unionOp.get(index) != op || op == EXCEPT) {
            unionOp.add(op);
            union.add(new QueryPartList<Select<?>>());

            index++;
        }

        union.get(index).add(other);
        return this;
    }

    @Override
    public final Select<R> union(Select<? extends R> other) {
        return combine(UNION, other);
    }

    @Override
    public final Select<R> unionAll(Select<? extends R> other) {
        return combine(UNION_ALL, other);
    }

    @Override
    public final Select<R> except(Select<? extends R> other) {
        return combine(EXCEPT, other);
    }

    @Override
    public final Select<R> intersect(Select<? extends R> other) {
        return combine(INTERSECT, other);
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
                xx xxxxxxxxxxxx xx xxxx xx xxxxxxxxxxxxxxxxxx x xx
                    x x xxxxxxxxxxxxxxxxxxxxxxxxxxx
                xx [/pro] */
                joined = o.on(conditions);
                break;
            }
            case RIGHT_OUTER_JOIN: {
                TablePartitionByStep p = getFrom().get(index).rightOuterJoin(table);
                TableOnStep o = p;
                /* [pro] xx
                xx xxxxxxxxxxxx xx xxxx xx xxxxxxxxxxxxxxxxxx x xx
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
