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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq.postgres.extensions.converters;

import static org.jooq.postgres.extensions.types.IntegerRange.integerRange;

import org.jooq.postgres.extensions.types.IntegerRange;

/**
 * A converter for {@link IntegerRange}.
 *
 * @author Lukas Eder
 */
public class IntegerRangeConverter extends AbstractRangeConverter<Integer, IntegerRange> {

    private static final IntegerRange EMPTY = integerRange(0, 0);

    public IntegerRangeConverter() {
        super(IntegerRange.class);
    }

    @Override
    final IntegerRange construct(String lower, boolean lowerIncluding, String upper, boolean upperIncluding) {
        return integerRange(
            lower == null ? null : Integer.valueOf(lower),
            lowerIncluding,
            upper == null ? null : Integer.valueOf(upper),
            upperIncluding
        );
    }

    @Override
    final IntegerRange empty() {
        return EMPTY;
    }
}
