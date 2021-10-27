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
 * The <code>NOT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class NotField
extends
    AbstractField<Boolean>
implements
    QOM.NotField
{

    final Field<Boolean> field;

    NotField(
        Field<Boolean> field
    ) {
        super(
            N_NOT,
            BOOLEAN
        );

        this.field = nullSafeNotNull(field, BOOLEAN);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {















            case CUBRID:
            case FIREBIRD:
                ctx.visit(DSL.field(not(condition(field))));
                break;

            default:
                ctx.visit(K_NOT)
                   .sql('(')
                   .visit(Tools.hasDefaultConverter(field) ? field : condition(field))
                   .sql(')');
                break;
        }
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<Boolean> $arg1() {
        return field;
    }

    @Override
    public final QOM.NotField $arg1(Field<Boolean> newValue) {
        return constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<Boolean>, ? extends QOM.NotField> constructor() {
        return (a1) -> new NotField(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.NotField) { QOM.NotField o = (QOM.NotField) that;
            return
                StringUtils.equals($field(), o.$field())
            ;
        }
        else
            return super.equals(that);
    }
}
