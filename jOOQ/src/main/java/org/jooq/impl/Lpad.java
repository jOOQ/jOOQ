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
class Lpad extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    private final Field<String>           field;
    private final Field<? extends Number> length;
    private final Field<String>           character;

    Lpad(Field<String> field, Field<? extends Number> length) {
        this(field, length, null);
    }

    Lpad(Field<String> field, Field<? extends Number> length, Field<String> character) {
        super("lpad", SQLDataType.VARCHAR, field, length, character);

        this.field = field;
        this.length = length;
        this.character = (character == null ? inline(" ") : character);
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {
            case ASE:
            case SQLSERVER:
            case SYBASE: {
                return DSL.concat(DSL.repeat(character, length.sub(DSL.length(field))), field);
            }

            // This beautiful expression was contributed by "Ludo", here:
            // http://stackoverflow.com/questions/6576343/how-to-simulate-lpad-rpad-with-sqlite
            case SQLITE: {
                return DSL.field(
                    "substr(" +
                      "replace(" +
                        "replace(" +
                          "substr(" +
                            "quote(" +
                              "zeroblob((({1} - length({0}) - 1 + length({2})) / length({2}) + 1) / 2)" +
                            "), 3" +
                          "), '''', ''" +
                        "), '0', {2}" +
                      "), 1, ({1} - length({0}))" +
                    ") || {0}",
                    String.class,
                    field, length, character);
            }

            // According to the Firebird documentation, LPAD outcomes should be
            // cast to truncate large results...
            case FIREBIRD: {
                return field("cast(lpad({0}, {1}, {2}) as varchar(4000))", SQLDataType.VARCHAR, field, length, character);
            }

            default: {
                return function("lpad", SQLDataType.VARCHAR, field, length, character);
            }
        }
    }
}
