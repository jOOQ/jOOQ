/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jooq.impl;

import static org.jooq.impl.DSL.trueCondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Operator;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class CombinedCondition extends AbstractCondition {

    private static final long     serialVersionUID = -7373293246207052549L;

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
    public final void bind(BindContext context) {
        context.bind(conditions);
    }

    @Override
    public final void toSQL(RenderContext context) {
        if (conditions.isEmpty()) {
            context.sql(trueCondition());
        }
        else if (conditions.size() == 1) {
            context.sql(conditions.get(0));
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
                context.sql(conditions.get(i));
                separator = operatorName;
            }

            context.formatIndentEnd()
                   .formatNewLine()
                   .sql(")");
        }
    }
}
