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

import static org.jooq.impl.Names.N_HEX;
import static org.jooq.impl.Names.N_REPEAT;
import static org.jooq.impl.Names.N_REPLACE;
import static org.jooq.impl.Names.N_REPLICATE;
import static org.jooq.impl.Names.N_RPAD;
import static org.jooq.impl.Names.N_ZEROBLOB;
import static org.jooq.impl.SQLDataType.VARCHAR;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Repeat extends AbstractField<String> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    private final Field<String>           string;
    private final Field<? extends Number> count;

    Repeat(Field<String> string, Field<? extends Number> count) {
        super(N_RPAD, VARCHAR);

        this.string = string;
        this.count = count;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {








            case FIREBIRD:
                ctx.visit(DSL.rpad(string, DSL.length(string).mul(count), string));
                break;

            // Emulation of REPEAT() for SQLite currently cannot be achieved
            // using RPAD() above, as RPAD() expects characters, not strings
            // Another option is documented here, though:
            // https://stackoverflow.com/a/51792334/521799
            case SQLITE:
                ctx.visit(N_REPLACE).sql('(').visit(N_HEX).sql('(').visit(N_ZEROBLOB).sql('(').visit(count).sql(")), '00', ").visit(string).sql(')');
                break;









            default:
                ctx.visit(N_REPEAT).sql('(').visit(string).sql(", ").visit(count).sql(')');
                break;
        }
    }
}
