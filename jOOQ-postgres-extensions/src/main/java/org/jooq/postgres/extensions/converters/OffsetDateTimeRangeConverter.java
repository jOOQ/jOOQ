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

import static org.jooq.postgres.extensions.types.OffsetDateTimeRange.offsetDateTimeRange;

import java.time.OffsetDateTime;

import org.jooq.Converter;
import org.jooq.impl.DefaultConverterProvider;
import org.jooq.postgres.extensions.types.OffsetDateTimeRange;

/**
 * A converter for {@link OffsetDateTimeRange}.
 *
 * @author Lukas Eder
 */
public class OffsetDateTimeRangeConverter extends AbstractRangeConverter<OffsetDateTime, OffsetDateTimeRange> {

    private static final OffsetDateTime                    EPOCH     = OffsetDateTime.parse("1970-01-01T00:00:00Z");
    private static final OffsetDateTimeRange               EMPTY     = offsetDateTimeRange(EPOCH, EPOCH);
    private static final Converter<String, OffsetDateTime> CONVERTER = new DefaultConverterProvider().provide(String.class, OffsetDateTime.class);

    public OffsetDateTimeRangeConverter() {
        super(OffsetDateTimeRange.class);
    }

    @Override
    final OffsetDateTimeRange construct(String lower, boolean lowerIncluding, String upper, boolean upperIncluding) {
        return offsetDateTimeRange(
            CONVERTER.from(lower),
            lowerIncluding,
            CONVERTER.from(upper),
            upperIncluding
        );
    }

    @Override
    final OffsetDateTimeRange empty() {
        return EMPTY;
    }
}
