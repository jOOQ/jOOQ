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
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTE;
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.Keywords.K_AND;
import static org.jooq.impl.Keywords.K_IN;
import static org.jooq.impl.Keywords.K_NOT_IN;
import static org.jooq.impl.Keywords.K_OR;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.embeddedFields;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONDITION;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.AbstractList;
import java.util.List;
import java.util.Set;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function2;
import org.jooq.RowN;
import org.jooq.SQLDialect;
import org.jooq.impl.QOM.InList;
import org.jooq.impl.QOM.MList;

/**
 * @author Lukas Eder
 */
abstract class AbstractInList<T> extends AbstractCondition {

    static final int              IN_LIMIT               = 1000;
    static final Set<SQLDialect>  REQUIRES_IN_LIMIT      = SQLDialect.supportedBy(FIREBIRD);
    static final Set<SQLDialect>  NO_SUPPORT_EMPTY_LISTS = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, YUGABYTE);

    final Field<T>                field;
    final QueryPartList<Field<T>> values;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    AbstractInList(Field<T> field, List<? extends Field<?>> values) {
        this.field = field;
        this.values = new QueryPartList(values);
    }

    abstract Function2<? super RowN, ? super RowN[], ? extends Condition> rowCondition();

    @Override
    public final void accept(Context<?> ctx) {
        if (field.getDataType().isEmbeddable())
            ctx.visit(rowCondition().apply(row(embeddedFields(field)), rows(values)));
        else if (field.getDataType().isMultiset() && !TRUE.equals(ctx.data(DATA_MULTISET_CONDITION)))
            ctx.data(DATA_MULTISET_CONDITION, true, c -> c.visit(this));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        boolean in = this instanceof InList;














        if (values.size() == 0 && NO_SUPPORT_EMPTY_LISTS.contains(ctx.dialect())) {
            if (in)
                ctx.visit(falseCondition());
            else
                ctx.visit(trueCondition());
        }
        else if (values.size() > IN_LIMIT) {
            // [#798] Oracle and some other dialects can only hold 1000 values
            // in an IN (...) clause
            switch (ctx.family()) {




                case FIREBIRD: {
                    ctx.sqlIndentStart('(');

                    for (int i = 0; i < values.size(); i += IN_LIMIT) {
                        if (i > 0) {

                            // [#1515] The connector depends on the IN / NOT IN
                            // operator
                            if (in)
                                ctx.formatSeparator()
                                   .visit(K_OR)
                                   .sql(' ');
                            else
                                ctx.formatSeparator()
                                   .visit(K_AND)
                                   .sql(' ');
                        }

                        toSQLSubValues(ctx, field, in, padded(ctx, values.subList(i, Math.min(i + IN_LIMIT, values.size()))));
                    }

                    ctx.sqlIndentEnd(')');
                    break;
                }

                // Most dialects can handle larger lists
                default: {
                    toSQLSubValues(ctx, field, in, values);
                    break;
                }
            }
        }
        else
            toSQLSubValues(ctx, field, in, padded(ctx, values));
    }

    static final RowN[] rows(List<? extends Field<?>> values) {
        return map(values, v -> row(embeddedFields(v)), RowN[]::new);
    }

    static final <T> List<T> padded(Context<?> ctx, List<T> list) {
        return ctx.paramType() == INDEXED && TRUE.equals(ctx.settings().isInListPadding())
            ? new PaddedList<>(list, REQUIRES_IN_LIMIT.contains(ctx.dialect())
                ? IN_LIMIT
                : Integer.MAX_VALUE,
                  defaultIfNull(ctx.settings().getInListPadBase(), 2))
            : list;
    }

    /**
     * Render the SQL for a sub-set of the <code>IN</code> clause's values
     */
    static final void toSQLSubValues(Context<?> ctx, Field<?> field, boolean in, List<? extends Field<?>> subValues) {
        ctx.visit(field)
           .sql(' ')
           .visit(in ? K_IN : K_NOT_IN)
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

    static final class PaddedList<T> extends AbstractList<T> {
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

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    public final Field<T> $arg1() {
        return field;
    }

    public final MList<? extends Field<T>> $arg2() {
        return values;
    }
}
