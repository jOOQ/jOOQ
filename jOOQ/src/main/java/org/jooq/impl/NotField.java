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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_NOT;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.not;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;

final class NotField extends AbstractField<Boolean> {

    private static final long     serialVersionUID = 2921001862882237932L;
    private static final Clause[] CLAUSES          = { CONDITION, CONDITION_NOT };

    private final Field<Boolean>  field;

    NotField(Field<Boolean> field) {
        super("not", field.getDataType());
        this.field = field;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

            // [#2485] These don't work nicely, yet
            case CUBRID:
            case FIREBIRD:










                ctx.visit(DSL.field(not(condition(field))));
                break;





            // Native support
            case DERBY:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            default:
                ctx.visit(DSL.field("{not}({0})", getDataType(), field));
                break;
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
