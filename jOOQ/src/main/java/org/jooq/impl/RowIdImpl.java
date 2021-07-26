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
package org.jooq.impl;

import org.jooq.RowId;

/**
 * @author Lukas Eder
 */
final /* record */ class RowIdImpl implements RowId { private final Object value; public RowIdImpl(Object value) { this.value = value; } public Object value() { return value; } @Override public boolean equals(Object o) { if (!(o instanceof RowIdImpl)) return false; RowIdImpl other = (RowIdImpl) o; if (!java.util.Objects.equals(this.value, other.value)) return false; return true; } @Override public int hashCode() { return java.util.Objects.hash(this.value); }

    @Override
    public String toString() {
        return "" + value;
    }
}
