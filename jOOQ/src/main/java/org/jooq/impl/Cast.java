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
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.SQLDataType.FLOAT;
import static org.jooq.impl.SQLDataType.REAL;
import static org.jooq.impl.SQLDataType.VARCHAR;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.RenderContext.CastMode;

/**
 * @author Lukas Eder
 */
final class Cast<T> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6776617606751542856L;

    private final Field<?>    field;

    public Cast(Field<?> field, DataType<T> type) {
        super("cast", type);

        this.field = field;
    }

    private final DataType<T> getSQLDataType() {
        return getDataType().getSQLDataType();
    }

    @Override
    final QueryPart getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {





            case DERBY:
                return new CastDerby();

            default:
                return new Native();
        }
    }



















































    private class CastDerby extends Native {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -8737153188122391258L;

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

        @Override
        public final void accept(Context<?> ctx) {

            // Avoid casting bind values inside an explicit cast...
            CastMode castMode = ctx.castMode();

            // [#857] Interestingly, Derby does not allow for casting numeric
            // types directly to VARCHAR. An intermediary cast to CHAR is needed
            if (field.getDataType().isNumeric() &&
                VARCHAR.equals(getSQLDataType())) {

                ctx.keyword("trim").sql('(')
                       .keyword("cast").sql('(')
                           .keyword("cast").sql('(')
                               .castMode(CastMode.NEVER)
                               .visit(field)
                               .castMode(castMode)
                               .sql(' ').keyword("as").sql(" char(38))")
                           .sql(' ').keyword("as").sql(' ')
                           .keyword(getDataType(ctx.configuration()).getCastTypeName(ctx.configuration()))
                       .sql("))");

                return;
            }

            // [#888] ... neither does casting character types to FLOAT (and similar)
            else if (field.getDataType().isString() &&
                     asList(FLOAT, DOUBLE, REAL).contains(getSQLDataType())) {

                ctx.keyword("cast").sql('(')
                       .keyword("cast").sql('(')
                           .castMode(CastMode.NEVER)
                           .visit(field)
                           .castMode(castMode)
                           .sql(' ').keyword("as").sql(' ').keyword("decimal")
                       .sql(") ")
                       .keyword("as")
                       .sql(' ')
                       .keyword(getDataType(ctx.configuration()).getCastTypeName(ctx.configuration()))
                   .sql(')');

                return;
            }

            // [#859] ... neither does casting numeric types to BOOLEAN
            else if (field.getDataType().isNumeric() &&
                     BOOLEAN.equals(getSQLDataType())) {

                ctx.visit(asDecodeNumberToBoolean());
                return;
            }

            // [#859] ... neither does casting character types to BOOLEAN
            else if (field.getDataType().isString() &&
                     BOOLEAN.equals(getSQLDataType())) {

                ctx.visit(asDecodeVarcharToBoolean());
                return;
            }

            super.accept(ctx);
        }
    }

    private class Native extends AbstractQueryPart {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -8497561014419483312L;

        @Override
        public void accept(Context<?> ctx) {

            // Avoid casting bind values inside an explicit cast...
            CastMode castMode = ctx.castMode();

            // Default rendering, if no special case has applied yet
            ctx.keyword("cast").sql('(')
               .castMode(CastMode.NEVER)
               .visit(field)
               .castMode(castMode)
               .sql(' ').keyword("as").sql(' ')
               .keyword(getDataType(ctx.configuration()).getCastTypeName(ctx.configuration()))
               .sql(')');
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return null;
        }
    }
}
