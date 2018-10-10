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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_IN;
import static org.jooq.Clause.CONDITION_NOT_IN;
import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.NOT_IN;
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.InCondition.padded;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.QueryPartInternal;
import org.jooq.Row;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class RowInCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long                  serialVersionUID = -1806139685201770706L;
    private static final Clause[]              CLAUSES_IN       = { CONDITION, CONDITION_IN };
    private static final Clause[]              CLAUSES_IN_NOT   = { CONDITION, CONDITION_NOT_IN };

    // Currently not yet supported in SQLite: https://www.sqlite.org/rowvalue.html
    private static final EnumSet<SQLDialect>   EMULATE_IN       = EnumSet.of(DERBY, FIREBIRD, SQLITE);

    private final Row                          left;
    private final QueryPartList<? extends Row> right;
    private final Comparator                   comparator;

    RowInCondition(Row left, QueryPartList<? extends Row> right, Comparator comparator) {
        this.left = left;
        this.right = right;
        this.comparator = comparator;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate(ctx.configuration()));
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    private final QueryPartInternal delegate(Configuration configuration) {
        if (EMULATE_IN.contains(configuration.family())) {
            List<Condition> conditions = new ArrayList<Condition>(right.size());

            for (Row row : right)
                conditions.add(new RowCondition(left, row, EQUALS));

            Condition result = DSL.or(conditions);

            if (comparator == NOT_IN)
                result = result.not();

            return (QueryPartInternal) result;
        }
        else {
            return new Native();
        }
    }

    private class Native extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -7019193803316281371L;

        @Override
        public final void accept(Context<?> ctx) {
            if (right.size() == 0) {
                if (comparator == IN)
                    ctx.visit(falseCondition());
                else
                    ctx.visit(trueCondition());
            }
            else {
                ctx.visit(left)
                   .sql(' ')
                   .visit(comparator.toKeyword())
                   .sql(" (")
                   .visit(new QueryPartList<Row>(padded(ctx, right)))
                   .sql(')');
            }
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return comparator == IN ? CLAUSES_IN : CLAUSES_IN_NOT;
        }
    }
}
