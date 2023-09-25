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

import org.jooq.Condition;
import org.jooq.Context;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.Select;
import org.jooq.Table;
// ...

/**
 * @author Lukas Eder
 */
final class InlineDerivedTable<R extends Record> extends DerivedTable<R> {

    final Table<R>  table;
    final Condition condition;

    InlineDerivedTable(TableImpl<R> t) {
        this(removeWhere(t), t.where);
    }

    InlineDerivedTable(Table<R> table, Condition condition) {
        super(Lazy.of(() -> selectFrom(table).where(condition)), table.getUnqualifiedName());

        this.table = table;
        this.condition = condition;
    }

    static final <R extends Record> Table<R> inlineDerivedTable(Context<?> ctx, Table<R> t) {






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

                    if (a.hasFieldAliases())
                        return q.asTable(a.getUnqualifiedName(), a.alias.fieldAliases);
                    else
                        return q.asTable(a);
                }
            }
        }

        return null;
    }

    private static final <R extends Record> Table<R> removeWhere(Table<R> t) {
        if (t instanceof TableImpl<R> i) {
            return new TableImpl<>(
                i.getQualifiedName(),
                i.getSchema(),
                i.path,
                i.childPath,
                i.parentPath,
                i.alias != null ? removeWhere(i.alias.wrapped) : null,
                i.parameters,
                i.getCommentPart(),
                i.getOptions(),
                null
            );
        }
        else
            return t;
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
