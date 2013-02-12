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

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.CaseConditionStep;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.RenderContext;

class CaseConditionStepImpl<T> extends AbstractField<T> implements CaseConditionStep<T> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -1735676153683257465L;

    private final List<Condition> conditions;
    private final List<Field<T>>  results;
    private Field<T>              otherwise;

    CaseConditionStepImpl(Condition condition, Field<T> result) {
        super("case", result.getDataType());

        this.conditions = new ArrayList<Condition>();
        this.results = new ArrayList<Field<T>>();

        when(condition, result);
    }

    @Override
    public final CaseConditionStep<T> when(Condition condition, T result) {
        return when(condition, Utils.field(result));
    }

    @Override
    public final CaseConditionStep<T> when(Condition condition, Field<T> result) {
        conditions.add(condition);
        results.add(result);

        return this;
    }

    @Override
    public final Field<T> otherwise(T result) {
        return otherwise(Utils.field(result));
    }

    @Override
    public final Field<T> otherwise(Field<T> result) {
        this.otherwise = result;

        return this;
    }

    @Override
    public final void bind(BindContext context) {
        for (int i = 0; i < conditions.size(); i++) {
            context.bind(conditions.get(i));
            context.bind(results.get(i));
        }

        if (otherwise != null) {
            context.bind(otherwise);
        }
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.formatIndentLockStart()
               .keyword("case")
               .formatIndentLockStart();

        int size = conditions.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                context.formatNewLine();
            }

            context.keyword(" when ")
                   .sql(conditions.get(i))
                   .keyword(" then ")
                   .sql(results.get(i));
        }

        if (otherwise != null) {
            context.formatNewLine()
                   .keyword(" else ")
                   .sql(otherwise);
        }

        context.formatIndentLockEnd();

        if (size > 1 || otherwise != null) {
            context.formatSeparator();
        }
        else {
            context.sql(" ");
        }

        context.keyword("end")
               .formatIndentLockEnd();
    }
}
