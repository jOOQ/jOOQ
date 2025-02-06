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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import java.util.function.Function;

import org.jooq.conf.Settings;

import org.jetbrains.annotations.NotNull;

/**
 * An object that can behave like a field (a field-like object).
 * <p>
 * Instances of this type cannot be created directly, only of its subtypes.
 *
 * @author Lukas Eder
 */
public interface FieldLike {

    /**
     * The underlying field representation of this object.
     * <p>
     * This method is useful for things like
     * <code>SELECT y.*, (SELECT a FROM x) FROM y</code>
     *
     * @return This result provider as a Field&lt;?&gt; object
     */
    @NotNull
    <T> Field<T> asField();

    /**
     * The underlying field representation of this object.
     * <p>
     * This method is useful for things like
     * <code>SELECT y.*, (SELECT a FROM x) [alias] FROM y</code>
     * <p>
     * Note that the case-sensitivity of the returned field depends on
     * {@link Settings#getRenderQuotedNames()}. By default, field aliases are
     * quoted, and thus case-sensitive in many SQL dialects!
     *
     * @return This result provider as a Field&lt;?&gt; object
     * @see SelectField#as(String)
     */
    @NotNull
    <T> Field<T> asField(String alias);

    /**
     * The underlying field representation of this object.
     * <p>
     * This method is useful for things like
     * <code>SELECT y.*, (SELECT a FROM x) [alias] FROM y</code>
     * <p>
     * Note that the case-sensitivity of the returned field depends on
     * {@link Settings#getRenderQuotedNames()}. By default, field aliases are
     * quoted, and thus case-sensitive in many SQL dialects!
     * <p>
     * This works like {@link #asField(String)}, except that field aliases are
     * provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix (on {@link Table#as(String, Function)}):
     * <p>
     * <pre><code>
     * MY_TABLE.as("t1", f -&gt; "prefix_" + f.getName());
     * </code></pre>
     * <p>
     * And then to use the same function also for individual fields:
     * <p>
     * <pre><code>
     * MY_TABLE.MY_COLUMN.as(f -&gt; "prefix_" + f.getName());
     * </code></pre>
     *
     * @deprecated - 3.14.0 - [#10156] - These methods will be removed without
     *             replacement from a future jOOQ. They offer convenience that
     *             is unidiomatic for jOOQ's DSL, without offering functionality
     *             that would not be possible otherwise - yet they add
     *             complexity in jOOQ's internals.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    @Support
    <T> Field<T> asField(Function<? super Field<T>, ? extends String> aliasFunction);
}
