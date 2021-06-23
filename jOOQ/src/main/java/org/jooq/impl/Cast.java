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
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_CAST;
import static org.jooq.impl.Keywords.K_TRIM;
import static org.jooq.impl.Names.N_CAST;
import static org.jooq.impl.Names.N_TO_CLOB;
import static org.jooq.impl.Names.N_TO_DATE;
import static org.jooq.impl.Names.N_TO_TIMESTAMP;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.CHAR;
import static org.jooq.impl.SQLDataType.DECIMAL;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.SQLDataType.FLOAT;
import static org.jooq.impl.SQLDataType.REAL;
import static org.jooq.impl.SQLDataType.VARCHAR;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.regex.Pattern;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Keyword;
import org.jooq.LanguageContext;
// ...
import org.jooq.QueryPart;
import org.jooq.RenderContext.CastMode;

/**
 * @author Lukas Eder
 */
final class Cast<T> extends AbstractField<T> {

    private final Field<?> field;

    public Cast(Field<?> field, DataType<T> type) {
        super(N_CAST, type.nullable(field.getDataType().nullable()));

        this.field = field;
    }

    private final DataType<T> getSQLDataType() {
        return getDataType().getSQLDataType();
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






















            case DERBY:
                ctx.visit(new CastDerby());
                break;

            default:
                ctx.visit(new CastNative<>(field, getDataType()));
                break;
        }
    }






























































    private final class CastDerby extends CastNative<T> {

        CastDerby() {
            super(field, Cast.this.getDataType());
        }

        @SuppressWarnings("unchecked")
        private final Field<Boolean> asDecodeNumberToBoolean() {

            // [#859] 0 => false, null => null, all else is true
            return DSL.choose((Field<Integer>) field)
                      .when(inline(0), inline(false))
                      .when(inline((Integer) null), inline((Boolean) null))
                      .otherwise(inline(true));
        }

        @SuppressWarnings("unchecked")
        private final Field<Boolean> asDecodeVarcharToBoolean() {
            Field<String> s = (Field<String>) field;

            // [#859] '0', 'f', 'false' => false, null => null, all else is true
            return DSL.when(s.equal(inline("0")), inline(false))
                      .when(DSL.lower(s).equal(inline("false")), inline(false))
                      .when(DSL.lower(s).equal(inline("f")), inline(false))
                      .when(s.isNull(), inline((Boolean) null))
                      .otherwise(inline(true));
        }

        @SuppressWarnings("unchecked")
        @Override
        public final void accept(Context<?> ctx) {
            DataType<T> type = getSQLDataType();

            // [#857] Interestingly, Derby does not allow for casting numeric
            // types directly to VARCHAR. An intermediary cast to CHAR is needed
            if (field.getDataType().isNumeric() && type.isString() && !CHAR.equals(type))
                ctx.visit(K_TRIM).sql('(').visit(new CastNative<>(new CastNative<>(field, CHAR(38)), (DataType<String>) getDataType())).sql(')');

            // [#888] ... neither does casting character types to FLOAT (and similar)
            else if (field.getDataType().isString() && (FLOAT.equals(type) || DOUBLE.equals(type) || REAL.equals(type)))
                ctx.visit(new CastNative<>(new CastNative<>(field, DECIMAL), getDataType()));

            // [#859] ... neither does casting numeric types to BOOLEAN
            else if (field.getDataType().isNumeric() && BOOLEAN.equals(type))
                ctx.visit(asDecodeNumberToBoolean());

            // [#859] ... neither does casting character types to BOOLEAN
            else if (field.getDataType().isString() && BOOLEAN.equals(type))
                ctx.visit(asDecodeVarcharToBoolean());
            else
                super.accept(ctx);
        }
    }









































































    static class CastNative<T> extends AbstractQueryPart {
        private final QueryPart   expression;
        private final DataType<T> type;
        private final Keyword     typeAsKeyword;

        CastNative(QueryPart expression, DataType<T> type) {
            this.expression = expression;
            this.type = type;
            this.typeAsKeyword = null;
        }

        CastNative(QueryPart expression, Keyword typeAsKeyword) {
            this.expression = expression;
            this.type = null;
            this.typeAsKeyword = typeAsKeyword;
        }

        @Override
        public void accept(Context<?> ctx) {

            // Avoid casting bind values inside an explicit cast...
            CastMode castMode = ctx.castMode();

            // Default rendering, if no special case has applied yet
            ctx.visit(K_CAST).sql('(')
               .castMode(CastMode.NEVER)
               .visit(expression)
               .castMode(castMode)
               .sql(' ').visit(K_AS).sql(' ');

            if (typeAsKeyword != null)
                ctx.visit(typeAsKeyword);





            else
                ctx.sql(type.getCastTypeName(ctx.configuration()));

            ctx.sql(')');
        }
    }
}
