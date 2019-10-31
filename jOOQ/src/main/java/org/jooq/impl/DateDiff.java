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

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.Keywords.F_DATEDIFF;
import static org.jooq.impl.Keywords.F_DAYS_BETWEEN;
import static org.jooq.impl.Keywords.F_STRFTIME;
import static org.jooq.impl.Keywords.F_TIMESTAMPDIFF;
import static org.jooq.impl.Keywords.K_DAY;
import static org.jooq.impl.Names.N_DATEDIFF;
import static org.jooq.impl.Tools.castIfNeeded;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class DateDiff<T> extends AbstractField<Integer> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4813228000332771961L;

    private final Field<T>    date1;
    private final Field<T>    date2;

    DateDiff(Field<T> date1, Field<T> date2) {
        super(N_DATEDIFF, SQLDataType.INTEGER);

        this.date1 = date1;
        this.date2 = date2;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {




            case MARIADB:
            case MYSQL:
                ctx.visit(F_DATEDIFF).sql('(').visit(date1).sql(", ").visit(date2).sql(')');
                break;

            case DERBY:
                ctx.sql("{fn ").visit(F_TIMESTAMPDIFF).sql('(').visit(keyword("sql_tsi_day")).sql(", ").visit(date2).sql(", ").visit(date1).sql(") }");
                break;

            case FIREBIRD:
                ctx.visit(F_DATEDIFF).sql('(').visit(K_DAY).sql(", ").visit(date2).sql(", ").visit(date1).sql(')');
                break;

            case H2:
            case HSQLDB:



                ctx.visit(F_DATEDIFF).sql("('day', ").visit(date2).sql(", ").visit(date1).sql(')');
                break;

            case SQLITE:
                ctx.sql('(').visit(F_STRFTIME).sql("('%s', ").visit(date1).sql(") - ").visit(F_STRFTIME).sql("('%s', ").visit(date2).sql(")) / 86400");
                break;





            case CUBRID:
            case POSTGRES:

                // [#4481] Parentheses are important in case this expression is
                //         placed in the context of other arithmetic
                ctx.sql('(').visit(date1).sql(" - ").visit(date2).sql(')');
                break;

























            default:
                // Default implementation for equals() and hashCode()
                ctx.visit(castIfNeeded(date1.sub(date2), Integer.class));
                break;
        }
    }
}
