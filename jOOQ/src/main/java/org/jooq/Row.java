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
package org.jooq;

import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;

/**
 * A row value expression.
 * <p>
 * Row value expressions are mainly useful for use in predicates, when comparing
 * several values in one go, which can be more elegant than expanding the row
 * value expression predicate in other equivalent syntaxes. This is especially
 * true for non-equality predicates. For instance, the following two predicates
 * are equivalent in SQL:
 * <p>
 * <code><pre>
 * (A, B) &gt; (X, Y)
 * (A &gt; X) OR (A = X AND B &gt; Y)
 * </pre></code>
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .select()
 *    .from(CUSTOMER)
 *    .where(row(CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME).in(
 *        select(ACTOR.FIRST_NAME, ACTOR.LAST_NAME).from(ACTOR)
 *    ))
 *    .fetch();
 * </pre></code>
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be emulated on all databases. See relevant row
 * value expression method Javadocs for details.
 * <p>
 * Instances can be created using {@link DSL#row(Object...)} and overloads.
 *
 * @author Lukas Eder
 */
public interface Row extends Fields, FieldOrRow {

    /**
     * Get the degree of this row value expression.
     */
    int size();

    // ------------------------------------------------------------------------
    // [NOT] NULL predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression contains only <code>NULL</code>
     * values.
     * <p>
     * Row NULL predicates can be emulated in those databases that do not
     * support such predicates natively: <code>(A, B) IS NULL</code> is
     * equivalent to <code>A IS NULL AND B IS NULL</code>
     */
    @NotNull
    @Support
    Condition isNull();

    /**
     * Check if this row value expression contains no <code>NULL</code> values.
     * <p>
     * Row NOT NULL predicates can be emulated in those databases that do not
     * support such predicates natively: <code>(A, B) IS NOT NULL</code> is
     * equivalent to <code>A IS NOT NULL AND B IS NOT NULL</code>
     * <p>
     * Note that the two following predicates are NOT equivalent:
     * <ul>
     * <li><code>(A, B) IS NOT NULL</code>, which is the same as
     * <code>(A IS NOT NULL) AND (B IS NOT NULL)</code></li>
     * <li><code>NOT((A, B) IS NULL)</code>, which is the same as
     * <code>(A IS NOT NULL) OR (B IS NOT NULL)</code></li>
     * </ul>
     */
    @NotNull
    @Support
    Condition isNotNull();

}
