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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_IN;
import static org.jooq.Clause.CONDITION_NOT_IN;
import static org.jooq.Comparator.IN;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.trueCondition;

import java.util.Arrays;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Context;
import org.jooq.Field;

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
    public final void accept(Context<?> ctx) {
        List<Field<?>> list = Arrays.asList(values);

        if (list.size() == 0) {
            if (comparator == IN)
                ctx.visit(falseCondition());
            else
                ctx.visit(trueCondition());
        }
        else if (list.size() > IN_LIMIT) {
            // [#798] Oracle and some other dialects can only hold 1000 values
            // in an IN (...) clause
            switch (ctx.configuration().dialect().family()) {





                case FIREBIRD: {
                    ctx.sql('(')
                       .formatIndentStart()
                       .formatNewLine();

                    for (int i = 0; i < list.size(); i += IN_LIMIT) {
                        if (i > 0) {

                            // [#1515] The connector depends on the IN / NOT IN
                            // operator
                            if (comparator == Comparator.IN) {
                                ctx.formatSeparator()
                                   .keyword("or")
                                   .sql(' ');
                            }
                            else {
                                ctx.formatSeparator()
                                   .keyword("and")
                                   .sql(' ');
                            }
                        }

                        toSQLSubValues(ctx, list.subList(i, Math.min(i + IN_LIMIT, list.size())));
                    }

                    ctx.formatIndentEnd()
                       .formatNewLine()
                       .sql(')');
                    break;
                }

                // Most dialects can handle larger lists
                default: {
                    toSQLSubValues(ctx, list);
                    break;
                }
            }
        }
        else {
            toSQLSubValues(ctx, list);
        }
    }

    /**
     * Render the SQL for a sub-set of the <code>IN</code> clause's values
     */
    private void toSQLSubValues(Context<?> ctx, List<Field<?>> subValues) {
        ctx.visit(field)
           .sql(' ')
           .keyword(comparator.toSQL())
           .sql(" (");

        if (subValues.size() > 1) {
            ctx.formatIndentStart()
               .formatNewLine();
        }

        String separator = "";
        for (Field<?> value : subValues) {
            ctx.sql(separator)
               .formatNewLineAfterPrintMargin()
               .visit(value);

            separator = ", ";
        }

        if (subValues.size() > 1) {
            ctx.formatIndentEnd()
               .formatNewLine();
        }

        ctx.sql(')');
    }
}
