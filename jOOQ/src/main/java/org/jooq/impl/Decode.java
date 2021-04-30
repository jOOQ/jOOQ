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

import static org.jooq.SQLDialect.*;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.Names.N_DECODE;
import static org.jooq.impl.Names.N_MAP;

import java.util.Set;

import org.jooq.CaseConditionStep;
import org.jooq.Context;
import org.jooq.Field;
// ...
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class Decode<T, Z> extends AbstractField<Z> {
    private static final Set<SQLDialect> EMULATE_DISTINCT = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE);





    private final Field<T>    field;
    private final Field<T>    search;
    private final Field<Z>    result;
    private final Field<?>[]  more;

    public Decode(Field<T> field, Field<T> search, Field<Z> result, Field<?>[] more) {
        super(N_DECODE, result.getDataType());

        this.field = field;
        this.search = search;
        this.result = result;
        this.more = more;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void accept(Context<?> ctx) {
        if (EMULATE_DISTINCT.contains(ctx.dialect())) {
            CaseConditionStep<Z> when = DSL.choose().when(field.isNotDistinctFrom(search), result);

            for (int i = 0; i + 1 < more.length; i += 2)
                when = when.when(field.isNotDistinctFrom((Field<T>) more[i]), (Field<Z>) more[i + 1]);

            if (more.length % 2 == 0)
                ctx.visit(when);
            else
                ctx.visit(when.otherwise((Field<Z>) more[more.length - 1]));
        }






        else
            ctx.visit(function(N_DECODE, getDataType(), Tools.combine(field, search, result, more)));
    }
}
