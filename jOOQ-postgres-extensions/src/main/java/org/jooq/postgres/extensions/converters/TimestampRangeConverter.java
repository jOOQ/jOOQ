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

import static org.jooq.postgres.extensions.types.TimestampRange.timestampRange;

import java.sql.Timestamp;

import org.jooq.Converter;
import org.jooq.impl.DefaultConverterProvider;
import org.jooq.postgres.extensions.types.TimestampRange;

/**
 * A converter for {@link TimestampRange}.
 *
 * @author Lukas Eder
 */
public class TimestampRangeConverter extends AbstractRangeConverter<Timestamp, TimestampRange> {

    private static final Timestamp                    EPOCH     = Timestamp.valueOf("1970-01-01 00:00:00");
    private static final TimestampRange               EMPTY     = timestampRange(EPOCH, EPOCH);
    private static final Converter<String, Timestamp> CONVERTER = new DefaultConverterProvider().provide(String.class, Timestamp.class);

    public TimestampRangeConverter() {
        super(TimestampRange.class);
    }

    @Override
    final TimestampRange construct(String lower, boolean lowerIncluding, String upper, boolean upperIncluding) {
        return timestampRange(
            CONVERTER.from(lower),
            lowerIncluding,
            CONVERTER.from(upper),
            upperIncluding
        );
    }

    @Override
    final TimestampRange empty() {
        return EMPTY;
    }
}
