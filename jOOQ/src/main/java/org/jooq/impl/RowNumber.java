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

import static org.jooq.impl.Names.N_ROWNUM;
import static org.jooq.impl.Names.N_ROW_NUMBER;
import static org.jooq.impl.SQLDataType.INTEGER;

import java.util.function.Consumer;
import java.util.function.Function;

import org.jooq.Context;
import org.jooq.QueryPart;
// ...
// ...

/**
 * @author Lukas Eder
 */
final class RowNumber extends AbstractWindowFunction<Integer, RowNumber> implements QOM.RowNumber {

    RowNumber() {
        super(N_ROW_NUMBER, INTEGER.notNull());
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#1524] Don't render this clause where it is not supported
        switch (ctx.family()) {
            case HSQLDB:
                ctx.visit(N_ROWNUM).sql("()");
                break;








            default:
                ctx.visit(N_ROW_NUMBER).sql("()");
                acceptOverClause(ctx);
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    final RowNumber copy1(Function<RowNumber, RowNumber> function) {
        return function.apply(new RowNumber());
    }



















}
