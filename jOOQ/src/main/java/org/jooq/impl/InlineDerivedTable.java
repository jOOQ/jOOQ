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

import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.tools.StringUtils.defaultIfNull;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.Scope;
import org.jooq.Select;
import org.jooq.Table;
// ...

/**
 * @author Lukas Eder
 */
final class InlineDerivedTable<R extends Record> extends DerivedTable<R> {

    final Table<R>  table;
    final Condition condition;
    final boolean   policyGenerated;

    InlineDerivedTable(TableImpl<R> t) {
        this(t.where((Condition) null), t.where, false);
    }

    InlineDerivedTable(Table<R> table, Condition condition, boolean policyGenerated) {
        super(Lazy.of(() -> selectFrom(table).where(condition)), table.getUnqualifiedName());

        this.table = table;
        this.condition = condition;
        this.policyGenerated = policyGenerated;
    }

    static final boolean hasInlineDerivedTables(Context<?> ctx, Table<?> t) {
        return InlineDerivedTable.inlineDerivedTable(ctx, t) != null
            || t instanceof JoinTable && (hasInlineDerivedTables(ctx, ((JoinTable<?>) t).lhs) || hasInlineDerivedTables(ctx, ((JoinTable<?>) t).rhs));
    }

    static final boolean hasInlineDerivedTables(Context<?> ctx, TableList tablelist) {
        return anyMatch(tablelist, t -> hasInlineDerivedTables(ctx, t));
    }

    static final TableList transformInlineDerivedTables(Context<?> ctx, TableList tablelist, ConditionProviderImpl where) {
        if (!hasInlineDerivedTables(ctx, tablelist))
            return tablelist;

        TableList result = new TableList();

        for (Table<?> table : tablelist)
            transformInlineDerivedTable0(ctx, table, result, where);

        return result;
    }

    static final void transformInlineDerivedTable0(
        Context<?> ctx,
        Table<?> table,
        TableList result,
        ConditionProviderImpl where
    ) {
        Table<?> t = InlineDerivedTable.inlineDerivedTable(ctx, table);

        if (t != null) {
            if (t instanceof InlineDerivedTable<?> i) {
                result.add(i.table);
                where.addConditions(i.condition);
            }
            else
                result.add(t);
        }
        else if (table instanceof JoinTable)
            result.add(transformInlineDerivedTables0(ctx, table, where, false));
        else
            result.add(table);
    }

    static final Table<?> transformInlineDerivedTables0(
        Context<?> ctx,
        Table<?> table,
        ConditionProviderImpl where,
        boolean keepDerivedTable
    ) {
        Table<?> t = InlineDerivedTable.inlineDerivedTable(ctx, table);

        if (t != null) {
            if (t instanceof InlineDerivedTable<?> i) {
                if (keepDerivedTable) {

                    // [#15632] SchemaMapping could produce a different table than the one contained
                    //          in the inline derived table specification, and the alias must reflect that
                    Table<?> m = DSL.table(defaultIfNull(ctx.dsl().map(i.table), i.table).getUnqualifiedName());

                    // [#2682] An explicit path join that produces an InlineDerivedTable (e.g. due to a Policy)
                    //         The InlineDerivedTable.condition is appended by the join tree logic
                    if (TableImpl.path(i.table) != null) {
                        where.addConditions(((TableImpl<?>) i.table).pathCondition());
                        return selectFrom(Tools.unwrap(i.table).as(m)).asTable(m);
                    }








                    else
                        return i.query().asTable(m);
                }

                where.addConditions(i.condition);
                return i.table;
            }
            else
                return t;
        }
        else if (table instanceof JoinTable<?> j) {
            Table<?> lhs;
            Table<?> rhs;
            ConditionProviderImpl w = new ConditionProviderImpl();

            switch (j.type) {
                case LEFT_OUTER_JOIN:
                case LEFT_ANTI_JOIN:
                case LEFT_SEMI_JOIN:
                case STRAIGHT_JOIN:
                case CROSS_APPLY:
                case OUTER_APPLY:
                case NATURAL_LEFT_OUTER_JOIN: {
                    lhs = transformInlineDerivedTables0(ctx, j.lhs, where, keepDerivedTable);
                    rhs = transformInlineDerivedTables0(ctx, j.rhs, w, true);
                    break;
                }

                case RIGHT_OUTER_JOIN:
                case NATURAL_RIGHT_OUTER_JOIN: {
                    lhs = transformInlineDerivedTables0(ctx, j.lhs, w, true);
                    rhs = transformInlineDerivedTables0(ctx, j.rhs, where, keepDerivedTable);
                    break;
                }

                case FULL_OUTER_JOIN:
                case NATURAL_FULL_OUTER_JOIN: {
                    lhs = transformInlineDerivedTables0(ctx, j.lhs, w, true);
                    rhs = transformInlineDerivedTables0(ctx, j.rhs, w, true);
                    break;
                }

                default: {
                    lhs = transformInlineDerivedTables0(ctx, j.lhs, where, keepDerivedTable);
                    rhs = transformInlineDerivedTables0(ctx, j.rhs, where, keepDerivedTable);
                    break;
                }
            }

            return j.transform(lhs, rhs, w.hasWhere() ? w : j.condition);
        }
        else
            return table;
    }

    static final <R extends Record> Table<R> inlineDerivedTable(Scope ctx, Table<R> t) {






        return InlineDerivedTable.derivedTable(t);
    }

    static final <R extends Record> Table<R> derivedTable(Table<R> t) {
        if (t instanceof InlineDerivedTable<R> i) {
            return i;
        }
        else if (t instanceof TableImpl<R> i) {
            if (i.where != null)
                return new InlineDerivedTable<>(i);

            Table<R> unaliased = Tools.unalias(i);
            if (unaliased instanceof TableImpl<R> u) {
                if (u.where != null)
                    return new InlineDerivedTable<>(u).query().asTable(i);
            }
        }
        else if (t instanceof TableAlias<R> a) {
            if (a.$aliased() instanceof TableImpl<R> u) {
                if (u.where != null) {
                    Select<R> q = new InlineDerivedTable<>(u).query();

                    if (a.alias.hasFieldAliases())
                        return q.asTable(a.getUnqualifiedName(), a.alias.fieldAliases);
                    else
                        return q.asTable(a);
                }
            }
        }

        return null;
    }

    @Override
    final FieldsImpl<R> fields0() {

        // [#8012] Re-use the existing fields row if this is an aliased table.
        if (table instanceof TableAlias)
            return new FieldsImpl<>(table.fields());

        // [#8012] Re-wrap fields in new TableAlias to prevent StackOverflowError.
        //         Cannot produce qualified table references here in case the
        //         InlineDerivedTable cannot be inlined.
        else
            return new FieldsImpl<>(Tools.qualify(table(table.getUnqualifiedName()), table.as(table).fields()));
    }
























}
