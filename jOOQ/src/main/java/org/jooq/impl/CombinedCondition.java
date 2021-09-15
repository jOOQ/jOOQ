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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_AND;
import static org.jooq.Clause.CONDITION_OR;
import static org.jooq.Operator.AND;
// ...
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.trueCondition;

import java.util.Collection;
import java.util.function.Function;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Operator;
import org.jooq.impl.Expression.Expr;

/**
 * @author Lukas Eder
 */
final class CombinedCondition extends AbstractCondition {

    private static final Clause[] CLAUSES_AND      = { CONDITION, CONDITION_AND };
    private static final Clause[] CLAUSES_OR       = { CONDITION, CONDITION_OR };

    final Operator                operator;
    final Condition               op1;
    final Condition               op2;

    static Condition of(Operator operator, Condition left, Condition right) {
        if (left == null || left instanceof NoCondition)
            return right;
        else if (right == null || right instanceof NoCondition)
            return left;
        else
            return new CombinedCondition(operator, left, right);
    }

    static Condition of(Operator operator, Collection<? extends Condition> conditions) {
        Condition result = null;

        for (Condition condition : conditions)
            if (!(condition instanceof NoCondition))
                if (result == null)
                    result = condition;
                else
                    result = new CombinedCondition(operator, result, condition);

        if (result != null)
            return result;

        // [#9998] All conditions were NoCondition
        else if (!conditions.isEmpty())
            return noCondition();

        // [#9998] Otherwise, return the identity for the operator
        else
            return identity(operator);
    }

    @Override
    final boolean isNullable() {
        return ((AbstractCondition) op1).isNullable() || ((AbstractCondition) op2).isNullable();
    }

    static final Condition identity(Operator operator) {
        return operator == AND ? trueCondition() : falseCondition();
    }

    final Condition transform(Function<? super Condition, ? extends Condition> function) {
        Condition t1 = op1 instanceof CombinedCondition
            ? ((CombinedCondition) op1).transform(function)
            : function.apply(op1);
        Condition t2 = op2 instanceof CombinedCondition
            ? ((CombinedCondition) op2).transform(function)
            : function.apply(op2);

        if (t1 == op1 && t2 == op2)
            return this;
        else
            return of(operator, t1, t2);
    }

    private CombinedCondition(Operator operator, Condition op1, Condition op2) {
        if (operator == null)
            throw new IllegalArgumentException("The argument 'operator' must not be null");

        this.operator = operator;
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return operator == AND ? CLAUSES_AND : CLAUSES_OR;
    }

    @Override
    public final void accept(Context<?> ctx) {














        {
            ctx.sqlIndentStart('(');
            Expression.<Condition, CombinedCondition>acceptAssociative(
                ctx,
                this,
                q -> new Expr<>(q.op1, q.operator.toKeyword(), q.op2),
                Context::formatSeparator
            );
            ctx.sqlIndentEnd(')');
        }
    }
}
