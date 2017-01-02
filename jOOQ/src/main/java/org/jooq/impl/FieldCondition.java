/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.inline;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPartInternal;

/**
 * @author Lukas Eder
 */
final class FieldCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -9170915951443879057L;
    private final Field<Boolean> field;

    FieldCondition(Field<Boolean> field) {
        this.field = field;
    }

    @Override
    public void accept(Context<?> ctx) {
        ctx.visit(delegate(ctx.configuration()));
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    private final QueryPartInternal delegate(Configuration configuration) {
        switch (configuration.family()) {

            // [#2485] These don't work nicely, yet
            case CUBRID:
            case FIREBIRD:










                return (QueryPartInternal) condition("{0} = {1}", field, inline(true));







            // Native support
            case DERBY:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            default:
                return (QueryPartInternal) field;
        }
    }
}
