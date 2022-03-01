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

import java.sql.Date;

/**
 * A data type representing the PostgreSQL <code>daterange</code> type.
 *
 * @author Lukas Eder
 */
public final class DateRange extends AbstractDiscreteRange<Date, DateRange> {

    private DateRange(Date lower, boolean lowerIncluding, Date upper, boolean upperIncluding) {
        super(lower, lowerIncluding, upper, upperIncluding);
    }

    /**
     * Create a new {@link DateRange} with a inclusive lower bound and an
     * exclusive upper bound.
     */
    public static final DateRange dateRange(Date lower, Date upper) {
        return new DateRange(lower, true, upper, false);
    }

    /**
     * Create a new {@link DateRange}.
     */
    public static final DateRange dateRange(Date lower, boolean lowerIncluding, Date upper, boolean upperIncluding) {
        return new DateRange(lower, lowerIncluding, upper, upperIncluding);
    }

    @Override
    final DateRange construct(Date lower, Date upper) {
        return new DateRange(lower, true, upper, false);
    }

    @Override
    final Date next(Date t) {
        return Date.valueOf(t.toLocalDate().plusDays(1));
    }

    @Override
    final Date prev(Date t) {
        return Date.valueOf(t.toLocalDate().minusDays(1));
    }
}
