/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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

            /* [pro] xx
            xx xxxxxx xxxxxxxx xxxxxxxxxxxxxxxxx xxxxx xxx xxxxxx xxx xx xxxxx
            xx xx xxx xxxxxxxxxxxxxxxxx xxxxxxxx
            xxxx xxxxxxx x
                xx xxxxxxxxxxxxxxxxxxxx xx xxxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxx
                x xxxx x
                    xxxxxxxxxxxxxxxxx
                x

                xxxxxxx
            x

            xx xxx xxxxxxxx xxxxx xxx xxxx xx xxxx xxxxxxxxxxxx xx xxxx
            xxxx xxxx x

                xx xxx xxxxxxxxxx xxx xxxxx xxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxx

                xxxxxx xxxxxxxxx x xxxxx
                xxx xxxxxxxxx xxxxx x xxxxxxxxxxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxx
                x

                xxxxxxx
            x

            xx [/pro] */
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

            /* [pro] xx
            xxxx xxxxxxx
            xxxx xxxx
            xx [/pro] */

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

            /* [pro] xx
            xx xxxxxx xxxxxxxx xxxxxxxxxxxxxxxxx xxxxx xxx xxxxxx xxx xx xxxxx
            xx xx xxx xxxxxxxxxxxxxxxxx xxxxxxxx
            xxxx xxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxx

            xx xx xxx xxx xxxx xxxxxxxx xxxxxx xx xx xxxxxxx xxxx xxx xxxxxxxx xxxxx
            xxxx xxxx

            xx [/pro] */
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
