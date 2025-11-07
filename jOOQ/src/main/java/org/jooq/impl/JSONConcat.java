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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.ExpressionOperator.CONCAT;
import static org.jooq.impl.Names.N_CONCAT;
import static org.jooq.impl.SQLDataType.JSONB;

import java.util.List;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.impl.QOM.UTransient;

/**
 * @author Lukas Eder
 */
final class JSONConcat<T>
extends
    AbstractField<T>
implements
    UTransient
{

    private final List<? extends Field<?>> arguments;

    JSONConcat(List<? extends Field<?>> arguments, DataType<T> type) {
        super(N_CONCAT, type);

        this.arguments = arguments;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void accept(Context<?> ctx) {
        if (arguments.isEmpty()) {
            ctx.visit(inline(null, getDataType()));
            return;
        }
        else if (arguments.size() == 1) {
            ctx.visit(arguments.get(0));
            return;
        }

        ExpressionOperator op = CONCAT;

        ctx.sql('(');
        Expression.acceptAssociative(
            ctx,
            (QOM.UOperator2) toExpression(op, arguments),
            op.toQueryPart(),
            c -> c.sql(' '),
            Expression.Associativity.BOTH
        );
        ctx.sql(')');
    }

    private final Expression<?> toExpression(ExpressionOperator op, List<? extends Field<?>> a) {
        Expression<?> expression = new Expression<>(op, false, jsonCast(a.get(0)), jsonCast(a.get(1)));

        for (int i = 2; i < a.size(); i++)
            expression = new Expression<>(op, false, expression, jsonCast(a.get(i)));

        return expression;
    }

    private final Field<?> jsonCast(Field<?> f) {
        return f.getDataType().getFromType() == JSON.class ? f.cast(JSONB) : f;
    }
}
