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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_IN;
import static org.jooq.Clause.CONDITION_NOT_IN;
import static org.jooq.Comparator.IN;
import static org.jooq.impl.Utils.visitAll;

import java.util.Arrays;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class InCondition<T> extends AbstractCondition {

    private static final long     serialVersionUID = -1653924248576930761L;
    private static final int      IN_LIMIT         = 1000;
    private static final Clause[] CLAUSES_IN       = { CONDITION, CONDITION_IN };
    private static final Clause[] CLAUSES_IN_NOT   = { CONDITION, CONDITION_NOT_IN };

    private final Field<T>        field;
    private final Field<?>[]      values;
    private final Comparator      comparator;

    InCondition(Field<T> field, Field<?>[] values, Comparator comparator) {
        this.field = field;
        this.values = values;
        this.comparator = comparator;
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return comparator == IN ? CLAUSES_IN : CLAUSES_IN_NOT;
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(field);
        visitAll(context, values);
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
                                       .keyword("or")
                                       .sql(" ");
                            }
                            else {
                                context.formatSeparator()
                                       .keyword("and")
                                       .sql(" ");
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
        context.visit(field)
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
                   .visit(value);

            separator = ", ";
        }

        if (subValues.size() > 1) {
            context.formatIndentEnd()
                   .formatNewLine();
        }

        context.sql(")");
    }
}
