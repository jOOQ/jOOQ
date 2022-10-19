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

import static org.jooq.postgres.extensions.types.LocalDateTimeRange.localDateTimeRange;

import java.time.LocalDateTime;

import org.jooq.Converter;
import org.jooq.impl.DefaultConverterProvider;
import org.jooq.postgres.extensions.types.LocalDateTimeRange;

/**
 * A converter for {@link LocalDateTimeRange}.
 *
 * @author Lukas Eder
 */
public class LocalDateTimeRangeConverter extends AbstractRangeConverter<LocalDateTime, LocalDateTimeRange> {

    private static final LocalDateTime                    EPOCH     = LocalDateTime.parse("1970-01-01T00:00:00");
    private static final LocalDateTimeRange               EMPTY     = localDateTimeRange(EPOCH, EPOCH);
    private static final Converter<String, LocalDateTime> CONVERTER = new DefaultConverterProvider().provide(String.class, LocalDateTime.class);

    public LocalDateTimeRangeConverter() {
        super(LocalDateTimeRange.class);
    }

    @Override
    final LocalDateTimeRange construct(String lower, boolean lowerIncluding, String upper, boolean upperIncluding) {
        return localDateTimeRange(
            CONVERTER.from(lower),
            lowerIncluding,
            CONVERTER.from(upper),
            upperIncluding
        );
    }

    @Override
    final LocalDateTimeRange empty() {
        return EMPTY;
    }
}
