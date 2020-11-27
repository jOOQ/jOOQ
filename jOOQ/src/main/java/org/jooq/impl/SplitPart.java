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

import static org.jooq.impl.Names.N_SPLIT_PART;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class SplitPart extends AbstractField<String> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -5368480391694006195L;
    private final Field<String>  field;
    private final Field<String>  delimiter;
    private final Field<Integer> n;

    SplitPart(Field<String> field, Field<String> delimiter, Field<Integer> n) {
        super(N_SPLIT_PART, field.getDataType());

        this.field = field;
        this.delimiter = delimiter;
        this.n = n;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(N_SPLIT_PART).sql('(').visit(field).sql(", ").visit(delimiter).sql(", ").visit(n).sql(')');
    }
}
