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

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_JOIN;
import static org.jooq.Clause.TABLE_JOIN_ANTI_LEFT;
import static org.jooq.Clause.TABLE_JOIN_CROSS;
import static org.jooq.Clause.TABLE_JOIN_CROSS_APPLY;
import static org.jooq.Clause.TABLE_JOIN_INNER;
import static org.jooq.Clause.TABLE_JOIN_NATURAL;
import static org.jooq.Clause.TABLE_JOIN_NATURAL_OUTER_FULL;
import static org.jooq.Clause.TABLE_JOIN_NATURAL_OUTER_LEFT;
import static org.jooq.Clause.TABLE_JOIN_NATURAL_OUTER_RIGHT;
import static org.jooq.Clause.TABLE_JOIN_ON;
import static org.jooq.Clause.TABLE_JOIN_OUTER_APPLY;
import static org.jooq.Clause.TABLE_JOIN_OUTER_FULL;
import static org.jooq.Clause.TABLE_JOIN_OUTER_LEFT;
import static org.jooq.Clause.TABLE_JOIN_OUTER_RIGHT;
import static org.jooq.Clause.TABLE_JOIN_PARTITION_BY;
import static org.jooq.Clause.TABLE_JOIN_SEMI_LEFT;
import static org.jooq.Clause.TABLE_JOIN_STRAIGHT;
import static org.jooq.Clause.TABLE_JOIN_USING;
import static org.jooq.JoinType.CROSS_JOIN;
import static org.jooq.JoinType.FULL_OUTER_JOIN;
import static org.jooq.JoinType.JOIN;
import static org.jooq.JoinType.LEFT_ANTI_JOIN;
import static org.jooq.JoinType.LEFT_OUTER_JOIN;
import static org.jooq.JoinType.LEFT_SEMI_JOIN;
import static org.jooq.JoinType.NATURAL_FULL_OUTER_JOIN;
import static org.jooq.JoinType.NATURAL_JOIN;
import static org.jooq.JoinType.NATURAL_LEFT_OUTER_JOIN;
import static org.jooq.JoinType.NATURAL_RIGHT_OUTER_JOIN;
import static org.jooq.JoinType.RIGHT_OUTER_JOIN;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
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
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.ConditionProviderImpl.extractCondition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.Keywords.K_ANTI_JOIN;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Keywords.K_PARTITION_BY;
import static org.jooq.impl.Keywords.K_SEMI_JOIN;
import static org.jooq.impl.Keywords.K_USING;
import static org.jooq.impl.Names.N_JOIN;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.TableImpl.path;
import static org.jooq.impl.Tools.containsTable;
import static org.jooq.impl.Tools.containsUnaliasedTable;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.visitAutoAliased;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_COLLECT_SEMI_ANTI_JOIN;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_RENDER_IMPLICIT_JOIN;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_COLLECTED_SEMI_ANTI_JOIN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JoinType;
import org.jooq.Keyword;
import org.jooq.Name;
import org.jooq.Operator;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
// ...
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOptions;
// ...
import org.jooq.conf.RenderOptionalKeyword;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.QOM.JoinHint;
import org.jooq.impl.QOM.Lateral;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * A table consisting of two joined tables and possibly a join condition.
 *
 * @author Lukas Eder
 */
abstract class JoinTable<J extends JoinTable<J>> extends AbstractJoinTable<J> {

    static final Clause[]         CLAUSES                    = { TABLE, TABLE_JOIN };








    static final Set<SQLDialect>  EMULATE_NATURAL_JOIN       = SQLDialect.supportedBy(CLICKHOUSE, CUBRID, TRINO);
    static final Set<SQLDialect>  EMULATE_NATURAL_OUTER_JOIN = SQLDialect.supportedBy(CLICKHOUSE, CUBRID, H2, IGNITE, TRINO);
    static final Set<SQLDialect>  EMULATE_JOIN_USING         = SQLDialect.supportedBy(CUBRID, IGNITE);
    static final Set<SQLDialect>  EMULATE_APPLY              = SQLDialect.supportedBy(FIREBIRD, POSTGRES, TRINO, YUGABYTEDB);
    static final Set<SQLDialect>  EMUlATE_SEMI_ANTI_JOIN     = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB);
    static final Set<SQLDialect>  NO_SUPPORT_NESTED_JOIN     = SQLDialect.supportedBy(CLICKHOUSE);

    final Table<?>                lhs;
    final Table<?>                rhs;
    final QueryPartList<Field<?>> lhsPartitionBy;
    final QueryPartList<Field<?>> rhsPartitionBy;

    final JoinType                type;
    final JoinHint                hint;
    final ConditionProviderImpl   condition;
    final QueryPartList<Field<?>> using;

    JoinTable(TableLike<?> lhs, TableLike<?> rhs, JoinType type, JoinHint hint) {
        this(lhs, rhs, type, hint, emptyList());
    }

    JoinTable(TableLike<?> lhs, TableLike<?> rhs, JoinType type, JoinHint hint, Collection<? extends Field<?>> lhsPartitionBy) {
        super(TableOptions.expression(), N_JOIN);

        this.lhs = lhs.asTable();
        this.rhs = rhs.asTable();
        this.lhsPartitionBy = new QueryPartList<>(lhsPartitionBy);
        this.rhsPartitionBy = new QueryPartList<>();
        this.type = type;
        this.hint = hint;
        this.condition = new ConditionProviderImpl();
        this.using = new QueryPartList<>();
    }

    @Deprecated
    final J transform(Table<?> newLhs, Table<?> newRhs) {
        return transform(newLhs, newRhs, condition);
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    final J transform(Table<?> newLhs, Table<?> newRhs, ConditionProviderImpl newCondition) {
        if (lhs == newLhs && rhs == newRhs && condition == newCondition)
            return (J) this;

        return construct(newLhs, lhsPartitionBy, rhsPartitionBy, newRhs, newCondition, using, hint);
    }

    // ------------------------------------------------------------------------
    // Table API
    // ------------------------------------------------------------------------

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final List<ForeignKey<Record, ?>> getReferences() {
        List<? extends ForeignKey<?, ?>> lhsReferences = lhs.getReferences();
        List<? extends ForeignKey<?, ?>> rhsReferences = rhs.getReferences();
        List<ForeignKey<?, ?>> result = new ArrayList<>(lhsReferences.size() + rhsReferences.size());
        result.addAll(lhsReferences);
        result.addAll(rhsReferences);
        return (List) result;
    }

    @Override
    public final void accept(Context<?> ctx) {
        boolean lpath = path(lhs) != null;
        boolean rpath = path(rhs) != null;
        boolean path = lpath || rpath;







        // [#14985] APPLY or LATERAL with path joins
        if ((this instanceof CrossApply || this instanceof OuterApply) && rpath)
            ctx.visit($table2(selectFrom(rhs).asTable(rhs)));
        else if (rhs instanceof Lateral && path(((Lateral<?>) rhs).$arg1()) != null)
            ctx.visit($table2(lateral(selectFrom(((Lateral<?>) rhs).$arg1()).asTable(((Lateral<?>) rhs).$arg1()))));
        else if (type == NATURAL_JOIN && path)
            ctx.visit(lhs.join(rhs, JOIN, hint).on(naturalCondition()));
        else if (type == NATURAL_LEFT_OUTER_JOIN && path)
            ctx.visit(lhs.join(rhs, LEFT_OUTER_JOIN, hint).on(naturalCondition()));
        else if (type == NATURAL_RIGHT_OUTER_JOIN && path)
            ctx.visit(lhs.join(rhs, RIGHT_OUTER_JOIN, hint).on(naturalCondition()));
        else if (type == NATURAL_FULL_OUTER_JOIN && path)
            ctx.visit(lhs.join(rhs, FULL_OUTER_JOIN, hint).on(naturalCondition()));
        else if (!using.isEmpty() && path)
            ctx.visit(lhs.join(rhs, type, hint).on(usingCondition()));

        // [#14988] Make sure APPLY table reference continues working by wrapping lateral(rhs)
        else if (this instanceof CrossApply && EMULATE_APPLY.contains(ctx.dialect()))
            ctx.visit(lhs.crossJoin(lateral(rhs)));
        else if (this instanceof OuterApply && EMULATE_APPLY.contains(ctx.dialect()))
            ctx.visit(lhs.leftJoin(lateral(rhs)).on(noCondition()));




        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        JoinType translatedType = translateType(ctx);
        Clause translatedClause = translateClause(translatedType);
        Keyword keyword = translateKeyword(ctx, translatedType);

        toSQLTable(ctx, lhs);













        switch (translatedType) {
            case LEFT_SEMI_JOIN:
            case LEFT_ANTI_JOIN:
                if (EMUlATE_SEMI_ANTI_JOIN.contains(ctx.dialect())
                    && TRUE.equals(ctx.data(DATA_COLLECT_SEMI_ANTI_JOIN))
                ) {

                    @SuppressWarnings("unchecked")
                    List<Condition> semiAntiJoinPredicates = (List<Condition>) ctx.data(DATA_COLLECTED_SEMI_ANTI_JOIN);

                    if (semiAntiJoinPredicates == null) {
                        semiAntiJoinPredicates = new ArrayList<>();
                        ctx.data(DATA_COLLECTED_SEMI_ANTI_JOIN, semiAntiJoinPredicates);
                    }

                    switch (translatedType) {
                        case LEFT_SEMI_JOIN:
                            semiAntiJoinPredicates.add(exists(selectOne().from(rhs).where(condition())));
                            break;

                        case LEFT_ANTI_JOIN:
                            semiAntiJoinPredicates.add(notExists(selectOne().from(rhs).where(condition())));
                            break;
                    }

                    return;
                }
        }

        ctx.formatIndentStart()
           .formatSeparator()
           .start(translatedClause)
           .visit(keyword)
           .sql(' ');

        toSQLTable(ctx, rhs);














        // CROSS JOIN and NATURAL JOIN do not have any condition clauses
        if (translatedType.qualified()) {
            ctx.formatIndentStart();
            toSQLJoinCondition(ctx);
            ctx.formatIndentEnd();
        }
        ctx.end(translatedClause)
           .formatIndentEnd();
    }






































































    private final Keyword translateKeyword(Context<?> ctx, JoinType translatedType) {
        Keyword keyword;

        switch (translatedType) {
            case JOIN:
            case NATURAL_JOIN:
                if (ctx.settings().getRenderOptionalInnerKeyword() == RenderOptionalKeyword.ON)
                    keyword = translatedType.toKeyword(true);











                else
                    keyword = translatedType.toKeyword();

                break;

            case LEFT_OUTER_JOIN:
            case NATURAL_LEFT_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:
            case NATURAL_RIGHT_OUTER_JOIN:
            case FULL_OUTER_JOIN:
            case NATURAL_FULL_OUTER_JOIN:
                if (ctx.settings().getRenderOptionalOuterKeyword() == RenderOptionalKeyword.OFF)
                    keyword = translatedType.toKeyword(false);







                else
                    keyword = translatedType.toKeyword();

                break;

            case LEFT_SEMI_JOIN:
                switch (ctx.family()) {
                    case DUCKDB:
                        keyword = K_SEMI_JOIN;
                        break;

                    default:
                        keyword = translatedType.toKeyword();
                        break;
                }

                break;

            case LEFT_ANTI_JOIN:
                switch (ctx.family()) {
                    case DUCKDB:
                        keyword = K_ANTI_JOIN;
                        break;

                    default:
                        keyword = translatedType.toKeyword();
                        break;
                }

                break;

            default:
                keyword = translatedType.toKeyword();
                break;
        }





        return keyword;
    }
































    private void toSQLTable(Context<?> ctx, Table<?> table) {

        // [#671] Some databases formally require nested JOINS on the right hand
        // side of the join expression to be wrapped in parentheses (e.g. MySQL).
        // In other databases, it's a good idea to wrap them all
        boolean wrap = table instanceof JoinTable && (table == rhs



            );

        if (wrap)
            ctx.sqlIndentStart('(');

        visitAutoAliased(ctx, table, Context::declareTables, (c, t) -> c.visit(t));

        if (wrap)
            ctx.sqlIndentEnd(')');
    }

    /**
     * Translate the join type into a join clause
     */
    final Clause translateClause(JoinType translatedType) {
        switch (translatedType) {
            case JOIN:                     return TABLE_JOIN_INNER;
            case CROSS_JOIN:               return TABLE_JOIN_CROSS;
            case NATURAL_JOIN:             return TABLE_JOIN_NATURAL;
            case LEFT_OUTER_JOIN:          return TABLE_JOIN_OUTER_LEFT;
            case RIGHT_OUTER_JOIN:         return TABLE_JOIN_OUTER_RIGHT;
            case FULL_OUTER_JOIN:          return TABLE_JOIN_OUTER_FULL;
            case NATURAL_LEFT_OUTER_JOIN:  return TABLE_JOIN_NATURAL_OUTER_LEFT;
            case NATURAL_RIGHT_OUTER_JOIN: return TABLE_JOIN_NATURAL_OUTER_RIGHT;
            case NATURAL_FULL_OUTER_JOIN:  return TABLE_JOIN_NATURAL_OUTER_FULL;
            case CROSS_APPLY:              return TABLE_JOIN_CROSS_APPLY;
            case OUTER_APPLY:              return TABLE_JOIN_OUTER_APPLY;
            case LEFT_SEMI_JOIN:           return TABLE_JOIN_SEMI_LEFT;
            case LEFT_ANTI_JOIN:           return TABLE_JOIN_ANTI_LEFT;
            case STRAIGHT_JOIN:            return TABLE_JOIN_STRAIGHT;
            default: throw new IllegalArgumentException("Bad join type: " + translatedType);
        }
    }

    /**
     * Translate the join type for SQL rendering
     */
    final JoinType translateType(Context<?> ctx) {
        if (emulateCrossJoin(ctx))
            return JOIN;
        else if (emulateNaturalJoin(ctx))
            return JOIN;
        else if (emulateNaturalLeftOuterJoin(ctx))
            return LEFT_OUTER_JOIN;
        else if (emulateNaturalRightOuterJoin(ctx))
            return RIGHT_OUTER_JOIN;
        else if (emulateNaturalFullOuterJoin(ctx))
            return FULL_OUTER_JOIN;
        else
            return type;
    }

    private final boolean emulateCrossJoin(Context<?> ctx) {
        return false



            ;
    }

    private final boolean emulateNaturalJoin(Context<?> ctx) {
        return type == NATURAL_JOIN && EMULATE_NATURAL_JOIN.contains(ctx.dialect());
    }

    private final boolean emulateNaturalLeftOuterJoin(Context<?> ctx) {
        return type == NATURAL_LEFT_OUTER_JOIN && EMULATE_NATURAL_OUTER_JOIN.contains(ctx.dialect());
    }

    private final boolean emulateNaturalRightOuterJoin(Context<?> ctx) {
        return type == NATURAL_RIGHT_OUTER_JOIN && EMULATE_NATURAL_OUTER_JOIN.contains(ctx.dialect());
    }

    private final boolean emulateNaturalFullOuterJoin(Context<?> ctx) {
        return type == NATURAL_FULL_OUTER_JOIN && EMULATE_NATURAL_OUTER_JOIN.contains(ctx.dialect());
    }

    private final void toSQLJoinCondition(Context<?> ctx) {
        if (!using.isEmpty()) {

            // [#582] Some dialects don't explicitly support a JOIN .. USING
            // syntax. This can be emulated with JOIN .. ON
            if (EMULATE_JOIN_USING.contains(ctx.dialect()))
                toSQLJoinCondition(ctx, usingCondition());

            // Native supporters of JOIN .. USING
            else
                ctx.formatSeparator()
                   .start(TABLE_JOIN_USING)
                   .visit(K_USING)
                   .sql(" (").visit(wrap(using).qualify(false)).sql(')')
                   .end(TABLE_JOIN_USING);
        }

        // [#577] If any NATURAL JOIN syntax needs to be emulated, find out
        // common fields in lhs and rhs of the JOIN clause
        else if (emulateNaturalJoin(ctx) ||
                 emulateNaturalLeftOuterJoin(ctx) ||
                 emulateNaturalRightOuterJoin(ctx) ||
                 emulateNaturalFullOuterJoin(ctx)) {
            toSQLJoinCondition(ctx, naturalCondition());
        }

        // [#14985] Path joins additional conditions
        else if ((TableImpl.path(lhs) != null || TableImpl.path(rhs) != null)

            // Do this only if we're *not* rendering implicit joins, in case of which join paths
            // are expected, and their predicates are already present.
            && ctx.data(DATA_RENDER_IMPLICIT_JOIN) == null
        ) {
            toSQLJoinCondition(ctx, DSL.and(
                lhs instanceof TableImpl<?> ti ? pathConditionIfInCurrentScope(ctx, ti) : noCondition(),
                rhs instanceof TableImpl<?> ti ? pathConditionIfInCurrentScope(ctx, ti) : noCondition(),
                condition.getWhere()
            ));
        }

        // Regular JOIN condition
        else
            toSQLJoinCondition(ctx, condition);
    }

    private final Condition pathConditionIfInCurrentScope(Context<?> ctx, TableImpl<?> ti) {

        // [#15936] Don't add path correlation predicates to JOIN .. ON clauses.
        //          It's wrong for OUTER JOIN or some nested join trees, and they're already
        //          being added in SelectQueryImpl
        return ctx.inCurrentScope(ti.path) ? ti.pathCondition() : noCondition();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    final Condition naturalCondition() {
        List<Condition> conditions = new ArrayList<>(using.size());

        for (Field<?> field : lhs.fields()) {
            Field<?> other = rhs.field(field);

            if (other != null)
                conditions.add(field.eq((Field) other));
        }

        return DSL.and(conditions);
    }

    final boolean hasCondition() {
        return condition.hasWhere() || !using.isEmpty();
    }

    final Condition condition() {
        if (condition.hasWhere())
            return condition.getWhere();
        else if (!using.isEmpty())
            return usingCondition();
        else
            return noCondition();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    final Condition usingCondition() {
        return DSL.and(map(using, f -> Tools.qualify(lhs, f).eq((Field) Tools.qualify(rhs, f))));
    }

    private final void toSQLJoinCondition(Context<?> context, Condition c) {
        context.formatSeparator()
               .start(TABLE_JOIN_ON)
               .visit(K_ON)
               .sql(' ')
               .visit(c)
               .end(TABLE_JOIN_ON);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final Table<Record> as(Name alias) {
        return new TableAlias<>(this, alias, true);
    }

    @Override
    public final Table<Record> as(Name alias, Name... fieldAliases) {
        return new TableAlias<>(this, alias, fieldAliases, true);
    }

    @Override
    public final Class<? extends Record> getRecordType() {

        // [#10183] The RHS does not contribute to the projection in these cases
        if (type == LEFT_SEMI_JOIN || type == LEFT_ANTI_JOIN)
            return lhs.getRecordType();

        // TODO: [#4695] Calculate the correct Record[B] type
        return RecordImplN.class;
    }

    @Override
    final FieldsImpl<Record> fields0() {
        if (type == LEFT_SEMI_JOIN || type == LEFT_ANTI_JOIN) {
            return new FieldsImpl<>(lhs.asTable().fields());
        }
        else {
            Field<?>[] l = lhs.asTable().fields();
            Field<?>[] r = rhs.asTable().fields();
            Field<?>[] all = new Field[l.length + r.length];

            System.arraycopy(l, 0, all, 0, l.length);
            System.arraycopy(r, 0, all, l.length, r.length);

            return new FieldsImpl<>(all);
        }
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    // ------------------------------------------------------------------------
    // Join API
    // ------------------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    final J partitionBy0(Collection<? extends Field<?>> fields) {
        rhsPartitionBy.addAll(fields);
        return (J) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J on(Condition conditions) {
        condition.addConditions(conditions);
        return (J) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J on(Condition... conditions) {
        condition.addConditions(conditions);
        return (J) this;
    }

    @Override
    public final J onKey() throws DataAccessException {
        List<?> leftToRight = lhs.getReferencesTo(rhs);
        List<?> rightToLeft = rhs.getReferencesTo(lhs);

        if (leftToRight.size() == 1 && rightToLeft.size() == 0)
            return onKey((ForeignKey<?, ?>) leftToRight.get(0), lhs, rhs);
        else if (rightToLeft.size() == 1 && leftToRight.size() == 0)
            return onKey((ForeignKey<?, ?>) rightToLeft.get(0), rhs, lhs);

        if (rightToLeft.isEmpty() && leftToRight.isEmpty())
            throw onKeyException(OnKeyExceptionReason.NOT_FOUND, null, null);
        else
            throw onKeyException(OnKeyExceptionReason.AMBIGUOUS, leftToRight, rightToLeft);
    }

    @Override
    public final J onKey(TableField<?, ?>... keyFields) throws DataAccessException {
        if (keyFields != null && keyFields.length > 0) {

            // [#7626] Make sure this works with aliased columns as well
            List<TableField<?, ?>> unaliased = new ArrayList<>(asList(keyFields));
            for (int i = 0; i < unaliased.size(); i++) {
                TableField<?, ?> f = unaliased.get(i);
                Alias<? extends Table<?>> alias = Tools.alias(f.getTable());

                if (alias != null)
                    unaliased.set(i, (TableField<?, ?>) alias.wrapped().field(f));
            }

            // [#14668] Try exact matches of aliases first
            for (boolean unalias : new boolean[] { false, true }) {
                if (containsTable(lhs, keyFields[0].getTable(), unalias)) {

                    // [#11150] Try exact matches of key columns first
                    for (ForeignKey<?, ?> key : lhs.getReferences())
                        if (key.getFields().containsAll(unaliased) && unaliased.containsAll(key.getFields()))
                            return onKey(key, lhs, rhs);

                    for (ForeignKey<?, ?> key : lhs.getReferences())
                        if (key.getFields().containsAll(unaliased))
                            return onKey(key, lhs, rhs);
                }
                else if (containsTable(rhs, keyFields[0].getTable(), unalias)) {

                    // [#11150] Try exact matches of key columns first
                    for (ForeignKey<?, ?> key : rhs.getReferences())
                        if (key.getFields().containsAll(unaliased) && unaliased.containsAll(key.getFields()))
                            return onKey(key, rhs, lhs);

                    for (ForeignKey<?, ?> key : rhs.getReferences())
                        if (key.getFields().containsAll(unaliased))
                            return onKey(key, rhs, lhs);
                }
            }
        }

        throw onKeyException(OnKeyExceptionReason.NOT_FOUND, null, null);
    }

    @Override
    public final J onKey(ForeignKey<?, ?> key) {

        // [#12214] Unlike onKey(TableField...), where the argument field is
        //          expected to be a referencing field, not a referenced field,
        //          here we have to check both ends of the key to avoid
        //          ambiguities
        if (containsUnaliasedTable(lhs, key.getTable()) && containsUnaliasedTable(rhs, key.getKey().getTable()))
            return onKey(key, lhs, rhs);
        else if (containsUnaliasedTable(rhs, key.getTable()) && containsUnaliasedTable(lhs, key.getKey().getTable()))
            return onKey(key, rhs, lhs);
        else
            throw onKeyException(OnKeyExceptionReason.NOT_FOUND, null, null);
    }

    private final J onKey(ForeignKey<?, ?> key, Table<?> fk, Table<?> pk) {
        return and(onKey0(key, fk, pk));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    static final Condition onKey0(ForeignKey<?, ?> key, Table<?> fk, Table<?> pk) {
        Condition result = noCondition();

        TableField<?, ?>[] references = key.getFieldsArray();
        TableField<?, ?>[] referenced = key.getKeyFieldsArray();

        for (int i = 0; i < references.length; i++) {
            Field f1 = fk.field(references[i]);
            Field f2 = pk.field(referenced[i]);

            // [#2870] TODO: If lhs or rhs are aliased tables, extract the appropriate fields from them
            result = result.and(f1.equal(f2));
        }

        return result;
    }

    private enum OnKeyExceptionReason {
        AMBIGUOUS, NOT_FOUND
    }

    private final DataAccessException onKeyException(OnKeyExceptionReason reason, List<?> leftToRight, List<?> rightToLeft) {
        switch (reason) {
            case AMBIGUOUS:
                return new DataAccessException("Key ambiguous between tables [" + lhs + "] and [" + rhs + "]. Found: " + leftToRight + " and " + rightToLeft);
            case NOT_FOUND:
            default:
                return new DataAccessException("No matching Key found between tables [" + lhs + "] and [" + rhs + "]");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J using(Collection<? extends Field<?>> fields) {
        using.addAll(fields);
        return (J) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J and(Condition c) {
        condition.addConditions(c);
        return (J) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J or(Condition c) {
        condition.addConditions(Operator.OR, c);
        return (J) this;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    abstract J construct(
        Table<?> table1,
        Collection<? extends Field<?>> partitionBy1,
        Collection<? extends Field<?>> partitionBy2,
        Table<?> table2,
        Condition on,
        Collection<? extends Field<?>> using,
        JoinHint hint
    );

    public final Table<?> $table1() {
        return lhs;
    }

    public final J $table1(Table<?> t1) {
        return construct(t1, $partitionBy1(), $partitionBy2(), $table2(), $on(), $using(), $hint());
    }

    public final UnmodifiableList<Field<?>> $partitionBy1() {
        return QOM.unmodifiable(lhsPartitionBy);
    }

    public final J $partitionBy1(Collection<? extends Field<?>> p1) {
        return construct($table1(), p1, $partitionBy2(), $table2(), $on(), $using(), $hint());
    }

    public final UnmodifiableList<Field<?>> $partitionBy2() {
        return QOM.unmodifiable(rhsPartitionBy);
    }

    public final J $partitionBy2(Collection<? extends Field<?>> p2) {
        return construct($table1(), $partitionBy1(), p2, $table2(), $on(), $using(), $hint());
    }

    public final Table<?> $table2() {
        return rhs;
    }

    public final J $table2(Table<?> t2) {
        return construct($table1(), $partitionBy1(), $partitionBy2(), t2, $on(), $using(), $hint());
    }

    public final JoinHint $hint() {
        return hint;
    }

    public final J $hint(JoinHint newHint) {
        return construct($table1(), $partitionBy1(), $partitionBy2(), $table2(), $on(), $using(), newHint);
    }

    public final Condition $on() {
        return condition.getWhereOrNull();
    }

    public final J $on(Condition o) {
        return construct($table1(), $partitionBy1(), $partitionBy2(), $table2(), o, emptyList(), $hint());
    }

    public final UnmodifiableList<Field<?>> $using() {
        return QOM.unmodifiable(using);
    }

    public final J $using(Collection<? extends Field<?>> u) {
        return construct($table1(), $partitionBy1(), $partitionBy2(), $table2(), null, u, $hint());
    }

































    // -------------------------------------------------------------------------
    // XXX: Object API
    // -------------------------------------------------------------------------

    @Override
    String toString0(RenderContext ctx) {
        return super.toString0(ctx.declareTables(true));
    }
}
