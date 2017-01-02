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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.keyword;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class LeadLag<T> extends Function<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7292087943334025737L;

    private final String   function;
    private final Field<T> field;
    private final int      offset;
    private final Field<T> defaultValue;

    LeadLag(String function, Field<T> field) {
        super(function, field.getDataType(), field);

        this.function = function;
        this.field = field;
        this.offset = 0;
        this.defaultValue = null;
    }

    LeadLag(String function, Field<T> field, int offset) {
        super(function, field.getDataType(), field, inline(offset));

        this.function = function;
        this.field = field;
        this.offset = offset;
        this.defaultValue = null;
    }

    LeadLag(String function, Field<T> field, int offset, Field<T> defaultValue) {
        super(function, field.getDataType(), field, inline(offset), defaultValue);

        this.function = function;
        this.field = field;
        this.offset = offset;
        this.defaultValue = defaultValue;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (defaultValue == null) {
            super.accept(ctx);
        }
        else {
            switch (ctx.family()) {





















                default:
                    super.accept(ctx);
                    break;
            }
        }
    }
}
