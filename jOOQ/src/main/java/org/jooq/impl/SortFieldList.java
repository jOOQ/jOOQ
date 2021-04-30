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

import static java.util.Collections.emptyList;
import static org.jooq.SortOrder.DESC;
import static org.jooq.impl.Tools.anyMatch;

import java.util.List;

import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.SortOrder;

/**
 * @author Lukas Eder
 */
final class SortFieldList extends QueryPartList<SortField<?>> {

    SortFieldList() {
        this(emptyList());
    }

    SortFieldList(List<SortField<?>> wrappedList) {
        super(wrappedList);
    }

    final void addAll(Field<?>... fields) {
        addAll(Tools.map(fields, f -> f.asc()));
    }

    /**
     * Whether the {@link SortField}s in this list are uniformly sorted, e.g.
     * all {@link SortOrder#ASC} or all {@link SortOrder#DESC}.
     */
    final boolean uniform() {
        return !anyMatch(this, f -> (f.getOrder() == DESC) != (get(0).getOrder() == DESC));
    }

    /**
     * Whether any of the {@link SortField}s in this list contains a
     * <code>NULLS FIRST</code> or <code>NULLS LAST</code> clause.
     */
    final boolean nulls() {
        return anyMatch(this, f -> ((SortFieldImpl<?>) f).getNullsFirst() || ((SortFieldImpl<?>) f).getNullsLast());
    }

    final List<Field<?>> fields() {
        return Tools.map(this, f -> ((SortFieldImpl<?>) f).getField());
    }
}
