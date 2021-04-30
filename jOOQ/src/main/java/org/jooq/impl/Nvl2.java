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

import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.iif;
import static org.jooq.impl.Names.N_NVL2;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Nvl2<T> extends AbstractField<T> {

    private final Field<?>    arg1;
    private final Field<T>    arg2;
    private final Field<T>    arg3;

    Nvl2(Field<?> arg1, Field<T> arg2, Field<T> arg3) {
        super(N_NVL2, !arg1.getDataType().nullable() ? arg2.getDataType() : Tools.allNotNull(arg2.getDataType(), arg2, arg3));

        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {







            case MARIADB:





                acceptDefault(ctx);
                break;










            case CUBRID:
            case DERBY:
            case FIREBIRD:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
                acceptCase(ctx);
                break;

            default:
                acceptDefault(ctx);
                break;
        }
    }

    private void acceptCase(Context<?> ctx) {
        ctx.visit(DSL.when(arg1.isNotNull(), arg2).otherwise(arg3));
    }

    private final void acceptDefault(Context<?> ctx) {
        ctx.visit(function(N_NVL2, getDataType(), arg1, arg2, arg3));
    }
}
