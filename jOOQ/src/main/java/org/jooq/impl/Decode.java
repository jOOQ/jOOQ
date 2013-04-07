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

import static org.jooq.impl.DSL.function;

import org.jooq.CaseConditionStep;
import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Decode<T, Z> extends AbstractFunction<Z> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    private final Field<T>    field;
    private final Field<T>    search;
    private final Field<Z>    result;
    private final Field<?>[]  more;

    public Decode(Field<T> field, Field<T> search, Field<Z> result, Field<?>[] more) {
        super("decode", result.getDataType(), Utils.combine(field, search, result, more));

        this.field = field;
        this.search = search;
        this.result = result;
        this.more = more;
    }

    @SuppressWarnings("unchecked")
    @Override
    final Field<Z> getFunction0(Configuration configuration) {
        switch (configuration.dialect()) {

            // Oracle actually has this function
            case ORACLE: {
                return function("decode", getDataType(), getArguments());
            }

            // Other dialects simulate it with a CASE ... WHEN expression
            default: {
                CaseConditionStep<Z> when = DSL
                    .decode()
                    .when(field.isNotDistinctFrom(search), result);

                for (int i = 0; i < more.length; i += 2) {

                    // search/result pair
                    if (i + 1 < more.length) {
                        when = when.when(field.isNotDistinctFrom((Field<T>) more[i]), (Field<Z>) more[i + 1]);
                    }

                    // trailing default value
                    else {
                        return when.otherwise((Field<Z>) more[i]);
                    }
                }

                return when;
            }
        }
    }
}
