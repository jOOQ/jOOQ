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

import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Least<T> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    Least(DataType<T> type, Field<?>... arguments) {
        super("least", type, arguments);
    }

    @SuppressWarnings("unchecked")
    @Override
    final Field<T> getFunction0(Configuration configuration) {

        // In any dialect, a single argument is always the least
        if (getArguments().length == 1) {
            return (Field<T>) getArguments()[0];
        }

        switch (configuration.dialect().family()) {
            // This implementation has O(2^n) complexity. Better implementations
            // are very welcome

            case ASE:
            case DERBY:
            case SQLSERVER:
            case SYBASE: {
                Field<T> first = (Field<T>) getArguments()[0];
                Field<T> other = (Field<T>) getArguments()[1];

                if (getArguments().length > 2) {
                    Field<?>[] remaining = new Field<?>[getArguments().length - 2];
                    System.arraycopy(getArguments(), 2, remaining, 0, remaining.length);

                    return DSL.decode()
                        .when(first.lessThan(other), DSL.least(first, remaining))
                        .otherwise(DSL.least(other, remaining));
                }
                else {
                    return DSL.decode()
                        .when(first.lessThan(other), first)
                        .otherwise(other);
                }
            }

            case FIREBIRD:
                return function("minvalue", getDataType(), getArguments());

            case SQLITE:
                return function("min", getDataType(), getArguments());

            default:
                return function("least", getDataType(), getArguments());
        }
    }
}
