/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static java.util.Arrays.asList;
import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_JOIN;
import static org.jooq.Clause.TABLE_JOIN_CROSS;
import static org.jooq.Clause.TABLE_JOIN_CROSS_APPLY;
import static org.jooq.Clause.TABLE_JOIN_INNER;
import static org.jooq.Clause.TABLE_JOIN_NATURAL;
import static org.jooq.Clause.TABLE_JOIN_NATURAL_OUTER_LEFT;
import static org.jooq.Clause.TABLE_JOIN_NATURAL_OUTER_RIGHT;
import static org.jooq.Clause.TABLE_JOIN_ON;
import static org.jooq.Clause.TABLE_JOIN_OUTER_APPLY;
import static org.jooq.Clause.TABLE_JOIN_OUTER_FULL;
import static org.jooq.Clause.TABLE_JOIN_OUTER_LEFT;
import static org.jooq.Clause.TABLE_JOIN_OUTER_RIGHT;
import static org.jooq.Clause.TABLE_JOIN_PARTITION_BY;
import static org.jooq.Clause.TABLE_JOIN_USING;
import static org.jooq.JoinType.CROSS_APPLY;
import static org.jooq.JoinType.CROSS_JOIN;
import static org.jooq.JoinType.JOIN;
import static org.jooq.JoinType.LEFT_OUTER_JOIN;
import static org.jooq.JoinType.NATURAL_JOIN;
import static org.jooq.JoinType.NATURAL_LEFT_OUTER_JOIN;
import static org.jooq.JoinType.NATURAL_RIGHT_OUTER_JOIN;
import static org.jooq.JoinType.OUTER_APPLY;
import static org.jooq.JoinType.RIGHT_OUTER_JOIN;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.H2;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JoinType;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnConditionStep;
import org.jooq.TableOnStep;
import org.jooq.TableOptionalOnStep;
import org.jooq.exception.DataAccessException;

/**
 * A table consisting of two joined tables and possibly a join condition
 *
 * @author Lukas Eder
 */
class JoinTable extends AbstractTable<Record> implements TableOptionalOnStep, TableOnConditionStep {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = 8377996833996498178L;
    private static final Clause[]         CLAUSES          = { TABLE, TABLE_JOIN };

    private final Table<?>                lhs;
    private final Table<?>                rhs;
    private final QueryPartList<Field<?>> rhsPartitionBy;

    private final JoinType                type;
    private final ConditionProviderImpl   condition;
    private final QueryPartList<Field<?>> using;

    JoinTable(TableLike<?> lhs, TableLike<?> rhs, JoinType type) {
        super("join");

        this.lhs = lhs.asTable();
        this.rhs = rhs.asTable();
        this.rhsPartitionBy = new QueryPartList<Field<?>>();
        this.type = type;

        this.condition = new ConditionProviderImpl();
        this.using = new QueryPartList<Field<?>>();
    }

    // ------------------------------------------------------------------------
    // Table API
    // ------------------------------------------------------------------------

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final List<ForeignKey<Record, ?>> getReferences() {
        List<ForeignKey<?, ?>> result = new ArrayList<ForeignKey<?, ?>>();

        result.addAll(lhs.getReferences());
        result.addAll(rhs.getReferences());

        return (List) result;
    }

    @Override
    public final void accept(Context<?> ctx) {
        JoinType translatedType = translateType(ctx);
        Clause translatedClause = translateClause(translatedType);

        String keyword = translatedType.toSQL();

        /* [pro] xx
        xx xx xx xxxxxxx xxx xxxxx xxxxxxx xx xxx xxxxxxxx
        xx xxxxxxxxxxxxxxx xx xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxx x
            xxxxxxx x xxxxxx xxxxxx
        x
        xx [/pro] */

        toSQLTable(ctx, lhs);

        ctx.formatIndentStart()
               .formatSeparator()
               .start(translatedClause)
               .keyword(keyword)
               .sql(' ');

        toSQLTable(ctx, rhs);

        // [#1645] The Oracle PARTITION BY clause can be put to the right of an
        // OUTER JOINed table
        if (!rhsPartitionBy.isEmpty()) {
            ctx.formatSeparator()
                   .start(TABLE_JOIN_PARTITION_BY)
                   .keyword("partition by")
                   .sql(" (")
                   .visit(rhsPartitionBy)
                   .sql(')')
                   .end(TABLE_JOIN_PARTITION_BY);
        }

        // CROSS JOIN and NATURAL JOIN do not have any condition clauses
        if (!asList(CROSS_JOIN,
                    NATURAL_JOIN,
                    NATURAL_LEFT_OUTER_JOIN,
                    NATURAL_RIGHT_OUTER_JOIN,
                    CROSS_APPLY,
                    OUTER_APPLY).contains(translatedType)) {
            toSQLJoinCondition(ctx);
        }

        ctx.end(translatedClause)
               .formatIndentEnd();
    }

    private void toSQLTable(Context<?> ctx, Table<?> table) {

        // [#671] Some databases formally require nested JOINS on the right hand
        // side of the join expression to be wrapped in parentheses (e.g. MySQL).
        // In other databases, it's a good idea to wrap them all
        boolean wrap = table instanceof JoinTable &&
            (table == rhs || asList().contains(ctx.configuration().dialect().family()));

        if (wrap) {
            ctx.sql('(')
               .formatIndentStart()
               .formatNewLine();
        }

        ctx.visit(table);

        if (wrap) {
            ctx.formatIndentEnd()
               .formatNewLine()
               .sql(')');
        }
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
            case CROSS_APPLY:              return TABLE_JOIN_CROSS_APPLY;
            case OUTER_APPLY:              return TABLE_JOIN_OUTER_APPLY;
            default: throw new IllegalArgumentException("Bad join type: " + translatedType);
        }
    }

    /**
     * Translate the join type for SQL rendering
     */
    final JoinType translateType(Context<?> context) {
        if (emulateCrossJoin(context)) {
            return JOIN;
        }
        else if (emulateNaturalJoin(context)) {
            return JOIN;
        }
        else if (emulateNaturalLeftOuterJoin(context)) {
            return LEFT_OUTER_JOIN;
        }
        else if (emulateNaturalRightOuterJoin(context)) {
            return RIGHT_OUTER_JOIN;
        }
        else {
            return type;
        }
    }

    private final boolean emulateCrossJoin(Context<?> context) {
        return type == CROSS_JOIN && asList().contains(context.configuration().dialect().family());
    }

    private final boolean emulateNaturalJoin(Context<?> context) {
        return type == NATURAL_JOIN && asList(CUBRID).contains(context.configuration().dialect().family());
    }

    private final boolean emulateNaturalLeftOuterJoin(Context<?> context) {
        return type == NATURAL_LEFT_OUTER_JOIN && asList(CUBRID, H2).contains(context.family());
    }

    private final boolean emulateNaturalRightOuterJoin(Context<?> context) {
        return type == NATURAL_RIGHT_OUTER_JOIN && asList(CUBRID, H2).contains(context.family());
    }

    private final void toSQLJoinCondition(Context<?> context) {
        if (!using.isEmpty()) {

            // [#582] Some dialects don't explicitly support a JOIN .. USING
            // syntax. This can be simulated with JOIN .. ON
            if (asList(CUBRID, H2).contains(context.family())) {
                boolean first = true;
                for (Field<?> field : using) {
                    context.formatSeparator();

                    if (first) {
                        first = false;

                        context.start(TABLE_JOIN_ON)
                               .keyword("on");
                    }
                    else {
                        context.keyword("and");
                    }

                    context.sql(' ')
                           .visit(lhs.field(field))
                           .sql(" = ")
                           .visit(rhs.field(field));
                }

                context.end(TABLE_JOIN_ON);
            }

            // Native supporters of JOIN .. USING
            else {
                context.formatSeparator()
                       .start(TABLE_JOIN_USING)
                       .keyword("using")
                       .sql('(');
                Utils.fieldNames(context, using);
                context.sql(')')
                       .end(TABLE_JOIN_USING);
            }
        }

        // [#577] If any NATURAL JOIN syntax needs to be simulated, find out
        // common fields in lhs and rhs of the JOIN clause
        else if (emulateNaturalJoin(context) ||
                 emulateNaturalLeftOuterJoin(context) ||
                 emulateNaturalRightOuterJoin(context)) {

            boolean first = true;
            for (Field<?> field : lhs.fields()) {
                Field<?> other = rhs.field(field);

                if (other != null) {
                    context.formatSeparator();

                    if (first) {
                        first = false;

                        context.start(TABLE_JOIN_ON)
                               .keyword("on");
                    }
                    else {
                        context.keyword("and");
                    }

                    context.sql(' ')
                           .visit(field)
                           .sql(" = ")
                           .visit(other);
                }
            }

            context.end(TABLE_JOIN_ON);
        }

        // Regular JOIN condition
        else {
            context.formatSeparator()
                   .start(TABLE_JOIN_ON)
                   .keyword("on")
                   .sql(' ')
                   .visit(condition)
                   .end(TABLE_JOIN_ON);
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final Table<Record> as(String alias) {
        return new TableAlias<Record>(this, alias, true);
    }

    @Override
    public final Table<Record> as(String alias, String... fieldAliases) {
        return new TableAlias<Record>(this, alias, fieldAliases, true);
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    final Fields<Record> fields0() {
        Field<?>[] l = lhs.asTable().fields();
        Field<?>[] r = rhs.asTable().fields();
        Field<?>[] all = new Field[l.length + r.length];

        System.arraycopy(l, 0, all, 0, l.length);
        System.arraycopy(r, 0, all, l.length, r.length);

        return new Fields<Record>(all);
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    // ------------------------------------------------------------------------
    // Join API
    // ------------------------------------------------------------------------

    /* [pro] xx
    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxx xxxxxxx x
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxx
    x

    xx [/pro] */
    @Override
    public final JoinTable on(Condition... conditions) {
        condition.addConditions(conditions);
        return this;
    }

    @Override
    public final JoinTable on(Field<Boolean> c) {
        return on(condition(c));
    }

    @Override
    public final JoinTable on(String sql) {
        and(sql);
        return this;
    }

    @Override
    public final JoinTable on(String sql, Object... bindings) {
        and(sql, bindings);
        return this;
    }

    @Override
    public final JoinTable on(String sql, QueryPart... parts) {
        and(sql, parts);
        return this;
    }

    @Override
    public final JoinTable onKey() throws DataAccessException {
        List<?> leftToRight = lhs.getReferencesTo(rhs);
        List<?> rightToLeft = rhs.getReferencesTo(lhs);

        if (leftToRight.size() == 1 && rightToLeft.size() == 0) {
            return onKey((ForeignKey<?, ?>) leftToRight.get(0), lhs, rhs);
        }
        else if (rightToLeft.size() == 1 && leftToRight.size() == 0) {
            return onKey((ForeignKey<?, ?>) rightToLeft.get(0), rhs, lhs);
        }

        throw onKeyException();
    }

    @Override
    public final JoinTable onKey(TableField<?, ?>... keyFields) throws DataAccessException {
        if (keyFields != null && keyFields.length > 0) {
            if (keyFields[0].getTable().equals(unaliased(lhs))) {
                for (ForeignKey<?, ?> key : lhs.getReferences()) {
                    if (key.getFields().containsAll(asList(keyFields))) {
                        return onKey(key);
                    }
                }
            }
            else if (keyFields[0].getTable().equals(unaliased(rhs))) {
                for (ForeignKey<?, ?> key : rhs.getReferences()) {
                    if (key.getFields().containsAll(asList(keyFields))) {
                        return onKey(key);
                    }
                }
            }
        }

        throw onKeyException();
    }

    @Override
    public final JoinTable onKey(ForeignKey<?, ?> key) {
        if (key.getTable().equals(unaliased(lhs)))
            return onKey(key, lhs, rhs);
        else if (key.getTable().equals(unaliased(rhs)))
            return onKey(key, rhs, lhs);

        throw onKeyException();
    }

    private final Table<?> unaliased(Table<?> aliased) {
        return
            (aliased instanceof TableImpl)
          ? (((TableImpl<?>) aliased).alias == null ? (TableImpl<?>) aliased : ((TableImpl<?>) aliased).alias.wrapped())
          : (aliased instanceof TableAlias)
          ? ((TableAlias<?>) aliased).alias.wrapped()
          : aliased;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final JoinTable onKey(ForeignKey<?, ?> key, Table<?> fk, Table<?> pk) {
        JoinTable result = this;

        TableField<?, ?>[] references = key.getFieldsArray();
        TableField<?, ?>[] referenced = key.getKey().getFieldsArray();

        for (int i = 0; i < references.length; i++) {
            Field f1 = fk.field(references[i]);
            Field f2 = pk.field(referenced[i]);

            // [#2870] TODO: If lhs or rhs are aliased tables, extract the appropriate fields from them
            result.and(f1.equal(f2));
        }

        return result;
    }

    private final DataAccessException onKeyException() {
        return new DataAccessException("Key ambiguous between tables " + lhs + " and " + rhs);
    }

    @Override
    public final JoinTable using(Field<?>... fields) {
        return using(asList(fields));
    }

    @Override
    public final JoinTable using(Collection<? extends Field<?>> fields) {
        using.addAll(fields);
        return this;
    }

    @Override
    public final JoinTable and(Condition c) {
        condition.addConditions(c);
        return this;
    }

    @Override
    public final JoinTable and(Field<Boolean> c) {
        return and(condition(c));
    }

    @Override
    public final JoinTable and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final JoinTable and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final JoinTable and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final JoinTable andNot(Condition c) {
        return and(c.not());
    }

    @Override
    public final JoinTable andNot(Field<Boolean> c) {
        return andNot(condition(c));
    }

    @Override
    public final JoinTable andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final JoinTable andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final JoinTable or(Condition c) {
        condition.addConditions(Operator.OR, c);
        return this;
    }

    @Override
    public final JoinTable or(Field<Boolean> c) {
        return or(condition(c));
    }

    @Override
    public final JoinTable or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final JoinTable or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final JoinTable or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final JoinTable orNot(Condition c) {
        return or(c.not());
    }

    @Override
    public final JoinTable orNot(Field<Boolean> c) {
        return orNot(condition(c));
    }

    @Override
    public final JoinTable orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final JoinTable orNotExists(Select<?> select) {
        return or(notExists(select));
    }
}
