/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */

package org.jooq.impl;

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_AND;
import static org.jooq.Clause.CONDITION_OR;
import static org.jooq.Operator.AND;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.Utils.visitAll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Operator;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class CombinedCondition extends AbstractCondition {

    private static final long     serialVersionUID = -7373293246207052549L;
    private static final Clause[] CLAUSES_AND      = { CONDITION, CONDITION_AND };
    private static final Clause[] CLAUSES_OR       = { CONDITION, CONDITION_OR };

    private final Operator        operator;
    private final List<Condition> conditions;

    CombinedCondition(Operator operator, Collection<? extends Condition> conditions) {
        if (operator == null) {
            throw new IllegalArgumentException("The argument 'operator' must not be null");
        }
        if (conditions == null) {
            throw new IllegalArgumentException("The argument 'conditions' must not be null");
        }
        for (Condition condition : conditions) {
            if (condition == null) {
                throw new IllegalArgumentException("The argument 'conditions' must not contain null");
            }
        }

        this.operator = operator;
        this.conditions = new ArrayList<Condition>();

        init(operator, conditions);
    }

    private final void init(Operator op, Collection<? extends Condition> cond) {
        for (Condition condition : cond) {
            if (condition instanceof CombinedCondition) {
                CombinedCondition combinedCondition = (CombinedCondition) condition;
                if (combinedCondition.operator == op) {
                    this.conditions.addAll(combinedCondition.conditions);
                }
                else {
                    this.conditions.add(condition);
                }
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
    public final void bind(BindContext context) {
        visitAll(context, conditions);
    }

    @Override
    public final void toSQL(RenderContext context) {
        if (conditions.isEmpty()) {
            context.visit(trueCondition());
        }
        else if (conditions.size() == 1) {
            context.visit(conditions.get(0));
        }
        else {
            context.sql("(")
                   .formatIndentStart()
                   .formatNewLine();

            String operatorName = operator.name().toLowerCase() + " ";
            String separator = "";

            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    context.formatSeparator();
                }

                context.keyword(separator);
                context.visit(conditions.get(i));
                separator = operatorName;
            }

            context.formatIndentEnd()
                   .formatNewLine()
                   .sql(")");
        }
    }
}
