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
 * The <code>CARDINALITY</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Cardinality
extends
    AbstractField<Integer>
implements
    QOM.Cardinality
{

    final Field<? extends Object[]> array;

    Cardinality(
        Field<? extends Object[]> array
    ) {
        super(
            N_CARDINALITY,
            allNotNull(INTEGER, array)
        );

        this.array = nullSafeNotNull(array, OTHER.getArrayDataType());
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(function(N_CARDINALITY, getDataType(), array));
                break;
        }
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<? extends Object[]> $array() {
        return array;
    }

    @Override
    public final QOM.Cardinality $array(Field<? extends Object[]> newValue) {
        return constructor().apply(newValue);
    }

    public final Function1<? super Field<? extends Object[]>, ? extends QOM.Cardinality> constructor() {
        return (a1) -> new Cardinality(a1);
    }






















    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Cardinality) { QOM.Cardinality o = (QOM.Cardinality) that;
            return
                StringUtils.equals($array(), o.$array())
            ;
        }
        else
            return super.equals(that);
    }
}
