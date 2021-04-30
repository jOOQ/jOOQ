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
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.Names.N_FIELD;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.combine;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class FieldFunction<T> extends AbstractField<Integer> {
    private final QueryPartListView<Field<?>> arguments;

    FieldFunction(Field<T> field, Field<T>[] arguments) {
        super(N_FIELD, INTEGER);

        this.arguments = wrap(combine(field, arguments));
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {


            case MARIADB:
            case MYSQL:
                if (arguments.size() > 1)
                    ctx.visit(N_FIELD).sql('(').visit(arguments).sql(')');
                else
                    acceptDefault(ctx);

                break;

            default:
                acceptDefault(ctx);
                break;
        }
    }

    private final void acceptDefault(Context<?> ctx) {
        int size = arguments.size();

        if (size == 1) {
            ctx.visit(zero());
        }
        else {
            List<Field<?>> args = new ArrayList<>();
            args.add(arguments.get(0));

            for (int i = 1; i < size; i++) {
                args.add(arguments.get(i));
                args.add(inline(i));
            }

            args.add(inline(0));

            ctx.visit(DSL.decode(
                args.get(0),
                args.get(1),
                args.get(2),
                (Object[]) args.subList(3, args.size()).toArray(EMPTY_FIELD)
            ));
        }
    }
}
