/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import static org.jooq.impl.Factory.table;

import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class ArrayAsSubqueryCondition<T> extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long      serialVersionUID = -1074020279396496305L;

    private final Field<T[]>       array;
    private final Field<?>         field;
    private final SubQueryOperator operator;

    ArrayAsSubqueryCondition(Field<T[]> array, Field<T> field, SubQueryOperator operator) {
        this.array = array;
        this.field = field;
        this.operator = operator;
    }

    @Override
    public final List<Attachable> getAttachables() {
        return getAttachables(field);
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.sql(field)
               .sql(" ")
               .sql(operator.toSQL())
               .sql(" (")
               .sql(array(context))
               .sql(")");
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(field).bind(array(context));
    }

    private final QueryPart array(Configuration context) {
        switch (context.getDialect()) {

            // [#869] Postgres supports this syntax natively
            case POSTGRES: {
                return array;
            }

            // [#869] H2 and HSQLDB can simulate this syntax by unnesting
            // the array in a subselect
            case H2:
            case HSQLDB:

            // [#1048] All other dialects simulate unnesting of arrays using
            // UNION ALL-connected subselects
            default: {
                return create(context).select().from(table(array));
            }
        }
    }
}
