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

import java.sql.ResultSet;

/**
 * A diagnostics listener.
 *
 * @author Lukas Eder
 */
public interface DiagnosticsListener {

    /**
     * The fetched JDBC {@link ResultSet} was too large.
     * <p>
     * An event indicating that a JDBC {@link ResultSet} was fetched with
     * <code>A</code> rows, but only <code>B</code> rows (<code>B &lt; A</code>)
     * were consumed.
     * <p>
     * Typically, this problem can be remedied by applying the appropriate
     * <code>LIMIT</code> clause in SQL, or {@link SelectLimitStep#limit(int)}
     * clause in jOOQ.
     */
    void resultSetTooLarge(DiagnosticsContext ctx);

}
