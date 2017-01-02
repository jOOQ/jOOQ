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


import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_BETWEEN;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QuantifiedSelect;

/**
 * @author Lukas Eder
 */
final class QuantifiedComparisonCondition extends AbstractCondition {

    private static final long         serialVersionUID = -402776705884329740L;
    private static final Clause[]     CLAUSES          = { CONDITION, CONDITION_BETWEEN };

    private final QuantifiedSelect<?> query;
    private final Field<?>            field;
    private final Comparator          comparator;

    QuantifiedComparisonCondition(QuantifiedSelect<?> query, Field<?> field, Comparator comparator) {
        this.query = query;
        this.field = field;
        this.comparator = comparator;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(field)
           .sql(' ')
           .keyword(comparator.toSQL())
           .sql(' ')
           .visit(query);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
