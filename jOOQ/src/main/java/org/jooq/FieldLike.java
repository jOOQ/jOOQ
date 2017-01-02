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

import java.util.function.Function;

import org.jooq.conf.Settings;

/**
 * An object that can behave like a field (a field-like object)
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
    <T> Field<T> asField();

    /**
     * The underlying field representation of this object.
     * <p>
     * This method is useful for things like
     * <code>SELECT y.*, (SELECT a FROM x) [alias] FROM y</code>
     * <p>
     * Note that the case-sensitivity of the returned field depends on
     * {@link Settings#getRenderNameStyle()}. By default, field aliases are
     * quoted, and thus case-sensitive!
     *
     * @return This result provider as a Field&lt;?&gt; object
     */
    <T> Field<T> asField(String alias);


    /**
     * The underlying field representation of this object.
     * <p>
     * This method is useful for things like
     * <code>SELECT y.*, (SELECT a FROM x) [alias] FROM y</code>
     * <p>
     * Note that the case-sensitivity of the returned field depends on
     * {@link Settings#getRenderNameStyle()}. By default, field aliases are
     * quoted, and thus case-sensitive!
     * <p>
     * This works like {@link #asField(String)}, except that field aliases are
     * provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix (on {@link Table#as(String, Function)}):
     * <p>
     * <code><pre>
     * MY_TABLE.as("t1", f -> "prefix_" + f.getName());
     * </pre></code>
     * <p>
     * And then to use the same function also for individual fields:
     * <p>
     * <code><pre>
     * MY_TABLE.MY_COLUMN.as(f -> "prefix_" + f.getName());
     * </pre></code>
     */
    @Support
    <T> Field<T> asField(Function<? super Field<T>, ? extends String> aliasFunction);


}
