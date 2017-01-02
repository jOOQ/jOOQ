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
 */
package org.jooq;

import java.io.Serializable;

/**
 * The common base type for all objects that can be used for query composition.
 * <p>
 * All <code>QueryPart</code> implementations can be cast to
 * {@link QueryPartInternal} in order to access the internal API.
 *
 * @author Lukas Eder
 */
public interface QueryPart extends Serializable {

    /**
     * Render a SQL string of this <code>QueryPart</code>
     * <p>
     * For improved debugging, this renders a SQL string of this
     * <code>QueryPart</code> with inlined bind variables. If you wish to gain
     * more control over the concrete SQL rendering of this
     * <code>QueryPart</code>, use {@link DSLContext#renderContext()} to obtain a
     * configurable render context for SQL rendering.
     *
     * @return A SQL string representation of this <code>QueryPart</code>
     */
    @Override
    String toString();

    /**
     * Check whether this <code>QueryPart</code> can be considered equal to
     * another <code>QueryPart</code>.
     * <p>
     * In general, <code>QueryPart</code> equality is defined in terms of
     * {@link #toString()} equality. In other words, two query parts are
     * considered equal if their rendered SQL (with inlined bind variables) is
     * equal. This means that the two query parts do not necessarily have to be
     * of the same type.
     * <p>
     * Some <code>QueryPart</code> implementations may choose to override this
     * behaviour for improved performance, as {@link #toString()} is an
     * expensive operation, if called many times.
     *
     * @param object The other <code>QueryPart</code>
     * @return Whether the two query parts are equal
     */
    @Override
    boolean equals(Object object);

    /**
     * Generate a hash code from this <code>QueryPart</code>.
     * <p>
     * In general, <code>QueryPart</code> hash codes are the same as the hash
     * codes generated from {@link #toString()}. This guarantees consistent
     * behaviour with {@link #equals(Object)}
     * <p>
     * Some <code>QueryPart</code> implementations may choose to override this
     * behaviour for improved performance, as {@link #toString()} is an
     * expensive operation, if called many times.
     *
     * @return The <code>QueryPart</code> hash code
     */
    @Override
    int hashCode();
}
