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
 * The <code>IS NULL</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class IsNull
extends
    AbstractCondition
implements
    QOM.IsNull
{

    final Field<?> field;

    IsNull(
        Field<?> field
    ) {

        this.field = nullSafeNotNull(field, OTHER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[] CLAUSES = { Clause.CONDITION, Clause.CONDITION_IS_NULL };

    @Override
    final boolean isNullable() {
        return false;
    }

    @Override
    public final void accept(Context<?> ctx) {





        if (field.getDataType().isEmbeddable())
            ctx.visit(row(embeddedFields(field)).isNull());
        else
            ctx.visit(field).sql(' ').visit(K_IS_NULL);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $arg1() {
        return field;
    }

    @Override
    public final QOM.IsNull $arg1(Field<?> newValue) {
        return constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<?>, ? extends QOM.IsNull> constructor() {
        return (a1) -> new IsNull(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.IsNull) { QOM.IsNull o = (QOM.IsNull) that;
            return
                StringUtils.equals($field(), o.$field())
            ;
        }
        else
            return super.equals(that);
    }
}
