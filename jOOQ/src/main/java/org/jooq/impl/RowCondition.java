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
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.GREATER;
import static org.jooq.Comparator.GREATER_OR_EQUAL;
import static org.jooq.Comparator.LESS;
import static org.jooq.Comparator.LESS_OR_EQUAL;
import static org.jooq.Comparator.NOT_EQUALS;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import java.util.ArrayList;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPartInternal;
import org.jooq.Row;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
final class RowCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -1806139685201770706L;
    private static final Clause[] CLAUSES          = { CONDITION, CONDITION_COMPARISON };

    private final Row             left;
    private final Row             right;
    private final Comparator      comparator;

    RowCondition(Row left, Row right, Comparator comparator) {
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
        SQLDialect dialect = configuration.dialect();

        // Regular comparison predicate emulation
        if (asList(EQUALS, NOT_EQUALS).contains(comparator) &&
            asList(DERBY, FIREBIRD, SQLITE).contains(dialect.family())) {
            List<Condition> conditions = new ArrayList<Condition>();

            Field<?>[] leftFields = left.fields();
            Field<?>[] rightFields = right.fields();

            for (int i = 0; i < leftFields.length; i++) {
                conditions.add(leftFields[i].equal((Field) rightFields[i]));
            }

            Condition result = DSL.and(conditions);

            if (comparator == NOT_EQUALS) {
                result = result.not();
            }

            return (QueryPartInternal) result;
        }

        // Ordering comparison predicate emulation
        else if (asList(GREATER, GREATER_OR_EQUAL, LESS, LESS_OR_EQUAL).contains(comparator) &&
                 asList(DERBY, CUBRID, FIREBIRD, SQLITE).contains(dialect.family())) {

            // The order component of the comparator (stripping the equal component)
            Comparator order
                = (comparator == GREATER) ? GREATER
                : (comparator == GREATER_OR_EQUAL) ? GREATER
                : (comparator == LESS) ? LESS
                : (comparator == LESS_OR_EQUAL) ? LESS
                : null;

            // [#2658] The factored order component of the comparator (enforcing the equal component)
            Comparator factoredOrder
                = (comparator == GREATER) ? GREATER_OR_EQUAL
                : (comparator == GREATER_OR_EQUAL) ? GREATER_OR_EQUAL
                : (comparator == LESS) ? LESS_OR_EQUAL
                : (comparator == LESS_OR_EQUAL) ? LESS_OR_EQUAL
                : null;

            // Whether the comparator has an equal component
            boolean equal
                = (comparator == GREATER_OR_EQUAL)
                ||(comparator == LESS_OR_EQUAL);

            // The following algorithm emulates the equivalency of these expressions:
            // (A, B, C) > (X, Y, Z)
            // (A > X) OR (A = X AND B > Y) OR (A = X AND B = Y AND C > Z)
            List<Condition> outer = new ArrayList<Condition>();

            Field<?>[] leftFields = left.fields();
            Field<?>[] rightFields = right.fields();

            for (int i = 0; i < leftFields.length; i++) {
                List<Condition> inner = new ArrayList<Condition>();

                for (int j = 0; j < i; j++) {
                    inner.add(leftFields[j].equal((Field) rightFields[j]));
                }

                inner.add(leftFields[i].compare(order, (Field) rightFields[i]));
                outer.add(DSL.and(inner));
            }

            if (equal) {
                outer.add(new RowCondition(left, right, Comparator.EQUALS));
            }

            Condition result = DSL.or(outer);

            // [#2658] For performance reasons, an additional, redundant
            // predicate is factored out to favour the application of range
            // scans as the topmost predicate is AND-connected, not
            // OR-connected:
            // (A, B, C) > (X, Y, Z)
            // (A >= X) AND ((A > X) OR (A = X AND B > Y) OR (A = X AND B = Y AND C > Z))
            if (leftFields.length > 1) {
                result = leftFields[0].compare(factoredOrder, (Field) rightFields[0]).and(result);
            }

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
        private static final long serialVersionUID = -2977241780111574353L;

        @Override
        public final void accept(Context<?> ctx) {

            // Some dialects do not support != comparison with rows
            if (comparator == NOT_EQUALS && asList().contains(ctx.configuration().dialect().family())) {
                ctx.keyword("not").sql('(')
                   .visit(left).sql(" = ").visit(right)
                   .sql(')');
            }
            else {
                // Some databases need extra parentheses around the RHS
                boolean extraParentheses = asList().contains(ctx.configuration().dialect().family());

                ctx.visit(left)
                   .sql(' ')
                   .sql(comparator.toSQL())
                   .sql(' ')
                   .sql(extraParentheses ? "(" : "")
                   .visit(right)
                   .sql(extraParentheses ? ")" : "");
            }
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return CLAUSES;
        }
    }
}