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
package org.jooq.conf;

import java.util.regex.Pattern;

import org.jooq.Qualified;

/**
 * A common base type for objects contained in {@link MappedSchema}, such as
 * {@link MappedTable} or {@link MappedUDT}.
 *
 * @author Lukas Eder
 */
public interface MappedSchemaObject {

    /**
     * The input name as defined in {@link Qualified#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided.
     */
    String getInput();

    /**
     * A regular expression matching the input name as defined in
     * {@link Qualified#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     */
    Pattern getInputExpression();

    /**
     * The output name as it will be rendered in SQL.
     * <ul>
     * <li>When &lt;input/&gt; is provided, &lt;output/&gt; is a constant
     * value.</li>
     * <li>When &lt;inputExpression/&gt; is provided, &lt;output/&gt; is a
     * replacement expression.</li>
     * </ul>
     */
    String getOutput();
}
