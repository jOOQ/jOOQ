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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
import static org.jooq.JoinType.CROSS_APPLY;
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
import static org.jooq.JoinType.OUTER_APPLY;
import static org.jooq.JoinType.RIGHT_OUTER_JOIN;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.Keywords.K_CROSS_JOIN_LATERAL;
import static org.jooq.impl.Keywords.K_LEFT_JOIN_LATERAL;
import static org.jooq.impl.Keywords.K_LEFT_OUTER_JOIN_LATERAL;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Keywords.K_PARTITION_BY;
import static org.jooq.impl.Keywords.K_USING;
import static org.jooq.impl.Names.N_JOIN;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.Tools.containsUnaliasedTable;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_COLLECT_SEMI_ANTI_JOIN;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_COLLECTED_SEMI_ANTI_JOIN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
// ...
import org.jooq.SQL;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnConditionStep;
import org.jooq.TableOptionalOnStep;
import org.jooq.TableOptions;
import org.jooq.TableOuterJoinStep;
import org.jooq.TablePartitionByStep;
// ...
import org.jooq.conf.RenderOptionalKeyword;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.QOM.Lateral;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * A table consisting of two joined tables and possibly a join condition
 *
 * @author Lukas Eder
 */
abstract class JoinTable<J extends JoinTable<J>>
extends
     AbstractTable<Record>
implements
    TableOuterJoinStep<Record>,
    TableOptionalOnStep<Record>,
    TablePartitionByStep<Record>,
    TableOnConditionStep<Record>
{

    static final Clause[]         CLAUSES                    = { TABLE, TABLE_JOIN };






    static final Set<SQLDialect>  EMULATE_NATURAL_JOIN       = SQLDialect.supportedBy(CUBRID);
    static final Set<SQLDialect>  EMULATE_NATURAL_OUTER_JOIN = SQLDialect.supportedBy(CUBRID, H2, IGNITE);
    static final Set<SQLDialect>  EMULATE_JOIN_USING         = SQLDialect.supportedBy(CUBRID, IGNITE);
    static final Set<SQLDialect>  EMULATE_APPLY              = SQLDialect.supportedBy(FIREBIRD, POSTGRES, YUGABYTEDB);

    final Table<?>                lhs;
    final Table<?>                rhs;
    final QueryPartList<Field<?>> lhsPartitionBy;
    final QueryPartList<Field<?>> rhsPartitionBy;

    final JoinType                type;
    final ConditionProviderImpl   condition;
    final QueryPartList<Field<?>> using;

    JoinTable(TableLike<?> lhs, TableLike<?> rhs, JoinType type) {
        this(lhs, rhs, type, emptyList());
    }

    JoinTable(TableLike<?> lhs, TableLike<?> rhs, JoinType type, Collection<? extends Field<?>> lhsPartitionBy) {
        super(TableOptions.expression(), N_JOIN);

        this.lhs = lhs.asTable();
        this.rhs = rhs.asTable();
        this.lhsPartitionBy = new QueryPartList<>(lhsPartitionBy);
        this.rhsPartitionBy = new QueryPartList<>();
        this.type = type;
        this.condition = new ConditionProviderImpl();
        this.using = new QueryPartList<>();
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    final J transform(Table<?> newLhs, Table<?> newRhs) {
        if (lhs == newLhs && rhs == newRhs)
            return (J) this;

        return construct(newLhs, lhsPartitionBy, rhsPartitionBy, newRhs, condition, using);
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
                if (TRUE.equals(ctx.data(DATA_COLLECT_SEMI_ANTI_JOIN))) {

                    @SuppressWarnings("unchecked")
                    List<Condition> semiAntiJoinPredicates = (List<Condition>) ctx.data(DATA_COLLECTED_SEMI_ANTI_JOIN);

                    if (semiAntiJoinPredicates == null) {
                        semiAntiJoinPredicates = new ArrayList<>();
                        ctx.data(DATA_COLLECTED_SEMI_ANTI_JOIN, semiAntiJoinPredicates);
                    }

                    Condition c = !using.isEmpty() ? usingCondition() : condition;
                    switch (translatedType) {
                        case LEFT_SEMI_JOIN:
                            semiAntiJoinPredicates.add(exists(selectOne().from(rhs).where(c)));
                            break;

                        case LEFT_ANTI_JOIN:
                            semiAntiJoinPredicates.add(notExists(selectOne().from(rhs).where(c)));
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
        else if (OUTER_APPLY == translatedType && EMULATE_APPLY.contains(ctx.dialect())) {
            ctx.formatIndentStart()
               .formatSeparator()
               .start(TABLE_JOIN_ON)
               .visit(K_ON)
               .sql(" 1 = 1")
               .end(TABLE_JOIN_ON)
               .formatIndentEnd();
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

            default:
                keyword = translatedType.toKeyword();
                break;
        }

        if (translatedType == CROSS_APPLY && EMULATE_APPLY.contains(ctx.dialect()))
            keyword = K_CROSS_JOIN_LATERAL;
        else if (translatedType == OUTER_APPLY && EMULATE_APPLY.contains(ctx.dialect()))
            if (ctx.settings().getRenderOptionalOuterKeyword() == RenderOptionalKeyword.OFF)
                keyword = K_LEFT_JOIN_LATERAL;
            else
                keyword = K_LEFT_OUTER_JOIN_LATERAL;


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

        ctx.visit(table);

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

        // Regular JOIN condition
        else {
            toSQLJoinCondition(ctx, condition);
        }
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
        return new TableAlias<>(this, alias, c -> true);
    }

    @Override
    public final Table<Record> as(Name alias, Name... fieldAliases) {
        return new TableAlias<>(this, alias, fieldAliases, c -> true);
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
    public final J on(Field<Boolean> c) {
        return on(condition(c));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J on(SQL sql) {
        and(sql);
        return (J) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J on(String sql) {
        and(sql);
        return (J) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J on(String sql, Object... bindings) {
        and(sql, bindings);
        return (J) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J on(String sql, QueryPart... parts) {
        and(sql, parts);
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
            throw onKeyException(OnKeyExceptionReason.NOT_FOUND, leftToRight, rightToLeft);
        else
            throw onKeyException(OnKeyExceptionReason.AMBIGUOUS, null, null);
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

            if (containsUnaliasedTable(lhs, keyFields[0].getTable())) {
                for (ForeignKey<?, ?> key : lhs.getReferences())
                    if (key.getFields().containsAll(unaliased) && unaliased.containsAll(key.getFields()))
                        return onKey(key);

                for (ForeignKey<?, ?> key : lhs.getReferences())
                    if (key.getFields().containsAll(unaliased))
                        return onKey(key);
            }
            else if (containsUnaliasedTable(rhs, keyFields[0].getTable())) {
                for (ForeignKey<?, ?> key : rhs.getReferences())
                    if (key.getFields().containsAll(unaliased) && unaliased.containsAll(key.getFields()))
                        return onKey(key);

                for (ForeignKey<?, ?> key : rhs.getReferences())
                    if (key.getFields().containsAll(unaliased))
                        return onKey(key);
            }
        }

        throw onKeyException(OnKeyExceptionReason.NOT_FOUND, null, null);
    }

    @Override
    public final J onKey(ForeignKey<?, ?> key) {
        if (containsUnaliasedTable(lhs, key.getTable()))
            return onKey(key, lhs, rhs);
        else if (containsUnaliasedTable(rhs, key.getTable()))
            return onKey(key, rhs, lhs);

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

    @Override
    public final J using(Field<?>... fields) {
        return using(asList(fields));
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

    @Override
    public final J and(Field<Boolean> c) {
        return and(condition(c));
    }

    @Override
    public final J and(SQL sql) {
        return and(condition(sql));
    }

    @Override
    public final J and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final J and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final J and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final J andNot(Condition c) {
        return and(c.not());
    }

    @Override
    public final J andNot(Field<Boolean> c) {
        return andNot(condition(c));
    }

    @Override
    public final J andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final J andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J or(Condition c) {
        condition.addConditions(Operator.OR, c);
        return (J) this;
    }

    @Override
    public final J or(Field<Boolean> c) {
        return or(condition(c));
    }

    @Override
    public final J or(SQL sql) {
        return or(condition(sql));
    }

    @Override
    public final J or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final J or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final J or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final J orNot(Condition c) {
        return or(c.not());
    }

    @Override
    public final J orNot(Field<Boolean> c) {
        return orNot(condition(c));
    }

    @Override
    public final J orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final J orNotExists(Select<?> select) {
        return or(notExists(select));
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
        Collection<? extends Field<?>> using
    );

    public final Table<?> $table1() {
        return lhs;
    }

    public final J $table1(Table<?> t1) {
        return construct(t1, $partitionBy1(), $partitionBy2(), $table2(), $on(), $using());
    }

    public final UnmodifiableList<Field<?>> $partitionBy1() {
        return QOM.unmodifiable(lhsPartitionBy);
    }

    public final J $partitionBy1(Collection<? extends Field<?>> p1) {
        return construct($table1(), p1, $partitionBy2(), $table2(), $on(), $using());
    }

    public final UnmodifiableList<Field<?>> $partitionBy2() {
        return QOM.unmodifiable(rhsPartitionBy);
    }

    public final J $partitionBy2(Collection<? extends Field<?>> p2) {
        return construct($table1(), $partitionBy1(), p2, $table2(), $on(), $using());
    }

    public final Table<?> $table2() {
        return rhs;
    }

    public final J $table2(Table<?> t2) {
        return construct($table1(), $partitionBy1(), $partitionBy2(), t2, $on(), $using());
    }

    public final Condition $on() {
        return condition.getWhereOrNull();
    }

    public final J $on(Condition o) {
        return construct($table1(), $partitionBy1(), $partitionBy2(), $table2(), o, emptyList());
    }

    public final UnmodifiableList<Field<?>> $using() {
        return QOM.unmodifiable(using);
    }

    public final J $using(Collection<? extends Field<?>> u) {
        return construct($table1(), $partitionBy1(), $partitionBy2(), $table2(), null, u);
    }
































}
