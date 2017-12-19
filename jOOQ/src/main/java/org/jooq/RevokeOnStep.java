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

import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...

/**
 * The step in the creation of a <code>REVOKE</code> statement where the
 * <code>ON</code> clause can be added.
 *
 * @author Timur Shaidullin
 * @author Lukas Eder
 */
public interface RevokeOnStep {

    /**
     * Revoke a privilege on a table.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    RevokeFromStep on(Table<?> table);

    /**
     * Revoke a privilege on a table.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    RevokeFromStep on(Name table);

    /**
     * Revoke a privilege on a table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @PlainSQL
    @Support({ H2, HSQLDB, POSTGRES })
    RevokeFromStep on(String table);
}
