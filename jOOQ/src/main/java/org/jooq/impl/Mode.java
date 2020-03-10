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

import org.jooq.AggregateFilterStep;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.OrderedAggregateFunctionOfDeferredType;

/**
 * @author Lukas Eder
 */
final class Mode implements OrderedAggregateFunctionOfDeferredType {

    @SuppressWarnings("unchecked")
    @Override
    public final <T> AggregateFilterStep<T> withinGroupOrderBy(OrderField<T> field) {
        DataType<T> type = field instanceof SortFieldImpl
            ? ((SortFieldImpl<T>) field).getField().getDataType()
            : field instanceof Field
            ? ((AbstractField<T>) field).getDataType()
            : (DataType<T>) SQLDataType.NUMERIC;

        return new DefaultAggregateFunction<>("mode", type).withinGroupOrderBy(field);
    }
}
