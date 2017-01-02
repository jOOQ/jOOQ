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
 */

package org.jooq.impl;

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_AND;
import static org.jooq.Clause.CONDITION_OR;
import static org.jooq.Operator.AND;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.trueCondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
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

    CombinedCondition(Operator operator, Collection<? extends Condition> conditions) {
        if (operator == null)
            throw new IllegalArgumentException("The argument 'operator' must not be null");
        if (conditions == null)
            throw new IllegalArgumentException("The argument 'conditions' must not be null");
        for (Condition condition : conditions)
            if (condition == null)
                throw new IllegalArgumentException("The argument 'conditions' must not contain null");

        this.operator = operator;
        this.conditions = new ArrayList<Condition>(conditions.size());

        init(operator, conditions);
    }

    private final void init(Operator op, Collection<? extends Condition> cond) {
        for (Condition condition : cond) {
            if (condition instanceof CombinedCondition) {
                CombinedCondition combinedCondition = (CombinedCondition) condition;

                if (combinedCondition.operator == op)
                    this.conditions.addAll(combinedCondition.conditions);
                else
                    this.conditions.add(condition);
            }
            else {
                this.conditions.add(condition);
            }
        }
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

            String operatorName = operator == AND ? "and" : "or";
            String separator = null;

            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0)
                    ctx.formatSeparator();
                if (separator != null)
                    ctx.keyword(separator).sql(' ');

                ctx.visit(conditions.get(i));
                separator = operatorName;
            }

            ctx.formatIndentEnd()
               .formatNewLine()
               .sql(')');
        }
    }
}
