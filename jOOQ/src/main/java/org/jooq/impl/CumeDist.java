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

// ...
// ...
// ...
import static org.jooq.impl.Internal.idiv;
import static org.jooq.impl.Names.N_CUME_DIST;
import static org.jooq.impl.SQLDataType.NUMERIC;

import java.math.BigDecimal;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.jooq.Context;
import org.jooq.Function1;
// ...
import org.jooq.QueryPart;
// ...
import org.jooq.SQLDialect;
// ...
import org.jooq.WindowSpecification;

/**
 * @author Lukas Eder
 */
final class CumeDist extends AbstractWindowFunction<BigDecimal> implements QOM.CumeDist {






    CumeDist() {
        super(N_CUME_DIST, NUMERIC.notNull());
    }

    @Override
    public final void accept(Context<?> ctx) {













        {
            ctx.visit(N_CUME_DIST).sql("()");
            acceptOverClause(ctx);
        }
    }























}
