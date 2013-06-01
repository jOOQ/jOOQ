/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

import java.util.Arrays;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Comparator;
import org.jooq.Field;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class InCondition<T> extends AbstractCondition {

    private static final long serialVersionUID = -1653924248576930761L;
    private static final int  IN_LIMIT         = 1000;

    private final Field<T>    field;
    private final Field<?>[]  values;
    private final Comparator  comparator;

    InCondition(Field<T> field, Field<?>[] values, Comparator comparator) {
        this.field = field;
        this.values = values;
        this.comparator = comparator;
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(field).bind(values);
    }

    @Override
    public final void toSQL(RenderContext context) {
        List<Field<?>> list = Arrays.asList(values);

        if (list.size() > IN_LIMIT) {
            // [#798] Oracle and some other dialects can only hold 1000 values
            // in an IN (...) clause
            switch (context.configuration().dialect().family()) {
                case FIREBIRD:
                case INGRES:
                case ORACLE:
                case SQLSERVER: {
                    context.sql("(")
                           .formatIndentStart()
                           .formatNewLine();

                    for (int i = 0; i < list.size(); i += IN_LIMIT) {
                        if (i > 0) {

                            // [#1515] The connector depends on the IN / NOT IN
                            // operator
                            if (comparator == Comparator.IN) {
                                context.formatSeparator()
                                       .keyword("or ");
                            }
                            else {
                                context.formatSeparator()
                                       .keyword("and ");
                            }
                        }

                        toSQLSubValues(context, list.subList(i, Math.min(i + IN_LIMIT, list.size())));
                    }

                    context.formatIndentEnd()
                           .formatNewLine()
                           .sql(")");
                    break;
                }

                // Most dialects can handle larger lists
                default: {
                    toSQLSubValues(context, list);
                    break;
                }
            }
        }
        else {
            toSQLSubValues(context, list);
        }
    }

    /**
     * Render the SQL for a sub-set of the <code>IN</code> clause's values
     */
    private void toSQLSubValues(RenderContext context, List<Field<?>> subValues) {
        context.sql(field)
               .sql(" ")
               .keyword(comparator.toSQL())
               .sql(" (");

        if (subValues.size() > 1) {
            context.formatIndentStart()
                   .formatNewLine();
        }

        String separator = "";
        for (Field<?> value : subValues) {
            context.sql(separator)
                   .formatNewLineAfterPrintMargin()
                   .sql(value);

            separator = ", ";
        }

        if (subValues.size() > 1) {
            context.formatIndentEnd()
                   .formatNewLine();
        }

        context.sql(")");
    }
}
