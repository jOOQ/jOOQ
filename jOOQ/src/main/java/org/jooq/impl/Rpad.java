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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Keywords.F_HEX;
import static org.jooq.impl.Keywords.F_LEN;
import static org.jooq.impl.Keywords.F_LENGTH;
import static org.jooq.impl.Keywords.F_REPLACE;
import static org.jooq.impl.Keywords.F_RPAD;
import static org.jooq.impl.Keywords.F_SPACE;
import static org.jooq.impl.Keywords.F_SUBSTR;
import static org.jooq.impl.Keywords.F_ZEROBLOB;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_CAST;
import static org.jooq.impl.Keywords.K_VARCHAR;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Rpad extends AbstractField<String> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    private final Field<String>           field;
    private final Field<? extends Number> length;
    private final Field<String>           character;

    Rpad(Field<String> field, Field<? extends Number> length) {
        this(field, length, null);
    }

    Rpad(Field<String> field, Field<? extends Number> length, Field<String> character) {
        super(DSL.name("rpad"), SQLDataType.VARCHAR);

        this.field = field;
        this.length = length;
        this.character = (character == null ? inline(" ") : character);
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {













            // This beautiful expression was contributed by "Ludo", here:
            // http://stackoverflow.com/questions/6576343/how-to-simulate-lpad-rpad-with-sqlite
            case SQLITE:
                ctx.visit(field).sql(" || ").visit(F_SUBSTR).sql('(')
                    .visit(F_REPLACE).sql('(')
                        .visit(F_HEX).sql('(')
                            .visit(F_ZEROBLOB).sql('(')
                                .visit(length)
                        .sql(")), '00', ").visit(character)
                    .sql("), 1, ").visit(length).sql(" - ").visit(F_LENGTH).sql('(').visit(field).sql(')')
                .sql(')');
                break;

            // According to the Firebird documentation, RPAD outcomes should be
            // cast to truncate large results...
            case FIREBIRD:
                ctx.visit(K_CAST).sql('(').visit(F_RPAD).sql('(').visit(field).sql(", ").visit(length).sql(", ").visit(character).sql(") ").visit(K_AS).sql(' ').visit(K_VARCHAR).sql("(4000))");
                break;

            default:
                ctx.visit(F_RPAD).sql('(').visit(field).sql(", ").visit(length).sql(", ").visit(character).sql(')');
                break;
        }
    }
}
