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

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.QueryPart;

/**
 * A base class for all built-in functions that have vendor-specific behaviour
 *
 * @author Lukas Eder
 */
abstract class AbstractFunction<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long  serialVersionUID = 8771262868110746571L;

    private final Field<?>[]   arguments;

    AbstractFunction(String name, DataType<T> type, Field<?>... arguments) {
        super(name, type);

        this.arguments = arguments;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(getFunction0(ctx.configuration()));
    }

    final Field<?>[] getArguments() {
        return arguments;
    }

    abstract QueryPart getFunction0(Configuration configuration);
}
