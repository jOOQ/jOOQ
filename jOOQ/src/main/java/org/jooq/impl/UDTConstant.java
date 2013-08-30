/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
