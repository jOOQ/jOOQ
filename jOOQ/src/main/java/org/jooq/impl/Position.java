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

import static org.jooq.impl.DSL.one;
import static org.jooq.impl.Keywords.F_CHARINDEX;
import static org.jooq.impl.Keywords.F_INSTR;
import static org.jooq.impl.Keywords.F_LOCATE;
import static org.jooq.impl.Keywords.F_POSITION;
import static org.jooq.impl.Keywords.K_IN;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Position extends AbstractField<Integer> {

    private static final long             serialVersionUID = 3544690069533526544L;

    private final Field<String>           search;
    private final Field<String>           in;
    private final Field<? extends Number> startIndex;

    Position(Field<String> search, Field<String> in) {
        this(search, in, null);
    }

    Position(Field<String> search, Field<String> in, Field<? extends Number> startIndex) {
        super(DSL.name("position"), SQLDataType.INTEGER);

        this.search = search;
        this.in = in;
        this.startIndex = startIndex;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (startIndex != null)
            switch (ctx.family()) {












                default:
                    ctx.visit(DSL.position(DSL.substring(in, startIndex), search).add(startIndex).sub(one()));
                    break;
            }
        else
            switch (ctx.family()) {



                case DERBY:
                    ctx.visit(F_LOCATE).sql('(').visit(search).sql(", ").visit(in).sql(')');
                    break;

















                case SQLITE:
                    ctx.visit(F_INSTR).sql('(').visit(in).sql(", ").visit(search).sql(')');
                    break;

                default:
                    ctx.visit(F_POSITION).sql('(').visit(search).sql(' ').visit(K_IN).sql(' ').visit(in).sql(')');
                    break;
            }
    }
}
