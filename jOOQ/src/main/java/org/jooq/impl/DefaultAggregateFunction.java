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

import static org.jooq.impl.Tools.EMPTY_FIELD;

import java.util.function.Function;

import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.QueryPart;
// ...

/**
 * A field that handles built-in functions, aggregate functions, and window
 * functions.
 *
 * @author Lukas Eder
 */
final class DefaultAggregateFunction<T>
extends
    AbstractAggregateFunction<T, DefaultAggregateFunction<T>>
{

    // -------------------------------------------------------------------------
    // XXX Constructors
    // -------------------------------------------------------------------------

    DefaultAggregateFunction(String name, DataType<T> type, Field<?>... arguments) {
        super(name, type, arguments);
    }

    DefaultAggregateFunction(Name name, DataType<T> type, Field<?>... arguments) {
        super(name, type, arguments);
    }

    DefaultAggregateFunction(boolean distinct, String name, DataType<T> type, Field<?>... arguments) {
        super(distinct, name, type, arguments);
    }

    DefaultAggregateFunction(boolean distinct, Name name, DataType<T> type, Field<?>... arguments) {
        super(distinct, name, type, arguments);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    final DefaultAggregateFunction<T> copy2(Function<DefaultAggregateFunction<T>, DefaultAggregateFunction<T>> function) {
        return function.apply(new DefaultAggregateFunction<>(distinct, getQualifiedName(), getDataType(), getArguments().toArray(EMPTY_FIELD)));
    }









}
