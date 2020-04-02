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

import static org.jooq.impl.Keywords.K_COALESCE;
import static org.jooq.impl.Keywords.K_IS_NULL;
import static org.jooq.impl.Names.N_IFNULL;
import static org.jooq.impl.Names.N_IIF;
import static org.jooq.impl.Names.N_NVL;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Nvl<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    private final Field<T>    arg1;
    private final Field<T>    arg2;

    Nvl(Field<T> arg1, Field<T> arg2) {
        super(N_NVL, arg1.getDataType());

        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {










            case H2:
            case HSQLDB:
                ctx.visit(N_NVL).sql('(').visit(arg1).sql(", ").visit(arg2).sql(')');
                break;





            case DERBY:
            case POSTGRES:
                ctx.visit(K_COALESCE).sql('(').visit(arg1).sql(", ").visit(arg2).sql(')');
                break;





            case MARIADB:
            case MYSQL:
            case SQLITE:
                ctx.visit(N_IFNULL).sql('(').visit(arg1).sql(", ").visit(arg2).sql(')');
                break;

            default:
                ctx.visit(DSL.when(arg1.isNotNull(), arg1).otherwise(arg2));
                break;
        }
    }
}
