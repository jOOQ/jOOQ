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

import static org.jooq.impl.Keywords.F_STRFTIME;
import static org.jooq.impl.Keywords.K_DATE;
import static org.jooq.impl.Keywords.K_TIME;
import static org.jooq.impl.Keywords.K_TIMESTAMP;
import static org.jooq.impl.Tools.castIfNeeded;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Keyword;


/**
 * @author Lukas Eder
 */
final class DateOrTime<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6729613078727690134L;

    private final Field<?>    field;

    DateOrTime(Field<?> field, DataType<T> dataType) {
        super(DSL.name(name(dataType)), dataType);

        this.field = field;
    }

    private static String name(DataType<?> dataType) {
        return dataType.isDate()
             ? "date"
             : dataType.isTime()
             ? "time"
             : "timestamp";
    }

    private static Keyword keyword(DataType<?> dataType) {
        return dataType.isDate()
             ? K_DATE
             : dataType.isTime()
             ? K_TIME
             : K_TIMESTAMP;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {




            case MYSQL:
            case MARIADB:
                ctx.visit(keyword(getDataType())).sql('(').visit(field).sql(')');
                break;

            case SQLITE: {
                if (getDataType().isDate())
                    ctx.visit(K_DATE).sql('(').visit(field).sql(')');
                else if (getDataType().isTime())
                    // [#8733] No fractional seconds for time literals
                    ctx.visit(K_TIME).sql('(').visit(field).sql(')');
                else
                    ctx.visit(F_STRFTIME).sql("('%Y-%m-%d %H:%M:%f', ").visit(field).sql(')');
                break;
            }

            default:
                ctx.visit(castIfNeeded(field, getDataType()));
                break;
        }
    }
}
