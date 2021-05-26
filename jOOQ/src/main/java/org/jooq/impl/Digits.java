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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.DataExtendedKey.*;
import static org.jooq.impl.Tools.DataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>DIGITS</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Digits
extends
    AbstractField<String>
{

    private final Field<? extends Number> value;

    Digits(
        Field<? extends Number> value
    ) {
        super(
            N_DIGITS,
            allNotNull(VARCHAR, value)
        );

        this.value = nullSafeNotNull(value, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_DIGITS = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE);

    @Override
    public final void accept(Context<?> ctx) {
        if (NO_SUPPORT_DIGITS.contains(ctx.dialect())) {
            DataType<?> t = value.getDataType();

            if (t.getType() == Byte.class)
                ctx.visit(DSL.lpad(DSL.abs(value).cast(VARCHAR(DefaultDataType.BYTE_PRECISION)), inline(DefaultDataType.BYTE_PRECISION), inline("0")));
            else if (t.getType() == Short.class)
                ctx.visit(DSL.lpad(DSL.abs(value).cast(VARCHAR(DefaultDataType.SHORT_PRECISION)), inline(DefaultDataType.SHORT_PRECISION), inline("0")));
            else if (t.getType() == Integer.class)
                ctx.visit(DSL.lpad(DSL.abs(value).cast(VARCHAR(DefaultDataType.INTEGER_PRECISION)), inline(DefaultDataType.INTEGER_PRECISION), inline("0")));
            else if (t.getType() == Long.class)
                ctx.visit(DSL.lpad(DSL.abs(value).cast(VARCHAR(DefaultDataType.LONG_PRECISION)), inline(DefaultDataType.LONG_PRECISION), inline("0")));
            else if (t.scaleDefined())
                ctx.visit(DSL.lpad(DSL.abs(value.mul(inline(java.math.BigDecimal.TEN.pow(t.scale())))).cast(t.scale(0)).cast(VARCHAR(t.precision())), inline(t.precision()), inline("0")));
            else
                ctx.visit(DSL.lpad(DSL.abs(value).cast(VARCHAR(t.precision())), inline(t.precision()), inline("0")));
        }




        else
            ctx.visit(N_DIGITS).sql('(').visit(value).sql(')');
    }














    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof Digits) {
            return
                StringUtils.equals(value, ((Digits) that).value)
            ;
        }
        else
            return super.equals(that);
    }
}
