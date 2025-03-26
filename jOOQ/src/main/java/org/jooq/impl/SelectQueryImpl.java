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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.IntStream.range;
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
import static org.jooq.JoinType.CROSS_JOIN;
import static org.jooq.JoinType.JOIN;
import static org.jooq.JoinType.LEFT_OUTER_JOIN;
import static org.jooq.JoinType.RIGHT_OUTER_JOIN;
import static org.jooq.Operator.OR;
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
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.SortOrder.DESC;
// ...
// ...
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.SettingsTools.getRenderTable;
import static org.jooq.impl.AsteriskImpl.NO_SUPPORT_NATIVE_EXCEPT;
import static org.jooq.impl.AsteriskImpl.NO_SUPPORT_UNQUALIFIED_COMBINED;
import static org.jooq.impl.CombineOperator.EXCEPT;
import static org.jooq.impl.CombineOperator.EXCEPT_ALL;
import static org.jooq.impl.CombineOperator.INTERSECT;
import static org.jooq.impl.CombineOperator.INTERSECT_ALL;
import static org.jooq.impl.CombineOperator.UNION;
import static org.jooq.impl.CombineOperator.UNION_ALL;
import static org.jooq.impl.CommonTableExpressionList.markTopLevelCteAndAccept;
import static org.jooq.impl.ConditionProviderImpl.extractCondition;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.createTable;
import static org.jooq.impl.DSL.emptyGroupingSet;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.generateSeries;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonbArrayAgg;
import static org.jooq.impl.DSL.jsonbObject;
import static org.jooq.impl.DSL.key;
// ...
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.orderBy;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.regexpReplaceAll;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.rowNumber;
// ...
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlattributes;
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.InlineDerivedTable.transformInlineDerivedTables;
import static org.jooq.impl.Internal.isub;
import static org.jooq.impl.JSONArrayAgg.EMULATE_WITH_GROUP_CONCAT;
import static org.jooq.impl.JSONArrayAgg.patchOracleArrayAggBug;
import static org.jooq.impl.JSONNull.NO_SUPPORT_ABSENT_ON_NULL;
import static org.jooq.impl.Keywords.K_BY;
import static org.jooq.impl.Keywords.K_CONNECT_BY;
import static org.jooq.impl.Keywords.K_DISTINCT;
import static org.jooq.impl.Keywords.K_DISTINCT_ON;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Keywords.K_GROUP_BY;
import static org.jooq.impl.Keywords.K_HAVING;
import static org.jooq.impl.Keywords.K_INLINE;
import static org.jooq.impl.Keywords.K_INTO;
import static org.jooq.impl.Keywords.K_MATERIALIZE;
import static org.jooq.impl.Keywords.K_NOCYCLE;
import static org.jooq.impl.Keywords.K_ORDER;
import static org.jooq.impl.Keywords.K_ORDER_BY;
import static org.jooq.impl.Keywords.K_QUALIFY;
import static org.jooq.impl.Keywords.K_SELECT;
import static org.jooq.impl.Keywords.K_SIBLINGS;
import static org.jooq.impl.Keywords.K_START_WITH;
import static org.jooq.impl.Keywords.K_TOP;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.Keywords.K_WINDOW;
import static org.jooq.impl.Keywords.K_WITH_CHECK_OPTION;
import static org.jooq.impl.Keywords.K_WITH_READ_ONLY;
import static org.jooq.impl.Multiset.returningClob;
import static org.jooq.impl.Names.N_LEVEL;
import static org.jooq.impl.Names.N_ROWNUM;
import static org.jooq.impl.Names.N_T;
import static org.jooq.impl.QueryPartCollectionView.wrap;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.JSON;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.SQLDataType.XML;
import static org.jooq.impl.Tools.aliased;
import static org.jooq.impl.Tools.aliasedFields;
import static org.jooq.impl.Tools.allMatch;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.autoAlias;
import static org.jooq.impl.Tools.camelCase;
import static org.jooq.impl.Tools.concat;
import static org.jooq.impl.Tools.containsUnaliasedTable;
import static org.jooq.impl.Tools.deleteQueryImpl;
import static org.jooq.impl.Tools.fieldArray;
import static org.jooq.impl.Tools.hasAmbiguousNames;
import static org.jooq.impl.Tools.hasAmbiguousNamesInTables;
import static org.jooq.impl.Tools.hasName;
import static org.jooq.impl.Tools.insertQueryImpl;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.impl.Tools.isInlineVal0;
import static org.jooq.impl.Tools.isNotEmpty;
import static org.jooq.impl.Tools.isWindow;
import static org.jooq.impl.Tools.joinedTables;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.qualify;
import static org.jooq.impl.Tools.recordType;
import static org.jooq.impl.Tools.selectQueryImpl;
import static org.jooq.impl.Tools.traverseJoins;
import static org.jooq.impl.Tools.unalias;
import static org.jooq.impl.Tools.unqualified;
import static org.jooq.impl.Tools.updateQueryImpl;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_COLLECT_SEMI_ANTI_JOIN;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_FORCE_LIMIT_WITH_ORDER_BY;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_INSERT_SELECT;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONTENT;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_NESTED_SET_OPERATIONS;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_OMIT_INTO_CLAUSE;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_UNALIAS_ALIASED_EXPRESSIONS;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES;
import static org.jooq.impl.Tools.ExtendedDataKey.DATA_RENDER_TABLE;
import static org.jooq.impl.Tools.ExtendedDataKey.DATA_TRANSFORM_ROWNUM_TO_LIMIT;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_COLLECTED_SEMI_ANTI_JOIN;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_DML_TARGET_TABLE;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_DML_USING_TABLES;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_OVERRIDE_ALIASES_IN_ORDER_BY;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_RENDERING_DATA_CHANGE_DELTA_TABLE;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_SELECT_ALIASES;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_SELECT_INTO_TABLE;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_TOP_LEVEL_CTE;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_WINDOW_DEFINITIONS;
import static org.jooq.impl.Transformations.transformGroupByColumnIndex;
import static org.jooq.impl.Transformations.transformInlineCTE;
import static org.jooq.impl.Transformations.transformQualify;
import static org.jooq.impl.Transformations.transformRownum;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jooq.AggregateFunction;
import org.jooq.Asterisk;
import org.jooq.Clause;
import org.jooq.CommonTableExpression;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.GeneratorStatementType;
import org.jooq.GroupField;
import org.jooq.JSONEntry;
import org.jooq.JSONObjectNullStep;
import org.jooq.JSONObjectReturningStep;
import org.jooq.JoinType;
import org.jooq.Name;
import org.jooq.Name.Quoted;
import org.jooq.Operator;
import org.jooq.OrderField;
import org.jooq.Param;
// ...
import org.jooq.QualifiedAsterisk;
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.SelectGroupByStep;
import org.jooq.SelectQuery;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnStep;
import org.jooq.TableOptionalOnStep;
import org.jooq.TablePartitionByStep;
import org.jooq.TableRecord;
// ...
// ...
// ...
import org.jooq.WindowDefinition;
import org.jooq.XML;
import org.jooq.conf.AutoAliasExpressions;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.AbstractContext.AliasOverride;
import org.jooq.impl.ForLock.ForLockMode;
import org.jooq.impl.ForLock.ForLockWaitMode;
import org.jooq.impl.QOM.CompareCondition;
import org.jooq.impl.QOM.JoinHint;
import org.jooq.impl.QOM.Materialized;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.impl.QOM.With;
import org.jooq.impl.Tools.BooleanDataKey;
import org.jooq.impl.Tools.ExtendedDataKey;
import org.jooq.impl.Tools.SimpleDataKey;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;


/**
 * A sub-select is a <code>SELECT</code> statement that can be combined with
 * other <code>SELECT</code> statement in <code>UNION</code>s and similar
 * operations.
 *
 * @author Lukas Eder
 */
final class SelectQueryImpl<R extends Record> extends AbstractResultQuery<R> implements SelectQuery<R> {
    private static final JooqLogger      log                                     = JooqLogger.getLogger(SelectQueryImpl.class);
    private static final Clause[]        CLAUSES                                 = { SELECT };
    static final Set<SQLDialect>         EMULATE_SELECT_INTO_AS_CTAS             = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB);
    private static final Set<SQLDialect> SUPPORT_SELECT_INTO_TABLE               = SQLDialect.supportedBy(HSQLDB, POSTGRES, YUGABYTEDB);



    static final Set<SQLDialect>         NO_SUPPORT_WINDOW_CLAUSE                = SQLDialect.supportedUntil(CUBRID, DERBY, HSQLDB, IGNITE, MARIADB);
    private static final Set<SQLDialect> REQUIRES_FROM_CLAUSE                    = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD, HSQLDB);
    private static final Set<SQLDialect> REQUIRES_DERIVED_TABLE_DML              = SQLDialect.supportedUntil(MYSQL);
    private static final Set<SQLDialect> NO_IMPLICIT_GROUP_BY_ON_HAVING          = SQLDialect.supportedBy(SQLITE);





















    static final Set<SQLDialect>         SUPPORT_FULL_WITH_TIES                  = SQLDialect.supportedBy(CLICKHOUSE, H2, MARIADB, POSTGRES, TRINO);
    static final Set<SQLDialect>         EMULATE_DISTINCT_ON                     = SQLDialect.supportedBy(DERBY, FIREBIRD, HSQLDB, MARIADB, MYSQL, SQLITE, TRINO);
    static final Set<SQLDialect>         NO_SUPPORT_FOR_UPDATE_OF_FIELDS         = SQLDialect.supportedBy(MYSQL, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>         NO_SUPPORT_UNION_ORDER_BY_ALIAS         = SQLDialect.supportedBy(FIREBIRD);
    static final Set<SQLDialect>         NO_SUPPORT_WITH_READ_ONLY               = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB);
    static final Set<SQLDialect>         NO_SUPPORT_LIMIT_ZERO                   = SQLDialect.supportedBy(DERBY, HSQLDB);
    static final Set<SQLDialect>         REQUIRES_ORDER_BY_ALIAS_OVERRIDE        = SQLDialect.supportedUntil(CLICKHOUSE, MARIADB, MYSQL, TRINO);
    static final Set<SQLDialect>         WRAP_EXP_BODY_IN_DERIVED_TABLE_LIMIT    = SQLDialect.supportedUntil(CLICKHOUSE);
    static final Set<SQLDialect>         WRAP_EXP_BODY_IN_DERIVED_TABLE_ORDER_BY = SQLDialect.supportedBy(CLICKHOUSE);
    static final Set<SQLDialect>         WRAP_UNION_SUBQ_IN_DERIVED_TABLE_LIMIT  = SQLDialect.supportedBy(FIREBIRD);


















    final WithImpl                                       with;
    private final SelectFieldList<SelectFieldOrAsterisk> select;
    private Table<?>                                     intoTable;
    private String                                       hint;
    private String                                       option;
    private boolean                                      distinct;
    private final QueryPartList<SelectFieldOrAsterisk>   distinctOn;
    private ForLock                                      forLock;
    private boolean                                      withCheckOption;
    private boolean                                      withReadOnly;














    private final TableList                              from;
    private final ConditionProviderImpl                  condition;
    private final GroupFieldList                         groupBy;
    private boolean                                      groupByDistinct;
    private final ConditionProviderImpl                  having;
    private WindowList                                   window;
    private final ConditionProviderImpl                  qualify;
    private final SortFieldList                          orderBy;




    private final QueryPartList<Field<?>>                seek;
    private boolean                                      seekBefore;
    private final Limit                                  limit;
    private final List<CombineOperator>                  unionOp;
    private final List<QueryPartList<Select<?>>>         union;
    private final SortFieldList                          unionOrderBy;




    private final QueryPartList<Field<?>>                unionSeek;
    private boolean                                      unionSeekBefore;      // [#3579] TODO
    private final Limit                                  unionLimit;
    private final Map<QueryPart, QueryPart>              localQueryPartMapping;

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
        this.distinctOn = new SelectFieldList<>();
        this.select = new SelectFieldList<>();
        this.from = new TableList();
        this.condition = new ConditionProviderImpl();




        this.groupBy = new GroupFieldList();
        this.having = new ConditionProviderImpl();
        this.qualify = new ConditionProviderImpl();
        this.orderBy = new SortFieldList();
        this.seek = new QueryPartList<>();
        this.limit = new Limit();
        this.unionOp = new ArrayList<>();
        this.union = new ArrayList<>();
        this.unionOrderBy = new SortFieldList();
        this.unionSeek = new QueryPartList<>();
        this.unionLimit = new Limit();

        if (from != null)
            this.from.add(from.asTable());

        this.localQueryPartMapping = new LinkedHashMap<>();
    }

    private enum CopyClause {
        START,
        WHERE,
        QUALIFY,
        UNION,
        END;

        final boolean between(CopyClause startInclusive, CopyClause endExclusive) {
            return compareTo(startInclusive) >= 0 && compareTo(endExclusive) < 0;
        }
    }


























































    private final SelectQueryImpl<R> copyTo(CopyClause clause, boolean scalarSelect, SelectQueryImpl<R> result) {
        return copyBetween(CopyClause.START, clause, scalarSelect, result);
    }

    private final SelectQueryImpl<R> copyAfter(CopyClause clause, boolean scalarSelect, SelectQueryImpl<R> result) {
        return copyBetween(clause, CopyClause.END, scalarSelect, result);
    }

    private final SelectQueryImpl<R> copyBetween(CopyClause start, CopyClause end, boolean scalarSelect, SelectQueryImpl<R> result) {
        if (CopyClause.START.between(start, end)) {
            result.from.addAll(from);
            result.condition.setWhere(condition.getWhere());

            if (scalarSelect)
                result.select.addAll(select);
        }

        if (CopyClause.WHERE.between(start, end)) {






            result.groupBy.addAll(groupBy);
            result.groupByDistinct = groupByDistinct;
            result.having.setWhere(having.getWhere());
            if (isNotEmpty(window))
                result.addWindow0(window);
            result.qualify.setWhere(qualify.getWhere());
        }

        if (CopyClause.QUALIFY.between(start, end)) {
            if (!scalarSelect)
                result.select.addAll(select);

            result.hint = hint;
            result.distinct = distinct;
            result.distinctOn.addAll(distinctOn);
            result.orderBy.addAll(orderBy);



            result.seek.addAll(seek);
            result.limit.from(limit);
            result.forLock = forLock;




            result.withCheckOption = withCheckOption;
            result.withReadOnly = withReadOnly;
            result.option = option;
            result.intoTable = intoTable;
        }

        if (CopyClause.UNION.between(start, end)) {
            for (QueryPartList<Select<?>> u : union) {
                QueryPartList<Select<?>> l = new QueryPartList<>();

                for (Select<?> s : u) {
                    SelectQueryImpl<?> q = selectQueryImpl(s);

                    if (q != null)
                        q = q.copy(x -> {});

                    l.add(q);
                }

                result.union.add(l);
            }

            result.unionOp.addAll(unionOp);
            result.unionOrderBy.addAll(unionOrderBy);



            result.unionSeek.addAll(unionSeek);
            result.unionSeekBefore = unionSeekBefore;
            result.unionLimit.from(unionLimit);
        }

        return result;
    }

    private final SelectQueryImpl<R> copy(Consumer<? super SelectQueryImpl<R>> finisher) {
        return copy(finisher, with);
    }

    private final SelectQueryImpl<R> copy(Consumer<? super SelectQueryImpl<R>> finisher, WithImpl newWith) {
        SelectQueryImpl<R> result = copyTo(CopyClause.END, false, new SelectQueryImpl<>(configuration(), newWith));
        finisher.accept(result);
        return result;
    }

    private final SelectQueryImpl<R> copyAfter(CopyClause clause, Consumer<? super SelectQueryImpl<R>> finisher) {
        SelectQueryImpl<R> result = copyAfter(clause, false, new SelectQueryImpl<>(configuration(), with));
        finisher.accept(result);
        return result;
    }














































    @SuppressWarnings("unchecked")
    @Override
    public final <T> Field<T> asField() {
        return new ScalarSubquery<>(this, (DataType<T>) Tools.scalarType(this), false);
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
    public final Field<Result<R>> asMultiset() {
        return DSL.multiset(this);
    }

    @Override
    public final Field<Result<R>> asMultiset(String alias) {
        return DSL.multiset(this).as(alias);
    }

    @Override
    public final Field<Result<R>> asMultiset(Name alias) {
        return DSL.multiset(this).as(alias);
    }

    @Override
    public final Field<Result<R>> asMultiset(Field<?> alias) {
        return DSL.multiset(this).as(alias);
    }

    @Override
    public final Table<R> asTable() {
        // [#13349] Delay the possibly expensive computation of the auto alias,
        //          possibly making the computation unnecessary
        return new DerivedTable<>(this).as(new LazyName(() -> DSL.name(autoAlias(this))));
    }

    @Override
    public final Table<R> asTable(String alias) {
        return new DerivedTable<>(this).as(alias);
    }

    @Override
    public final Table<R> asTable(String alias, String... fieldAliases) {
        return new DerivedTable<>(this).as(alias, fieldAliases);
    }

    @Override
    public final Table<R> asTable(String alias, Collection<? extends String> fieldAliases) {
        return new DerivedTable<>(this).as(alias, fieldAliases);
    }

    @Override
    public final Table<R> asTable(Name alias) {
        return new DerivedTable<>(this).as(alias);
    }

    @Override
    public final Table<R> asTable(Name alias, Name... fieldAliases) {
        return new DerivedTable<>(this).as(alias, fieldAliases);
    }

    @Override
    public final Table<R> asTable(Name alias, Collection<? extends Name> fieldAliases) {
        return new DerivedTable<>(this).as(alias, fieldAliases);
    }

    @Override
    public final Table<R> asTable(Table<?> alias) {
        return new DerivedTable<>(this).as(alias);
    }

    @Override
    public final Table<R> asTable(Table<?> alias, Field<?>... fieldAliases) {
        return new DerivedTable<>(this).as(alias, fieldAliases);
    }

    @Override
    public final Table<R> asTable(Table<?> alias, Collection<? extends Field<?>> fieldAliases) {
        return new DerivedTable<>(this).as(alias, fieldAliases);
    }

    @Override
    public final Table<R> asTable(String alias, Function<? super Field<?>, ? extends String> aliasFunction) {
        return new DerivedTable<>(this).as(alias, aliasFunction);
    }

    @Override
    public final Table<R> asTable(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> aliasFunction) {
        return new DerivedTable<>(this).as(alias, aliasFunction);
    }

    @Override
    public final Field<?>[] getFields(ThrowingSupplier<? extends ResultSetMetaData, SQLException> rs) throws SQLException {
        Field<?>[] fields = getFields();

        // If no projection was specified explicitly, create fields from result
        // set meta data instead. This is typically the case for SELECT * ...
        if (fields.length == 0)
            return new MetaDataFieldProvider(configuration(), rs.get()).getFields();

        return fields;
    }

    @Override
    public final Field<?>[] getFields() {
        Collection<? extends Field<?>> fields = coerce();

        // [#1808] TODO: Restrict this field list, in case a restricting fetch()
        // method was called to get here
        if (fields == null || fields.isEmpty())












            fields = getSelect();

        return fieldArray(fields);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
































































































































































































































































































































































































































































































    @SuppressWarnings("unchecked")
    private final Select<?> distinctOnEmulation() {

        // [#3564] TODO: Extract and merge this with getSelectResolveSomeAsterisks0()
        List<Field<?>> partitionBy = new ArrayList<>(distinctOn.size());

        for (SelectFieldOrAsterisk s : distinctOn)
            if (s instanceof Field<?> f)
                partitionBy.add(f);

        Field<Integer> rn = rowNumber().over(partitionBy(partitionBy).orderBy(orderBy)).as("rn");

        SelectQueryImpl<R> copy = copy(x -> {
            x.distinctOn.clear();
            x.select.add(rn);






















            x.orderBy.clear();
            x.limit.clear();
            x.unionOp.clear();
            x.union.clear();
            x.unionOrderBy.clear();
            x.unionSeek.clear();
            x.unionLimit.clear();
        });

        return copyAfter(CopyClause.UNION, x -> {
            x.select.addAll(new QualifiedSelectFieldList(table(N_T), select));
            x.from.add(copy.asTable(N_T));
            x.condition.addConditions(rn.eq(one()));
            x.orderBy.addAll(map(orderBy, (SortField<?> s) -> unqualified(s)));
            x.limit.from(limit);
        });
    }




























































































































    @SuppressWarnings("unchecked")
    @Override
    public final void accept(Context<?> ctx) {
        Table<?> dmlTable;
        Table<?> dcdTable;
        List<Table<?>> dmlTables;

        // [#6583] [#8609] [#14742] Work around MySQL's self-reference-in-DML-subquery restriction
        if (ctx.subqueryLevel() == 1
            && REQUIRES_DERIVED_TABLE_DML.contains(ctx.dialect())
            && !TRUE.equals(ctx.data(DATA_INSERT_SELECT))
            && (dmlTable = (Table<?>) ctx.data(DATA_DML_TARGET_TABLE)) != null
            && containsUnaliasedTable(getFrom(), dmlTable)) {
            ctx.visit(DSL.select(asterisk()).from(asTable("t")));
        }

        // [#14742] MariaDB still has this bug: https://jira.mariadb.org/browse/MDEV-17954
        else if (ctx.subqueryLevel() == 1
            && ctx.family() == MARIADB
            && !TRUE.equals(ctx.data(DATA_INSERT_SELECT))
            && (

            // A USING clause can be generated when the target table is aliased
                    (dmlTable = (Table<?>) ctx.data(DATA_DML_TARGET_TABLE)) != null
                && Tools.aliased(dmlTable) != null
                && containsUnaliasedTable(getFrom(), dmlTable)

            // Or, explicitly
            ||      (dmlTables = (List<Table<?>>) ctx.data(DATA_DML_USING_TABLES)) != null
                && Tools.anyMatch(dmlTables, t -> containsUnaliasedTable(getFrom(), t)))) {
            ctx.visit(DSL.select(asterisk()).from(asTable("t")));
        }

        // [#5810] Emulate the Teradata QUALIFY clause
        else if (qualify.hasWhere() && transformQualify(ctx.configuration())) {



        }

        // [#3564] Emulate DISTINCT ON queries at the top level
        else if (Tools.isNotEmpty(distinctOn) && EMULATE_DISTINCT_ON.contains(ctx.dialect())) {
            ctx.visit(distinctOnEmulation());
        }

















































        else if (withReadOnly && NO_SUPPORT_WITH_READ_ONLY.contains(ctx.dialect())) {
            ctx.visit(copy(s -> {
                s.withReadOnly = false;

                // [#14611] Avoid emulation if view isn't updatable anyway
                if (!s.distinct
                    && s.groupBy.isEmpty()
                    && !s.having.hasWhere()
                    && !s.limit.isApplicable()
                    && !s.hasUnions())
                    s.union((Select<R>) DSL.select(map(s.getSelect(), (Field<?> f) -> inline((Object) null, f))).where(falseCondition()));
            }));
        }
        else if (from.size() == 1
            && (dcdTable = unalias(from.get(0))) instanceof DataChangeDeltaTable
            && ((DataChangeDeltaTable<?>) dcdTable).emulateUsingReturning(ctx)
            && ctx.configuration().requireCommercial(() -> "Data change delta table emulations are a commercial only feature. Please consider upgrading to the jOOQ Professional Edition or jOOQ Enterprise Edition.")



        ) {






















        }
        else
            accept0(ctx);
    }






























































































































    @SuppressWarnings({ "unchecked", "rawtypes" })
    final void accept0(Context<?> context) {







        boolean topLevelCte = false;

        // Subquery scopes are started in AbstractContext
        if (context.subqueryLevel() == 0) {
            context.scopeStart(this);

            if (topLevelCte |= (context.data(DATA_TOP_LEVEL_CTE) == null))
                context.data(DATA_TOP_LEVEL_CTE, new TopLevelCte());
        }

        SQLDialect dialect = context.dialect();

        switch (getRenderTable(context.settings())) {
            case NEVER: {
                context.data(DATA_RENDER_TABLE, false);
                break;
            }

            case WHEN_MULTIPLE_TABLES: {
                if (knownTableSource()) {
                    Iterator<Table<?>> it = concat(
                        joinedTables(getFrom()),
                        context.scopeParts((Class<Table<?>>) (Class) Table.class)
                    ).iterator();

                    // [#15996] At least 2 tables are in scope
                    if (!it.hasNext() || it.next() != null && !it.hasNext())
                        context.data(DATA_RENDER_TABLE, false);
                }

                break;
            }

            case WHEN_AMBIGUOUS_COLUMNS: {
                if (knownTableSource() && !hasAmbiguousNamesInTables(concat(
                    joinedTables(getFrom()),
                    context.scopeParts((Class<Table<?>>) (Class) Table.class)
                )))
                    context.data(DATA_RENDER_TABLE, false);

                break;
            }
        }

        // [#2791] [#9981] TODO: We have an automatic way of pushing / popping
        //                 these values onto the scope stack. Use that, instead
        Object renderTrailingLimit = context.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE);
        Object transformRownumToLimit = context.data(DATA_TRANSFORM_ROWNUM_TO_LIMIT);
        Name[] selectAliases = (Name[]) context.data(DATA_SELECT_ALIASES);











        try {
            AliasOverride aliasOverride = null;

            if (selectAliases != null) {
                List<Field<?>> originalFields = null;
                List<Field<?>> alternativeFields = null;

                context.data().remove(DATA_SELECT_ALIASES);



                Name[] a = selectAliases;
                alternativeFields = map(originalFields = getSelect(),
                    (f, i) -> i < a.length && a[i] != null ? f.as(a[i]) : f
                );

                aliasOverride = new AliasOverride(originalFields, alternativeFields);
            }

            if (TRUE.equals(renderTrailingLimit))
                context.data().remove(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE);
            if (TRUE.equals(transformRownumToLimit))
                context.data().remove(DATA_TRANSFORM_ROWNUM_TO_LIMIT);

            if (intoTable != null
                    && !TRUE.equals(context.data(DATA_OMIT_INTO_CLAUSE))
                    && EMULATE_SELECT_INTO_AS_CTAS.contains(dialect)) {

                context.data(DATA_OMIT_INTO_CLAUSE, true, c -> c.visit(createTable(intoTable).as(this)));
                return;
            }

            if (with != null)
                context.visit(with);
            else if (topLevelCte)
                markTopLevelCteAndAccept(context, c -> {});

            pushWindow(context);

            Boolean wrapDerivedTables = (Boolean) context.data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES);
            if (TRUE.equals(wrapDerivedTables)) {
                context.sqlIndentStart('(')
                       .data().remove(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES);
            }

            switch (context.family()) {
























































































































                case MARIADB: {
                    if (getLimit().isApplicable() && getLimit().isExpression())
                        toSQLReferenceLimitWithWindowFunctions(context, aliasOverride);




                    else
                        toSQLReferenceLimitDefault(context, aliasOverride);

                    break;
                }
                case POSTGRES: {





                    toSQLReferenceLimitDefault(context, aliasOverride);
                    break;
                }





                case FIREBIRD:
                case MYSQL: {
                    if (getLimit().isApplicable() && (getLimit().withTies() || getLimit().isExpression()))
                        toSQLReferenceLimitWithWindowFunctions(context, aliasOverride);
                    else
                        toSQLReferenceLimitDefault(context, aliasOverride);

                    break;
                }

                case TRINO: {
                    if (getLimit().isApplicable() && getLimit().isExpression())
                        toSQLReferenceLimitWithWindowFunctions(context, aliasOverride);
                    else
                        toSQLReferenceLimitDefault(context, aliasOverride);

                    break;
                }




                case CUBRID:
                case DUCKDB:
                case YUGABYTEDB: {
                    if (getLimit().isApplicable() && getLimit().withTies())
                        toSQLReferenceLimitWithWindowFunctions(context, aliasOverride);
                    else
                        toSQLReferenceLimitDefault(context, aliasOverride);

                    break;
                }

                // By default, render the dialect's limit clause
                default: {
                    toSQLReferenceLimitDefault(context, aliasOverride);
                    break;
                }
            }





            // [#1296] [#7328] FOR UPDATE is emulated in some dialects using hints
            if (forLock != null)
                context.visit(forLock);

            // [#3600] The Oracle / SQL Server WITH CHECK OPTION / WITH READ ONLY clauses
            else if (withCheckOption)
                context.formatSeparator()
                       .visit(K_WITH_CHECK_OPTION);
            else if (withReadOnly && !NO_SUPPORT_WITH_READ_ONLY.contains(context.dialect()))
                context.formatSeparator()
                       .visit(K_WITH_READ_ONLY);














            // [#1952] SQL Server OPTION() clauses as well as many other optional
            // end-of-query clauses are appended to the end of a query
            if (!StringUtils.isBlank(option))
                context.formatSeparator()
                       .sql(option);

            if (TRUE.equals(wrapDerivedTables))
                context.sqlIndentEnd(')')
                       .data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, true);
























        }
        finally {
            if (transformRownumToLimit != null)
                context.data(DATA_TRANSFORM_ROWNUM_TO_LIMIT, transformRownumToLimit);
            if (renderTrailingLimit != null)
                context.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE, renderTrailingLimit);
            if (selectAliases != null)
                context.data(DATA_SELECT_ALIASES, selectAliases);
        }

        if (context.subqueryLevel() == 0)
            context.scopeEnd();
    }















    private final void pushWindow(Context<?> context) {
        // [#531] [#2790] Make the WINDOW clause available to the SELECT clause
        // to be able to inline window definitions if the WINDOW clause is not
        // supported.
        if (Tools.isNotEmpty(window))
            context.data(DATA_WINDOW_DEFINITIONS, window);
    }

    /**
     * The default LIMIT / OFFSET clause in most dialects
     */
    private final void toSQLReferenceLimitDefault(Context<?> context, AliasOverride aliasOverride) {
        context.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE, true, c -> toSQLReference0(context, aliasOverride, null));
    }

    /**
     * Omit rendering any limit clause
     */
    private final void toSQLReferenceQualifyInsteadOfLimit(Context<?> context, AliasOverride aliasOverride) {
        context.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE, false, c -> toSQLReference0(context, aliasOverride, limitWindowFunctionCondition(limitWindowFunction(context))));
    }

    /**
     * Emulate the LIMIT / OFFSET clause using window functions, specifically
     * when the WITH TIES clause is specified.
     */
    private final void toSQLReferenceLimitWithWindowFunctions(Context<?> ctx, AliasOverride aliasOverride) {
        if (Transformations.EMULATE_QUALIFY.contains(ctx.dialect()) || getQualify().hasWhere())
            toSQLReferenceLimitWithWindowFunctions0(ctx);
        else
            toSQLReferenceQualifyInsteadOfLimit(ctx, aliasOverride);
    }


    private final void toSQLReferenceLimitWithWindowFunctions0(Context<?> ctx) {

        // AUTHOR.ID, BOOK.ID, BOOK.TITLE
        final List<Field<?>> originalFields = getSelect();

        // AUTHOR.ID as v1, BOOK.ID as v2, BOOK.TITLE as v3
        // Enforce x.* or just * if we have no known field names (e.g. when plain SQL tables are involved)
        final List<Field<?>> aliasedFields = new ArrayList<>(originalFields.size());

        if (originalFields.isEmpty())
            aliasedFields.add(DSL.field("*"));
        else
            aliasedFields.addAll(aliasedFields(originalFields));

        aliasedFields.add(CustomField.of("rn", SQLDataType.INTEGER, c -> {
            boolean wrapQueryExpressionBodyInDerivedTable = wrapQueryExpressionBodyInDerivedTable(c, true);

            // [#3575] Ensure that no column aliases from the surrounding SELECT clause
            // are referenced from the below ranking functions' ORDER BY clause.
            c.data(DATA_UNALIAS_ALIASED_EXPRESSIONS, !wrapQueryExpressionBodyInDerivedTable);

            boolean q = c.qualify();

            c.data(DATA_OVERRIDE_ALIASES_IN_ORDER_BY, new AliasOverride(originalFields, aliasedFields));
            if (wrapQueryExpressionBodyInDerivedTable)
                c.qualify(false);

            // [#2580] FETCH NEXT n ROWS ONLY emulation:
            // -----------------------------------------
            // When DISTINCT is applied, we mustn't use ROW_NUMBER() OVER(),
            // which changes the DISTINCT semantics. Instead, use DENSE_RANK() OVER(),
            // ordering by the SELECT's ORDER BY clause AND all the expressions from
            // the projection
            //
            // [#6197] FETCH NEXT n ROWS WITH TIES emulation:
            // ----------------------------------------------
            // DISTINCT seems irrelevant here (to be proven)

            c.visit(limitWindowFunction(c));
            c.data().remove(DATA_UNALIAS_ALIASED_EXPRESSIONS);
            c.data().remove(DATA_OVERRIDE_ALIASES_IN_ORDER_BY);
            if (wrapQueryExpressionBodyInDerivedTable)
                c.qualify(q);
        }).as("rn"));

        // v1 as ID, v2 as ID, v3 as TITLE
        final List<Field<?>> unaliasedFields = Tools.unaliasedFields(originalFields);

        ctx.visit(K_SELECT).separatorRequired(true)
           .declareFields(true, c -> c.visit(new SelectFieldList<>(unaliasedFields)))
           .formatSeparator()
           .visit(K_FROM).sqlIndentStart(" (")
           .subquery(true);

        // [#18234] Re-push window specifications to the new subquery scope
        pushWindow(ctx);

        // [#13560] [#17947] Communicate the enforcement of MULTISET content to the CursorImpl.
        if (ctx.executeContext() != null)
            ctx.executeContext().data(DATA_MULTISET_CONTENT, true);
        ctx.data(DATA_MULTISET_CONTENT, true, c -> toSQLReference0(c, new AliasOverride(originalFields, aliasedFields), null));

        ctx.subquery(false)
           .sqlIndentEnd(") ")
           .visit(name("x"))
           .formatSeparator()
           .visit(K_WHERE).sql(' ')
           .visit(limitWindowFunctionCondition(DSL.field(name("rn"), INTEGER)));

        // [#5068] Don't rely on nested query's ordering in case an operation
        //         like DISTINCT or JOIN produces hashing.
        // [#7427] Don't order if not strictly required.
        // [#7609] Don't order if users prefer not to
        if (!ctx.subquery()
                && !getOrderBy().isEmpty()
                && !Boolean.FALSE.equals(ctx.settings().isRenderOrderByRownumberForEmulatedPagination()))
            ctx.formatSeparator()
               .visit(K_ORDER_BY)
               .sql(' ')
               .visit(name("rn"));
    }

    @SuppressWarnings("unchecked")
    private final Condition limitWindowFunctionCondition(Field<Integer> limitWindowFunction) {
        return getLimit().limitAbsent()
            ? limitWindowFunction.gt((Field<Integer>) getLimit().getLowerRownum())
            : limitWindowFunction
                    .between((Field<Integer>) getLimit().getLowerRownum().add(inline(1)))
                    .and((Field<Integer>) getLimit().getUpperRownum());
    }

    private final Field<Integer> limitWindowFunction(Context<?> c) {
        return distinct
            ? DSL.denseRank().over(orderBy(getNonEmptyOrderByForDistinct(c.configuration())))
            : getLimit().withTies()
            ? DSL.rank().over(orderBy(getNonEmptyOrderBy(c.configuration())))
            : DSL.rowNumber().over(orderBy(getNonEmptyOrderBy(c.configuration())));
    }








































































    /**
     * This method renders the main part of a query without the LIMIT clause.
     * This part is common to any type of limited query
     */
    @SuppressWarnings("unchecked")
    private final void toSQLReference0(
        Context<?> context,
        AliasOverride aliasOverride,
        Condition additionalQualify
    ) {
        SQLDialect family = context.family();
        boolean qualify = context.qualify();

        int unionOpSize = unionOp.size();
        boolean unionParensRequired = false;
        boolean unionOpNesting = false;

        // The SQL standard specifies:
        //
        // <query expression> ::=
        //    [ <with clause> ] <query expression body>
        //    [ <order by clause> ] [ <result offset clause> ] [ <fetch first clause> ]
        //
        // Depending on the dialect and on various syntax elements, parts of the above must be wrapped in
        // synthetic parentheses
        boolean wrapQueryExpressionInDerivedTable;
        boolean wrapQueryExpressionBodyInDerivedTable;
        boolean applySeekOnDerivedTable = applySeekOnDerivedTable();


        wrapQueryExpressionInDerivedTable = false







//        // [#2995] Prevent the generation of wrapping parentheses around the
//        //         INSERT .. SELECT statement's SELECT because they would be
//        //         interpreted as the (missing) INSERT column list's parens.
//         || (context.data(DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST) != null && unionOpSize > 0)
         ;

        if (wrapQueryExpressionInDerivedTable)
            context.visit(K_SELECT).sql(" *")
                   .formatSeparator()
                   .visit(K_FROM).sql(" (")
                   .formatIndentStart()
                   .formatNewLine();

        wrapQueryExpressionBodyInDerivedTable = false









            || wrapQueryExpressionBodyInDerivedTable(context, aliasOverride != null)

        // [#7459] In the presence of UNIONs and other set operations, the SEEK
        //         predicate must be applied on a derived table, not on the individual subqueries
            || applySeekOnDerivedTable;

        if (wrapQueryExpressionBodyInDerivedTable) {
            context.visit(K_SELECT).sql(' ');





            context.formatIndentStart()
                   .formatNewLine()
                   .sql("t.*");

            if (aliasOverride != null && aliasOverride.originalFields().size() < aliasOverride.aliasedFields().size())
                context.sql(", ")
                       .formatSeparator()
                       .declareFields(true, c -> c.visit(aliasOverride.aliasedFields().get(aliasOverride.aliasedFields().size() - 1)));

            context.formatIndentEnd()
                   .formatSeparator()
                   .visit(K_FROM).sql(" (")
                   .formatIndentStart()
                   .formatNewLine();
        }

        // [#1658] jOOQ applies left-associativity to set operators. In order to enforce that across
        // all databases, we need to wrap relevant subqueries in parentheses.
        if (unionOpSize > 0) {
            if (!TRUE.equals(context.data(DATA_NESTED_SET_OPERATIONS)))
                context.data(DATA_NESTED_SET_OPERATIONS, unionOpNesting = unionOpNesting());

            for (int i = unionOpSize - 1; i >= 0; i--) {
                switch (unionOp.get(i)) {
                    case EXCEPT:        context.start(SELECT_EXCEPT);        break;
                    case EXCEPT_ALL:    context.start(SELECT_EXCEPT_ALL);    break;
                    case INTERSECT:     context.start(SELECT_INTERSECT);     break;
                    case INTERSECT_ALL: context.start(SELECT_INTERSECT_ALL); break;
                    case UNION:         context.start(SELECT_UNION);         break;
                    case UNION_ALL:     context.start(SELECT_UNION_ALL);     break;
                }

                // [#3676] There might be cases where nested set operations do not
                //         imply required parentheses in some dialects, but better
                //         play safe than sorry
                unionParenthesis(
                    context,
                    '(',
                    aliasOverride != null ? aliasOverride.aliasedFields() : getSelect(),
                    derivedTableRequired(context, this),
                    unionParensRequired = unionOpNesting || unionParensRequired(context),
                    null
                );
            }
        }

        TableList tablelist = getFrom();
        traverseJoins(tablelist, t -> {
            if (t instanceof TableImpl || t instanceof TableAlias)
                context.scopeRegister(t, true);
        });
        ConditionProviderImpl where = new ConditionProviderImpl();

        if (TRUE.equals(context.data().get(BooleanDataKey.DATA_SELECT_NO_DATA)))
            where.addConditions(falseCondition());

        // [#14985] [#15755] Add skipped join segments from path joins
        tablelist = prependPathJoins(context, where, tablelist);

        if (with != null && transformInlineCTE(context.configuration())) {




        }

        for (Entry<QueryPart, QueryPart> entry : localQueryPartMapping.entrySet())
            context.scopeRegister(entry.getKey(), true, entry.getValue());

        // SELECT clause
        // -------------
        context.start(SELECT_SELECT)
               .visit(K_SELECT).separatorRequired(true);

        // [#1493] Oracle hints come directly after the SELECT keyword
        if (!StringUtils.isBlank(hint))
            context.sql(' ').sql(hint).separatorRequired(true);






















        if (Tools.isNotEmpty(distinctOn))
            context.visit(K_DISTINCT_ON).sql(" (").visit(distinctOn).sql(')').separatorRequired(true);
        else if (distinct)
            context.visit(K_DISTINCT).separatorRequired(true);






        if (Integer.valueOf(1).equals(context.data(DATA_RENDERING_DATA_CHANGE_DELTA_TABLE)))
            context.qualify(false);

        context.declareFields(true);

        // [#2335] When emulating LIMIT .. OFFSET, the SELECT clause needs to generate
        // non-ambiguous column names as ambiguous column names are not allowed in subqueries
        if (aliasOverride != null)
            if (wrapQueryExpressionBodyInDerivedTable && aliasOverride.originalFields().size() < aliasOverride.aliasedFields().size())
                context.visit(new SelectFieldList<>(aliasOverride.aliasedFields().subList(0, aliasOverride.originalFields().size())));
            else
                context.visit(new SelectFieldList<>(aliasOverride.aliasedFields()));

        // The default behaviour
        else
            context.visit(getSelectResolveUnsupportedAsterisks(context));

        if (Integer.valueOf(1).equals(context.data(DATA_RENDERING_DATA_CHANGE_DELTA_TABLE)))
            context.qualify(qualify);

        context.declareFields(false)
               .end(SELECT_SELECT);

        // INTO clauses
        // ------------
        // [#4910] This clause (and the Clause.SELECT_INTO signal) must be emitted
        //         only in top level SELECTs
        if (!context.subquery()) {
            context.start(SELECT_INTO);

            QueryPart actualIntoTable = (QueryPart) context.data(DATA_SELECT_INTO_TABLE);




            if (actualIntoTable == null)
                actualIntoTable = intoTable;


            if (actualIntoTable != null
                && !TRUE.equals(context.data(DATA_OMIT_INTO_CLAUSE))
                && (SUPPORT_SELECT_INTO_TABLE.contains(context.dialect()) || !(actualIntoTable instanceof Table))



            ) {
                context.formatSeparator()
                       .visit(K_INTO)
                       .sql(' ')
                       .visit(actualIntoTable);
            }










            context.end(SELECT_INTO);
        }

        // FROM and JOIN clauses
        // ---------------------
        where = getWhere(context, tablelist, where);

        context.start(SELECT_FROM)
               .declareTables(true);

        // [#....] Some SQL dialects do not require a FROM clause. Others do and
        //         jOOQ generates a "DUAL" table or something equivalent.
        //         See also org.jooq.impl.Dual for details.
        boolean hasFrom = !tablelist.isEmpty()
            || REQUIRES_FROM_CLAUSE.contains(context.dialect())


















            ;

        List<Condition> semiAntiJoinPredicates = null;

        if (hasFrom) {
            Object previousCollect = context.data(DATA_COLLECT_SEMI_ANTI_JOIN, true);
            Object previousCollected = context.data(DATA_COLLECTED_SEMI_ANTI_JOIN, null);











            tablelist = transformInlineDerivedTables(context, tablelist, where);

            context.formatSeparator()
                   .visit(K_FROM)
                   .separatorRequired(true)
                   .visit(tablelist);














            semiAntiJoinPredicates = (List<Condition>) context.data(DATA_COLLECTED_SEMI_ANTI_JOIN, previousCollected);
            context.data(DATA_COLLECT_SEMI_ANTI_JOIN, previousCollect);
        }

        context.declareTables(false)
               .end(SELECT_FROM);

        // WHERE clause
        // ------------
        context.start(SELECT_WHERE);

        if (where.hasWhere() || semiAntiJoinPredicates != null || TRUE.equals(context.data().get(BooleanDataKey.DATA_MANDATORY_WHERE_CLAUSE))) {
            ConditionProviderImpl actual = new ConditionProviderImpl();

            if (semiAntiJoinPredicates != null)
                actual.addConditions(semiAntiJoinPredicates);

            if (where.hasWhere())
                actual.addConditions(where.getWhere());

            context.formatSeparator()
                   .visit(K_WHERE)
                   .sql(' ')
                   .visit(actual);
        }

        context.end(SELECT_WHERE);


































        // GROUP BY and HAVING clause
        // --------------------------
        context.start(SELECT_GROUP_BY);

        ConditionProviderImpl having0 = getHaving(context);
        if (!getGroupBy().isEmpty() || having0.hasWhere() && NO_IMPLICIT_GROUP_BY_ON_HAVING.contains(context.dialect())) {
            context.formatSeparator()
                   .visit(K_GROUP_BY);

            if (groupByDistinct)
                context.sql(' ').visit(K_DISTINCT);

            GroupFieldList g = groupBy;


















            context.separatorRequired(true).visit(g);
        }

        context.end(SELECT_GROUP_BY);

        // HAVING clause
        // -------------
        context.start(SELECT_HAVING);

        if (having0.hasWhere())
            context.formatSeparator()
                   .visit(K_HAVING)
                   .sql(' ')
                   .visit(having0);

        context.end(SELECT_HAVING);

        // WINDOW clause
        // -------------



        acceptWindow(context);

        // QUALIFY clause
        // -------------

        if (additionalQualify != null || getQualify().hasWhere())
            context.formatSeparator()
                   .visit(K_QUALIFY)
                   .sql(' ')

                   // [#15188] Callers ensure that the two QUALIFY conditions are mutually exclusive
                   .visit(additionalQualify != null ? additionalQualify : getQualify().getWhere());






        // ORDER BY clause for local subselect
        // -----------------------------------
        toSQLOrderBy(
            context, aliasOverride,
            false, wrapQueryExpressionBodyInDerivedTable,
            false, orderBy, limit
        );

        // SET operations like UNION, EXCEPT, INTERSECT
        // --------------------------------------------
        if (unionOpSize > 0) {
            unionParenthesis(context, ')', null, derivedTableRequired(context, this), unionParensRequired, null);

            for (int i = 0; i < unionOpSize; i++) {
                CombineOperator op = unionOp.get(i);

                for (Select<?> other : union.get(i)) {
                    boolean derivedTableRequired = derivedTableRequired(context, other);
                    boolean otherUnionParensRequired = unionParensRequired || unionOpNesting();

                    context.formatSeparator()
                           .visit(op.toKeyword(family));

                    if (otherUnionParensRequired)
                        context.sql(' ');
                    else
                        context.formatSeparator();

                    unionParenthesis(context, '(', other.getSelect(), derivedTableRequired, otherUnionParensRequired, other);
                    context.visit(other);
                    unionParenthesis(context, ')', null, derivedTableRequired, otherUnionParensRequired, null);
                }

                // [#1658] Close parentheses opened previously
                if (i < unionOpSize - 1)
                    unionParenthesis(context, ')', null, derivedTableRequired(context, this), unionParensRequired, null);

                switch (unionOp.get(i)) {
                    case EXCEPT:        context.end(SELECT_EXCEPT);        break;
                    case EXCEPT_ALL:    context.end(SELECT_EXCEPT_ALL);    break;
                    case INTERSECT:     context.end(SELECT_INTERSECT);     break;
                    case INTERSECT_ALL: context.end(SELECT_INTERSECT_ALL); break;
                    case UNION:         context.end(SELECT_UNION);         break;
                    case UNION_ALL:     context.end(SELECT_UNION_ALL);     break;
                }
            }

            if (unionOpNesting)
                context.data().remove(DATA_NESTED_SET_OPERATIONS);
        }

        if (wrapQueryExpressionBodyInDerivedTable) {
            context.formatIndentEnd()
                   .formatNewLine()
                   .sql(") t");

            if (applySeekOnDerivedTable) {
                context.formatSeparator()
                       .visit(K_WHERE)
                       .sql(' ')
                       .qualify(false, c -> c.visit(getSeekCondition(context)));
            }
        }

        // ORDER BY clause for UNION
        // -------------------------
        context.qualify(false, c -> toSQLOrderBy(
            context, aliasOverride,
            wrapQueryExpressionInDerivedTable, wrapQueryExpressionBodyInDerivedTable,
            true, unionOrderBy, unionLimit
        ));
    }

    private void acceptWindow(Context<?> context) {
        context.start(SELECT_WINDOW);

        if (Tools.isNotEmpty(window) && !NO_SUPPORT_WINDOW_CLAUSE.contains(context.dialect()))
            context.formatSeparator()
                   .visit(K_WINDOW)
                   .separatorRequired(true)
                   .declareWindows(true, c -> c.visit(window));

        context.end(SELECT_WINDOW);
    }














































































































    static final TableList prependPathJoins(Context<?> ctx, ConditionProviderImpl where, TableList tablelist) {
        TableList result = new TableList(tablelist);

        for (int i = 0; i < result.size(); i++) {
            Table<?> t0 = result.get(i);
            Table<?> t1 = prependPathJoins(ctx, where, t0, CROSS_JOIN);

            if (t0 != t1)
                result.set(i, t1);
        }

        return result;
    }

    private static final Table<?> prependPathJoins(Context<?> ctx, ConditionProviderImpl where, Table<?> t, JoinType joinType) {
        if (t instanceof TableImpl<?> ti)
            return prependPathJoins(ctx, where, ti, joinType);
        else if (t instanceof JoinTable<?> j)
            return prependPathJoins(ctx, where, j);
        else
            return t;
    }

    private static final JoinTable<?> prependPathJoins(Context<?> ctx, ConditionProviderImpl where, JoinTable<?> j) {
        if (j.type.qualified())
            where = new ConditionProviderImpl();

        Table<?> lhs0 = j.lhs;
        Table<?> rhs0 = j.rhs;
        Table<?> lhs1 = lhs0;
        Table<?> rhs1 = rhs0;

        // [#13639] [#14985] [#15755] If path segments are skipped, create them recursively,
        // in a left associative way, as the user would have created them, e.g.
        // A ⋈ A.B.C.D -> ((A ⋈ A.B) ⋈ A.B.C) ⋈ A.B.C.D
        if (rhs0 instanceof TableImpl<?> ti) {
            if (ti.path != null && !ctx.inScope(ti.path)) {
                lhs1 = j.transform(lhs0, ti.path);
                ctx.scopeRegister(ti.path, true);
            }
        }

        lhs1 = prependPathJoins(ctx, where, lhs1, j.type);
        rhs1 = prependPathJoins(ctx, where, rhs1, j.type);

        if (lhs0 != lhs1 || rhs0 != rhs1)
            j = j.transform(lhs1, rhs1);

        if (j.type.qualified() && where.hasWhere())
            j.condition.addConditions(where.getWhere());

        return j;
    }

    private static final Table<?> prependPathJoins(Context<?> ctx, ConditionProviderImpl where, TableImpl<?> ti, JoinType joinType) {
        Table<?> r = ti;

        if (ti.path != null && !ctx.inScope(ti.path)) {
            Table<?> curr = ti;
            Table<?> next = curr;
            List<Table<?>> tables = new ArrayList<>();
            while ((next = TableImpl.path(curr)) != null) {
                tables.add(curr);

                // [#14985] [#15967] In some edge cases, users may have chosen to use a path in the FROM
                //                   clause, whose path root isn't in scope. We mustn't put it in scope here!
                if (!ctx.inScope(curr))
                    ctx.scopeRegister(curr, true);

                if (ctx.inScope(next))
                    break;

                curr = next;
            }

            Table<?> result = null;
            for (int i = tables.size() - 1; i >= 0; i--)
                result = result == null ? tables.get(i) : result.join(tables.get(i), joinType);

            return result;
        }

        return r;
    }














































































































































































































































































































































































































    @SuppressWarnings("unchecked")
    private final void toSQLOrderBy(
        final Context<?> ctx,
        final AliasOverride aliasOverride,
        final boolean wrapQueryExpressionInDerivedTable,
        final boolean wrapQueryExpressionBodyInDerivedTable,
        final boolean isUnionOrderBy,
        QueryPartListView<SortField<?>> actualOrderBy,
        Limit actualLimit
    ) {

        ctx.start(SELECT_ORDER_BY);

        // [#6197] When emulating WITH TIES using RANK() in a subquery, we must avoid rendering the
        //         subquery's ORDER BY clause
        if (!actualLimit.withTies()
            // Dialects with native support
            || SUPPORT_FULL_WITH_TIES.contains(ctx.dialect())





        ) {
            if (!actualOrderBy.isEmpty()) {
                ctx.formatSeparator()
                   .visit(K_ORDER);






                ctx.sql(' ').visit(K_BY).separatorRequired(true);

                // [#11904] Shift field indexes in ORDER BY <field index>, in
                //          case we are projecting emulated nested records of some sort
                if (RowAsField.NO_NATIVE_SUPPORT.contains(ctx.dialect())
                    && Tools.findAny(actualOrderBy, s -> s.$field() instanceof Val) != null) {
                    SelectFieldIndexes s = getSelectFieldIndexes(ctx);

                    if (s.mapped) {
                        actualOrderBy = new QueryPartListView<>(actualOrderBy).map(t1 -> {
                            Field<?> in = t1.$field();

                            if (in instanceof Val && in.getDataType().isNumeric()) {
                                Val<?> val = (Val<?>) in;
                                int x = Convert.convert(val.getValue(), int.class) - 1;
                                int mapped = s.mapping[x];
                                Field<?> out = s.projectionSizes[x] == 1
                                    ? val.copy(mapped + 1)
                                    : DSL.field("{0}", DSL.list(range(mapped, mapped + s.projectionSizes[mapped])
                                        .mapToObj(i -> val.copy(i + 1))
                                        .toArray(SelectField<?>[]::new)
                                    ));
                                return t1.$field(out);
                            }
                            else
                                return t1;
                        });
                    }
                }

                actualOrderBy = new QueryPartListView<>(actualOrderBy).map(t1 -> {
                    Field<?> out = getResolveProjection(ctx, t1.$field());
                    return t1.$field() == out ? t1 : t1.$field(out);
                });

                // [#2080] DB2, Oracle, and Sybase can deal with column aliases from the SELECT clause
                // but in case the aliases have been overridden to emulate OFFSET pagination, the
                // overrides must also apply to the ORDER BY clause
                // [#16928] [#17676] This is also required in some dialects, but must not be done in others,
                // e.g. in case there are expressions in ORDER BY (such as in PostgreSQL)
                if (aliasOverride != null && REQUIRES_ORDER_BY_ALIAS_OVERRIDE.contains(ctx.dialect())) {
                    QueryPart o = actualOrderBy;

                    ctx.qualify(
                        !wrapQueryExpressionBodyInDerivedTable && ctx.qualify(),
                        c1 -> c1.data(DATA_OVERRIDE_ALIASES_IN_ORDER_BY, aliasOverride, c2 -> c2.visit(o))
                    );
                }
                else {
                    if (NO_SUPPORT_UNION_ORDER_BY_ALIAS.contains(ctx.dialect()) && hasUnions() && isUnionOrderBy) {
                        List<String> n = map(getSelect(), Field::getName);

                        actualOrderBy = new QueryPartListView<>(actualOrderBy).map(t1 -> {
                            int i = n.indexOf(t1.$field().getName());
                            return i >= 0 ? t1.$field(inline(i + 1)) : t1;
                        });
                    }

























                    ctx.visit(actualOrderBy);
                }
            }

















        }

        ctx.end(SELECT_ORDER_BY);

        if (wrapQueryExpressionInDerivedTable)
            ctx.formatIndentEnd()
               .formatNewLine()
               .sql(") x");

        if (TRUE.equals(ctx.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE))



        ) {
            if (actualLimit.isApplicable()) {
                if (!(actualLimit.limitZero() && NO_SUPPORT_LIMIT_ZERO.contains(ctx.dialect())))
                    ctx.visit(actualLimit);
            }

            // [#13509] Force a LIMIT clause to prevent optimisation of "unnecessary" ORDER BY
            // [#13533] A variant of this are dialects where ORDER BY in subqueries is only syntactically valid with LIMIT
            else if (!actualOrderBy.isEmpty()
                && (
                    TRUE.equals(ctx.data(DATA_FORCE_LIMIT_WITH_ORDER_BY))



                )
            ) {
                Limit l = new Limit();

                switch (ctx.family()) {
                    case TRINO:
                        l.setLimit(inline(Integer.MAX_VALUE));
                        break;
                    default:
                        l.setLimit(inline(Long.MAX_VALUE));
                        break;
                }

                ctx.visit(l);
            }
        }
    }

    private final boolean applySeekOnDerivedTable() {
        return !getSeek().isEmpty() && !getOrderBy().isEmpty() && !unionOp.isEmpty();
    }

    private final boolean wrapQueryExpressionBodyInDerivedTable(Context<?> ctx, boolean hasAlternativeFields) {

        // [#2059] [#7539] Some dialects require query in derived table when using ORDER BY
        return !unionOp.isEmpty() && (

            // [#16928] "Alternative fields" such as ROW_NUMBER() calculations only work with UNIONs when the
            //          UNION is nested.
            hasAlternativeFields
            || WRAP_EXP_BODY_IN_DERIVED_TABLE_LIMIT.contains(ctx.dialect()) && getLimit().isApplicable()








            || WRAP_EXP_BODY_IN_DERIVED_TABLE_ORDER_BY.contains(ctx.dialect()) && !getOrderBy().isEmpty()
        )
            // [#15189] Window functions calculated in "alternative fields" must be calculated
            //          *after* qualify filtering has been done
            || hasAlternativeFields && qualify.hasWhere();
    }







































































    private static final Set<SQLDialect> NO_SUPPORT_UNION_PARENTHESES = SQLDialect.supportedBy(SQLITE);
    private static final Set<SQLDialect> NO_SUPPORT_CTE_IN_UNION      = SQLDialect.supportedBy(HSQLDB, MARIADB, TRINO);
    private static final Set<SQLDialect> UNION_PARENTHESIS            = SQLDialect.supportedUntil(DERBY);

    final boolean hasUnions() {
        return !unionOp.isEmpty();
    }

    private final boolean unionOpNesting() {
        if (unionOp.size() > 1)
            return true;

        SelectQueryImpl<?> s;
        for (QueryPartList<Select<?>> s1 : union)
            for (Select<?> s2 : s1)
                if ((s = selectQueryImpl(s2)) != null && !s.unionOp.isEmpty())
                    return true;

        return false;
    }

    private final boolean derivedTableRequired(Context<?> context, Select<?> s1) {
        SelectQueryImpl<?> s;

        // [#10711] Some derived tables are needed if dialects don't support CTE in union subqueries
        return NO_SUPPORT_CTE_IN_UNION.contains(context.dialect())
                    && (s = selectQueryImpl(s1)) != null
                    && s.with != null
            || WRAP_UNION_SUBQ_IN_DERIVED_TABLE_LIMIT.contains(context.dialect())
                    && (s = selectQueryImpl(s1)) != null
                    && (s.limit.isApplicable() || !s.orderBy.isEmpty());
    }

    private final boolean unionParensRequired(Context<?> context) {
        if (unionOp.isEmpty())
            return false;

        if (unionParensRequired(this) || context.settings().isRenderParenthesisAroundSetOperationQueries())
            return true;

        CombineOperator op = unionOp.get(0);

        // [#3676] EXCEPT and EXCEPT ALL are not associative
        if ((op == EXCEPT || op == EXCEPT_ALL) && union.get(0).size() > 1)
            return true;

        // [#3676] if a query has an ORDER BY or LIMIT clause parens are required
        SelectQueryImpl<?> s;
        for (QueryPartList<Select<?>> s1 : union)
            for (Select<?> s2 : s1)
                if ((s = selectQueryImpl(s2)) != null && unionParensRequired(s))
                    return true;

        return false;
    }

    private final boolean unionParensRequired(SelectQueryImpl<?> s) {
        return s.orderBy.size() > 0 || s.limit.isApplicable() || s.with != null;
    }

    private final void unionParenthesis(
        Context<?> ctx,
        char parenthesis,
        List<Field<?>> fields,
        boolean derivedTableRequired,
        boolean parensRequired,
        QueryPart subquery
    ) {
        if ('(' == parenthesis)
            ((AbstractContext<?>) ctx).subquery0(true, true, subquery);
        else if (')' == parenthesis)
            ((AbstractContext<?>) ctx).subquery0(false, true, null);

        derivedTableRequired |= derivedTableRequired

            // [#3579] [#6431] [#7222] [#11582]
            // Some databases don't support nested set operations at all because
            // they do not allow wrapping set op subqueries in parentheses
            || parensRequired && NO_SUPPORT_UNION_PARENTHESES.contains(ctx.dialect())

            // [#3579] [#6431] [#7222]
            // Nested set operations aren't supported, but parenthesised set op
            // subqueries are.
            || (TRUE.equals(ctx.data(DATA_NESTED_SET_OPERATIONS)) && UNION_PARENTHESIS.contains(ctx.dialect()))

            // [#2995] Ambiguity may need to be resolved when parentheses could mean both:
            //         Set op subqueries or insert column lists
            || TRUE.equals(ctx.data(DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST))
            ;

        parensRequired |= derivedTableRequired;

        if (parensRequired && ')' == parenthesis) {
            ctx.formatIndentEnd()
               .formatNewLine();
        }

        // [#3579] Nested set operators aren't supported in some databases. Emulate them via derived tables...
        // [#7222] Do this only in the presence of actual nested set operators
        else if (parensRequired && '(' == parenthesis) {
            if (derivedTableRequired) {
                ctx.formatNewLine()
                   .visit(K_SELECT).sql(' ');

                // [#7222] Workaround for https://issues.apache.org/jira/browse/DERBY-6983
                if (ctx.family() == DERBY)
                    ctx.visit(new SelectFieldList<>(Tools.<Field<?>, Field<?>, RuntimeException>map(fields, f -> Tools.unqualified(f))));
                else
                    ctx.sql('*');


                ctx.formatSeparator()
                   .visit(K_FROM)
                   .sql(' ');
            }
        }

        // [#3579] ... but don't use derived tables to emulate nested set operators for Firebird, as that
        // only causes many more issues in various contexts where they are not allowed:
        // - Recursive CTE
        // - INSERT SELECT
        // - Derived tables with undefined column names (see also [#3679])

        switch (ctx.family()) {
            case FIREBIRD:
                if (derivedTableRequired)
                    ctx.sql(parenthesis);

                break;

            default:
                if (parensRequired)
                    ctx.sql(parenthesis);

                break;
        }

        if (parensRequired && '(' == parenthesis) {
            ctx.formatIndentStart()
               .formatNewLine();
        }

        else if (parensRequired && ')' == parenthesis) {
            if (derivedTableRequired)
                ctx.sql(" x");
        }
    }

    @Override
    public final void addSelect(Collection<? extends SelectFieldOrAsterisk> fields) {
        getSelectAsSpecified().addAll(fields);
    }

    @Override
    public final void addSelect(SelectFieldOrAsterisk... fields) {
        addSelect(Arrays.asList(fields));
    }

    @Override
    public final void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public final void addDistinctOn(SelectFieldOrAsterisk... fields) {
        addDistinctOn(Arrays.asList(fields));
    }

    @Override
    public final void addDistinctOn(Collection<? extends SelectFieldOrAsterisk> fields) {
        distinctOn.addAll(fields);
    }

    @Override
    public final void setInto(Table<?> table) {
        this.intoTable = table;
    }










    @Override
    public final void addOffset(Number offset) {
        getLimit().setOffset(offset);
    }

    @Override
    public final void addOffset(Field<? extends Number> offset) {
        getLimit().setOffset(offset);
    }

    @Override
    public final void addLimit(Number l) {
        getLimit().setLimit(l);
    }

    @Override
    public final void addLimit(Field<? extends Number> l) {
        getLimit().setLimit(l);
    }

    @Override
    public final void addLimit(Number offset, Number l) {
        getLimit().setOffset(offset);
        getLimit().setLimit(l);
    }

    @Override
    public final void addLimit(Number offset, Field<? extends Number> l) {
        getLimit().setOffset(offset);
        getLimit().setLimit(l);
    }

    @Override
    public final void addLimit(Field<? extends Number> offset, Number l) {
        getLimit().setOffset(offset);
        getLimit().setLimit(l);
    }

    @Override
    public final void addLimit(Field<? extends Number> offset, Field<? extends Number> l) {
        getLimit().setOffset(offset);
        getLimit().setLimit(l);
    }

    @Override
    public final void setLimitPercent(boolean percent) {
        getLimit().setPercent(percent);
    }

    @Override
    public final void setWithTies(boolean withTies) {
        getLimit().setWithTies(withTies);
    }

    final ForLock forLock() {
        if (forLock == null)
            forLock = new ForLock();

        return forLock;
    }

    @Override
    public final void setForUpdate(boolean forUpdate) {
        if (forUpdate)
            forLock().forLockMode = ForLockMode.UPDATE;
        else
            forLock = null;
    }

    @Override
    public final void setForNoKeyUpdate(boolean forNoKeyUpdate) {
        if (forNoKeyUpdate)
            forLock().forLockMode = ForLockMode.NO_KEY_UPDATE;
        else
            forLock = null;
    }

    @Override
    public final void setForKeyShare(boolean forKeyShare) {
        if (forKeyShare)
            forLock().forLockMode = ForLockMode.KEY_SHARE;
        else
            forLock = null;
    }

    @Override
    public final void setForUpdateOf(Field<?>... fields) {
        setForLockModeOf(fields);
    }

    @Override
    public final void setForUpdateOf(Collection<? extends Field<?>> fields) {
        setForLockModeOf(fields);
    }

    @Override
    public final void setForUpdateOf(Table<?>... tables) {
        setForLockModeOf(tables);
    }

    @Override
    public final void setForUpdateWait(int seconds) {
        setForLockModeWait(seconds);
    }

    @Override
    public final void setForUpdateNoWait() {
        setForLockModeNoWait();
    }

    @Override
    public final void setForUpdateSkipLocked() {
        setForLockModeSkipLocked();
    }

    @Override
    public final void setForShare(boolean forShare) {
        if (forShare)
            forLock().forLockMode = ForLockMode.SHARE;
        else
            forLock = null;
    }

    @Override
    public final void setForLockModeOf(Field<?>... fields) {
        setForLockModeOf(Arrays.asList(fields));
    }

    @Override
    public final void setForLockModeOf(Collection<? extends Field<?>> fields) {
        initLockMode();
        forLock().forLockOf = new QueryPartList<>(fields);
        forLock().forLockOfTables = null;
    }

    @Override
    public final void setForLockModeOf(Table<?>... tables) {
        initLockMode();
        forLock().forLockOf = null;
        forLock().forLockOfTables = new TableList(Arrays.asList(tables));
    }

    @Override
    public final void setForLockModeWait(int seconds) {
        initLockMode();
        forLock().forLockWaitMode = ForLockWaitMode.WAIT;
        forLock().forLockWait = seconds;
    }

    @Override
    public final void setForLockModeNoWait() {
        initLockMode();
        forLock().forLockWaitMode = ForLockWaitMode.NOWAIT;
        forLock().forLockWait = 0;
    }

    @Override
    public final void setForLockModeSkipLocked() {
        initLockMode();
        forLock().forLockWaitMode = ForLockWaitMode.SKIP_LOCKED;
        forLock().forLockWait = 0;
    }

    private final void initLockMode() {
        forLock().forLockMode = forLock().forLockMode == null ? ForLockMode.UPDATE : forLock().forLockMode;
    }



































































































































    @Override
    public final void setWithCheckOption() {
        this.withCheckOption = true;
        this.withReadOnly = false;
    }

    @Override
    public final void setWithReadOnly() {
        this.withCheckOption = false;
        this.withReadOnly = true;
    }

    @Override
    public final List<Field<?>> getSelect() {
        return getSelectResolveAllAsterisks(Tools.configuration(configuration()).dsl());
    }

    private static final Collection<? extends Field<?>> subtract(List<? extends Field<?>> left, List<? extends Field<?>> right) {

        // [#7921] TODO Make this functionality more generally reusable
        FieldsImpl<?> e = new FieldsImpl<>(right);
        List<Field<?>> result = new ArrayList<>();

        for (Field<?> f : left)
            if (e.field(f) == null)
                result.add(f);

        return result;
    }

    /**
     * The select list as specified by the API user.
     */
    final SelectFieldList<SelectFieldOrAsterisk> getSelectAsSpecified() {
        return select;
    }

    /**
     * The select list with resolved implicit asterisks.
     */
    final SelectFieldList<SelectFieldOrAsterisk> getSelectResolveImplicitAsterisks() {
        if (getSelectAsSpecified().isEmpty())
            return resolveAsterisk(new SelectFieldList<SelectFieldOrAsterisk>());

        return getSelectAsSpecified();
    }

    /**
     * The select list with resolved explicit asterisks (if they contain the
     * except clause and that is not supported).
     */
    final SelectFieldList<SelectFieldOrAsterisk> getSelectResolveUnsupportedAsterisks(Context<?> ctx) {
        return getSelectResolveSomeAsterisks0(ctx, false);
    }

    /**
     * The select list with resolved explicit asterisks (if they contain the
     * except clause and that is not supported).
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    final SelectFieldList<Field<?>> getSelectResolveAllAsterisks(Scope ctx) {
        return (SelectFieldList) getSelectResolveSomeAsterisks0(ctx, true);
    }

    private final SelectFieldList<SelectFieldOrAsterisk> getSelectResolveSomeAsterisks0(Scope ctx, boolean resolveSupported) {
        SelectFieldList<SelectFieldOrAsterisk> result = new SelectFieldList<>();

        boolean knownTableSource = knownTableSource();

        // [#7921] Only H2 supports the * EXCEPT (..) syntax
        boolean resolveExcept = resolveSupported || knownTableSource && NO_SUPPORT_NATIVE_EXCEPT.contains(ctx.dialect());
        boolean resolveUnqualifiedCombined = resolveSupported || knownTableSource && NO_SUPPORT_UNQUALIFIED_COMBINED.contains(ctx.dialect());

        // [#7921] TODO Find a better, more efficient way to resolve asterisks
        SelectFieldList<SelectFieldOrAsterisk> list = getSelectResolveImplicitAsterisks();

        int size = 0;
        for (SelectFieldOrAsterisk s : list) {
            appendResolveSomeAsterisks0(ctx,
                resolveSupported,
                result,
                resolveExcept,
                resolveUnqualifiedCombined,
                list,
                s
            );

            // [#7841] Each iteration must contribute new fields to the result.
            //         Otherwise, we couldn't resolve an asterisk, and must fall
            //         back to determining fields from the ResultSetMetaData
            if (resolveSupported && size == result.size())
                return new SelectFieldList<>();
            else
                size = result.size();
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private final void appendResolveSomeAsterisks0(
        Scope ctx,
        boolean resolveSupported,
        SelectFieldList<SelectFieldOrAsterisk> result,
        boolean resolveExcept,
        boolean resolveUnqualifiedCombined,
        SelectFieldList<SelectFieldOrAsterisk> list,
        SelectFieldOrAsterisk s
    ) {
        if (s instanceof Field<?> f) {
            result.add(getResolveProjection(ctx, f));
        }
        else if (s instanceof QualifiedAsterisk q) {

            // [#9743] Split join table asterisks
            if (q.qualifier() instanceof QOM.JoinTable<?, ?> j) {
                appendResolveSomeAsterisks0(ctx, resolveSupported, result, resolveExcept, resolveUnqualifiedCombined, list, j.$table1().asterisk());
                appendResolveSomeAsterisks0(ctx, resolveSupported, result, resolveExcept, resolveUnqualifiedCombined, list, j.$table2().asterisk());
            }
            else if (q.$except().isEmpty()) {
                if (resolveSupported
        
                )
                    result.addAll(asList(q.qualifier().fields()));
                else
                    result.add(s);
            }
            else if (resolveExcept
        
            )
                result.addAll(subtract(asList(q.qualifier().fields()), q.$except()));
            else
                result.add(s);
        }
        else if (s instanceof Asterisk a) {
            if (a.$except().isEmpty()) {
                if (resolveSupported
                    || resolveUnqualifiedCombined && list.size() > 1
        
                )
                    result.addAll(resolveAsterisk(new QueryPartList<>()));
                else
                    result.add(s);
            }
            else if (resolveExcept
        
            )
                result.addAll(resolveAsterisk(new QueryPartList<>(), (QueryPartListView<Field<?>>) a.$except()));
            else
                result.add(s);
        }
        else if (s instanceof Row r)
            result.add(getResolveProjection(ctx, new RowAsField<Row, Record>(r)));
        else if (s instanceof Table<?> t)
            result.add(getResolveProjection(ctx, new TableAsField<>(t)));
        else
            throw new AssertionError("Type not supported: " + s);
    }

    private final Field<?> getResolveProjection(Scope ctx, Field<?> f) {












































        return f;
    }

    private final <Q extends QueryPartList<? super Field<?>>> Q resolveAsterisk(Q result) {
        return resolveAsterisk(result, null);
    }

    private final <Q extends QueryPartList<? super Field<?>>> Q resolveAsterisk(Q result, QueryPartCollectionView<? extends Field<?>> except) {
        FieldsImpl<?> e = except == null ? null : new FieldsImpl<>(except);

        // [#109] [#489] [#7231]: SELECT * is only applied when at least one
        // table from the table source is "unknown", i.e. not generated from a
        // physical table. Otherwise, the fields are selected explicitly
        if (knownTableSource())
            if (e == null)
                for (TableLike<?> table : getFrom())
                    for (Field<?> field : table.asTable().fields())
                        result.add(field);
            else
                for (TableLike<?> table : getFrom())
                    for (Field<?> field : table.asTable().fields())
                        if (e.field(field) == null)
                            result.add(field);

        // The default is SELECT 1, when projections and table sources are
        // both empty
        if (getFrom().isEmpty())
            result.add(one());

        return result;
    }

    private static record SelectFieldIndexes(boolean mapped, int[] mapping, int[] projectionSizes) {}

    /**
     * [#11904] Get a mapping { projected field index -> generated field index }
     */
    private final SelectFieldIndexes getSelectFieldIndexes(Context<?> ctx) {
        List<Field<?>> s = getSelect();
        boolean mapped = false;
        int[] mapping = new int[s.size()];
        int[] projectionSizes = new int[s.size()];

        if (RowAsField.NO_NATIVE_SUPPORT.contains(ctx.dialect())) {
            for (int i = 0; i < mapping.length; i++) {
                projectionSizes[i] = ((AbstractField<?>) s.get(i)).projectionSize();
                mapped |= projectionSizes[i] > 1;

                if (i < mapping.length - 1)
                    mapping[i + 1] = mapping[i] + projectionSizes[i];
            }
        }
        else
            for (int i = 0; i < mapping.length; i++)
                mapping[i] = i;

        return new SelectFieldIndexes(mapped, mapping, projectionSizes);
    }

    private final boolean knownTableSource() {
        return traverseJoins(
            getFrom(),
            true,
            r -> !r,
            null,

            // [#12328] Don't recurse into the RHS if the join does not affect the projection
            j -> j.type != JoinType.LEFT_ANTI_JOIN && j.type != JoinType.LEFT_SEMI_JOIN,
            null,

            // TODO: PostgreSQL supports tables without columns, see e.g.
            // https://blog.jooq.org/creating-tables-dum-and-dee-in-postgresql/
            (r, t) -> r && t.fieldsRow().size() > 0
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    final Table<? extends R> getTable0() {
        return getFrom().size() == 1 && TableRecord.class.isAssignableFrom(getRecordType0())
             ? (Table<? extends R>) getFrom().get(0)
             : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    final Class<? extends R> getRecordType0() {
        // Generated record classes only come into play, when the select is
        // - on a single table
        // - a select *

        if (getFrom().size() == 1 && getSelectAsSpecified().isEmpty())
            return (Class<? extends R>) getFrom().get(0).asTable().getRecordType();

        // [#4695] [#11521] Calculate the correct Record[B] type
        else
            return (Class<? extends R>) recordType(getSelect().size());
    }

    final TableList getFrom() {
        return from;
    }

    /**
     * @deprecated - [#15871] don't reuse this, cache where clause instead.
     */
    @Deprecated
    final ConditionProviderImpl getWhere(Context<?> ctx, TableList tablelist) {
        return getWhere(ctx, tablelist, new ConditionProviderImpl());
    }

    final ConditionProviderImpl getWhere(Context<?> ctx, TableList tablelist, ConditionProviderImpl where0) {
        if (condition.hasWhere())
            where0.addConditions(condition.getWhere());

        // Apply SEEK predicates in the WHERE clause only if:
        // - There is an ORDER BY clause (SEEK is non-deterministic)
        // - There is a SEEK clause (obvious case)
        // - There are no unions (union is nested in derived table
        //   and SEEK predicate is applied outside). See [#7459]
        //   [#15820] We're not grouping
        if (!isGrouping() && !getOrderBy().isEmpty() && !getSeek().isEmpty() && unionOp.isEmpty())
            where0.addConditions(getSeekCondition(ctx));

        addPathConditions(ctx, where0, tablelist);







        if (NO_SUPPORT_LIMIT_ZERO.contains(ctx.dialect()) && limit.limitZero() && !isGrouping())
            where0.addConditions(falseCondition());

        return where0;
    }

    static final void addPathConditions(Context<?> ctx, ConditionProviderImpl where0, TableList tablelist) {

        // [#15936] Avoid duplicate conditions produced by join tree and path correlation
        //          TODO: This de-duplication shouldn't be necessary: find the cause.
        Set<Condition> set = new LinkedHashSet<>();

        // [#13639] Add JOIN predicates generated by path expressions in FROM clause.
        for (Table<?> t : tablelist)
            addPathConditions(ctx, set, t);

        traverseJoins(
            tablelist, set, null,
            t -> {
                if (t instanceof CrossJoin j) {
                    addPathConditions(ctx, set, j.lhs);
                    addPathConditions(ctx, set, j.rhs);
                }
                return true;
            },
            null, null,

            // [#15936] If join tree leaves contain paths, generate the path correlation predicate here
            // [#15967] But only if they're in scope, and not in the current scope, in a subquery
            !ctx.subquery() ? null :
            (w, t) -> {
                addPathConditions(ctx, w, t, ti -> !ctx.inCurrentScope(ti.path));
                return w;
            }
        );

        where0.addConditions(set);
    }

    private static final void addPathConditions(Context<?> ctx, Collection<Condition> result, Table<?> t) {
        addPathConditions(ctx, result, t, null);
    }

    private static final void addPathConditions(Context<?> ctx, Collection<Condition> result, Table<?> t, Predicate<? super TableImpl<?>> predicate) {
        if (t instanceof TableImpl<?> ti) {

            // [#14985] [#15967] In some edge cases, users may have chosen to use a path in the FROM
            //                   clause, whose path root isn't in scope. In that case, we must ignore
            //                   the path.
            if (ti.path != null && ctx.inScope(ti.path) && (predicate == null || predicate.test(ti)))
                result.add(ti.pathCondition());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    final Condition getSeekCondition(Context<?> ctx) {
        SortFieldList o = getOrderBy();
        Condition c = null;
        QueryPartList<Field<?>> s = getSeek();

        // [#2786] TODO: Check if NULLS FIRST | NULLS LAST clauses are
        // contained in the SortFieldList, in case of which, the below
        // predicates will become a lot more complicated.
        if (o.nulls()) {}

        // If we have uniform sorting, more efficient row value expression
        // predicates can be applied, which can be heavily optimised on some
        // databases.
        if (o.size() > 1 && o.uniform() && !FALSE.equals(ctx.settings().isRenderRowConditionForSeekClause())) {
            List<Field<?>> l = o.fields();
            List<Field<?>> r = s;

            // [#14395] Exclude NoField markers
            if (anyMatch(r, e -> e instanceof NoField)) {
                l = new ArrayList<>(l);
                r = new ArrayList<>(r);

                Iterator<Field<?>> lit = l.iterator();
                Iterator<Field<?>> rit = r.iterator();

                while (lit.hasNext() && rit.hasNext()) {
                    lit.next();

                    if (rit.next() instanceof NoField) {
                        lit.remove();
                        rit.remove();
                    }
                }
            }

            if (l.isEmpty())
                c = noCondition();
            else if (o.get(0).getOrder() != DESC ^ seekBefore)
                if (l.size() == 1)
                    c = l.get(0).gt((Field) r.get(0));
                else
                    c = row(l).gt(row(r));
            else
                if (l.size() == 1)
                    c = l.get(0).lt((Field) r.get(0));
                else
                    c = row(l).lt(row(r));
        }

        // With alternating sorting, the SEEK clause has to be explicitly
        // phrased for each ORDER BY field.
        else {
            ConditionProviderImpl or = new ConditionProviderImpl();

            for (int i = 0; i < o.size(); i++) {
                if (s.get(i) instanceof NoField)
                    continue;

                ConditionProviderImpl and = new ConditionProviderImpl();

                for (int j = 0; j < i; j++)
                    if (!(s.get(j) instanceof NoField))
                        and.addConditions(((Field) o.get(j).$field()).eq(s.get(j)));

                SortField<?> sf = o.get(i);
                if (sf.getOrder() != DESC ^ seekBefore)
                    and.addConditions(((Field) sf.$field()).gt(s.get(i)));
                else
                    and.addConditions(((Field) sf.$field()).lt(s.get(i)));

                or.addConditions(OR, and);
            }

            c = or.getWhere();
        }

        if (o.size() > 1 && TRUE.equals(ctx.settings().isRenderRedundantConditionForSeekClause())) {
            if (o.get(0).getOrder() != DESC ^ seekBefore)
                c = ((Field) o.get(0).$field()).ge(s.get(0)).and(c);
            else
                c = ((Field) o.get(0).$field()).le(s.get(0)).and(c);
        }

        return c;
    }















    final boolean isGrouping() {

        // [#15820] TODO: The presence of any aggregate function also implies at least GROUP BY ()
        return !groupBy.isEmpty() || having.hasWhere();
    }

    final GroupFieldList getGroupBy() {
        return groupBy;
    }

    final ConditionProviderImpl getHaving(Context<?> ctx) {
        ConditionProviderImpl result = new ConditionProviderImpl();

        if (having.hasWhere())
            result.addConditions(having.getWhere());

        // Apply SEEK predicates in the WHERE clause only if:
        // - There is an ORDER BY clause (SEEK is non-deterministic)
        // - There is a SEEK clause (obvious case)
        // - There are no unions (union is nested in derived table
        //   and SEEK predicate is applied outside). See [#7459]
        //   [#15820] We're not grouping
        if (isGrouping() && !getOrderBy().isEmpty() && !getSeek().isEmpty() && unionOp.isEmpty())
            result.addConditions(getSeekCondition(ctx));

        if (NO_SUPPORT_LIMIT_ZERO.contains(ctx.dialect()) && limit.limitZero() && isGrouping())
            result.addConditions(falseCondition());

        return result;
    }

    final ConditionProviderImpl getQualify() {
        return qualify;
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

    final SortFieldList getNonEmptyOrderBy(Configuration configuration) {
        if (getOrderBy().isEmpty()) {
            SortFieldList result = new SortFieldList();

            switch (configuration.family()) {










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
        Set<Field<?>> fields = new HashSet<>(map(order, o -> o.$field()));

        for (Field<?> field : getSelect())
            if (!fields.contains(field))
                order.add(field.asc());

        return order;
    }

    @Override
    public final void addOrderBy(Collection<? extends OrderField<?>> fields) {
        getOrderBy().addAll(Tools.sortFields(fields));
    }

    @Override
    public final void addOrderBy(OrderField<?>... fields) {
        addOrderBy(Arrays.asList(fields));
    }

    @Override
    public final void addOrderBy(int... fieldIndexes) {
        addOrderBy(map(fieldIndexes, v -> DSL.inline(v)));
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
    @Deprecated
    public final void addSeekBefore(Field<?>... fields) {
        addSeekBefore(Arrays.asList(fields));
    }

    @Override
    @Deprecated
    public final void addSeekBefore(Collection<? extends Field<?>> fields) {
        if (unionOp.size() == 0)
            seekBefore = true;
        else
            unionSeekBefore = true;

        getSeek().addAll(fields);
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
    public final void addConditions(Collection<? extends Condition> conditions) {
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















    final void setHint(String hint) {
        this.hint = hint;
    }

    final void setOption(String option) {
        this.option = option;
    }

    @Override
    public final void addFrom(Collection<? extends TableLike<?>> tables) {
        for (TableLike<?> t : tables)
            addFrom(t);
    }

    @Override
    public final void addFrom(TableLike<?> table) {
        if (table instanceof NoTable) {}
        else if (table instanceof NoTableJoin t)
            addFrom(t.table);
        else
            getFrom().add(table.asTable());
    }

    @Override
    public final void addFrom(TableLike<?>... tables) {
        for (TableLike<?> t : tables)
            addFrom(t);
    }
























    @Override
    public final void addGroupBy(Collection<? extends GroupField> fields) {

        // [#12910] For backwards compatibility, adding empty GROUP BY lists to
        //          a blank GROUP BY clause must maintain empty grouping set
        //          semantics
        if (fields.isEmpty())
            groupBy.add(emptyGroupingSet());
        else
            groupBy.addAll(fields);
    }

    @Override
    public final void setGroupByDistinct(boolean groupByDistinct) {
        this.groupByDistinct = groupByDistinct;
    }

    @Override
    public final void addGroupBy(GroupField... fields) {
        addGroupBy(Arrays.asList(fields));
    }

    @Override
    public final void addHaving(Condition conditions) {
        having.addConditions(conditions);
    }

    @Override
    public final void addHaving(Condition... conditions) {
        having.addConditions(conditions);
    }

    @Override
    public final void addHaving(Collection<? extends Condition> conditions) {
        having.addConditions(conditions);
    }

    @Override
    public final void addHaving(Operator operator, Condition conditions) {
        having.addConditions(operator, conditions);
    }

    @Override
    public final void addHaving(Operator operator, Condition... conditions) {
        having.addConditions(operator, conditions);
    }

    @Override
    public final void addHaving(Operator operator, Collection<? extends Condition> conditions) {
        having.addConditions(operator, conditions);
    }

    @Override
    public final void addWindow(WindowDefinition... definitions) {
        addWindow0(Arrays.asList(definitions));
    }

    @Override
    public final void addWindow(Collection<? extends WindowDefinition> definitions) {
        addWindow0(definitions);
    }

    final void addWindow0(Collection<? extends WindowDefinition> definitions) {
        if (window == null)
            window = new WindowList();

        window.addAll(definitions);
    }

    @Override
    public final void addQualify(Condition conditions) {
        getQualify().addConditions(conditions);
    }

    @Override
    public final void addQualify(Condition... conditions) {
        getQualify().addConditions(conditions);
    }

    @Override
    public final void addQualify(Collection<? extends Condition> conditions) {
        getQualify().addConditions(conditions);
    }

    @Override
    public final void addQualify(Operator operator, Condition conditions) {
        getQualify().addConditions(operator, conditions);
    }

    @Override
    public final void addQualify(Operator operator, Condition... conditions) {
        getQualify().addConditions(operator, conditions);
    }

    @Override
    public final void addQualify(Operator operator, Collection<? extends Condition> conditions) {
        getQualify().addConditions(operator, conditions);
    }

    @SuppressWarnings("rawtypes")
    private final SelectQueryImpl<R> combine(CombineOperator op, Select<? extends R> other) {

        // [#8557] Prevent StackOverflowError when using same query instance on
        //         both sides of a set operation
        if (this == other || (other instanceof SelectImpl && this == ((SelectImpl) other).getDelegate()))
            throw new IllegalArgumentException("In jOOQ 3.x's mutable DSL API, it is not possible to use the same instance of a Select query on both sides of a set operation like s.union(s)");

        int index = unionOp.size() - 1;

        if (index == -1 || unionOp.get(index) != op || op == EXCEPT || op == EXCEPT_ALL) {
            unionOp.add(op);
            union.add(new QueryPartList<>());

            index++;
        }

        union.get(index).add(other);
        return this;
    }

    @Override
    public final SelectQueryImpl<R> union(Select<? extends R> other) {
        return combine(UNION, other);
    }

    @Override
    public final SelectQueryImpl<R> unionDistinct(Select<? extends R> other) {
        return combine(UNION, other);
    }

    @Override
    public final SelectQueryImpl<R> unionAll(Select<? extends R> other) {
        return combine(UNION_ALL, other);
    }

    @Override
    public final SelectQueryImpl<R> except(Select<? extends R> other) {
        return combine(EXCEPT, other);
    }

    @Override
    public final SelectQueryImpl<R> exceptDistinct(Select<? extends R> other) {
        return combine(EXCEPT, other);
    }

    @Override
    public final SelectQueryImpl<R> exceptAll(Select<? extends R> other) {
        return combine(EXCEPT_ALL, other);
    }

    @Override
    public final SelectQueryImpl<R> intersect(Select<? extends R> other) {
        return combine(INTERSECT, other);
    }

    @Override
    public final SelectQueryImpl<R> intersectDistinct(Select<? extends R> other) {
        return combine(INTERSECT, other);
    }

    @Override
    public final SelectQueryImpl<R> intersectAll(Select<? extends R> other) {
        return combine(INTERSECT_ALL, other);
    }

    @Override
    public final void addJoin(TableLike<?> table, Condition conditions) {
        addJoin(table, JoinType.JOIN, conditions);
    }

    @Override
    public final void addJoin(TableLike<?> table, Condition... conditions) {
        addJoin(table, JoinType.JOIN, conditions);
    }

    @Override
    public final void addJoin(TableLike<?> table, JoinType type, Condition conditions) {
        addJoin0(table, type, null, conditions, null);
    }

    @Override
    public final void addJoin(TableLike<?> table, JoinType type, Condition... conditions) {
        addJoin0(table, type, null, conditions, null);
    }

    @Override
    public final void addJoin(TableLike<?> table, JoinType type, JoinHint hint, Condition conditions) {
        addJoin0(table, type, hint, conditions, null);
    }

    @Override
    public final void addJoin(TableLike<?> table, JoinType type, JoinHint hint, Condition... conditions) {
        addJoin0(table, type, hint, conditions, null);
    }



























    private final void addJoin0(TableLike<?> table, JoinType type, JoinHint hint, Object conditions, Field<?>[] partitionBy) {

        // TODO: This and similar methods should be refactored, patterns extracted...
        int index = getFrom().size() - 1;
        Table<?> joined = null;

        switch (type) {
            case JOIN:
            case STRAIGHT_JOIN:
            case LEFT_SEMI_JOIN:
            case LEFT_ANTI_JOIN:
            case FULL_OUTER_JOIN: {
                TableOptionalOnStep<Record> o = getFrom().get(index).join(table, type, hint);

                if (conditions instanceof Condition c)
                    joined = o.on(c);
                else
                    joined = o.on((Condition[]) conditions);

                break;
            }

            case LEFT_OUTER_JOIN:
            case RIGHT_OUTER_JOIN: {
                TablePartitionByStep<?> p = (TablePartitionByStep<?>) getFrom().get(index).join(table, type, hint);
                TableOnStep<?> o = p;




                if (conditions instanceof Condition c)
                    joined = o.on(c);
                else
                    joined = o.on((Condition[]) conditions);

                break;
            }

            // These join types don't take any ON clause. Ignore conditions.
            case CROSS_JOIN:
            case NATURAL_JOIN:
            case NATURAL_LEFT_OUTER_JOIN:
            case NATURAL_RIGHT_OUTER_JOIN:
            case NATURAL_FULL_OUTER_JOIN:
            case CROSS_APPLY:
            case OUTER_APPLY:
                joined = getFrom().get(index).join(table, type, hint);
                break;

            default: throw new IllegalArgumentException("Bad join type: " + type);
        }

        getFrom().set(index, joined);
    }

    @Override
    public final void addJoinOnKey(TableLike<?> table, JoinType type) throws DataAccessException {
        addJoinOnKey(table, type, (JoinHint) null);
    }

    @Override
    public final void addJoinOnKey(TableLike<?> table, JoinType type, JoinHint hint) throws DataAccessException {

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
                joined = getFrom().get(index).join(table, type, hint).onKey();
                break;

            default:
                throw new IllegalArgumentException("JoinType " + type + " is not supported with the addJoinOnKey() method. Use INNER or OUTER JOINs only");
        }

        getFrom().set(index, joined);
    }

    @Override
    public final void addJoinOnKey(TableLike<?> table, JoinType type, TableField<?, ?>... keyFields) throws DataAccessException {
        addJoinOnKey(table, type, null, keyFields);
    }

    @Override
    public final void addJoinOnKey(TableLike<?> table, JoinType type, JoinHint hint, TableField<?, ?>... keyFields) throws DataAccessException {

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
                joined = getFrom().get(index).join(table, type, hint).onKey(keyFields);
                break;

            default:
                throw new IllegalArgumentException("JoinType " + type + " is not supported with the addJoinOnKey() method. Use INNER or OUTER JOINs only");
        }

        getFrom().set(index, joined);
    }

    @Override
    public final void addJoinOnKey(TableLike<?> table, JoinType type, ForeignKey<?, ?> key) {
        addJoinOnKey(table, type, null, key);
    }

    @Override
    public final void addJoinOnKey(TableLike<?> table, JoinType type, JoinHint hint, ForeignKey<?, ?> key) {
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
                joined = getFrom().get(index).join(table, type, hint).onKey(key);
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
        addJoinUsing(table, type, null, fields);
    }

    @Override
    public final void addJoinUsing(TableLike<?> table, JoinType type, JoinHint hint, Collection<? extends Field<?>> fields) {
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
                joined = getFrom().get(index).join(table, type, hint).using(fields);
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
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final WithImpl $with() {
        return with;
    }

    @Override
    public final SelectQueryImpl<?> $with(With newWith) {
        return copy(s -> {}, (WithImpl) newWith);
    }

    @Override
    public final UnmodifiableList<SelectFieldOrAsterisk> $select() {
        return QOM.unmodifiable(select);
    }

    @Override
    public final SelectQueryImpl<?> $select(Collection<? extends SelectFieldOrAsterisk> newSelect) {
        return copy(s -> {
            s.select.clear();
            s.select.addAll(newSelect);
        });
    }

    @Override
    public final boolean $distinct() {
        return distinct;
    }

    @Override
    public final Select<R> $distinct(boolean newDistinct) {
        if ($distinct() == newDistinct)
            return this;
        else
            return copy(s -> s.distinct = newDistinct);
    }

    @Override
    public final UnmodifiableList<SelectFieldOrAsterisk> $distinctOn() {
        return QOM.unmodifiable(distinctOn);
    }

    @Override
    public final Select<R> $distinctOn(Collection<? extends SelectFieldOrAsterisk> newDistinctOn) {
        return copy(s -> {
            s.distinctOn.clear();
            s.distinctOn.addAll(newDistinctOn);
        });
    }

    @Override
    public final UnmodifiableList<Table<?>> $from() {
        return QOM.unmodifiable(from);
    }

    @Override
    public final SelectQueryImpl<R> $from(Collection<? extends Table<?>> newFrom) {
        return copy(s -> {
            s.from.clear();
            s.from.addAll(newFrom);
        });
    }

    @Override
    public final Condition $where() {
        return condition.getWhereOrNull();
    }

    @Override
    public final Select<R> $where(Condition newWhere) {
        if ($where() == newWhere)
            return this;
        else
            return copy(s -> s.condition.setWhere(newWhere));
    }

    @Override
    public final UnmodifiableList<GroupField> $groupBy() {
        return QOM.unmodifiable(groupBy);
    }

    @Override
    public final Select<R> $groupBy(Collection<? extends GroupField> newGroupBy) {
        return copy(s -> {
            s.groupBy.clear();
            s.groupBy.addAll(newGroupBy);
        });
    }

    @Override
    public final boolean $groupByDistinct() {
        return groupByDistinct;
    }

    @Override
    public final Select<R> $groupByDistinct(boolean newGroupByDistinct) {
        if ($groupByDistinct() == newGroupByDistinct)
            return this;
        else
            return copy(s -> s.groupByDistinct = newGroupByDistinct);
    }

    @Override
    public final Condition $having() {
        return having.getWhereOrNull();
    }

    @Override
    public final Select<R> $having(Condition newHaving) {
        if ($having() == newHaving)
            return this;
        else
            return copy(s -> s.having.setWhere(newHaving));
    }

    @Override
    public final UnmodifiableList<? extends WindowDefinition> $window() {
        return QOM.unmodifiable(window == null ? QueryPartList.emptyList() : window);
    }

    @Override
    public final Select<R> $window(Collection<? extends WindowDefinition> newWindow) {
        return copy(s -> {
            s.window.clear();
            s.window.addAll(newWindow);
        });
    }

    @Override
    public final Condition $qualify() {
        return qualify.getWhereOrNull();
    }

    @Override
    public final Select<R> $qualify(Condition newQualify) {
        if ($qualify() == newQualify)
            return this;
        else
            return copy(s -> s.qualify.setWhere(newQualify));
    }

    @Override
    public final UnmodifiableList<SortField<?>> $orderBy() {
        return QOM.unmodifiable(orderBy);
    }

    @Override
    public final Select<R> $orderBy(Collection<? extends SortField<?>> newOrderBy) {
        return copy(s -> {
            s.orderBy.clear();
            s.orderBy.addAll(newOrderBy);
        });
    }

    @Override
    public final Field<? extends Number> $limit() {
        return getLimit().limit;
    }

    @Override
    public final Select<R> $limit(Field<? extends Number> newLimit) {
        if ($limit() == newLimit)
            return this;
        else
            return copy(s -> s.getLimit().setLimit(newLimit));
    }

    @Override
    public final boolean $limitPercent() {
        return getLimit().percent;
    }

    @Override
    public final Select<R> $limitPercent(boolean newLimitPercent) {
        if ($limitPercent() == newLimitPercent)
            return this;
        else
            return copy(s -> s.getLimit().setPercent(newLimitPercent));
    }

    @Override
    public final boolean $limitWithTies() {
        return getLimit().withTies;
    }

    @Override
    public final Select<R> $limitWithTies(boolean newLimitWithTies) {
        if ($limitPercent() == newLimitWithTies)
            return this;
        else
            return copy(s -> s.getLimit().setWithTies(newLimitWithTies));
    }

    @Override
    public final Field<? extends Number> $offset() {
        return getLimit().offset;
    }

    @Override
    public final Select<R> $offset(Field<? extends Number> newOffset) {
        if ($limit() == newOffset)
            return this;

        // [#5695] TODO: Support all types of Field!
        else
            return copy(s -> s.getLimit().setOffset(newOffset));
    }














































































































}
