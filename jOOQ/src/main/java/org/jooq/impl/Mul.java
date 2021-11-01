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
 * The <code>MUL</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Mul<T>
extends
    AbstractTransformable<T>
implements
    QOM.Mul<T>
{

    final Field<T> arg1;
    final Field<T> arg2;

    Mul(
        Field<T> arg1,
        Field<T> arg2
    ) {
        super(
            N_MUL,
            allNotNull((DataType) dataType(arg1), arg1, arg2)
        );

        this.arg1 = nullSafeNotNull(arg1, (DataType) OTHER);
        this.arg2 = nullSafeNotNull(arg2, (DataType) OTHER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept0(Context<?> ctx) {







        ctx.sql('(');
        Expression.<Field<T>, Mul<T>>acceptAssociative(
            ctx,
            this,
            q -> new Expression.Expr<>(q.arg1, Operators.OP_AST, q.arg2),
            c -> c.sql(' ')
        );
        ctx.sql(')');
    }

    @Override
    public final Field<?> transform(TransformUnneededArithmeticExpressions transform) {
        return Expression.transform(this, arg1, ExpressionOperator.MULTIPLY, arg2, false, transform);
    }

    @Override
    final DataType<?> getExpressionDataType() {

        // [#11959] Workaround for lack of proper data type information for interval based expressions
        return Expression.getExpressionDataType((AbstractField<?>) arg1, ExpressionOperator.MULTIPLY, (AbstractField<?>) arg2);
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
    public final QOM.Mul<T> $arg1(Field<T> newValue) {
        return constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.Mul<T> $arg2(Field<T> newValue) {
        return constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<T>, ? extends QOM.Mul<T>> constructor() {
        return (a1, a2) -> new Mul<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Mul) { QOM.Mul<?> o = (QOM.Mul<?>) that;
            return
                StringUtils.equals($arg1(), o.$arg1()) &&
                StringUtils.equals($arg2(), o.$arg2())
            ;
        }
        else
            return super.equals(that);
    }
}
