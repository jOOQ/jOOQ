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

import org.jooq.conf.ParamType;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;

/**
 * A plain SQL {@link QueryPart}.
 * <p>
 * Plain SQL query parts can be constructed in a variety of ways from the
 * {@link DSL} API ({@link DSL#field(String)}, {@link DSL#table(String)}, etc.)
 * as well as from convenience methods such as for instance
 * {@link SelectWhereStep#where(String)}. These query parts allow for embedding
 * simple SQL strings with associated bind variables or nested queryparts.
 * <p>
 * <h3>Template language</h3>
 * <p>
 * A simple template language is implemented by such plain SQL query parts,
 * exposing the following features:
 * <ul>
 * <li><strong>Nested query parts</strong>: A template may refer to argument
 * query parts using zero-based, numbered references wrapped in curly braces.
 * The following expression will embed <code>expr1</code> and <code>expr2</code>
 * at the appropriate locations:
 * <p>
 * <code><pre>
 * DSL.field("GROUP_CONCAT(DISTINCT {0} ORDER BY {1} ASC SEPARATOR '-')", expr1, expr2);
 * </pre></code> Query part indexes may be referenced multiple times from within
 * a template.</li>
 * <li><strong>Keywords</strong>: Curly braces are also used to delimit
 * keywords, which expose the behaviour specified in {@link DSL#keyword(String)}
 * (e.g. upper-casing, lower-casing, etc.) . The following expression makes use
 * of embedded keywords in a plain SQL template:
 * <p>
 * <code><pre>DSL.field("{current_timestamp}");</pre></code></li>
 * <li><strong>JDBC escape syntax</strong>: JDBC also allows for using curly
 * braces to embed escape expressions in SQL statements, such as date literals,
 * for instance. JDBC escape syntax is left untouched by jOOQ's plain SQL query
 * part renderings. The following example shows such usage:
 * <p>
 * <code><pre>DSL.field("{d '2015-01-01'}");</pre></code> The following JDBC
 * escape syntax elements are recognised:
 * <ul>
 * <li><code>{d [date literal]}</code></li>
 * <li><code>{t [time literal]}</code></li>
 * <li><code>{ts [timestamp literal]}</code></li>
 * <li><code>{fn [function literal]}</code></li>
 * </ul>
 * </li>
 * <li><strong>JDBC bind variables</strong>: in case bind variables should be
 * inlined (as in {@link DSL#inline(CharSequence)}, {@link ParamType#INLINED},
 * or {@link StatementType#STATIC_STATEMENT}), plain SQL query parts will
 * discover question marks (<code>?</code>) at syntactically appropriate
 * positions (outside of comments, string literals, quoted name literals) and
 * substitute the appropriate bind value into the template.</li>
 * </ul>
 *
 * @author Lukas Eder
 */
@PlainSQL
public interface SQL extends QueryPart {

}
