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
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.sql.Timestamp;


/**
 * The <code>TO TIMESTAMP</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ToTimestamp
extends
    AbstractField<Timestamp>
implements
    QOM.ToTimestamp
{

    final Field<String> value;
    final Field<String> formatMask;

    ToTimestamp(
        Field<String> value,
        Field<String> formatMask
    ) {
        super(
            N_TO_TIMESTAMP,
            allNotNull(TIMESTAMP, value, formatMask)
        );

        this.value = nullSafeNotNull(value, VARCHAR);
        this.formatMask = nullSafeNotNull(formatMask, VARCHAR);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

















            default:
                ctx.visit(function(N_TO_TIMESTAMP, getDataType(), value, formatMask));
                break;
        }
    }
















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<String> $value() {
        return value;
    }

    @Override
    public final Field<String> $formatMask() {
        return formatMask;
    }

    @Override
    public final QOM.ToTimestamp $value(Field<String> newValue) {
        return constructor().apply(newValue, $formatMask());
    }

    @Override
    public final QOM.ToTimestamp $formatMask(Field<String> newValue) {
        return constructor().apply($value(), newValue);
    }

    public final Function2<? super Field<String>, ? super Field<String>, ? extends QOM.ToTimestamp> constructor() {
        return (a1, a2) -> new ToTimestamp(a1, a2);
    }
























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ToTimestamp) { QOM.ToTimestamp o = (QOM.ToTimestamp) that;
            return
                StringUtils.equals($value(), o.$value()) &&
                StringUtils.equals($formatMask(), o.$formatMask())
            ;
        }
        else
            return super.equals(that);
    }
}
