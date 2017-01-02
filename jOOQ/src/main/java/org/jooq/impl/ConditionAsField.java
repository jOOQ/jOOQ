/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.not;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.QueryPart;

/**
 * @author Lukas Eder
 */
final class ConditionAsField extends AbstractFunction<Boolean> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -5921673852489483721L;
    private final Condition   condition;

    ConditionAsField(Condition condition) {
        super(condition.toString(), SQLDataType.BOOLEAN);

        this.condition = condition;
    }

    @Override
    final QueryPart getFunction0(Configuration configuration) {
        switch (configuration.family()) {

            // Some databases don't accept predicates where column expressions
            // are expected.








            case CUBRID:
            case FIREBIRD:

                // [#3206] Correct implementation of three-valued logic is important here
                return DSL.when(condition, inline(true))
                          .when(not(condition), inline(false))
                          .otherwise(inline((Boolean) null));

            // These databases can inline predicates in column expression contexts
            case DERBY:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:





            // The default, for new dialects
            default:
                return DSL.sql("({0})", condition);
        }
    }
}
