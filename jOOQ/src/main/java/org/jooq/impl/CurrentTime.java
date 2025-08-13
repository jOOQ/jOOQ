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

import static org.jooq.impl.Keywords.K_CURRENT;
import static org.jooq.impl.Keywords.K_HOUR_TO_SECOND;
import static org.jooq.impl.Keywords.K_TIME;
import static org.jooq.impl.Names.N_CONVERT;
import static org.jooq.impl.Names.N_CURRENT_TIME;
import static org.jooq.impl.Names.N_CURRENT_TIMESTAMP;
import static org.jooq.impl.SQLDataType.TIME;

import java.sql.Time;

import org.jooq.Context;
import org.jooq.DataType;

/**
 * @author Lukas Eder
 */
final class CurrentTime<T> extends AbstractField<T> implements QOM.CurrentTime<T> {

    CurrentTime(DataType<T> type) {
        super(N_CURRENT_TIME, type);
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

































            case CLICKHOUSE:
                ctx.visit(N_CURRENT_TIMESTAMP).sql("()");
                break;





            case MARIADB:
            case MYSQL:
                ctx.visit(N_CURRENT_TIME).sql("()");
                break;

            // [#18865] DuckDB's CURRENT_TIME expression is of type TIMETZ
            case DUCKDB:
                Cast.renderCast(ctx, c -> c.visit(N_CURRENT_TIME), c -> c.visit(K_TIME));
                break;

            default:
                ctx.visit(N_CURRENT_TIME);
                break;
        }
    }
}
