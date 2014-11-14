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

import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.ParamType.NAMED_OR_INLINED;

import java.sql.SQLException;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.RenderContext;
import org.jooq.conf.ParamType;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
class Val<T> extends AbstractParam<T> {

    private static final long   serialVersionUID = 6807729087019209084L;

    Val(T value, DataType<T> type) {
        super(value, type);
    }

    Val(T value, DataType<T> type, String paramName) {
        super(value, type, paramName);
    }

    // ------------------------------------------------------------------------
    // XXX: Field API
    // ------------------------------------------------------------------------

    @Override
    public void accept(Context<?> ctx) {
        if (ctx instanceof RenderContext) {
            ParamType paramType = ctx.paramType();

            if (isInline(ctx))
                ctx.paramType(ParamType.INLINED);

            try {
                getBinding().sql(new DefaultBindingSQLContext<T>(ctx.configuration(), (RenderContext) ctx, value, getBindVariable(ctx)));
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while generating SQL for Binding", e);
            }

            ctx.paramType(paramType);
        }

        else {

            // [#1302] Bind value only if it was not explicitly forced to be inlined
            if (!isInline(ctx)) {
                ctx.bindValue(value, this);
            }
        }
    }

    /**
     * Get a bind variable, depending on value of
     * {@link RenderContext#namedParams()}
     */
    final String getBindVariable(Context<?> ctx) {
        if (ctx.paramType() == NAMED || ctx.paramType() == NAMED_OR_INLINED) {
            int index = ctx.nextIndex();

            if (StringUtils.isBlank(getParamName())) {
                return ":" + index;
            }
            else {
                return ":" + getParamName();
            }
        }
        else {
            return "?";
        }
    }
}
