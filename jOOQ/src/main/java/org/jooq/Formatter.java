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

import org.jooq.conf.NestedCollectionEmulation;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

/**
 * A set of {@link Field} formatting expressions that allow for formatting a
 * {@link Field} when embedding it in {@link SQLDataType#JSON},
 * {@link SQLDataType#JSONB}, or {@link SQLDataType#XML}.
 * <p>
 * Implementations should ensure:
 * <ul>
 * <li>That {@link FormatterContext#multiset()} vs non-multiset specific
 * behaviour is distinguished, if necessary</li>
 * <li>That <code>NULL</code> behaviour is maintained.</li>
 * </ul>
 * <p>
 * <h3>Comparison to other API types</h3>
 * <p>
 * Unlike {@link Formattable}, which talks about formatting data to JSON, XML,
 * and other text types in the client using Java, the {@link Formatter} takes
 * care of doing this directly in SQL.
 * <p>
 * Unlike {@link Converter}, the formatting affects how the RDBMS embeds the
 * value into a JSON or XML datastructure <em>before</em> sending the data to
 * the client. A {@link Converter} is applied only after fetching data from
 * JDBC.
 *
 * @author Lukas Eder
 */
public interface Formatter {

    /**
     * Format the {@link FormatterContext#field()} expression to embed it in a
     * {@link SQLDataType#JSON} context.
     * <p>
     * If applicable, a {@link Binding} should place the resulting expression in
     * the {@link FormatterContext#field(Field)} out parameter. If the out
     * parameter is left untouched, then no JSON specific formatting is applied
     * to the {@link FormatterContext#field()} expression.
     * <p>
     * The {@link FormatterContext#multiset()} flag indicates that the JSON
     * context is from a {@link NestedCollectionEmulation#JSON} emulation for a
     * {@link DSL#multiset(TableLike)} expression.
     */
    void formatJSON(FormatterContext ctx);

    /**
     * Format the {@link FormatterContext#field()} expression to embed it in a
     * {@link SQLDataType#JSONB} context.
     * <p>
     * If applicable, a {@link Binding} should place the resulting expression in
     * the {@link FormatterContext#field(Field)} out parameter. If the out
     * parameter is left untouched, then no JSONB specific formatting is applied
     * to the {@link FormatterContext#field()} expression.
     * <p>
     * The {@link FormatterContext#multiset()} flag indicates that the JSONB
     * context is from a {@link NestedCollectionEmulation#JSONB} emulation for a
     * {@link DSL#multiset(TableLike)} expression.
     */
    void formatJSONB(FormatterContext ctx);

    /**
     * Format the {@link FormatterContext#field()} expression to embed it in a
     * {@link SQLDataType#XML} context.
     * <p>
     * If applicable, a {@link Binding} should place the resulting expression in
     * the {@link FormatterContext#field(Field)} out parameter. If the out
     * parameter is left untouched, then no XML specific formatting is applied
     * to the {@link FormatterContext#field()} expression.
     * <p>
     * The {@link FormatterContext#multiset()} flag indicates that the XML
     * context is from a {@link NestedCollectionEmulation#XML} emulation for a
     * {@link DSL#multiset(TableLike)} expression.
     */
    void formatXML(FormatterContext ctx);
}
