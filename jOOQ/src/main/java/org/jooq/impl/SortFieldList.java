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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.SortOrder;

/**
 * @author Lukas Eder
 */
final class SortFieldList extends QueryPartList<SortField<?>> {

    private static final long serialVersionUID = -1825164005148183725L;

    SortFieldList() {
        // Java 6's compiler seems to be unable to infer this type
        this(Collections.<SortField<?>>emptyList());
    }

    SortFieldList(List<SortField<?>> wrappedList) {
        super(wrappedList);
    }

    final void addAll(Field<?>... fields) {
        SortField<?>[] result = new SortField[fields.length];

        for (int i = 0; i < fields.length; i++) {
            result[i] = fields[i].asc();
        }

        addAll(Arrays.asList(result));
    }

    /**
     * Whether the {@link SortField}s in this list are uniformly sorted, e.g.
     * all {@link SortOrder#ASC} or all {@link SortOrder#DESC}.
     */
    final boolean uniform() {
        for (SortField<?> field : this)
            if (field.getOrder() != get(0).getOrder())
                return false;

        return true;
    }

    /**
     * Whether any of the {@link SortField}s in this list contains a
     * <code>NULLS FIRST</code> or <code>NULLS LAST</code> clause.
     */
    final boolean nulls() {
        for (SortField<?> field : this)
            if (((SortFieldImpl<?>) field).getNullsFirst() ||
                ((SortFieldImpl<?>) field).getNullsLast())
                return true;

        return false;
    }

    final List<Field<?>> fields() {
        List<Field<?>> result = new ArrayList<Field<?>>();

        for (SortField<?> field : this)
            result.add(((SortFieldImpl<?>) field).getField());

        return result;
    }
}
