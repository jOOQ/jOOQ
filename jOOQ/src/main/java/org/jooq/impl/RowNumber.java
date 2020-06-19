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

import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.impl.Names.N_ROWNUM;
import static org.jooq.impl.Names.N_ROW_NUMBER;
import static org.jooq.impl.SQLDataType.INTEGER;

import org.jooq.Context;

/**
 * @author Lukas Eder
 */
final class RowNumber extends AbstractWindowFunction<Integer> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7318928420486422195L;

    RowNumber() {
        super(N_ROW_NUMBER, INTEGER);
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#1524] Don't render this clause where it is not supported
        if (ctx.family() == HSQLDB) {
            ctx.visit(N_ROWNUM).sql("()");
        }
        else {
            ctx.visit(N_ROW_NUMBER).sql("()");
            acceptOverClause(ctx);
        }
    }
}
