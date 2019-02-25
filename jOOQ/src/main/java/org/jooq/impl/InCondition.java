/*
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

import static java.lang.Boolean.TRUE;
import static java.lang.Math.ceil;
import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_IN;
import static org.jooq.Clause.CONDITION_NOT_IN;
import static org.jooq.Comparator.IN;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
// ...
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.Keywords.K_AND;
import static org.jooq.impl.Keywords.K_OR;
import static org.jooq.impl.Tools.embeddedFields;
import static org.jooq.impl.Tools.isEmbeddable;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.RowN;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class InCondition<T> extends AbstractCondition {

    private static final long                serialVersionUID  = -1653924248576930761L;
    private static final int                 IN_LIMIT          = 1000;
    private static final Clause[]            CLAUSES_IN        = { CONDITION, CONDITION_IN };
    private static final Clause[]            CLAUSES_IN_NOT    = { CONDITION, CONDITION_NOT_IN };
    private static final EnumSet<SQLDialect> REQUIRES_IN_LIMIT = EnumSet.of(FIREBIRD);

    private final Field<T>                   field;
    private final Field<?>[]                 values;
    private final Comparator                 comparator;

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
        if (isEmbeddable(field))
            if (comparator == IN)
                ctx.visit(row(embeddedFields(field)).in(rows()));
            else
                ctx.visit(row(embeddedFields(field)).notIn(rows()));
        else
            accept0(ctx);
    }

    private final RowN[] rows() {
        RowN[] result = new RowN[values.length];

        for (int i = 0; i < values.length; i++)
            result[i] = row(embeddedFields(values[i]));

        return result;
    }

    private final void accept0(Context<?> ctx) {
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
            switch (ctx.family()) {






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
                                   .visit(K_OR)
                                   .sql(' ');
                            }
                            else {
                                ctx.formatSeparator()
                                   .visit(K_AND)
                                   .sql(' ');
                            }
                        }

                        toSQLSubValues(ctx, padded(ctx, list.subList(i, Math.min(i + IN_LIMIT, list.size()))));
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
            toSQLSubValues(ctx, padded(ctx, list));
        }
    }

    static <T> List<T> padded(Context<?> ctx, List<T> list) {
        return ctx.paramType() == INDEXED && TRUE.equals(ctx.settings().isInListPadding())
            ? new PaddedList<T>(list, REQUIRES_IN_LIMIT.contains(ctx.family())
                ? IN_LIMIT
                : Integer.MAX_VALUE,
                  defaultIfNull(ctx.settings().getInListPadBase(), 2))
            : list;
    }

    /**
     * Render the SQL for a sub-set of the <code>IN</code> clause's values
     */
    private void toSQLSubValues(Context<?> ctx, List<Field<?>> subValues) {
        ctx.visit(field)
           .sql(' ')
           .visit(comparator.toKeyword())
           .sql(" (");

        if (subValues.size() > 1)
            ctx.formatIndentStart()
               .formatNewLine();

        String separator = "";
        for (Field<?> value : subValues) {
            ctx.sql(separator)
               .formatNewLineAfterPrintMargin()
               .visit(value);

            separator = ", ";
        }

        if (subValues.size() > 1)
            ctx.formatIndentEnd()
               .formatNewLine();

        ctx.sql(')');
    }

    static class PaddedList<T> extends AbstractList<T> {
        private final List<T> delegate;
        private final int     realSize;
        private final int     padSize;

        PaddedList(List<T> delegate, int maxPadding, int padBase) {
            int b = max(2, padBase);

            this.delegate = delegate;
            this.realSize = delegate.size();
            this.padSize = min(maxPadding, (int) round(pow(b, ceil(log(realSize) / log(b)))));
        }

        @Override
        public T get(int index) {
            return index < realSize ? delegate.get(index) : delegate.get(realSize - 1);
        }

        @Override
        public int size() {
            return padSize;
        }
    }
}
