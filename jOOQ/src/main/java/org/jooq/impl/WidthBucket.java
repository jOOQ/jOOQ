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

import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.zero;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class WidthBucket<T extends Number> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -4866100604361006859L;
    private final Field<T>       field;
    private final Field<T>       low;
    private final Field<T>       high;
    private final Field<Integer> buckets;

    WidthBucket(Field<T> field, Field<T> low, Field<T> high, Field<Integer> buckets) {
        super(DSL.name("width_bucket"), field.getDataType());

        this.field = field;
        this.low = low;
        this.high = high;
        this.buckets = buckets;
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public void accept(Context<?> ctx) {
        switch (ctx.family()) {



            case POSTGRES:
                ctx.visit(DSL.field("{width_bucket}({0}, {1}, {2}, {3})", getType(), field, low, high, buckets));
                break;

            default:
                ctx.visit(
                    DSL.when(field.lt(low), zero())
                       .when(field.ge(high), buckets.add(one()))
                       .otherwise((Field<Integer>) DSL.floor(field.sub(low).mul(buckets).div(high.sub(low))).add(one()))
                );
                break;
        }
    }

}
