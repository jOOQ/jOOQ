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

/* [pro] */

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.val;

import org.jooq.ArrayRecord;
import org.jooq.BindContext;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class ArrayConstant<R extends ArrayRecord<?>> extends AbstractParam<R> {

    private static final long serialVersionUID = -8538560256712388066L;
    private final R           array;
    private final DataType<?> baseType;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    ArrayConstant(R array) {
        super(array, (DataType) array.getArrayType());

        this.array = array;
        this.baseType = array.getDataType();
    }

    final DataType<?> getBaseType() {
        return baseType;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ctx instanceof RenderContext)
            toSQL0((RenderContext) ctx);
        else
            bind0((BindContext) ctx);
    }

    final void toSQL0(RenderContext context) {
        if (context.paramType() == INLINED) {
            context.sql(array.getName());
            context.sql('(');

            String separator = "";
            for (Object object : array) {
                context.sql(separator);
                context.visit(val(object));

                separator = ", ";
            }

            context.sql(')');
        }
        else {
            context.sql('?');
        }
    }

    final void bind0(BindContext context) {
        context.bindValue(array, this);
    }
}
/* [/pro] */