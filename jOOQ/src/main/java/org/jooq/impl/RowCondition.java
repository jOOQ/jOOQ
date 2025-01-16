/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.GREATER;
import static org.jooq.Comparator.GREATER_OR_EQUAL;
import static org.jooq.Comparator.LESS;
import static org.jooq.Comparator.LESS_OR_EQUAL;
import static org.jooq.Comparator.NOT_EQUALS;
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Tools.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
// ...
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
final class RowCondition
extends
    AbstractCondition
implements
    UNotYetImplemented
{
    private static final Clause[]        CLAUSES            = { CONDITION, CONDITION_COMPARISON };





    private static final Set<SQLDialect> EMULATE_EQ_AND_NE  = SQLDialect.supportedBy(DERBY, DUCKDB, FIREBIRD);
    private static final Set<SQLDialect> EMULATE_RANGES     = SQLDialect.supportedBy(CUBRID, DERBY, DUCKDB, FIREBIRD);

    private final Row                    left;
    private final Row                    right;
    private final Comparator             comparator;
    private final boolean                forceEmulation;

    RowCondition(Row left, Row right, Comparator comparator) {
        this(left, right, comparator, false);
    }

    RowCondition(Row left, Row right, Comparator comparator, boolean forceEmulation) {
        this.left = ((AbstractRow) left).convertTo(right);
        this.right = ((AbstractRow) right).convertTo(left);
        this.comparator = comparator;
        this.forceEmulation = forceEmulation;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (forceEmulation) {
            ctx.visit(emulation());
        }
















        else if (EMULATE_EQ_AND_NE.contains(ctx.dialect()) && (comparator == EQUALS || comparator == NOT_EQUALS)) {
            ctx.visit(emulation());
        }
        else if (EMULATE_RANGES.contains(ctx.dialect()) && (comparator == GREATER || comparator == GREATER_OR_EQUAL || comparator == LESS || comparator == LESS_OR_EQUAL)) {
            ctx.visit(emulation());
        }








        else {

            // Some databases need extra parentheses around the RHS
            boolean extraParentheses = false



                ;

            ctx.visit(left)
               .sql(' ')
               .sql(comparator.toSQL())
               .sql(' ')
               .sql(extraParentheses ? "(" : "")
               .visit(right)
               .sql(extraParentheses ? ")" : "");
        }
    }

    Condition emulation() {
        switch (comparator) {
            case EQUALS:
            case NOT_EQUALS: {
                Field<?>[] rightFields = right.fields();
                Condition result = DSL.and(map(left.fields(), (f, i) -> f.eq((Field) rightFields[i])));

                if (comparator == NOT_EQUALS)
                    result = result.not();

                return result;
            }

            case GREATER:
            case GREATER_OR_EQUAL:
            case LESS:
            case LESS_OR_EQUAL: {
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

                Field<?>[] leftFields = left.fields();
                Field<?>[] rightFields = right.fields();

                // [#14555] Implement recursive emulation
                Condition result = emulate(left, right, order, comparator);

                // [#2658] For performance reasons, an additional, redundant
                // predicate is factored out to favour the application of range
                // scans as the topmost predicate is AND-connected, not
                // OR-connected:
                // (A, B, C) > (X, Y, Z)
                // (A >= X) AND ((A > X) OR (A = X AND B > Y) OR (A = X AND B = Y AND C > Z))
                if (leftFields.length > 1)
                    result = leftFields[0].compare(factoredOrder, (Field) rightFields[0]).and(result);

                return result;
            }

            default:
                throw new UnsupportedOperationException("Emulation not available for: " + comparator);
        }
    }

    private static final Condition emulate(
        Row r1,
        Row r2,
        org.jooq.Comparator comp,
        org.jooq.Comparator last
    ) {
        Condition result = r1.field(r1.size() - 1).compare(last, (Field) r2.field(r1.size() - 1));

        for (int i = r1.size() - 2; i >= 0; i--) {
            Field e1 = r1.field(i);
            Field e2 = r2.field(i);
            result = e1.compare(comp, e2).or(e1.eq(e2).and(result));
        }

        return result;
    }

    @Override // Avoid AbstractCondition implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
