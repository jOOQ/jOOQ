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

/**
 * A data type representing the PostgreSQL <code>int4range</code> type.
 *
 * @author Lukas Eder
 */
public final class IntegerRange extends AbstractDiscreteRange<Integer, IntegerRange> {

    private IntegerRange(Integer lower, boolean lowerIncluding, Integer upper, boolean upperIncluding) {
        super(lower, lowerIncluding, upper, upperIncluding);
    }

    /**
     * Create a new {@link IntegerRange} with a inclusive lower bound and an
     * exclusive upper bound.
     */
    public static final IntegerRange integerRange(Integer lower, Integer upper) {
        return new IntegerRange(lower, true, upper, false);
    }

    /**
     * Create a new {@link IntegerRange}.
     */
    public static final IntegerRange integerRange(Integer lower, boolean lowerIncluding, Integer upper, boolean upperIncluding) {
        return new IntegerRange(lower, lowerIncluding, upper, upperIncluding);
    }

    @Override
    final IntegerRange construct(Integer lower, Integer upper) {
        return new IntegerRange(lower, true, upper, false);
    }

    @Override
    final Integer next(Integer t) {
        return t.intValue() + 1;
    }

    @Override
    final Integer prev(Integer t) {
        return t.intValue() - 1;
    }
}
