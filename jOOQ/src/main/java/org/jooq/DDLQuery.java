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

import org.jooq.impl.DSL;

/**
 * A marker interface for all DDL queries.
 * <p>
 * A lot of DDL queries come with syntax that requires emulation using anonymous
 * blocks via {@link DSL#begin(Statement...)}. While basic anonymous blocks are
 * supported in the jOOQ Open Source Edition as well, more sophisticated blocks
 * and other procedural logic is a commercial only feature. Examples for this
 * are:
 * <ul>
 * <li>Various <code>IF [ NOT ] EXISTS</code> clauses.</li>
 * <li><code>ALTER VIEW … AS</code> (emulated via a block containing
 * <code>DROP VIEW</code> and then <code>CREATE VIEW</code>).</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface DDLQuery extends RowCountQuery {

}
