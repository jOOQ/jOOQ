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

import static org.jooq.impl.Keywords.F_NVL2;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Nvl2<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    private final Field<?>    arg1;
    private final Field<T>    arg2;
    private final Field<T>    arg3;

    Nvl2(Field<?> arg1, Field<T> arg2, Field<T> arg3) {
        super(DSL.name("nvl2"), arg2.getDataType());

        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {





            case H2:
            case HSQLDB:
                ctx.visit(F_NVL2).sql('(').visit(arg1).sql(", ").visit(arg2).sql(", ").visit(arg3).sql(')');
                break;

            default:
                ctx.visit(DSL.when(arg1.isNotNull(), arg2).otherwise(arg3));
                break;
        }
    }
}
