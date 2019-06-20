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

import static org.jooq.impl.Keywords.F_CONVERT;
import static org.jooq.impl.Keywords.F_TO_CHAR;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
// ...

/**
 * @author Lukas Eder
 */
@Pro
public class ConvertDateTime<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 1995680747168683170L;

    private final Field<?>    expression;
    private final int         style;

    ConvertDateTime(DataType<T> type, Field<?> expression, int style) {
        super(DSL.name("convert"), type);

        this.expression = expression;
        this.style = style;
    }

    @Override
    public final void accept(Context<?> ctx) {
        // Full list can be seen here:
        // https://docs.microsoft.com/en-us/sql/t-sql/functions/cast-and-convert-transact-sql?view=sql-server-2017

        switch (ctx.family()) {
            case ORACLE:
            case POSTGRES:
                switch (style) {

                    // U.S. Style
                    case 1:   ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'MM/DD/YY')"); break;
                    case 101: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'MM/DD/YYYY')"); break;

                    // ANSI
                    case 2:   ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'YY.MM.DD')"); break;
                    case 102: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'YYYY.MM.DD')"); break;

                    // British/French
                    case 3:   ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'DD/MM/YY')"); break;
                    case 103: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'DD/MM/YYYY')"); break;

                    // German
                    case 4:   ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'DD.MM.YY')"); break;
                    case 104: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'DD.MM.YYYY')"); break;

                    // German
                    case 5:   ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'DD-MM-YY')"); break;
                    case 105: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'DD-MM-YYYY')"); break;

                    // -
                    case 6:   ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'DD MON YY')"); break;
                    case 106: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'DD MON YYYY')"); break;

                    // -
                    case 7:   ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'MON DD, YY')"); break;
                    case 107: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'MON DD, YYYY')"); break;

                    // -
                    case 8:
                    case 108: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'HH24:MI:SS')"); break;

                    // Default + milliseconds
                    case 9:
                    case 109: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'MON DD YYYY HH:MI:SS.FF AM')"); break;

                    // USA
                    case 10:  ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'MM-DD-YY')"); break;
                    case 110: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'MM-DD-YYYY')"); break;

                    // Japan
                    case 11:  ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'YY/MM/DD')"); break;
                    case 111: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'YYYY/MM/DD')"); break;

                    // ISO
                    case 12:  ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'YYMMDD')"); break;
                    case 112: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'YYYYMMDD')"); break;

                    // Europe default + milliseconds
                    case 13:
                    case 113: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'DD MON YYYY HH24:MI:SS.FF')"); break;

                    // -
                    case 14:
                    case 114: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'HH24:MI:SS.FF')"); break;

                    // ODBC canonical
                    case 20:
                    case 120: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'YYYY-MM-DD HH24:MI:SS')"); break;

                    // ODBC canonical (with milliseconds)
                    case 21:
                    case 121: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'YYYY-MM-DD HH24:MI:SS.FF')"); break;

                    // ISO8601
                    case 126: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'YYYY-MM-DD\"T\"HH24:MI:SS.FF')"); break;

                    // ISO8601 with time zone Z.
                    case 127: ctx.visit(F_TO_CHAR).sql('(').visit(expression).sql(", 'YYYY-MM-DD\"T\"HH24:MI:SS.FF')"); break;
                }

                // No break

            case SQLDATAWAREHOUSE:
            case SQLSERVER:
            default:
                ctx.visit(F_CONVERT).sql('(')
                   .visit(DSL.keyword(getDataType().getCastTypeName(ctx.configuration())))
                   .visit(expression)
                   .visit(DSL.inline(style))
                   .sql(')');
                break;
        }
    }
}

/* [/pro] */
