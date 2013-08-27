/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */

package org.jooq.impl;

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.val;

import org.jooq.BindContext;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.Schema;
import org.jooq.UDT;
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
    public final void toSQL(RenderContext context) {
        switch (context.configuration().dialect().family()) {

            // Oracle supports java.sql.SQLData, hence the record can be bound
            // to the CallableStatement directly
            case ORACLE: {
                if (context.paramType() == INLINED) {
                    toSQLInline(context);
                } else {
                    context.sql("?");
                }

                return;
            }

            // Due to lack of UDT support in the Postgres JDBC drivers, all UDT's
            // have to be inlined
            case POSTGRES: {
                toSQLInline(context);
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
                    context.sql("(");
                    context.visit(val(value.getValue(field)));
                    context.sql(")");
                }

                return;
            }

            // Assume default behaviour if dialect is not available
            default:
                toSQLInline(context);
                return;
        }
    }

    private void toSQLInline(RenderContext context) {
        context.sql(getInlineConstructor(context));
        context.sql("(");

        String separator = "";
        for (Field<?> field : value.fields()) {
            context.sql(separator);
            context.visit(val(value.getValue(field), field));
            separator = ", ";
        }

        context.sql(")");
    }

    private String getInlineConstructor(RenderContext context) {
        // TODO [#884] Fix this with a local render context (using ctx.literal)
        switch (context.configuration().dialect().family()) {
            case POSTGRES:
                return "ROW";

            case ORACLE:
            case DB2:

            // Assume default behaviour if dialect is not available
            default: {
                UDT<?> udt = value.getUDT();
                Schema mappedSchema = Utils.getMappedSchema(context.configuration(), udt.getSchema());

                if (mappedSchema != null) {
                    return mappedSchema + "." + udt.getName();
                }
                else {
                    return udt.getName();
                }
            }
        }
    }

    @Override
    public final void bind(BindContext context) {
        switch (context.configuration().dialect().family()) {

            // Oracle supports java.sql.SQLData, hence the record can be bound
            // to the CallableStatement directly
            case ORACLE:
                context.bindValues(value);
                break;

            // Is the DB2 case correct? Should it be inlined like the Postgres case?
            case DB2:

            // Postgres cannot bind a complete structured type. The type is
            // inlined instead: ROW(.., .., ..)
            case POSTGRES: {
                for (Field<?> field : value.fields()) {
                    context.visit(val(value.getValue(field)));
                }

                break;
            }

            default:
                throw new SQLDialectNotSupportedException("UDTs not supported in dialect " + context.configuration().dialect());
        }
    }
}
