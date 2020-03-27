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
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.Keywords.K_AND;
import static org.jooq.impl.Keywords.K_OR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Keyword;
import org.jooq.Operator;

/**
 * @author Lukas Eder
 */
final class CombinedCondition extends AbstractCondition {

    private static final long     serialVersionUID = -7373293246207052549L;
    private static final Clause[] CLAUSES_AND      = { CONDITION, CONDITION_AND };
    private static final Clause[] CLAUSES_OR       = { CONDITION, CONDITION_OR };

    private final Operator        operator;
    private final List<Condition> conditions;

    static Condition of(Operator operator, Condition left, Condition right) {
        if (left instanceof NoCondition)
            return right;
        else if (right instanceof NoCondition)
            return left;
        else
            return new CombinedCondition(operator, left, right);
    }

    static Condition of(Operator operator, Collection<? extends Condition> conditions) {
        CombinedCondition result = null;
        Condition first = null;

        for (Condition condition : conditions)
            if (!(condition instanceof NoCondition))
                if (first == null)
                    first = condition;
                else if (result == null)
                    (result = new CombinedCondition(operator, conditions.size()))
                        .add(operator, first)
                        .add(operator, condition);
                else
                    result.add(operator, condition);

        if (result != null)
            return result;
        else if (first != null)
            return first;

        // [#9998] All conditions were NoCondition
        else if (!conditions.isEmpty())
            return noCondition();

        // [#9998] Otherwise, return the identity for the operator
        else if (operator == AND)
            return trueCondition();
        else
            return falseCondition();
    }

    private CombinedCondition(Operator operator, int size) {
        if (operator == null)
            throw new IllegalArgumentException("The argument 'operator' must not be null");

        this.operator = operator;
        this.conditions = new ArrayList<>(size);
    }

    private CombinedCondition(Operator operator, Condition left, Condition right) {
        this(operator, 2);

        add(operator, left);
        add(operator, right);
    }

    private final CombinedCondition add(Operator op, Condition condition) {
        if (condition instanceof CombinedCondition) {
            CombinedCondition combinedCondition = (CombinedCondition) condition;

            if (combinedCondition.operator == op)
                this.conditions.addAll(combinedCondition.conditions);
            else
                this.conditions.add(condition);
        }
        else if (condition == null)
            throw new IllegalArgumentException("The argument 'conditions' must not contain null");
        else
            this.conditions.add(condition);

        return this;
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return operator == AND ? CLAUSES_AND : CLAUSES_OR;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (conditions.isEmpty()) {
            if (operator == AND)
                ctx.visit(trueCondition());
            else
                ctx.visit(falseCondition());
        }
        else if (conditions.size() == 1) {
            ctx.visit(conditions.get(0));
        }
        else {
            ctx.sql('(')
               .formatIndentStart()
               .formatNewLine();

            Keyword op = operator == AND ? K_AND : K_OR;
            Keyword separator = null;

            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0)
                    ctx.formatSeparator();
                if (separator != null)
                    ctx.visit(separator).sql(' ');

                ctx.visit(conditions.get(i));
                separator = op;
            }

            ctx.formatIndentEnd()
               .formatNewLine()
               .sql(')');
        }
    }
}
