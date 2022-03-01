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
package org.jooq.postgres.extensions.test;

import static org.jooq.postgres.extensions.test.RangeTestUtils.assertEqualsHashCode;
import static org.jooq.postgres.extensions.types.LongRange.longRange;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class LongRangeTest {

    @Test
    public void testEqualsHashCode() {
        assertEqualsHashCode(longRange(0L, 0L), longRange(0L, 0L));
        assertEqualsHashCode(longRange(0L, 0L), longRange(1L, 1L));
        assertEqualsHashCode(longRange(0L, 1L), longRange(0L, 1L));
        assertEqualsHashCode(longRange(0L, 2L), longRange(0L, true, 1L, true));
        assertEqualsHashCode(longRange(0L, 2L), longRange(0L, true, 2L, false));
        assertEqualsHashCode(longRange(0L, 2L), longRange(-1L, false, 1L, true));
        assertEqualsHashCode(longRange(0L, 2L), longRange(-1L, false, 2L, false));

        assertEqualsHashCode(longRange(0L, null), longRange(0L, true, null, true));
        assertEqualsHashCode(longRange(0L, null), longRange(0L, true, null, false));
        assertEqualsHashCode(longRange(0L, null), longRange(-1L, false, null, true));
        assertEqualsHashCode(longRange(0L, null), longRange(-1L, false, null, false));

        assertEqualsHashCode(longRange(null, 2L), longRange(null, true, 1L, true));
        assertEqualsHashCode(longRange(null, 2L), longRange(null, true, 2L, false));
        assertEqualsHashCode(longRange(null, 2L), longRange(null, false, 1L, true));
        assertEqualsHashCode(longRange(null, 2L), longRange(null, false, 2L, false));

        assertEqualsHashCode(longRange(null, null), longRange(null, true, null, true));
        assertEqualsHashCode(longRange(null, null), longRange(null, true, null, false));
        assertEqualsHashCode(longRange(null, null), longRange(null, false, null, true));
        assertEqualsHashCode(longRange(null, null), longRange(null, false, null, false));

        assertNotEquals(longRange(0L, 1L), longRange(1L, 0L));
    }
}
