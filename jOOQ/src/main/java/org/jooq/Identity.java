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

import java.io.Serializable;

/**
 * An <code>Identity</code> is an object representing an <code>IDENTITY</code>
 * column as understood by the SQL:2003 standard. In most RDBMS, this is
 * actually called an <code>IDENTITY</code> column. Some RDMBS such as MySQL
 * call it <code>AUTO_INCREMENT</code> columns.
 *
 * @param <R> The <code>IDENTITY</code>'s owner table record
 * @param <T> The <code>IDENTITY</code>'s field type
 * @author Lukas Eder
 */
public interface Identity<R extends Record, T> extends Serializable {

    /**
     * The <code>IDENTITY</code>'s owner table
     */
    Table<R> getTable();

    /**
     * The <code>IDENTITY</code> column.
     */
    TableField<R, T> getField();
}
