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

import java.util.List;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function2;
import org.jooq.RowN;
// ...
// ...
// ...
// ...

/**
 * @author Lukas Eder
 */
final class InList<T> extends AbstractInList<T> implements MInList<T> {

    static final Clause[] CLAUSES = { CONDITION, CONDITION_IN };

    InList(Field<T> field, List<? extends Field<?>> values) {
        super(field, values);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    final Function2<? super RowN, ? super RowN[], ? extends Condition> rowCondition() {
        return RowN::in;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Function2<? super MField<T>, ? super MList<? extends MField<T>>, ? extends MCondition> constructor() {
        return (a1, a2) -> new InList<T>((Field<T>) a1, (List<? extends Field<?>>) a2);
    }
}
