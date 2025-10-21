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

import static org.jooq.impl.Names.N_DENSE_RANK;
import static org.jooq.impl.SQLDataType.INTEGER;

import java.util.function.Function;

import org.jooq.Context;
import org.jooq.QueryPart;
// ...
// ...

/**
 * @author Lukas Eder
 */
final class DenseRank extends AbstractWindowFunction<Integer, DenseRank> implements QOM.DenseRank {

    DenseRank() {
        super(N_DENSE_RANK, INTEGER.notNull());
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {







            default:
                ctx.visit(N_DENSE_RANK).sql("()");
                acceptOverClause(ctx);
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    final DenseRank copy1(Function<DenseRank, DenseRank> function) {
        return function.apply(new DenseRank());
    }



















}
