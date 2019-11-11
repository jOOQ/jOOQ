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

import static java.math.BigDecimal.TEN;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.Keywords.F_ROUND;
import static org.jooq.impl.Keywords.F_ROUND_DOWN;
import static org.jooq.impl.Keywords.F_TRUNC;
import static org.jooq.impl.Keywords.F_TRUNCATE;
import static org.jooq.impl.Keywords.F_TRUNCNUM;
import static org.jooq.impl.Names.N_TRUNC;
import static org.jooq.impl.Tools.castIfNeeded;
import static org.jooq.impl.Tools.extractVal;

import java.math.BigDecimal;
import java.math.MathContext;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Trunc<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 4291348230758816484L;

    private final Field<T>       field;
    private final Field<Integer> decimals;

    Trunc(Field<T> field, Field<Integer> decimals) {
        super(N_TRUNC, field.getDataType());

        this.field = field;
        this.decimals = decimals;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {




            case DERBY: {
                Field<BigDecimal> power;

                // [#1334] if possible, calculate the power in Java to prevent
                // inaccurate arithmetics in the Derby database
                Integer decimalsVal = extractVal(decimals);
                if (decimalsVal != null) {
                    power = inline(TEN.pow(decimalsVal, MathContext.DECIMAL128));
                }
                else {
                    power = DSL.power(inline(TEN), decimals);
                }

                ctx.visit(DSL.decode()
                    .when(field.sign().greaterOrEqual(zero()),
                          field.mul(power).floor().div(power))
                    .otherwise(
                          field.mul(power).ceil().div(power)));
                break;
            }





            case H2:
            case MARIADB:
            case MYSQL:
                ctx.visit(F_TRUNCATE).sql('(').visit(field).sql(", ").visit(decimals).sql(')');
                break;

            // Postgres TRUNC() only takes NUMERIC arguments, no
            // DOUBLE PRECISION ones




            case POSTGRES:
                ctx.visit(castIfNeeded(
                    DSL.function("trunc", SQLDataType.NUMERIC,
                        castIfNeeded(field, BigDecimal.class),
                        decimals
                    ),
                    field.getDataType()
                ));
                break;






















            case CUBRID:
            case HSQLDB:
            default:
                ctx.visit(F_TRUNC).sql('(').visit(field).sql(", ").visit(decimals).sql(')');
                break;
        }
    }
}
