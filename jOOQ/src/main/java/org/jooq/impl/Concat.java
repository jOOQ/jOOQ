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

import static org.jooq.impl.DSL.castAll;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.ExpressionOperator.ADD;
import static org.jooq.impl.ExpressionOperator.CONCAT;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Concat extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    Concat(Field<?>... arguments) {
        super("concat", SQLDataType.VARCHAR, arguments);
    }

    @SuppressWarnings("unchecked")
    @Override
    final Field<String> getFunction0(Configuration configuration) {

        // [#461] Type cast the concat expression, if this isn't a VARCHAR field
        Field<String>[] cast = castAll(String.class, getArguments());

        // If there is only one argument, return it immediately
        if (cast.length == 1) {
            return cast[0];
        }

        Field<String> first = cast[0];
        Field<String>[] others = new Field[cast.length - 1];
        System.arraycopy(cast, 1, others, 0, others.length);

        switch (configuration.dialect().family()) {
            case MARIADB:
            case MYSQL:
                return function("concat", SQLDataType.VARCHAR, cast);

            case SQLSERVER:
                return new Expression<String>(ADD, first, others);

            default:
                return new Expression<String>(CONCAT, first, others);
        }
    }
}
