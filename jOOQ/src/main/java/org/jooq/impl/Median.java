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
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.percentileCont;
import static org.jooq.impl.Names.N_MEDIAN;

import java.math.BigDecimal;
import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class Median extends DefaultAggregateFunction<BigDecimal> {

    /**
     * Generated UID
     */
    private static final long            serialVersionUID         = -7378732863724089028L;
    private static final Set<SQLDialect> EMULATE_WITH_PERCENTILES = SQLDialect.supportedBy(POSTGRES);

    Median(Field<? extends Number> arg) {
        super(false, N_MEDIAN, SQLDataType.NUMERIC, arg);
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (EMULATE_WITH_PERCENTILES.contains(ctx.dialect()))
            ctx.visit(percentileCont(inline(new BigDecimal("0.5"))).withinGroupOrderBy(arguments));
        else
            super.accept(ctx);
    }
}
