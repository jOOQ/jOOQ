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

import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Names.N_SELECT;

import java.util.Arrays;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;

/**
 * A scalar subquery that wraps a non-scalar subquery, but projects only one of
 * its columns either by SQL transformation, or via a derived table.
 *
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
final class ProjectSingleScalarSubquery<T> extends AbstractField<T> implements QOM.UTransient {

    final Select<?> query;
    final int       index;

    ProjectSingleScalarSubquery(Select<?> query, int index) {
        super(N_SELECT, (DataType<T>) query.getSelect().get(index).getDataType());

        this.query = query;
        this.index = index;
    }

    @Override
    public final void accept(Context<?> ctx) {
        SelectQueryImpl<?> q = Tools.selectQueryImpl(query);

        if (q.$distinct()
            || !q.$orderBy().isEmpty()
            || q.hasUnions()) {
            Field<?>[] f = Tools.fields(query.getSelect().size());
            ctx.visit(DSL.field(select(f[index]).from(query.asTable(table(name("t")), f))));
        }
        else
            ctx.visit(DSL.field((Select<Record1<T>>) query.$select(Arrays.asList(query.$select().get(index)))));
    }
}
