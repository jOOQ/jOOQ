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
package org.jooq;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.jooq.conf.Settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Experimental;

/**
 * The common base type for all objects that can be used for query composition.
 *
 * @author Lukas Eder
 */
public interface QueryPart extends Serializable {

    /**
     * Render a SQL string representation of this <code>QueryPart</code>.
     * <p>
     * For improved debugging, this renders a SQL string of this
     * <code>QueryPart</code> with inlined bind variables. If this
     * <code>QueryPart</code> is {@link Attachable}, then the attached
     * {@link Configuration} may be used for rendering the SQL string, including
     * {@link SQLDialect} and {@link Settings}. Do note that most
     * <code>QueryPart</code> instances are not attached to a
     * {@link Configuration}, and thus there is no guarantee that the SQL string
     * will make sense in the context of a specific database.
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
