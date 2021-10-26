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
 *
 *
 *
 */
package org.jooq.impl;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
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
import static org.jooq.JoinType.JOIN;
import static org.jooq.JoinType.LEFT_OUTER_JOIN;
import static org.jooq.JoinType.RIGHT_OUTER_JOIN;
import static org.jooq.Operator.OR;
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
// ...
import static org.jooq.SQLDialect.DEFAULT;
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
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
import static org.jooq.SQLDialect.MYSQL;
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
import static org.jooq.SortOrder.DESC;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.AsteriskImpl.NO_SUPPORT_UNQUALIFIED_COMBINED;
import static org.jooq.impl.AsteriskImpl.SUPPORT_NATIVE_EXCEPT;
import static org.jooq.impl.CombineOperator.EXCEPT;
import static org.jooq.impl.CombineOperator.EXCEPT_ALL;
import static org.jooq.impl.CombineOperator.INTERSECT;
import static org.jooq.impl.CombineOperator.INTERSECT_ALL;
import static org.jooq.impl.CombineOperator.UNION;
import static org.jooq.impl.CombineOperator.UNION_ALL;
import static org.jooq.impl.CommonTableExpressionList.markTopLevelCteAndAccept;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.createTable;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.generateSeries;
import static org.jooq.impl.DSL.groupingSets;
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
import static org.jooq.impl.Internal.isub;
import static org.jooq.impl.JSONArrayAgg.EMULATE_WITH_GROUP_CONCAT;
import static org.jooq.impl.JSONArrayAgg.patchOracleArrayAggBug;
import static org.jooq.impl.JSONNull.NO_SUPPORT_ABSENT_ON_NULL;
import static org.jooq.impl.Keywords.K_AND;
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
import static org.jooq.impl.Keywords.K_PERCENT;
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
import static org.jooq.impl.QueryPartCollectionView.wrap;
import static org.jooq.impl.SQLDataType.JSON;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.SQLDataType.XML;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.aliased;
import static org.jooq.impl.Tools.aliasedFields;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.autoAlias;
import static org.jooq.impl.Tools.camelCase;
import static org.jooq.impl.Tools.containsUnaliasedTable;
import static org.jooq.impl.Tools.fieldArray;
import static org.jooq.impl.Tools.findAny;
import static org.jooq.impl.Tools.hasAmbiguousNames;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.impl.Tools.isNotEmpty;
import static org.jooq.impl.Tools.isWindow;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.qualify;
import static org.jooq.impl.Tools.recordType;
import static org.jooq.impl.Tools.selectQueryImpl;
// ...
import static org.jooq.impl.Tools.traverseJoins;
// ...
// ...
import static org.jooq.impl.Tools.unalias;
import static org.jooq.impl.Tools.unqualified;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_COLLECT_SEMI_ANTI_JOIN;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_INSERT_SELECT;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_NESTED_SET_OPERATIONS;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_OMIT_INTO_CLAUSE;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_UNALIAS_ALIASED_EXPRESSIONS;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES;
import static org.jooq.impl.Tools.DataExtendedKey.DATA_TRANSFORM_ROWNUM_TO_LIMIT;
import static org.jooq.impl.Tools.DataKey.DATA_COLLECTED_SEMI_ANTI_JOIN;
import static org.jooq.impl.Tools.DataKey.DATA_DML_TARGET_TABLE;
import static org.jooq.impl.Tools.DataKey.DATA_OVERRIDE_ALIASES_IN_ORDER_BY;
import static org.jooq.impl.Tools.DataKey.DATA_SELECT_ALIASES;
import static org.jooq.impl.Tools.DataKey.DATA_SELECT_INTO_TABLE;
import static org.jooq.impl.Tools.DataKey.DATA_TOP_LEVEL_CTE;
import static org.jooq.impl.Tools.DataKey.DATA_WINDOW_DEFINITIONS;
import static org.jooq.impl.Transformations.transformQualify;
import static org.jooq.impl.Transformations.transformRownum;

import java.sql.ResultSetMetaData;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jooq.Asterisk;
import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.GroupField;
import org.jooq.JSONEntry;
import org.jooq.JSONObjectNullStep;
import org.jooq.JSONObjectReturningStep;
import org.jooq.JoinType;
import org.jooq.Name;
import org.jooq.Operator;
import org.jooq.OrderField;
import org.jooq.Param;
// ...
import org.jooq.QualifiedAsterisk;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.SelectHavingStep;
import org.jooq.SelectLimitPercentStep;
import org.jooq.SelectLimitStep;
import org.jooq.SelectOffsetStep;
import org.jooq.SelectQuery;
import org.jooq.SelectWithTiesStep;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnStep;
import org.jooq.TableOptionalOnStep;
import org.jooq.TablePartitionByStep;
// ...
import org.jooq.WindowDefinition;
import org.jooq.XML;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.ForLock.ForLockMode;
import org.jooq.impl.ForLock.ForLockWaitMode;
import org.jooq.impl.Tools.BooleanDataKey;
import org.jooq.impl.Tools.DataExtendedKey;
import org.jooq.impl.Tools.DataKey;
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
    private static final JooqLogger      log                             = JooqLogger.getLogger(SelectQueryImpl.class);
    private static final Clause[]        CLAUSES                         = { SELECT };
    static final Set<SQLDialect>         EMULATE_SELECT_INTO_AS_CTAS     = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE);
    private static final Set<SQLDialect> SUPPORT_SELECT_INTO_TABLE       = SQLDialect.supportedBy(HSQLDB, POSTGRES);



    static final Set<SQLDialect>         NO_SUPPORT_WINDOW_CLAUSE        = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD, HSQLDB, IGNITE, MARIADB);
    private static final Set<SQLDialect> OPTIONAL_FROM_CLAUSE            = SQLDialect.supportedBy(DEFAULT, H2, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE);
    private static final Set<SQLDialect> REQUIRES_DERIVED_TABLE_DML      = SQLDialect.supportedBy(MARIADB, MYSQL);
    private static final Set<SQLDialect> EMULATE_EMPTY_GROUP_BY_CONSTANT = SQLDialect.supportedUntil(DERBY, HSQLDB, IGNITE);
    private static final Set<SQLDialect> EMULATE_EMPTY_GROUP_BY_OTHER    = SQLDialect.supportedUntil(FIREBIRD, MARIADB, MYSQL, SQLITE);













    private static final Set<SQLDialect> SUPPORT_FULL_WITH_TIES          = SQLDialect.supportedBy(H2, POSTGRES);
    private static final Set<SQLDialect> EMULATE_DISTINCT_ON             = SQLDialect.supportedBy(DERBY, FIREBIRD, HSQLDB, MARIADB, MYSQL, SQLITE);
    static final Set<SQLDialect>         NO_SUPPORT_FOR_UPDATE_OF_FIELDS = SQLDialect.supportedBy(MYSQL, POSTGRES);

























    final WithImpl                                       with;
    private final SelectFieldList<SelectFieldOrAsterisk> select;
    private Table<?>                                     intoTable;
    private String                                       hint;
    private String                                       option;
    private boolean                                      distinct;
    private QueryPartList<SelectFieldOrAsterisk>         distinctOn;
    private ForLock                                      forLock;


















    private final TableList                              from;
    private final ConditionProviderImpl                  condition;
    private boolean                                      grouping;
    private QueryPartList<GroupField>                    groupBy;
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
        this.select = new SelectFieldList<>();
        this.from = new TableList();
        this.condition = new ConditionProviderImpl();




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






            result.grouping = grouping;
            result.groupBy = groupBy;
            result.having.setWhere(having.getWhere());
            if (window != null)
                result.addWindow(window);
            result.qualify.setWhere(qualify.getWhere());
        }

        if (CopyClause.QUALIFY.between(start, end)) {
            if (!scalarSelect)
                result.select.addAll(select);

            result.hint = hint;
            result.distinct = distinct;
            result.distinctOn = distinctOn;
            result.orderBy.addAll(orderBy);



            result.seek.addAll(seek);
            result.limit.from(limit);
            result.forLock = forLock;






            result.option = option;
            result.intoTable = intoTable;

            // TODO: Should the remaining union subqueries also be copied?
            result.union.addAll(union);
            result.unionOp.addAll(unionOp);
            result.unionOrderBy.addAll(unionOrderBy);



            result.unionSeek.addAll(unionSeek);
            result.unionSeekBefore = unionSeekBefore;
            result.unionLimit.from(unionLimit);
        }

        return result;
    }

    private final SelectQueryImpl<R> copy(Function<? super SelectQueryImpl<R>, ? extends SelectQueryImpl<R>> finisher) {
        return finisher.apply(copyTo(CopyClause.END, false, new SelectQueryImpl<>(configuration(), with)));
    }














































    @SuppressWarnings("unchecked")
    @Override
    public final <T> Field<T> asField() {
        return new ScalarSubquery<>(this, (DataType<T>) Tools.scalarType(this));
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
    public final Table<R> asTable() {
        // Its usually better to alias nested selects that are used in
        // the FROM clause of a query
        return new DerivedTable<>(this).as(autoAlias(this));
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
    public final Table<R> asTable(Name alias) {
        return new DerivedTable<>(this).as(alias);
    }

    @Override
    public final Table<R> asTable(Name alias, Name... fieldAliases) {
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
    public final Table<R> asTable(String alias, Function<? super Field<?>, ? extends String> aliasFunction) {
        return new DerivedTable<>(this).as(alias, aliasFunction);
    }

    @Override
    public final Table<R> asTable(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> aliasFunction) {
        return new DerivedTable<>(this).as(alias, aliasFunction);
    }

    @Override
    public final Field<?>[] getFields(ResultSetMetaData meta) {
        Field<?>[] fields = getFields();

        // If no projection was specified explicitly, create fields from result
        // set meta data instead. This is typically the case for SELECT * ...
        if (fields.length == 0)
            return new MetaDataFieldProvider(configuration(), meta).getFields();

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



































































































































































































































































































































































































































































































    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final Select<?> distinctOnEmulation() {

        // [#3564] TODO: Extract and merge this with getSelectResolveSomeAsterisks0()
        List<Field<?>> partitionBy = new ArrayList<>(distinctOn.size());

        for (SelectFieldOrAsterisk f : distinctOn)
            if (f instanceof Field)
                partitionBy.add((Field<?>) f);

        Field<Integer> rn = rowNumber().over(partitionBy(partitionBy).orderBy(orderBy)).as("rn");

        SelectQueryImpl<R> copy = copy(identity());
        copy.distinctOn = null;
        copy.select.add(rn);
        copy.orderBy.clear();
        copy.limit.clear();

        SelectLimitStep<?> s1 =
        DSL.select(qualify(table(name("t")), select))
           .from(copy.asTable("t"))
           .where(rn.eq(one()))
           .orderBy(map(orderBy, o -> unqualified(o)));

        if (limit.numberOfRows != null) {
            SelectLimitPercentStep<?> s2 = s1.limit((Param) limit.numberOfRows);
            SelectWithTiesStep<?> s3 = limit.percent ? s2.percent() : s2;
            SelectOffsetStep<?> s4 = limit.withTies ? s3.withTies() : s3;
            return limit.offset != null ? s4.offset((Param) limit.offset) : s4;
        }
        else
            return limit.offset != null ? s1.offset((Param) limit.offset) : s1;
    }


































































































    @Override
    public final void accept(Context<?> ctx) {
        Table<?> dmlTable;

        // [#6583] Work around MySQL's self-reference-in-DML-subquery restriction
        if (ctx.subqueryLevel() == 1
            && REQUIRES_DERIVED_TABLE_DML.contains(ctx.dialect())
            && !TRUE.equals(ctx.data(DATA_INSERT_SELECT))
            && (dmlTable = (Table<?>) ctx.data(DATA_DML_TARGET_TABLE)) != null
            && containsUnaliasedTable(getFrom(), dmlTable)) {
            ctx.visit(DSL.select(asterisk()).from(asTable("t")));
        }

        // [#3564] Emulate DISTINCT ON queries at the top level
        else if (Tools.isNotEmpty(distinctOn) && EMULATE_DISTINCT_ON.contains(ctx.dialect())) {
            ctx.visit(distinctOnEmulation());
        }

        // [#5810] Emulate the Teradata QUALIFY clause
        else if (qualify.hasWhere() && transformQualify(ctx.configuration())) {



        }













































        else
            accept0(ctx);
    }

























































































































    final void accept0(Context<?> context) {







        boolean topLevelCte = false;

        // Subquery scopes are started in AbstractContext
        if (context.subqueryLevel() == 0) {
            context.scopeStart();

            if (topLevelCte |= (context.data(DATA_TOP_LEVEL_CTE) == null))
                context.data(DATA_TOP_LEVEL_CTE, new TopLevelCte());
        }

        SQLDialect dialect = context.dialect();

        // [#2791] TODO: Instead of explicitly manipulating these data() objects, future versions
        // of jOOQ should implement a push / pop semantics to clearly delimit such scope.
        Object renderTrailingLimit = context.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE);
        Object localWindowDefinitions = context.data(DATA_WINDOW_DEFINITIONS);
        Name[] selectAliases = (Name[]) context.data(DATA_SELECT_ALIASES);

        try {
            List<Field<?>> originalFields = null;
            List<Field<?>> alternativeFields = null;

            if (selectAliases != null) {
                context.data().remove(DATA_SELECT_ALIASES);
                alternativeFields = map(originalFields = getSelect(),
                    (f, i) -> i < selectAliases.length ? f.as(selectAliases[i]) : f
                );
            }

            if (TRUE.equals(renderTrailingLimit))
                context.data().remove(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE);

            // [#5127] Lazy initialise this map
            if (localWindowDefinitions != null)
                context.data(DATA_WINDOW_DEFINITIONS, null);

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

            switch (dialect) {













































































































































                case CUBRID:
                case FIREBIRD:
                case MARIADB:
                case MYSQL: {
                    if (getLimit().isApplicable() && getLimit().withTies())
                        toSQLReferenceLimitWithWindowFunctions(context);
                    else
                        toSQLReferenceLimitDefault(context, originalFields, alternativeFields);

                    break;
                }

                // By default, render the dialect's limit clause
                default: {
                    toSQLReferenceLimitDefault(context, originalFields, alternativeFields);

                    break;
                }
            }

            // [#1296] [#7328] FOR UPDATE is emulated in some dialects using hints
            if (forLock != null)
                context.visit(forLock);



















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
            context.data(DATA_WINDOW_DEFINITIONS, localWindowDefinitions);
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
    private final void toSQLReferenceLimitDefault(Context<?> context, List<Field<?>> originalFields, List<Field<?>> alternativeFields) {
        context.data(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE, true, c -> toSQLReference0(context, originalFields, alternativeFields));
    }

    /**
     * Emulate the LIMIT / OFFSET clause using window functions, specifically
     * when the WITH TIES clause is specified.
     */
    private final void toSQLReferenceLimitWithWindowFunctions(Context<?> ctx) {

        // AUTHOR.ID, BOOK.ID, BOOK.TITLE
        final List<Field<?>> originalFields = getSelect();

        // AUTHOR.ID as v1, BOOK.ID as v2, BOOK.TITLE as v3
        // Enforce x.* or just * if we have no known field names (e.g. when plain SQL tables are involved)
        final List<Field<?>> alternativeFields = new ArrayList<>(originalFields.size());

        if (originalFields.isEmpty())
            alternativeFields.add(DSL.field("*"));
        else
            alternativeFields.addAll(aliasedFields(originalFields));

        alternativeFields.add(CustomField.of("rn", SQLDataType.INTEGER, c -> {
            boolean wrapQueryExpressionBodyInDerivedTable = wrapQueryExpressionBodyInDerivedTable(c);

            // [#3575] Ensure that no column aliases from the surrounding SELECT clause
            // are referenced from the below ranking functions' ORDER BY clause.
            c.data(DATA_UNALIAS_ALIASED_EXPRESSIONS, !wrapQueryExpressionBodyInDerivedTable);

            boolean q = c.qualify();

            c.data(DATA_OVERRIDE_ALIASES_IN_ORDER_BY, new Object[] { originalFields, alternativeFields });
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

            c.visit(distinct
                ? DSL.denseRank().over(orderBy(getNonEmptyOrderByForDistinct(c.configuration())))
                : getLimit().withTies()
                ? DSL.rank().over(orderBy(getNonEmptyOrderBy(c.configuration())))
                : DSL.rowNumber().over(orderBy(getNonEmptyOrderBy(c.configuration())))
            );

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

        toSQLReference0(ctx, originalFields, alternativeFields);

        ctx.subquery(false)
           .sqlIndentEnd(") ")
           .visit(name("x"))
           .formatSeparator()
           .visit(K_WHERE).sql(' ')
           .visit(name("rn"))
           .sql(" > ")
           .visit(getLimit().getLowerRownum());

        if (!getLimit().limitZero())
            ctx.formatSeparator()
               .visit(K_AND).sql(' ')
               .visit(name("rn"))
               .sql(" <= ")
               .visit(getLimit().getUpperRownum());

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





































































    /**
     * This method renders the main part of a query without the LIMIT clause.
     * This part is common to any type of limited query
     */
    @SuppressWarnings("unchecked")
    private final void toSQLReference0(Context<?> context, List<Field<?>> originalFields, List<Field<?>> alternativeFields) {
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











        // [#7459] In the presence of UNIONs and other set operations, the SEEK
        //         predicate must be applied on a derived table, not on the individual subqueries
            || applySeekOnDerivedTable;

        if (wrapQueryExpressionBodyInDerivedTable) {
            context.visit(K_SELECT).sql(' ');





            context.formatIndentStart()
                   .formatNewLine()
                   .sql("t.*");

            if (alternativeFields != null && originalFields.size() < alternativeFields.size())
                context.sql(", ")
                       .formatSeparator()
                       .declareFields(true, c -> c.visit(alternativeFields.get(alternativeFields.size() - 1)));

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
                    alternativeFields != null ? alternativeFields : getSelect(),
                    derivedTableRequired(context, this),
                    unionParensRequired = unionOpNesting || unionParensRequired(context)
                );
            }
        }

        traverseJoins(getFrom(), t -> {
            if (t instanceof TableImpl)
                context.scopeRegister(t, true);
        });

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









        context.declareFields(true);

        // [#2335] When emulating LIMIT .. OFFSET, the SELECT clause needs to generate
        // non-ambiguous column names as ambiguous column names are not allowed in subqueries
        if (alternativeFields != null)
            if (wrapQueryExpressionBodyInDerivedTable && originalFields.size() < alternativeFields.size())
                context.visit(new SelectFieldList<>(alternativeFields.subList(0, originalFields.size())));
            else
                context.visit(new SelectFieldList<>(alternativeFields));

        // The default behaviour
        else
            context.visit(getSelectResolveUnsupportedAsterisks(context.configuration()));






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
        context.start(SELECT_FROM)
               .declareTables(true);

        // [#....] Some SQL dialects do not require a FROM clause. Others do and
        //         jOOQ generates a "DUAL" table or something equivalent.
        //         See also org.jooq.impl.Dual for details.
        boolean hasFrom = !getFrom().isEmpty()
            || !OPTIONAL_FROM_CLAUSE.contains(context.dialect())











            ;

        List<Condition> semiAntiJoinPredicates = null;
        ConditionProviderImpl where = getWhere(context);

        if (hasFrom) {
            Object previousCollect = context.data(DATA_COLLECT_SEMI_ANTI_JOIN, true);
            Object previousCollected = context.data(DATA_COLLECTED_SEMI_ANTI_JOIN, null);

            TableList tablelist = getFrom();













            tablelist = transformInlineDerivedTables(tablelist, where);

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

        if (TRUE.equals(context.data().get(BooleanDataKey.DATA_SELECT_NO_DATA)))
            context.formatSeparator()
                   .visit(K_WHERE)
                   .sql(' ')
                   .visit(falseCondition());
        else if (!where.hasWhere() && semiAntiJoinPredicates == null)
            ;
        else {
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

        if (grouping) {
            context.formatSeparator()
                   .visit(K_GROUP_BY)
                   .separatorRequired(true);

            // [#1665] Empty GROUP BY () clauses need parentheses
            if (Tools.isEmpty(groupBy)) {
                context.sql(' ');

                // [#4292] Some dialects accept constant expressions in GROUP BY
                // Note that dialects may consider constants as indexed field
                // references, as in the ORDER BY clause!
                if (EMULATE_EMPTY_GROUP_BY_CONSTANT.contains(context.dialect()))
                    context.sql('0');

                // [#4447] CUBRID can't handle subqueries in GROUP BY
                else if (family == CUBRID)
                    context.sql("1 + 0");











                // [#4292] Some dialects don't support empty GROUP BY () clauses
                else if (EMULATE_EMPTY_GROUP_BY_OTHER.contains(context.dialect()))
                    context.sql('(').visit(DSL.select(one())).sql(')');

                // Few dialects support the SQL standard "grand total" (i.e. empty grouping set)
                else
                    context.sql("()");
            }
            else
                context.visit(groupBy);
        }

        context.end(SELECT_GROUP_BY);

        // HAVING clause
        // -------------
        context.start(SELECT_HAVING);

        if (getHaving().hasWhere())
            context.formatSeparator()
                   .visit(K_HAVING)
                   .sql(' ')
                   .visit(getHaving());

        context.end(SELECT_HAVING);

        // WINDOW clause
        // -------------
        context.start(SELECT_WINDOW);

        if (Tools.isNotEmpty(window) && !NO_SUPPORT_WINDOW_CLAUSE.contains(context.dialect()))
            context.formatSeparator()
                   .visit(K_WINDOW)
                   .separatorRequired(true)
                   .declareWindows(true, c -> c.visit(window));

        context.end(SELECT_WINDOW);

        // QUALIFY clause
        // -------------

        if (getQualify().hasWhere())
            context.formatSeparator()
                   .visit(K_QUALIFY)
                   .sql(' ')
                   .visit(getQualify());

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
            unionParenthesis(context, ')', null, derivedTableRequired(context, this), unionParensRequired);

            for (int i = 0; i < unionOpSize; i++) {
                CombineOperator op = unionOp.get(i);

                for (Select<?> other : union.get(i)) {
                    boolean derivedTableRequired = derivedTableRequired(context, other);

                    context.formatSeparator()
                           .visit(op.toKeyword(family));

                    if (unionParensRequired)
                        context.sql(' ');
                    else
                        context.formatSeparator();

                    unionParenthesis(context, '(', other.getSelect(), derivedTableRequired, unionParensRequired);
                    context.visit(other);
                    unionParenthesis(context, ')', null, derivedTableRequired, unionParensRequired);
                }

                // [#1658] Close parentheses opened previously
                if (i < unionOpSize - 1)
                    unionParenthesis(context, ')', null, derivedTableRequired(context, this), unionParensRequired);

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
                       .qualify(false, c -> c.visit(getSeekCondition()));
            }
        }

        // ORDER BY clause for UNION
        // -------------------------
        context.qualify(false, c -> toSQLOrderBy(
            context,
            originalFields, alternativeFields,
            wrapQueryExpressionInDerivedTable, wrapQueryExpressionBodyInDerivedTable,
            unionOrderBy, unionLimit
        ));
    }

    private final boolean hasInlineDerivedTables(TableList tablelist) {
        return anyMatch(tablelist, t ->
               t instanceof InlineDerivedTable
            || t instanceof JoinTable && hasInlineDerivedTables((JoinTable) t)
        );
    }

    private final boolean hasInlineDerivedTables(JoinTable join) {
        return join.lhs instanceof InlineDerivedTable
            || join.rhs instanceof InlineDerivedTable
            || join.lhs instanceof JoinTable && hasInlineDerivedTables((JoinTable) join.lhs)
            || join.rhs instanceof JoinTable && hasInlineDerivedTables((JoinTable) join.rhs);
    }

    private final TableList transformInlineDerivedTables(TableList tablelist, ConditionProviderImpl where) {
        if (!hasInlineDerivedTables(tablelist))
            return tablelist;

        TableList result = new TableList();

        for (Table<?> table : tablelist)
            transformInlineDerivedTable0(table, result, where);

        return result;
    }

    private final void transformInlineDerivedTable0(Table<?> table, TableList result, ConditionProviderImpl where) {
        if (table instanceof InlineDerivedTable) {
            InlineDerivedTable<?> t = (InlineDerivedTable<?>) table;

            result.add(t.table());
            where.addConditions(t.condition());
        }
        else if (table instanceof JoinTable)
            result.add(transformInlineDerivedTables0(table, where, false));
        else
            result.add(table);
    }

    private final Table<?> transformInlineDerivedTables0(Table<?> table, ConditionProviderImpl where, boolean keepDerivedTable) {
        if (table instanceof InlineDerivedTable) {
            InlineDerivedTable<?> t = (InlineDerivedTable<?>) table;

            if (keepDerivedTable)
                return t.query().asTable(t.table());

            where.addConditions(t.condition());
            return t.table();
        }
        else if (table instanceof JoinTable) {
            JoinTable j = (JoinTable) table;
            Table<?> lhs;
            Table<?> rhs;

            switch (j.type) {
                case LEFT_OUTER_JOIN:
                case LEFT_ANTI_JOIN:
                case LEFT_SEMI_JOIN:
                case STRAIGHT_JOIN:
                case CROSS_APPLY:
                case OUTER_APPLY:
                case NATURAL_LEFT_OUTER_JOIN:
                    lhs = transformInlineDerivedTables0(j.lhs, where, keepDerivedTable);
                    rhs = transformInlineDerivedTables0(j.rhs, where, true);
                    break;

                case RIGHT_OUTER_JOIN:
                case NATURAL_RIGHT_OUTER_JOIN:
                    lhs = transformInlineDerivedTables0(j.lhs, where, true);
                    rhs = transformInlineDerivedTables0(j.rhs, where, keepDerivedTable);
                    break;

                case FULL_OUTER_JOIN:
                case NATURAL_FULL_OUTER_JOIN:
                    lhs = transformInlineDerivedTables0(j.lhs, where, true);
                    rhs = transformInlineDerivedTables0(j.rhs, where, true);
                    break;

                default:
                    lhs = transformInlineDerivedTables0(j.lhs, where, keepDerivedTable);
                    rhs = transformInlineDerivedTables0(j.rhs, where, keepDerivedTable);
                    break;
            }

            return j.transform(lhs, rhs);
        }
        else
            return table;
    }






















































































































































































































































































































































    private final void toSQLOrderBy(
        final Context<?> ctx,
        final List<Field<?>> originalFields,
        final List<Field<?>> alternativeFields,
        final boolean wrapQueryExpressionInDerivedTable,
        final boolean wrapQueryExpressionBodyInDerivedTable,
        QueryPartListView<SortField<?>> actualOrderBy,
        Limit actualLimit
    ) {

        ctx.start(SELECT_ORDER_BY);

        // [#6197] When emulating WITH TIES using RANK() in a subquery, we must avoid rendering the
        //         subquery's ORDER BY clause
        if (!getLimit().withTies()
            // Dialects with native support
            || SUPPORT_FULL_WITH_TIES.contains(ctx.dialect())





        ) {
            if (!actualOrderBy.isEmpty()) {
                ctx.formatSeparator()
                   .visit(K_ORDER);






                ctx.sql(' ').visit(K_BY).separatorRequired(true);

                // [#11904] Shift field indexes in ORDER BY <field index>, in
                //          case we are projecting emulated nested records of some sort
                if (RowField.NO_NATIVE_SUPPORT.contains(ctx.dialect())
                    && findAny(actualOrderBy, s -> ((SortFieldImpl<?>) s).getField() instanceof Val) != null) {
                    SelectFieldIndexes s = getSelectFieldIndexes(ctx);

                    if (s.mapped) {
                        actualOrderBy = new QueryPartListView<>(actualOrderBy).map(t1 -> {
                            Field<?> in = ((SortFieldImpl<?>) t1).getField();

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
                                return ((SortFieldImpl<?>) t1).transform(out);
                            }
                            else
                                return t1;
                        });
                    }
                }





















                {
                    ctx.visit(actualOrderBy);
                }
            }












        }

        ctx.end(SELECT_ORDER_BY);

        if (wrapQueryExpressionInDerivedTable)
            ctx.formatIndentEnd()
               .formatNewLine()
               .sql(") x");

        if (TRUE.equals(ctx.data().get(DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE)) && actualLimit.isApplicable())
            ctx.visit(actualLimit);
    }

    private final boolean applySeekOnDerivedTable() {
        return !getSeek().isEmpty() && !getOrderBy().isEmpty() && !unionOp.isEmpty();
    }

    private final boolean wrapQueryExpressionBodyInDerivedTable(Context<?> ctx) {
        return true






        ;
    }

























































    private static final Set<SQLDialect> NO_SUPPORT_UNION_PARENTHESES = SQLDialect.supportedBy(SQLITE);
    private static final Set<SQLDialect> NO_SUPPORT_CTE_IN_UNION      = SQLDialect.supportedBy(HSQLDB, MARIADB);
    private static final Set<SQLDialect> UNION_PARENTHESIS            = SQLDialect.supportedBy(DERBY, MARIADB, MYSQL);

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
        return NO_SUPPORT_CTE_IN_UNION.contains(context.dialect()) && (s = selectQueryImpl(s1)) != null && s.with != null;
    }

    private final boolean unionParensRequired(Context<?> context) {
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
        boolean parensRequired
    ) {
        if ('(' == parenthesis)
            ((AbstractContext<?>) ctx).subquery0(true, true);
        else if (')' == parenthesis)
            ((AbstractContext<?>) ctx).subquery0(false, true);

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
                    ctx.visit(new SelectFieldList<>(map(fields, f -> Tools.unqualified(f))));
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
        if (distinctOn == null)
            distinctOn = new QueryPartList<>();

        distinctOn.addAll(fields);
    }

    @Override
    public final void setInto(Table<?> table) {
        this.intoTable = table;
    }










    @Override
    public final void addOffset(int offset) {
        addOffset((Number) offset);
    }

    @Override
    public final void addOffset(Number offset) {
        getLimit().setOffset(offset);
    }

    @Override
    public final void addOffset(Param<? extends Number> offset) {
        getLimit().setOffset(offset);
    }

    @Override
    public final void addLimit(int numberOfRows) {
        addLimit((Number) numberOfRows);
    }

    @Override
    public final void addLimit(Number numberOfRows) {
        getLimit().setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(Param<? extends Number> numberOfRows) {
        getLimit().setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(int offset, int numberOfRows) {
        addLimit((Number) offset, (Number) numberOfRows);
    }

    @Override
    public final void addLimit(Number offset, Number numberOfRows) {
        getLimit().setOffset(offset);
        getLimit().setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(int offset, Param<Integer> numberOfRows) {
        addLimit((Number) offset, numberOfRows);
    }

    @Override
    public final void addLimit(Number offset, Param<? extends Number> numberOfRows) {
        getLimit().setOffset(offset);
        getLimit().setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(Param<Integer> offset, int numberOfRows) {
        addLimit(offset, (Number) numberOfRows);
    }

    @Override
    public final void addLimit(Param<? extends Number> offset, Number numberOfRows) {
        getLimit().setOffset(offset);
        getLimit().setNumberOfRows(numberOfRows);
    }

    @Override
    public final void addLimit(Param<? extends Number> offset, Param<? extends Number> numberOfRows) {
        getLimit().setOffset(offset);
        getLimit().setNumberOfRows(numberOfRows);
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
    public final List<Field<?>> getSelect() {
        return getSelectResolveAllAsterisks(Tools.configuration(configuration()));
    }

    private final Collection<? extends Field<?>> subtract(List<Field<?>> left, List<Field<?>> right) {

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
            return resolveAsterisk(new SelectFieldList<>());

        return getSelectAsSpecified();
    }

    /**
     * The select list with resolved explicit asterisks (if they contain the
     * except clause and that is not supported).
     */
    final SelectFieldList<SelectFieldOrAsterisk> getSelectResolveUnsupportedAsterisks(Configuration c) {
        return getSelectResolveSomeAsterisks0(c, false);
    }

    /**
     * The select list with resolved explicit asterisks (if they contain the
     * except clause and that is not supported).
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    final SelectFieldList<Field<?>> getSelectResolveAllAsterisks(Configuration c) {
        return (SelectFieldList) getSelectResolveSomeAsterisks0(c, true);
    }

    private final SelectFieldList<SelectFieldOrAsterisk> getSelectResolveSomeAsterisks0(Configuration c, boolean resolveSupported) {
        SelectFieldList<SelectFieldOrAsterisk> result = new SelectFieldList<>();

        // [#7921] Only H2 supports the * EXCEPT (..) syntax
        boolean resolveExcept = resolveSupported || !SUPPORT_NATIVE_EXCEPT.contains(c.dialect());
        boolean resolveUnqualifiedCombined = resolveSupported || NO_SUPPORT_UNQUALIFIED_COMBINED.contains(c.dialect());

        // [#7921] TODO Find a better, more efficient way to resolve asterisks
        SelectFieldList<SelectFieldOrAsterisk> list = getSelectResolveImplicitAsterisks();

        for (SelectFieldOrAsterisk f : list)
            if (f instanceof Field<?>)
                result.add(getResolveProjection(c, (Field<?>) f));
            else if (f instanceof QualifiedAsterisk)
                if (((QualifiedAsteriskImpl) f).fields.isEmpty())
                    if (resolveSupported)
                        result.addAll(Arrays.asList(((QualifiedAsterisk) f).qualifier().fields()));
                    else
                        result.add(f);
                else if (resolveExcept)
                    result.addAll(subtract(Arrays.asList(((QualifiedAsterisk) f).qualifier().fields()), (((QualifiedAsteriskImpl) f).fields)));
                else
                    result.add(f);
            else if (f instanceof Asterisk)
                if (((AsteriskImpl) f).fields.isEmpty())
                    if (resolveSupported || resolveUnqualifiedCombined && list.size() > 1)
                        result.addAll(resolveAsterisk(new QueryPartList<>()));
                    else
                        result.add(f);
                else if (resolveExcept)
                    result.addAll(resolveAsterisk(new QueryPartList<>(), ((AsteriskImpl) f).fields));
                else
                    result.add(f);
            else if (f instanceof Row)
                result.add(getResolveProjection(c, new RowField<Row, Record>((Row) f)));
            else
                throw new AssertionError("Type not supported: " + f);

        return result;
    }

    private final Field<?> getResolveProjection(Configuration c, Field<?> f) {


















        return f;
    }

    private final <Q extends QueryPartList<? super Field<?>>> Q resolveAsterisk(Q result) {
        return resolveAsterisk(result, null);
    }

    private final <Q extends QueryPartList<? super Field<?>>> Q resolveAsterisk(Q result, QueryPartList<Field<?>> except) {
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

    private static final /* record */ class SelectFieldIndexes { private final boolean mapped; private final int[] mapping; private final int[] projectionSizes; public SelectFieldIndexes(boolean mapped, int[] mapping, int[] projectionSizes) { this.mapped = mapped; this.mapping = mapping; this.projectionSizes = projectionSizes; } public boolean mapped() { return mapped; } public int[] mapping() { return mapping; } public int[] projectionSizes() { return projectionSizes; } @Override public boolean equals(Object o) { if (!(o instanceof SelectFieldIndexes)) return false; SelectFieldIndexes other = (SelectFieldIndexes) o; if (!java.util.Objects.equals(this.mapped, other.mapped)) return false; if (!java.util.Objects.equals(this.mapping, other.mapping)) return false; if (!java.util.Objects.equals(this.projectionSizes, other.projectionSizes)) return false; return true; } @Override public int hashCode() { return java.util.Objects.hash(this.mapped, this.mapping, this.projectionSizes); } @Override public String toString() { return new StringBuilder("SelectFieldIndexes[").append("mapped=").append(this.mapped).append(", mapping=").append(this.mapping).append(", projectionSizes=").append(this.projectionSizes).append("]").toString(); } }

    /**
     * [#11904] Get a mapping { projected field index -> generated field index }
     */
    private final SelectFieldIndexes getSelectFieldIndexes(Context<?> ctx) {
        List<Field<?>> s = getSelect();
        boolean mapped = false;
        int[] mapping = new int[s.size()];
        int[] projectionSizes = new int[s.size()];

        if (RowField.NO_NATIVE_SUPPORT.contains(ctx.dialect())) {
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

    final void setGrouping() {
        grouping = true;
    }

    final ConditionProviderImpl getWhere(Context<?> ctx) {
        ConditionProviderImpl result = new ConditionProviderImpl();

        if (condition.hasWhere())
            result.addConditions(condition.getWhere());

        // Apply SEEK predicates in the WHERE clause only if:
        // - There is an ORDER BY clause (SEEK is non-deterministic)
        // - There is a SEEK clause (obvious case)
        // - There are no unions (union is nested in derived table
        //   and SEEK predicate is applied outside). See [#7459]
        if (!getOrderBy().isEmpty() && !getSeek().isEmpty() && unionOp.isEmpty())
            result.addConditions(getSeekCondition());







        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    final Condition getSeekCondition() {
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
            if (o.get(0).getOrder() != DESC ^ seekBefore)
                c = row(o.fields()).gt(row(getSeek()));
            else
                c = row(o.fields()).lt(row(getSeek()));
        }

        // With alternating sorting, the SEEK clause has to be explicitly
        // phrased for each ORDER BY field.
        else {
            ConditionProviderImpl or = new ConditionProviderImpl();

            for (int i = 0; i < o.size(); i++) {
                ConditionProviderImpl and = new ConditionProviderImpl();

                for (int j = 0; j < i; j++)
                    and.addConditions(((Field) ((SortFieldImpl<?>) o.get(j)).getField()).eq(getSeek().get(j)));

                SortFieldImpl<?> s = (SortFieldImpl<?>) o.get(i);
                if (s.getOrder() != DESC ^ seekBefore)
                    and.addConditions(((Field) s.getField()).gt(getSeek().get(i)));
                else
                    and.addConditions(((Field) s.getField()).lt(getSeek().get(i)));

                or.addConditions(OR, and);
            }

            c = or;
        }

        return c;
    }















    final ConditionProviderImpl getHaving() {
        return having;
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

        for (Field<?> field : getSelect())
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
    public final void addFrom(Collection<? extends TableLike<?>> f) {
        for (TableLike<?> provider : f)
            getFrom().add(provider.asTable());
    }

    @Override
    public final void addFrom(TableLike<?> f) {
        getFrom().add(f.asTable());
    }

    @Override
    public final void addFrom(TableLike<?>... f) {
        for (TableLike<?> provider : f)
            getFrom().add(provider.asTable());
    }
























    @Override
    public final void addGroupBy(Collection<? extends GroupField> fields) {
        setGrouping();

        if (groupBy == null)
            groupBy = new QueryPartList<>();

        groupBy.addAll(fields);
    }

    @Override
    public final void addGroupBy(GroupField... fields) {
        addGroupBy(Arrays.asList(fields));
    }

    @Override
    public final void addHaving(Condition conditions) {
        getHaving().addConditions(conditions);
    }

    @Override
    public final void addHaving(Condition... conditions) {
        getHaving().addConditions(conditions);
    }

    @Override
    public final void addHaving(Collection<? extends Condition> conditions) {
        getHaving().addConditions(conditions);
    }

    @Override
    public final void addHaving(Operator operator, Condition conditions) {
        getHaving().addConditions(operator, conditions);
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
    public final SelectQueryImpl<R> unionAll(Select<? extends R> other) {
        return combine(UNION_ALL, other);
    }

    @Override
    public final SelectQueryImpl<R> except(Select<? extends R> other) {
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
        addJoin0(table, type, conditions, null);
    }

    @Override
    public final void addJoin(TableLike<?> table, JoinType type, Condition... conditions) {
        addJoin0(table, type, conditions, null);
    }















    private final void addJoin0(TableLike<?> table, JoinType type, Object conditions, Field<?>[] partitionBy) {

        // TODO: This and similar methods should be refactored, patterns extracted...
        int index = getFrom().size() - 1;
        Table<?> joined = null;

        switch (type) {
            case JOIN:
            case STRAIGHT_JOIN:
            case LEFT_SEMI_JOIN:
            case LEFT_ANTI_JOIN:
            case FULL_OUTER_JOIN: {
                TableOptionalOnStep<Record> o = getFrom().get(index).join(table, type);

                if (conditions instanceof Condition)
                    joined = o.on((Condition) conditions);
                else
                    joined = o.on((Condition[]) conditions);

                break;
            }

            case LEFT_OUTER_JOIN:
            case RIGHT_OUTER_JOIN: {
                TablePartitionByStep<?> p = (TablePartitionByStep<?>) getFrom().get(index).join(table, type);
                TableOnStep<?> o = p;




                if (conditions instanceof Condition)
                    joined = o.on((Condition) conditions);
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
}
