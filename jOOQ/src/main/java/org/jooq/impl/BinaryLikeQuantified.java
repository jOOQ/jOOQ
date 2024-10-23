/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;



/**
 * The <code>BINARY LIKE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class BinaryLikeQuantified
extends
    AbstractCondition
implements
    QOM.BinaryLikeQuantified
{

    final Field<?>                                             value;
    final org.jooq.QuantifiedSelect<? extends Record1<byte[]>> pattern;

    BinaryLikeQuantified(
        Field<?> value,
        org.jooq.QuantifiedSelect<? extends Record1<byte[]>> pattern
    ) {

        this.value = nullSafeNotNull(value, (DataType) OTHER);
        this.pattern = nullSafeQuantifiedSelect(pattern, value.getDataType());
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {







        EqQuantified.acceptCompareCondition(ctx, this, (Field<byte[]>) value, org.jooq.Comparator.LIKE, pattern, null);
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $arg1() {
        return value;
    }

    @Override
    public final org.jooq.QuantifiedSelect<? extends Record1<byte[]>> $arg2() {
        return pattern;
    }

    @Override
    public final QOM.BinaryLikeQuantified $arg1(Field<?> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.BinaryLikeQuantified $arg2(org.jooq.QuantifiedSelect<? extends Record1<byte[]>> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<?>, ? super org.jooq.QuantifiedSelect<? extends Record1<byte[]>>, ? extends QOM.BinaryLikeQuantified> $constructor() {
        return (a1, a2) -> new BinaryLikeQuantified(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BinaryLikeQuantified o) {
            return
                StringUtils.equals($value(), o.$value()) &&
                StringUtils.equals($pattern(), o.$pattern())
            ;
        }
        else
            return super.equals(that);
    }
}
