/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
class UDTConstant<R extends UDTRecord<R>> extends AbstractParam<R> {

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

            /* [pro] */
            // Oracle supports java.sql.SQLData, hence the record can be bound
            // to the CallableStatement directly
            case ORACLE: {
                if (context.paramType() == INLINED) {
                    toSQLInline(context);
                } else {
                    context.sql('?');
                }

                return;
            }

            // DB2 supports UDT's but this is only experimental in jOOQ
            case DB2: {

                // The subsequent DB2 logic should be refactored into toSQLInline()
                context.sql(getInlineConstructor(context));
                context.sql("()");

                String separator = "..";
                for (Field<?> field : value.fields()) {
                    context.sql(separator);
                    context.sql(field.getName());
                    context.sql('(');
                    context.visit(val(value.getValue(field)));
                    context.sql(')');
                }

                return;
            }

            /* [/pro] */
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

            /* [pro] */
            case ORACLE:
            case DB2:
            /* [/pro] */

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
            context.visit(val(value.getValue(field), field));
            separator = ", ";
        }

        context.sql(')');
    }

    @Deprecated
    private final String getInlineConstructor(RenderContext context) {
        switch (context.family()) {
            case POSTGRES:
                return "ROW";

            /* [pro] */
            case ORACLE:
            case DB2:
            /* [/pro] */

            // Assume default behaviour if dialect is not available
            default:
                return Utils.getMappedUDTName(context.configuration(), value);
        }
    }

    final void bind0(BindContext context) {
        switch (context.family()) {

            /* [pro] */
            // Oracle supports java.sql.SQLData, hence the record can be bound
            // to the CallableStatement directly
            case ORACLE:
                context.bindValue(value, this);
                break;

            // Is the DB2 case correct? Should it be inlined like the Postgres case?
            case DB2:

            /* [/pro] */
            // Postgres cannot bind a complete structured type. The type is
            // inlined instead: ROW(.., .., ..)
            case POSTGRES: {
                for (Field<?> field : value.fields()) {
                    context.visit(val(value.getValue(field)));
                }

                break;
            }

            default:
                throw new SQLDialectNotSupportedException("UDTs not supported in dialect " + context.dialect());
        }
    }
}
