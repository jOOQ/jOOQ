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
package org.jooq.postgres.extensions.types;

import java.sql.Timestamp;

/**
 * A data type representing the PostgreSQL <code>tsrange</code> type.
 *
 * @author Lukas Eder
 */
public final class TimestampRange extends AbstractRange<Timestamp> {

    private TimestampRange(Timestamp lower, boolean lowerIncluding, Timestamp upper, boolean upperIncluding) {
        super(lower, lowerIncluding, upper, upperIncluding);
    }

    /**
     * Create a new {@link TimestampRange} with a inclusive lower bound and an
     * exclusive upper bound.
     */
    public static final TimestampRange timestampRange(Timestamp lower, Timestamp upper) {
        return new TimestampRange(lower, true, upper, false);
    }

    /**
     * Create a new {@link TimestampRange}.
     */
    public static final TimestampRange timestampRange(Timestamp lower, boolean lowerIncluding, Timestamp upper, boolean upperIncluding) {
        return new TimestampRange(lower, lowerIncluding, upper, upperIncluding);
    }
}
