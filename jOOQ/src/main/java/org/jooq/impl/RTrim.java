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
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.Keywords.F_RTRIM;
import static org.jooq.impl.Keywords.F_TRIM;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Keywords.K_TRAILING;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class RTrim extends AbstractField<String> {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -7273879239726265322L;

    private final Field<String> argument;
    private final Field<String> characters;

    RTrim(Field<String> argument) {
        this(argument, null);
    }

    RTrim(Field<String> argument, Field<String> characters) {
        super(DSL.name("rtrim"), SQLDataType.VARCHAR);

        this.argument = argument;
        this.characters = characters;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (characters == null) {
            switch (ctx.family()) {
                case FIREBIRD:
                    ctx.visit(F_TRIM).sql('(').visit(K_TRAILING).sql(' ').visit(K_FROM).sql(' ').visit(argument).sql(')');
                    break;

                default:
                    ctx.visit(F_RTRIM).sql('(').visit(argument).sql(')');
                    break;
            }
        }
        else {
            switch (ctx.family()) {





                case SQLITE:
                    ctx.visit(F_RTRIM).sql('(').visit(argument).sql(", ").visit(characters).sql(')');
                    break;

                default:
                    ctx.visit(F_TRIM).sql('(').visit(K_TRAILING).sql(' ').visit(characters).sql(' ').visit(K_FROM).sql(' ').visit(argument).sql(')');
                    break;
            }
        }
    }
}
