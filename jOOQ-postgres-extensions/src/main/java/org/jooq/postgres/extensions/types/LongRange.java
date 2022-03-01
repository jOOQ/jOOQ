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
package org.jooq.postgres.extensions.types;

import org.jooq.exception.DataTypeException;

/**
 * A data type representing the PostgreSQL <code>int8range</code> type.
 *
 * @author Lukas Eder
 */
public final class LongRange extends AbstractDiscreteRange<Long, LongRange> {

    private LongRange(Long lower, boolean lowerIncluding, Long upper, boolean upperIncluding) {
        super(lower, lowerIncluding, upper, upperIncluding);
    }

    /**
     * Create a new {@link LongRange} with a inclusive lower bound and an
     * exclusive upper bound.
     */
    public static final LongRange longRange(Long lower, Long upper) {
        return new LongRange(lower, true, upper, false);
    }

    /**
     * Create a new {@link LongRange}.
     */
    public static final LongRange longRange(Long lower, boolean lowerIncluding, Long upper, boolean upperIncluding) {
        return new LongRange(lower, lowerIncluding, upper, upperIncluding);
    }

    @Override
    final LongRange construct(Long lower, Long upper) {
        return new LongRange(lower, true, upper, false);
    }

    @Override
    final Long next(Long t) {
        long l = t.longValue();

        if (l == Long.MAX_VALUE)
            throw new DataTypeException("Long overflow: " + this);
        else
            return l + 1L;
    }

    @Override
    final Long prev(Long t) {
        long l = t.longValue();

        if (l == Long.MIN_VALUE)
            throw new DataTypeException("Long underflow: " + this);
        else
            return l - 1L;
    }
}
