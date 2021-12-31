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


/**
 * The <code>COUNT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Count
extends
    AbstractAggregateFunction<Integer>
implements
    QOM.Count
{

    Count(
        Field<?> field,
        boolean distinct
    ) {
        super(
            distinct,
            N_COUNT,
            INTEGER,
            nullSafeNotNull(field, OTHER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        super.accept(ctx);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $field() {
        return getArguments().get(0);
    }

    @Override
    public final QOM.Count $field(Field<?> newValue) {
        return constructor().apply(newValue, $distinct());
    }

    @Override
    public final QOM.Count $distinct(boolean newValue) {
        return constructor().apply($field(), newValue);
    }

    public final Function2<? super Field<?>, ? super Boolean, ? extends QOM.Count> constructor() {
        return (a1, a2) -> new Count(a1, a2);
    }
























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Count) { QOM.Count o = (QOM.Count) that;
            return
                StringUtils.equals($field(), o.$field()) &&
                $distinct() == o.$distinct()
            ;
        }
        else
            return super.equals(that);
    }
}
