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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import java.time.LocalDate;

/**
 * A data type representing the PostgreSQL <code>daterange</code> type.
 *
 * @author Lukas Eder
 */
public final class LocalDateRange extends AbstractDiscreteRange<LocalDate, LocalDateRange> {

    private LocalDateRange(LocalDate lower, boolean lowerIncluding, LocalDate upper, boolean upperIncluding) {
        super(lower, lowerIncluding, upper, upperIncluding);
    }

    /**
     * Create a new {@link LocalDateRange} with a inclusive lower bound and an
     * exclusive upper bound.
     */
    public static final LocalDateRange localDateRange(LocalDate lower, LocalDate upper) {
        return new LocalDateRange(lower, true, upper, false);
    }

    /**
     * Create a new {@link LocalDateRange}.
     */
    public static final LocalDateRange localDateRange(LocalDate lower, boolean lowerIncluding, LocalDate upper, boolean upperIncluding) {
        return new LocalDateRange(lower, lowerIncluding, upper, upperIncluding);
    }

    @Override
    final LocalDateRange construct(LocalDate lower, LocalDate upper) {
        return new LocalDateRange(lower, true, upper, false);
    }

    @Override
    final LocalDate next(LocalDate t) {
        return t.plusDays(1);
    }

    @Override
    final LocalDate prev(LocalDate t) {
        return t.minusDays(1);
    }
}
