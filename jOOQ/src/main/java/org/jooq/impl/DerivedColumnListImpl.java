/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static org.jooq.impl.DSL.name;

import org.jooq.Clause;
import org.jooq.CommonTableExpression;
import org.jooq.Context;
import org.jooq.DerivedColumnList;
import org.jooq.Record;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
class DerivedColumnListImpl extends AbstractQueryPart implements DerivedColumnList {

    /**
     * Gemerated UID
     */
    private static final long serialVersionUID = -369633206858851863L;

    final String              name;
    final String[]            fieldNames;

    DerivedColumnListImpl(String name, String[] fieldNames) {
        this.name = name;
        this.fieldNames = fieldNames;
    }

    @Override
    public final <R extends Record> CommonTableExpression<R> as(Select<R> select) {
        return new CommonTableExpressionImpl<R>(this, select);
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(name(name));

        if (fieldNames != null && fieldNames.length > 0) {
            ctx.sql('(');

            for (int i = 0; i < fieldNames.length; i++) {
                if (i > 0)
                    ctx.sql(", ");

                ctx.visit(name(fieldNames[i]));
            }

            ctx.sql(')');
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
