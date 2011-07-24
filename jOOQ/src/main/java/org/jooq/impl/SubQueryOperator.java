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

/**
 * Any operator used in a {@link SubQueryCondition}
 *
 * @author Lukas Eder
 */
enum SubQueryOperator {

    IN("in"),
    NOT_IN("not in"),
    EQUALS("="),
    EQUALS_ANY("= any"),
    EQUALS_SOME("= some"),
    EQUALS_ALL("= all"),
    NOT_EQUALS("<>"),
    NOT_EQUALS_ANY("<> any"),
    NOT_EQUALS_SOME("<> some"),
    NOT_EQUALS_ALL("<> all"),
    LESS("<"),
    LESS_THAN_ANY("< any"),
    LESS_THAN_SOME("< some"),
    LESS_THAN_ALL("< all"),
    LESS_OR_EQUAL("<="),
    LESS_OR_EQUAL_ANY("<= any"),
    LESS_OR_EQUAL_SOME("<= some"),
    LESS_OR_EQUAL_ALL("<= all"),
    GREATER(">"),
    GREATER_THAN_ANY("> any"),
    GREATER_THAN_SOME("> some"),
    GREATER_THAN_ALL("> all"),
    GREATER_OR_EQUAL(">="),
    GREATER_OR_EQUAL_ANY(">= any"),
    GREATER_OR_EQUAL_SOME(">= some"),
    GREATER_OR_EQUAL_ALL(">= all"), ;

    private final String sql;

    private SubQueryOperator(String sql) {
        this.sql = sql;
    }

    public String toSQL() {
        return sql;
    }
}
