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
package org.jooq.postgres.extensions.converters;

import static org.jooq.postgres.extensions.types.BigDecimalRange.bigDecimalRange;

import java.math.BigDecimal;

import org.jooq.postgres.extensions.types.BigDecimalRange;
import org.jooq.postgres.extensions.types.TimestampRange;

/**
 * A converter for {@link TimestampRange}.
 *
 * @author Lukas Eder
 */
public class BigDecimalRangeConverter extends AbstractRangeConverter<BigDecimal, BigDecimalRange> {

    private static final BigDecimalRange EMPTY = bigDecimalRange(BigDecimal.ZERO, BigDecimal.ZERO);

    public BigDecimalRangeConverter() {
        super(BigDecimalRange.class);
    }

    @Override
    final BigDecimalRange construct(String lower, boolean lowerIncluding, String upper, boolean upperIncluding) {
        return bigDecimalRange(
            lower == null ? null : new BigDecimal(lower),
            lowerIncluding,
            upper == null ? null : new BigDecimal(upper),
            upperIncluding
        );
    }

    @Override
    final BigDecimalRange empty() {
        return EMPTY;
    }
}
