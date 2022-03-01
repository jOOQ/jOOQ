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

import static org.jooq.postgres.extensions.types.DateRange.dateRange;
import static org.jooq.postgres.extensions.types.LocalDateRange.localDateRange;

import java.sql.Date;
import java.time.LocalDate;

import org.jooq.postgres.extensions.types.DateRange;
import org.jooq.postgres.extensions.types.IntegerRange;
import org.jooq.postgres.extensions.types.LocalDateRange;

/**
 * A converter for {@link IntegerRange}.
 *
 * @author Lukas Eder
 */
public class LocalDateRangeConverter extends AbstractRangeConverter<LocalDate, LocalDateRange> {

    private static final LocalDate      EPOCH = LocalDate.parse("1970-01-01");
    private static final LocalDateRange EMPTY = localDateRange(EPOCH, EPOCH);

    public LocalDateRangeConverter() {
        super(LocalDateRange.class);
    }

    @Override
    final LocalDateRange construct(String lower, boolean lowerIncluding, String upper, boolean upperIncluding) {
        return localDateRange(
            lower == null ? null : LocalDate.parse(lower),
            lowerIncluding,
            upper == null ? null : LocalDate.parse(upper),
            upperIncluding
        );
    }

    @Override
    final LocalDateRange empty() {
        return EMPTY;
    }
}
