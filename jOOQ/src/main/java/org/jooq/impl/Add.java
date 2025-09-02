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
import java.util.Objects;
import java.util.Set;

import org.jooq.conf.TransformUnneededArithmeticExpressions;


/**
 * The <code>ADD</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Add<T>
extends
    AbstractTransformable<T>
implements
    QOM.Add<T>
{

    final Field<T> arg1;
    final Field<T> arg2;

    Add(
        Field<T> arg1,
        Field<T> arg2
    ) {
        super(
            N_ADD,
            allNotNull((DataType) dataType(arg1), arg1, arg2)
        );

        this.arg1 = nullSafeNotNull(arg1, (DataType) OTHER);
        this.arg2 = nullSafeNotNull(arg2, (DataType) OTHER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    final boolean parenthesised(Context<?> ctx) {
        return true;
    }

    @Override
    public final void accept0(Context<?> ctx) {







        ctx.sql('(');
        Expression.acceptAssociative(
            ctx,
            this,
            Operators.OP_PLUS,
            c -> c.sql(' ')
        );
        ctx.sql(')');
    }

    @Override
    public final Field<?> transform(TransformUnneededArithmeticExpressions transform) {
        return Expression.transform(this, arg1, ExpressionOperator.ADD, arg2, false, transform);
    }

    @Override
    final DataType<?> getExpressionDataType() {

        // [#11959] Workaround for lack of proper data type information for interval based expressions
        return Expression.getExpressionDataType((AbstractField<?>) arg1, ExpressionOperator.ADD, (AbstractField<?>) arg2);
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return arg1;
    }

    @Override
    public final Field<T> $arg2() {
        return arg2;
    }

    @Override
    public final QOM.Add<T> $arg1(Field<T> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.Add<T> $arg2(Field<T> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<T>, ? extends QOM.Add<T>> $constructor() {
        return (a1, a2) -> new Add<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Add<?> o) {
            return
                Objects.equals($arg1(), o.$arg1()) &&
                Objects.equals($arg2(), o.$arg2())
            ;
        }
        else
            return super.equals(that);
    }
}
