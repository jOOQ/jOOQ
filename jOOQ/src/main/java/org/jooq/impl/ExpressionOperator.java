/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.raw;

import org.jooq.Name;
import org.jooq.QueryPart;
import org.jooq.SQL;

/**
 * The operator used in <code>Expression</code>
 *
 * @author Lukas Eder
 */
enum ExpressionOperator {

    /**
     * Concatenation
     */
    CONCAT("||", true, false),

    /**
     * Addition
     */
    ADD("+", true, true),

    /**
     * Subtraction
     */
    SUBTRACT("-"),

    /**
     * Multiplication
     */
    MULTIPLY("*", true, true),

    /**
     * Division
     */
    DIVIDE("/"),

    ;

    private final String  sql;
    private final SQL     queryPart;
    private final Name    name;
    private final boolean associative;
    private final boolean commutative;

    private ExpressionOperator(String sql) {
        this(sql, false, false);
    }

    private ExpressionOperator(String sql, boolean associative, boolean commutative) {
        this.sql = sql;
        this.queryPart = raw(sql);
        this.name = DSL.name(name().toLowerCase());
        this.associative = associative;
        this.commutative = commutative;
    }

    final String toSQL() {
        return sql;
    }

    final QueryPart toQueryPart() {
        return queryPart;
    }

    final Name toName() {
        return name;
    }

    final boolean associative() {
        return associative;
    }

    final boolean commutative() {
        return commutative;
    }
}
