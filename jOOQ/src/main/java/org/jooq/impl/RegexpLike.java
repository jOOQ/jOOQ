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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_COMPARISON;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class RegexpLike extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 3162855665213654276L;
    private static final Clause[] CLAUSES          = { CONDITION, CONDITION_COMPARISON };

    private final Field<?>        search;
    private final Field<String>   pattern;

    RegexpLike(Field<?> search, Field<String> pattern) {
        this.search = search;
        this.pattern = pattern;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

            // [#620] These databases are compatible with the MySQL syntax



            case CUBRID:
            case H2:
            case MARIADB:
            case MYSQL:
            case SQLITE: {
                ctx.visit(search)
                   .sql(' ')
                   .keyword("regexp")
                   .sql(' ')
                   .visit(pattern);

                break;
            }

            // [#620] HSQLDB has its own syntax
            case HSQLDB: {

                // [#1570] TODO: Replace this by SQL.condition(String, QueryPart...)
                ctx.visit(DSL.condition("{regexp_matches}({0}, {1})", search, pattern));
                break;
            }

            // [#620] Postgres has its own syntax



            case POSTGRES: {

                // [#1570] TODO: Replace this by SQL.condition(String, QueryPart...)
                ctx.visit(DSL.condition("{0} ~ {1}", search, pattern));
                break;
            }












            // Render the SQL standard for those databases that do not support
            // regular expressions






            case DERBY:
            case FIREBIRD:
            default: {
                ctx.visit(search)
                   .sql(' ')
                   .keyword("like_regex")
                   .sql(' ')
                   .visit(pattern);

                break;
            }
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
