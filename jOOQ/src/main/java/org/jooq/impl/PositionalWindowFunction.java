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

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.inlined;
import static org.jooq.impl.Names.N_COALESCE;
import static org.jooq.impl.PositionalWindowFunction.PositionalFunctionType.LAG;
import static org.jooq.impl.PositionalWindowFunction.PositionalFunctionType.LEAD;
import static org.jooq.impl.Tools.camelCase;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;

/**
 * @author Lukas Eder
 */
final class PositionalWindowFunction<T> extends AbstractWindowFunction<T> {
    private final PositionalFunctionType functionType;
    private final Field<T>               arg;
    private final Field<Integer>         offset;
    private final Field<T>               defaultValue;

    PositionalWindowFunction(PositionalFunctionType functionType, Field<T> arg) {
        this(functionType, arg, null, null);
    }

    PositionalWindowFunction(PositionalFunctionType functionType, Field<T> arg, Field<Integer> offset, Field<T> defaultValue) {
        super(functionType.name, arg.getDataType().null_());

        this.functionType = functionType;
        this.arg = arg;
        this.offset = offset;
        this.defaultValue = defaultValue;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (defaultValue == null) {
            accept0(ctx);
        }
        else {
            switch (ctx.family()) {













                default:
                    accept0(ctx);
                    break;
            }
        }
    }

    private final void accept0(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(functionType.name).sql('(');
                break;
        }

        ctx.visit(arg);

        if (offset != null) {
            switch (ctx.family()) {










                default:
                    ctx.sql(", ").visit(offset);
                    break;
            }
        }

        if (defaultValue != null) {
            switch (ctx.family()) {












                default:
                    ctx.sql(", ").visit(defaultValue);
                    break;
            }
        }





        ctx.sql(')');

        acceptFromFirstOrLast(ctx);
        acceptNullTreatment(ctx);
        acceptOverClause(ctx);
    }

    final boolean isLeadOrLag() {
        return functionType == LEAD || functionType == LAG;
    }

    enum PositionalFunctionType {
        LEAD, LAG, FIRST_VALUE, LAST_VALUE, NTH_VALUE;

        private final Name name;

        private PositionalFunctionType() {
            this.name = DSL.unquotedName(name().toLowerCase());
        }
    }
}
