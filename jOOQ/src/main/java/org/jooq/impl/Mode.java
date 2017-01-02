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

import org.jooq.AggregateFilterStep;
import org.jooq.Field;
import org.jooq.OrderedAggregateFunctionOfDeferredType;
import org.jooq.SortField;

/**
 * @author Lukas Eder
 */
final class Mode implements OrderedAggregateFunctionOfDeferredType {

    @Override
    public final <T> AggregateFilterStep<T> withinGroupOrderBy(Field<T> field) {
        return new Function<T>("mode", field.getDataType()).withinGroupOrderBy(field);
    }

    @Override
    public final <T> AggregateFilterStep<T> withinGroupOrderBy(SortField<T> field) {
        return new Function<T>("mode", ((SortFieldImpl<T>) field).getField().getDataType()).withinGroupOrderBy(field);
    }
}
