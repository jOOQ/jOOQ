/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.impl.Factory.val;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.CaseWhenStep;
import org.jooq.Field;
import org.jooq.RenderContext;

class CaseWhenStepImpl<V, T> extends AbstractField<T> implements CaseWhenStep<V, T> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -3817194006479624228L;

    private final Field<V>       value;
    private final List<Field<V>> compareValues;
    private final List<Field<T>> results;
    private Field<T>             otherwise;

    CaseWhenStepImpl(Field<V> value, Field<V> compareValue, Field<T> result) {
        super("case", result.getDataType());

        this.value = value;
        this.compareValues = new ArrayList<Field<V>>();
        this.results = new ArrayList<Field<T>>();

        when(compareValue, result);
    }

    @Override
    public final List<Attachable> getAttachables() {
        List<Attachable> result = new ArrayList<Attachable>();

        result.addAll(getAttachables(value));
        result.addAll(getAttachables(compareValues));
        result.addAll(getAttachables(results));
        result.addAll(getAttachables(otherwise));

        return result;
    }

    @Override
    public final Field<T> otherwise(T result) {
        return otherwise(val(result));
    }

    @Override
    public final Field<T> otherwise(Field<T> result) {
        this.otherwise = result;

        return this;
    }

    @Override
    public final CaseWhenStep<V, T> when(V compareValue, T result) {
        return when(val(compareValue), val(result));
    }

    @Override
    public final CaseWhenStep<V, T> when(V compareValue, Field<T> result) {
        return when(val(compareValue), result);
    }

    @Override
    public final CaseWhenStep<V, T> when(Field<V> compareValue, T result) {
        return when(compareValue, val(result));
    }

    @Override
    public final CaseWhenStep<V, T> when(Field<V> compareValue, Field<T> result) {
        compareValues.add(compareValue);
        results.add(result);

        return this;
    }

    @Override
    public final void bind(BindContext context) {
        switch (context.getDialect()) {

            // The DERBY dialect doesn't support the simple CASE clause
            case DERBY: {
                for (int i = 0; i < compareValues.size(); i++) {
                    context.bind(value);
                    context.bind(compareValues.get(i));
                    context.bind(results.get(i));
                }

                break;
            }

            default: {
                context.bind(value);

                for (int i = 0; i < compareValues.size(); i++) {
                    context.bind(compareValues.get(i));
                    context.bind(results.get(i));
                }

                break;
            }
        }

        if (otherwise != null) {
            context.bind(otherwise);
        }
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.sql("case");

        switch (context.getDialect()) {

            // The DERBY dialect doesn't support the simple CASE clause
            case DERBY: {
                for (int i = 0; i < compareValues.size(); i++) {
                    context.sql(" when ");
                    context.sql(value.equal(compareValues.get(i)));
                    context.sql(" then ");
                    context.sql(results.get(i));
                }

                break;
            }

            default: {
                context.sql(" ").sql(value);

                for (int i = 0; i < compareValues.size(); i++) {
                    context.sql(" when ");
                    context.sql(compareValues.get(i));
                    context.sql(" then ");
                    context.sql(results.get(i));
                }

                break;
            }
        }

        if (otherwise != null) {
            context.sql(" else ").sql(otherwise);
        }

        context.sql(" end");
    }

    @Override
    public final boolean isNullLiteral() {
        return false;
    }
}
