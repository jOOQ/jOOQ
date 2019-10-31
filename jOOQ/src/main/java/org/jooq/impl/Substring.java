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
import static org.jooq.impl.Keywords.F_MID;
import static org.jooq.impl.Keywords.F_SUBSTR;
import static org.jooq.impl.Keywords.F_SUBSTRING;
import static org.jooq.impl.Keywords.K_FOR;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Names.N_SUBSTRING;
import static org.jooq.impl.SQLDataType.VARCHAR;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Keyword;

/**
 * @author Lukas Eder
 */
final class Substring extends AbstractField<String> {

    /**
     * Generated UID
     */
    private static final long                    serialVersionUID = -7273879239726265322L;

    private final        Field<String>           field;
    private final        Field<? extends Number> startingPosition;
    private final        Field<? extends Number> length;

    Substring(Field<String> field, Field<? extends Number> startingPosition) {
        this(field, startingPosition, null);
    }

    Substring(Field<String> field, Field<? extends Number> startingPosition, Field<? extends Number> length) {
        super(N_SUBSTRING, VARCHAR);

        this.field = field;
        this.startingPosition = startingPosition;
        this.length = length;
    }

    @Override
    public final void accept(Context<?> ctx) {
        Keyword functionName = F_SUBSTRING;

        switch (ctx.family()) {

            // [#430] These databases use SQL standard syntax



            case FIREBIRD: {
                if (length == null)
                    ctx.visit(F_SUBSTRING).sql('(').visit(field).sql(' ').visit(K_FROM).sql(' ').visit(startingPosition).sql(')');
                else
                    ctx.visit(F_SUBSTRING).sql('(').visit(field).sql(' ').visit(K_FROM).sql(' ').visit(startingPosition).sql(' ').visit(K_FOR).sql(' ').visit(length).sql(')');
                return;
            }
































            case DERBY:
            case SQLITE:
                functionName = F_SUBSTR;
                break;
        }

        if (length == null)
            ctx.visit(functionName).sql('(').visit(field).sql(", ").visit(startingPosition).sql(')');
        else
            ctx.visit(functionName).sql('(').visit(field).sql(", ").visit(startingPosition).sql(", ").visit(length).sql(')');
    }
}
