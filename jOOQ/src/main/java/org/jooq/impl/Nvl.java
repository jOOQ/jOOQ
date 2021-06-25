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

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Keywords.K_IS_NULL;
import static org.jooq.impl.Names.N_IFNULL;
import static org.jooq.impl.Names.N_IIF;
import static org.jooq.impl.Names.N_NVL;
import static org.jooq.impl.Tools.anyNotNull;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Nvl<T> extends AbstractField<T> {

    private final Field<T>    arg1;
    private final Field<T>    arg2;

    Nvl(Field<T> arg1, Field<T> arg2) {
        super(N_NVL, anyNotNull(arg1.getDataType(), arg1, arg2));

        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {




























            case CUBRID:
            case DERBY:
            case IGNITE:
            case FIREBIRD:
            case POSTGRES:
                ctx.visit(DSL.coalesce(arg1, arg2));
                break;




            case MARIADB:
            case MYSQL:
            case SQLITE:
                ctx.visit(function(N_IFNULL, getDataType(), arg1, arg2));
                break;

            default:
                ctx.visit(function(N_NVL, getDataType(), arg1, arg2));
                break;
        }
    }
}
