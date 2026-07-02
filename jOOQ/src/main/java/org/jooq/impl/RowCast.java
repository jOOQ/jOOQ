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

import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Names.N_CAST;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.QualifiedRecord;
import org.jooq.QueryPart;
// ...
import org.jooq.Row;
// ...

/**
 * @author Lukas Eder
 */
final class RowCast<R extends QualifiedRecord<R>>
extends
    AbstractField<R>
implements
    QOM.RowCast<R>
{

    private final Row row;

    RowCast(Row row, DataType<R> type) {
        super(N_CAST, type);

        this.row = row;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(N_CAST).sql('(')
                   .visit(new RowAsField<>(row)).sql(' ')
                   .visit(K_AS).sql(' ')
                   .sql(getDataType().getCastTypeName(ctx.configuration()))
                   .sql(')');

                break;
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Row $row() {
        return row;
    }

    @Override
    public final RowCast<R> $row(Row newRow) {
        return new RowCast<>(newRow, $dataType());
    }

    @Override
    public final <U extends QualifiedRecord<U>> RowCast<U> $dataType(DataType<U> newType) {
        return new RowCast<>($row(), newType);
    }

























}
