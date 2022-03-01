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

import static org.jooq.postgres.extensions.types.IntegerRange.integerRange;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class IntegerRangeTest {

    @Test
    public void testEqualsHashCode() {
        assertEqualsHashCode(integerRange(0, 0), integerRange(0, 0));
        assertEqualsHashCode(integerRange(0, 1), integerRange(0, 1));
        assertEqualsHashCode(integerRange(0, 2), integerRange(0, true, 1, true));
        assertEqualsHashCode(integerRange(0, 2), integerRange(0, true, 2, false));
        assertEqualsHashCode(integerRange(0, 2), integerRange(-1, false, 1, true));
        assertEqualsHashCode(integerRange(0, 2), integerRange(-1, false, 2, false));

        assertEqualsHashCode(integerRange(0, null), integerRange(0, true, null, true));
        assertEqualsHashCode(integerRange(0, null), integerRange(0, true, null, false));
        assertEqualsHashCode(integerRange(0, null), integerRange(-1, false, null, true));
        assertEqualsHashCode(integerRange(0, null), integerRange(-1, false, null, false));

        assertEqualsHashCode(integerRange(null, 2), integerRange(null, true, 1, true));
        assertEqualsHashCode(integerRange(null, 2), integerRange(null, true, 2, false));
        assertEqualsHashCode(integerRange(null, 2), integerRange(null, false, 1, true));
        assertEqualsHashCode(integerRange(null, 2), integerRange(null, false, 2, false));

        assertEqualsHashCode(integerRange(null, null), integerRange(null, true, null, true));
        assertEqualsHashCode(integerRange(null, null), integerRange(null, true, null, false));
        assertEqualsHashCode(integerRange(null, null), integerRange(null, false, null, true));
        assertEqualsHashCode(integerRange(null, null), integerRange(null, false, null, false));

        assertNotEquals(integerRange(0, 1), integerRange(1, 0));
    }

    private void assertEqualsHashCode(Object expected, Object actual) {
        assertEquals(expected, actual);
        assertEquals(expected.hashCode(), actual.hashCode());
    }
}
