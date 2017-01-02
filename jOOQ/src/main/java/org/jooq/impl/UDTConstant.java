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
 */

package org.jooq.impl;

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.val;

import org.jooq.BindContext;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.UDTRecord;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
final class UDTConstant<R extends UDTRecord<R>> extends AbstractParam<R> {

    private static final long serialVersionUID = 6807729087019209084L;

    UDTConstant(R value) {
        super(value, value.getUDT().getDataType());
    }

    @Override
    public void accept(Context<?> ctx) {
        if (ctx instanceof RenderContext)
            toSQL0((RenderContext) ctx);
        else
            bind0((BindContext) ctx);
    }

    final void toSQL0(RenderContext context) {
        switch (context.family()) {


































            // Due to lack of UDT support in the Postgres JDBC drivers, all UDT's
            // have to be inlined
            case POSTGRES: {
                toSQLInline(context);
                return;
            }

            // Assume default behaviour if dialect is not available
            default:
                toSQLInline(context);
                return;
        }
    }

    private final void toSQLInline(RenderContext context) {
        switch (context.family()) {
            case POSTGRES:
                context.keyword("row");
                break;






            // Assume default behaviour if dialect is not available
            default: {
                context.visit(value.getUDT());
                break;
            }
        }

        context.sql('(');

        String separator = "";
        for (Field<?> field : value.fields()) {
            context.sql(separator);
            context.visit(val(value.get(field), field));
            separator = ", ";
        }

        context.sql(')');
    }

    @Deprecated
    private final String getInlineConstructor(RenderContext context) {
        switch (context.family()) {
            case POSTGRES:
                return "ROW";






            // Assume default behaviour if dialect is not available
            default:
                return Tools.getMappedUDTName(context.configuration(), value);
        }
    }

    final void bind0(BindContext context) {
        switch (context.family()) {












            // Postgres cannot bind a complete structured type. The type is
            // inlined instead: ROW(.., .., ..)
            case POSTGRES: {
                for (Field<?> field : value.fields())
                    context.visit(val(value.get(field)));

                break;
            }

            default:
                throw new SQLDialectNotSupportedException("UDTs not supported in dialect " + context.dialect());
        }
    }
}
