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

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.jooq.Clause.SELECT;
import static org.jooq.Clause.SELECT_CONNECT_BY;
import static org.jooq.Clause.SELECT_EXCEPT;
import static org.jooq.Clause.SELECT_EXCEPT_ALL;
import static org.jooq.Clause.SELECT_FROM;
import static org.jooq.Clause.SELECT_GROUP_BY;
import static org.jooq.Clause.SELECT_HAVING;
import static org.jooq.Clause.SELECT_INTERSECT;
import static org.jooq.Clause.SELECT_INTERSECT_ALL;
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
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SortOrder.ASC;
import static org.jooq.impl.CombineOperator.EXCEPT;
import static org.jooq.impl.CombineOperator.EXCEPT_ALL;
import static org.jooq.impl.CombineOperator.INTERSECT;
import static org.jooq.impl.CombineOperator.INTERSECT_ALL;
import static org.jooq.impl.CombineOperator.UNION;
import static org.jooq.impl.CombineOperator.UNION_ALL;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.orderBy;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.Tools.fieldArray;
import static org.jooq.impl.Tools.hasAmbiguousNames;
import static org.jooq.impl.Tools.DataKey.DATA_COLLECTED_SEMI_ANTI_JOIN;
import static org.jooq.impl.Tools.DataKey.DATA_COLLECT_SEMI_ANTI_JOIN;
import static org.jooq.impl.Tools.DataKey.DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST;
import static org.jooq.impl.Tools.DataKey.DATA_LOCALLY_SCOPED_DATA_MAP;
import static org.jooq.impl.Tools.DataKey.DATA_OMIT_INTO_CLAUSE;
import static org.jooq.impl.Tools.DataKey.DATA_OVERRIDE_ALIASES_IN_ORDER_BY;
import static org.jooq.impl.Tools.DataKey.DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE;
import static org.jooq.impl.Tools.DataKey.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY;
import static org.jooq.impl.Tools.DataKey.DATA_SELECT_INTO_TABLE;
import static org.jooq.impl.Tools.DataKey.DATA_UNALIAS_ALIASES_IN_ORDER_BY;
import static org.jooq.impl.Tools.DataKey.DATA_WINDOW_DEFINITIONS;
import static org.jooq.impl.Tools.DataKey.DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.GroupField;
import org.jooq.JoinType;
import org.jooq.Name;
import org.jooq.Operator;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.SelectQuery;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnStep;
import org.jooq.TablePartitionByStep;
import org.jooq.WindowDefinition;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.Tools.DataKey;
import org.jooq.tools.StringUtils;

/**
 * A sub-select is a <code>SELECT</code> statement that can be combined with
 * other <code>SELECT</code> statement in <code>UNION</code>s and similar
 * operations.
 *
 * @author Lukas Eder
 */
final class SelectQueryImpl<R extends Record> extends AbstractResultQuery<R> implements SelectQuery<R> {

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
    private final QueryPartList<SelectField<?>>  distinctOn;
    private boolean                              forUpdate;
    private final QueryPartList<Field<?>>        forUpdateOf;
    private final TableList                      forUpdateOfTables;
    private ForUpdateMode                        forUpdateMode;
    private int                                  forUpdateWait;
    private boolean                              forShare;




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

    SelectQueryImpl(Configuration configuration, WithImpl with) {
        this(configuration, with, null);
    }

    SelectQueryImpl(Configuration configuration, WithImpl with, boolean distinct) {
        this(configuration, with, null, distinct);
    }

    SelectQueryImpl(Configuration configuration, WithImpl with, TableLike<? extends R> from) {
        this(configuration, with, from, false);
    }

    SelectQueryImpl(Configuration configuration, WithImpl with, TableLike<? extends R> from, boolean distinct) {
        super(configuration);

        this.with = with;
        this.distinct = distinct;
        this.distinctOn = new QueryPartList<SelectField<?>>();
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
        List<Field<?>> s = getSelect();

        if (s.size() != 1) {
            throw new IllegalStateException("Can only use single-column ResultProviderQuery as a field");
        }

        return new ScalarSubquery<T>(this, (DataType<T>) s.get(0).getDataType());
    }

    @Override
    public final <T> Field<T> asField(String alias) {
        return this.<T> asField().as(alias);
    }


    @Override
    public <T> Field<T> asField(Function<? super Field<T>, ? extends String> aliasFunction) {
        return this.<T> asField().as(aliasFunction);
    }


    @Override
    public final Row fieldsRow() {
        return asTable().fieldsRow();
    }


    @Override
    public final Stream<Field<?>> fieldStream() {
        return Stream.of(fields());
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
    public final <T> Field<T> field(String name, Class<T> type) {
        return asTable().field(name, type);
    }

    @Override
    public final <T> Field<T> field(String name, DataType<T> dataType) {
        return asTable().field(name, dataType);
    }

    @Override
    public final Field<?> field(Name string) {
        return asTable().field(string);
    }

    @Override
    public final <T> Field<T> field(Name name, Class<T> type) {
        return asTable().field(name, type);
    }

    @Override
    public final <T> Field<T> field(Name name, DataType<T> dataType) {
        return asTable().field(name, dataType);
    }

    @Override
    public final Field<?> field(int index) {
        return asTable().field(index);
    }

    @Override
    public final <T> Field<T> field(int index, Class<T> type) {
        return asTable().field(index, type);
    }

    @Override
    public final <T> Field<T> field(int index, DataType<T> dataType) {
        return asTable().field(index, dataType);
    }

    @Override
    public final Field<?>[] fields() {
        return asTable().fields();
    }

    @Override
    public final Field<?>[] fields(Field<?>... fields) {
        return asTable().fields(fields);
    }

    @Override
    public final Field<?>[] fields(String... fieldNames) {
        return asTable().fields(fieldNames);
    }

    @Override
    public final Field<?>[] fields(Name... fieldNames) {
        return asTable().fields(fieldNames);
    }

    @Override
    public final Field<?>[] fields(int... fieldIndexes) {
        return asTable().fields(fieldIndexes);
    }

    @Override
    public final Table<R> asTable() {
        // Its usually better to alias nested selects that are used in
        // the FROM clause of a query
        return new DerivedTable<R>(this).as("alias_" + Tools.hash(this));
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
    public final Table<R> asTable(String alias, Function<? super Field<?>, ? extends String> aliasFunction) {
        return new DerivedTable<R>(this).as(alias, aliasFunction);
    }

    @Override
    public final Table<R> asTable(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> aliasFunction) {
        return new DerivedTable<R>(this).as(alias, aliasFunction);
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
        Object localDataMap = context.data(DATA_LOCALLY_SCOPED_DATA_MAP);
        try {
            if (renderTrailingLimit != null)
                context.data().remove(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE);
            context.data(DATA_LOCALLY_SCOPED_DATA_MAP, new HashMap<Object, Object>());

            if (into != null
                    && context.data(DATA_OMIT_INTO_CLAUSE) == null
                    && asList(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE).contains(family)) {

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
                context.sql('(')
                       .formatIndentStart()
                       .formatNewLine()
                       .data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, null);
            }

            switch (dialect) {






















































































                // By default, render the dialect's limit clause
                default: {
                    toSQLReferenceLimitDefault(context);

                    break;
                }
            }

            // [#1296] FOR UPDATE is emulated in some dialects using ResultSet.CONCUR_UPDATABLE
            if (forUpdate && !asList(CUBRID).contains(family)) {
                context.formatSeparator()
                       .keyword("for update");

                if (!forUpdateOf.isEmpty()) {

                    // [#4151] Some databases don't allow for qualifying column
                    // names here. Copy also to TableList
                    boolean unqualified = asList(DERBY, FIREBIRD, H2, HSQLDB).contains(context.family());
                    boolean qualify = context.qualify();

                    if (unqualified)
                        context.qualify(false);

                    context.sql(' ').keyword("of")
                           .sql(' ').visit(forUpdateOf);

                    if (unqualified)
                        context.qualify(qualify);
                }
                else if (!forUpdateOfTables.isEmpty()) {
                    context.sql(' ').keyword("of").sql(' ');

                    switch (family) {

                        // Some dialects don't allow for an OF [table-names] clause
                        // It can be emulated by listing the table's fields, though







                        case DERBY: {
                            forUpdateOfTables.toSQLFields(context);
                            break;
                        }

                        // Render the OF [table-names] clause
                        default:
                            Tools.tableNames(context, forUpdateOfTables);
                            break;
                    }
                }

                // [#3186] Firebird's FOR UPDATE clause has a different semantics. To achieve "regular"
                // FOR UPDATE semantics, we should use FOR UPDATE WITH LOCK
                if (family == FIREBIRD) {
                    context.sql(' ').keyword("with lock");
                }

                if (forUpdateMode != null) {
                    context.sql(' ');
                    context.keyword(forUpdateMode.toSQL());

                    if (forUpdateMode == ForUpdateMode.WAIT) {
                        context.sql(' ');
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
                context.formatIndentEnd()
                       .formatNewLine()
                       .sql(')')
                       .data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, true);
            }
























        }
        finally {
            context.data(DATA_LOCALLY_SCOPED_DATA_MAP, localDataMap);
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
    @SuppressWarnings("unchecked")
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
        boolean wrapQueryExpressionInDerivedTable;
        boolean wrapQueryExpressionBodyInDerivedTable = false;


        wrapQueryExpressionInDerivedTable = false







        // [#2995] Prevent the generation of wrapping parentheses around the
        //         INSERT .. SELECT statement's SELECT because they would be
        //         interpreted as the (missing) INSERT column list's parens.
         || (context.data(DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST) != null && unionOpSize > 0);

        if (wrapQueryExpressionInDerivedTable)
            context.keyword("select").sql(" *")
                   .formatSeparator()
                   .keyword("from").sql(" (")
                   .formatIndentStart()
                   .formatNewLine();



































        // [#1658] jOOQ applies left-associativity to set operators. In order to enforce that across
        // all databases, we need to wrap relevant subqueries in parentheses.
        if (unionOpSize > 0) {
            for (int i = unionOpSize - 1; i >= 0; i--) {
                switch (unionOp.get(i)) {
                    case EXCEPT:        context.start(SELECT_EXCEPT);        break;
                    case EXCEPT_ALL:    context.start(SELECT_EXCEPT_ALL);    break;
                    case INTERSECT:     context.start(SELECT_INTERSECT);     break;
                    case INTERSECT_ALL: context.start(SELECT_INTERSECT_ALL); break;
                    case UNION:         context.start(SELECT_UNION);         break;
                    case UNION_ALL:     context.start(SELECT_UNION_ALL);     break;
                }

                unionParenthesis(context, "(");
            }
        }

        // SELECT clause
        // -------------
        context.start(SELECT_SELECT)
               .keyword("select")
               .sql(' ');

        // [#1493] Oracle hints come directly after the SELECT keyword
        if (!StringUtils.isBlank(hint)) {
            context.sql(hint).sql(' ');
        }








        if (!distinctOn.isEmpty()) {
            context.keyword("distinct on").sql(" (").visit(distinctOn).sql(") ");
        }
        else if (distinct) {
            context.keyword("distinct").sql(' ');
        }






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
                context.sql('(')
                       .visit(getSelect1())
                       .sql(')');
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
        // [#4910] This clause (and the Clause.SELECT_INTO signal) must be emitted
        //         only in top level SELECTs
        if (!context.subquery() && !asList().contains(family)) {
            context.start(SELECT_INTO);

            Table<?> actualInto = (Table<?>) context.data(DATA_SELECT_INTO_TABLE);
            if (actualInto == null)
                actualInto = into;

            if (actualInto != null
                    && context.data(DATA_OMIT_INTO_CLAUSE) == null
                    && asList(HSQLDB, POSTGRES).contains(family)) {

                context.formatSeparator()
                       .keyword("into")
                       .sql(' ')
                       .visit(actualInto);
            }

            context.end(SELECT_INTO);
        }

        // FROM and JOIN clauses
        // ---------------------
        context.start(SELECT_FROM)
               .declareTables(true);

        // [#....] Some SQL dialects do not require a FROM clause. Others do and
        //         jOOQ generates a "DUAL" table or something equivalent.
        //         See also org.jooq.impl.Dual for details.
        boolean hasFrom = !getFrom().isEmpty()



            || asList(CUBRID, DERBY, FIREBIRD, HSQLDB, MARIADB, MYSQL).contains(family)
        ;

        List<Condition> semiAntiJoinPredicates = null;

        if (hasFrom) {
            Object previousCollect = context.data(DATA_COLLECT_SEMI_ANTI_JOIN, true);
            Object previousCollected = context.data(DATA_COLLECTED_SEMI_ANTI_JOIN, null);

            context.formatSeparator()
                   .keyword("from")
                   .sql(' ')
                   .visit(getFrom());













            semiAntiJoinPredicates = (List<Condition>) context.data(DATA_COLLECTED_SEMI_ANTI_JOIN, previousCollected);
            context.data(DATA_COLLECT_SEMI_ANTI_JOIN, previousCollect);
        }

        context.declareTables(false)
               .end(SELECT_FROM);

        // WHERE clause
        // ------------
        context.start(SELECT_WHERE);

        if (getWhere().getWhere() instanceof TrueCondition && semiAntiJoinPredicates == null)
            ;
        else {
            ConditionProviderImpl where = new ConditionProviderImpl();

            if (semiAntiJoinPredicates != null)
                where.addConditions(semiAntiJoinPredicates);

            if (!(getWhere().getWhere() instanceof TrueCondition))
                where.addConditions(getWhere());

            context.formatSeparator()
                   .keyword("where")
                   .sql(' ')
                   .visit(where);
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
                   .sql(' ')
                   .visit(getConnectByStartWith());
        }

        context.end(SELECT_START_WITH);
        context.start(SELECT_CONNECT_BY);

        if (!(getConnectBy().getWhere() instanceof TrueCondition)) {
            context.formatSeparator()
                   .keyword("connect by");

            if (connectByNoCycle) {
                context.sql(' ').keyword("nocycle");
            }

            context.sql(' ').visit(getConnectBy());
        }

        context.end(SELECT_CONNECT_BY);

        // GROUP BY and HAVING clause
        // --------------------------
        context.start(SELECT_GROUP_BY);

        if (grouping) {
            context.formatSeparator()
                   .keyword("group by")
                   .sql(' ');

            // [#1665] Empty GROUP BY () clauses need parentheses
            if (getGroupBy().isEmpty()) {

                // [#1681] Use the constant field from the dummy table Sybase ASE, Ingres
                if (asList().contains(family)) {
                    context.sql("empty_grouping_dummy_table.dual");
                }

                // [#4292] Some dialects accept constant expressions in GROUP BY
                // Note that dialects may consider constants as indexed field
                // references, as in the ORDER BY clause!
                else if (asList(DERBY).contains(family)) {
                    context.sql('0');
                }

                // [#4447] CUBRID can't handle subqueries in GROUP BY
                else if (family == CUBRID) {
                    context.sql("1 + 0");
                }

                // [#4292] Some dialects don't support empty GROUP BY () clauses
                else if (asList(FIREBIRD, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE).contains(family)) {
                    context.sql('(').visit(DSL.select(one())).sql(')');
                }

                // Few dialects support the SQL standard "grand total" (i.e. empty grouping set)
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
                   .sql(' ')
                   .visit(getHaving());
        }

        context.end(SELECT_HAVING);

        // WINDOW clause
        // -------------
        context.start(SELECT_WINDOW);

        if (!getWindow().isEmpty() && asList(POSTGRES).contains(family)) {
            context.formatSeparator()
                   .keyword("window")
                   .sql(' ')
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
                           .sql(' ');

                    unionParenthesis(context, "(");
                    context.visit(other);
                    unionParenthesis(context, ")");
                }

                // [#1658] Close parentheses opened previously
                if (i < unionOpSize - 1)
                    unionParenthesis(context, ")");

                switch (unionOp.get(i)) {
                    case EXCEPT:        context.end(SELECT_EXCEPT);        break;
                    case EXCEPT_ALL:    context.end(SELECT_EXCEPT_ALL);    break;
                    case INTERSECT:     context.end(SELECT_INTERSECT);     break;
                    case INTERSECT_ALL: context.end(SELECT_INTERSECT_ALL); break;
                    case UNION:         context.end(SELECT_UNION);         break;
                    case UNION_ALL:     context.end(SELECT_UNION_ALL);     break;
                }
            }
        }








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
                   .sql(' ')
                   .keyword("by")
                   .sql(' ');




















            {
                context.visit(actualOrderBy);
            }
        }













        context.end(SELECT_ORDER_BY);

        if (wrapQueryExpressionInDerivedTable)
            context.formatIndentEnd()
                   .formatNewLine()
                   .sql(") x");

        if (context.data().containsKey(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE) && actualLimit.isApplicable())
            context.visit(actualLimit);
    }

























































    private final void unionParenthesis(Context<?> ctx, String parenthesis) {
        if (")".equals(parenthesis)) {
            ctx.formatIndentEnd()
               .formatNewLine();
        }

        // [#3579] Nested set operators aren't supported in some databases. Emulate them via derived tables...
        else if ("(".equals(parenthesis)) {
            switch (ctx.family()) {




                case DERBY:
                case SQLITE:
                case MARIADB:
                case MYSQL:
                    ctx.formatNewLine()
                       .keyword("select")
                       .sql(" *")
                       .formatSeparator()
                       .keyword("from")
                       .sql(' ');
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
    public final void addSelect(Collection<? extends SelectField<?>> fields) {
        getSelect0().addAll(Tools.fields(fields));
    }

    @Override
    public final void addSelect(SelectField<?>... fields) {
        addSelect(Arrays.asList(fields));
    }

    @Override
    public final void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public final void addDistinctOn(SelectField<?>... fields) {
        addDistinctOn(Arrays.asList(fields));
    }

    @Override
    public final void addDistinctOn(Collection<? extends SelectField<?>> fields) {
        this.distinctOn.addAll(fields);
    }

    @Override
    public final void setInto(Table<?> into) {
        this.into = into;
    }

    @Override
    public final void addOffset(int offset) {
        getLimit().setOffset(offset);
    }

    @Override
    public final void addOffset(Param<Integer> offset) {
        getLimit().setOffset(offset);
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

        if (index == -1 || unionOp.get(index) != op || op == EXCEPT || op == EXCEPT_ALL) {
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
    public final Select<R> exceptAll(Select<? extends R> other) {
        return combine(EXCEPT_ALL, other);
    }

    @Override
    public final Select<R> intersect(Select<? extends R> other) {
        return combine(INTERSECT, other);
    }

    @Override
    public final Select<R> intersectAll(Select<? extends R> other) {
        return combine(INTERSECT_ALL, other);
    }

    @Override
    public final void addJoin(TableLike<?> table, Condition... conditions) {
        addJoin(table, JoinType.JOIN, conditions);
    }

    @Override
    public final void addJoin(TableLike<?> table, JoinType type, Condition... conditions) {
        addJoin0(table, type, conditions, null);
    }








    private final void addJoin0(TableLike<?> table, JoinType type, Condition[] conditions, Field<?>[] partitionBy) {

        // TODO: This and similar methods should be refactored, patterns extracted...
        int index = getFrom().size() - 1;
        Table<?> joined = null;

        switch (type) {
            case JOIN:
            case STRAIGHT_JOIN:
            case LEFT_SEMI_JOIN:
            case LEFT_ANTI_JOIN:
            case FULL_OUTER_JOIN: {
                joined = getFrom().get(index).join(table, type).on(conditions);
                break;
            }

            case LEFT_OUTER_JOIN:
            case RIGHT_OUTER_JOIN: {
                TablePartitionByStep<?> p = getFrom().get(index).join(table, type);
                TableOnStep<?> o = p;




                joined = o.on(conditions);
                break;
            }

            // These join types don't take any ON clause. Ignore conditions.
            case CROSS_JOIN:
            case NATURAL_JOIN:
            case NATURAL_LEFT_OUTER_JOIN:
            case NATURAL_RIGHT_OUTER_JOIN:




                joined = getFrom().get(index).join(table, type);
                break;

            default: throw new IllegalArgumentException("Bad join type: " + type);
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
            case LEFT_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:
            case FULL_OUTER_JOIN:
            case LEFT_SEMI_JOIN:
            case LEFT_ANTI_JOIN:
                joined = getFrom().get(index).join(table, type).onKey();
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
            case LEFT_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:
            case FULL_OUTER_JOIN:
            case LEFT_SEMI_JOIN:
            case LEFT_ANTI_JOIN:
                joined = getFrom().get(index).join(table, type).onKey(keyFields);
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
            case LEFT_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:
            case FULL_OUTER_JOIN:
            case LEFT_SEMI_JOIN:
            case LEFT_ANTI_JOIN:
                joined = getFrom().get(index).join(table, type).onKey(key);
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
            case LEFT_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:
            case FULL_OUTER_JOIN:
            case LEFT_SEMI_JOIN:
            case LEFT_ANTI_JOIN:
                joined = getFrom().get(index).join(table, type).using(fields);
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
