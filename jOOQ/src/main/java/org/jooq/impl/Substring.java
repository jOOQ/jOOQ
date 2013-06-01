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

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Substring extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    Substring(Field<?>... arguments) {
        super("substring", SQLDataType.VARCHAR, arguments);
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        String functionName = "substring";

        switch (configuration.dialect().family()) {

            // Sybase ASE and SQL Server requires 3 arguments
            case ASE:
            case SQLSERVER: {
                if (getArguments().length == 2) {
                    return function(functionName,
                        SQLDataType.VARCHAR, getArguments()[0],
                        getArguments()[1],
                        inline(Integer.MAX_VALUE));
                }

                // Default behaviour
                else {
                    break;
                }
            }

            // [#430] Firebird has its own syntax
            case FIREBIRD: {
                if (getArguments().length == 2) {
                    return field("{substring}({0} {from} {1})", SQLDataType.VARCHAR, getArguments());
                }
                else {
                    return field("{substring}({0} {from} {1} {for} {2})", SQLDataType.VARCHAR, getArguments());
                }
            }

            // [#722] For undocumented reasons, Ingres needs explicit casting
            case INGRES: {
                if (getArguments().length == 2) {
                    return field("{substring}({0}, {cast}({1} {as integer}))",
                        SQLDataType.VARCHAR,
                        getArguments());
                }
                else {
                    return field("{substring}({0}, {cast}({1} {as integer}), {cast}({2} {as integer}))",
                        SQLDataType.VARCHAR,
                        getArguments());
                }
            }

            case DB2:
            case DERBY:
            case ORACLE:
            case SQLITE:
                functionName = "substr";
                break;
        }

        return function(functionName, SQLDataType.VARCHAR, getArguments());
    }
}
