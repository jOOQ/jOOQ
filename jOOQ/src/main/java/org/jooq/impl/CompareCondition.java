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
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.NOT_IN;
// ...
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Tools.embeddedFields;
import static org.jooq.impl.Tools.nullSafe;
import static org.jooq.impl.Tools.nullableIf;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONDITION;
import static org.jooq.impl.Transformations.subqueryWithLimit;
import static org.jooq.impl.Transformations.transformInConditionSubqueryWithLimitToDerivedTable;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Context;
import org.jooq.Field;
// ...
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class CompareCondition extends AbstractCondition {

    private static final Clause[]        CLAUSES               = { CONDITION, CONDITION_COMPARISON };

    final Field<?>                       field1;
    final Field<?>                       field2;
    final Comparator                     comparator;

    CompareCondition(Field<?> field1, Field<?> field2, Comparator comparator) {
        this.field1 = nullableIf(comparator.supportsNulls(), nullSafe(field1, field2.getDataType()));
        this.field2 = nullableIf(comparator.supportsNulls(), nullSafe(field2, field1.getDataType()));
        this.comparator = comparator;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void accept(Context<?> ctx) {
        boolean field1Embeddable = field1.getDataType().isEmbeddable();
        SelectQueryImpl<?> s;

        if (field1Embeddable && field2 instanceof ScalarSubquery)
            ctx.visit(row(embeddedFields(field1)).compare(comparator, ((ScalarSubquery<?>) field2).query));
        else if (field1Embeddable && field2.getDataType().isEmbeddable())
            ctx.visit(row(embeddedFields(field1)).compare(comparator, embeddedFields(field2)));
        else if ((comparator == IN || comparator == NOT_IN)
                && (s = subqueryWithLimit(field2)) != null
                && transformInConditionSubqueryWithLimitToDerivedTable(ctx.configuration())) {



        }
        else if (field1.getDataType().isMultiset()
                && field2.getDataType().isMultiset()
                && !TRUE.equals(ctx.data(DATA_MULTISET_CONDITION)))
            ctx.data(DATA_MULTISET_CONDITION, true, c -> c.visit(this));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {







        ctx.visit(field1).sql(' ').visit(comparator.toKeyword()).sql(' ').visit(field2);
    }

































    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
