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
import static org.jooq.Clause.CONDITION_IN;
import static org.jooq.Clause.CONDITION_NOT_IN;
import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.NOT_EQUALS;
import static org.jooq.Comparator.NOT_IN;
import static org.jooq.Constants.MAX_ROW_DEGREE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPartInternal;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * @author Lukas Eder
 */
final class RowInCondition
extends
    AbstractCondition
implements
    UNotYetImplemented
{

    private static final Clause[]        CLAUSES_IN     = { CONDITION, CONDITION_IN };
    private static final Clause[]        CLAUSES_IN_NOT = { CONDITION, CONDITION_NOT_IN };

    // Currently not yet supported in SQLite:
    // https://www.sqlite.org/rowvalue.html
    private static final Set<SQLDialect> EMULATE_IN       = SQLDialect.supportedBy(DERBY, FIREBIRD, SQLITE);

    private final Row                    left;
    private final QueryPartList<Row>     right;
    private final boolean                not;

    RowInCondition(Row left, QueryPartList<? extends Row> right, boolean not) {
        this.left = left;
        this.right = new QueryPartList<>(right);
        this.not = not;

        this.right.replaceAll(r -> ((AbstractRow<?>) r).convertTo(left));
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (EMULATE_IN.contains(ctx.dialect())) {
            Condition result = DSL.or(map(right, r -> AbstractRow.compare(left, EQUALS, r)));

            if (not)
                result = result.not();

            ctx.visit(result);
        }












        else if (right.size() == 0) {
            if (not)
                ctx.visit(trueCondition());
            else
                ctx.visit(falseCondition());
        }

















        else {
            ctx.visit(left)
               .sql(' ')
               .visit((not ? NOT_IN : IN).toKeyword())
               .sql(" (").visit(new QueryPartListView<>(AbstractInList.padded(ctx, right, Integer.MAX_VALUE))).sql(')');
        }
    }

    @Override // Avoid AbstractCondition implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
