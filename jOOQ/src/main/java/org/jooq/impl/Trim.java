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

import static org.jooq.impl.Keywords.K_BOTH;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Names.N_TRIM;
import static org.jooq.impl.SQLDataType.VARCHAR;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Trim extends AbstractField<String> {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -7273879239726265322L;

    private final Field<String> argument;
    private final Field<String> characters;

    Trim(Field<String> argument) {
        this(argument, null);
    }

    Trim(Field<String> argument, Field<String> characters) {
        super(N_TRIM, VARCHAR);

        this.argument = argument;
        this.characters = characters;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (characters == null) {
            switch (ctx.dialect()) {















                default:
                    ctx.visit(N_TRIM).sql('(').visit(argument).sql(')');
                    break;
            }
        }
        else {
            switch (ctx.dialect()) {





                case SQLITE:
                    ctx.visit(N_TRIM).sql('(').visit(argument).sql(", ").visit(characters).sql(')');
                    break;







                default:
                    ctx.visit(N_TRIM).sql('(').visit(K_BOTH).sql(' ').visit(characters).sql(' ').visit(K_FROM).sql(' ').visit(argument).sql(')');
                    break;
            }
        }
    }
}
