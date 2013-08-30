/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq;

/**
 * The <code>Loader</code> API is used for configuring data loads.
 * <p>
 * The step in constructing the {@link Loader} object where you can set the
 * optional CSV loader options.
 *
 * @author Lukas Eder
 */
public interface LoaderCSVOptionsStep<R extends TableRecord<R>> extends LoaderLoadStep<R> {

    /**
     * Specify that a certain number of rows should be ignored from the CSV
     * file. This is useful for skipping processing information
     * <p>
     * By default, this is set to <code>1</code>, as CSV files are expected to
     * hold a header row.
     *
     * @param number The number of rows to ignore.
     */
    @Support
    LoaderCSVOptionsStep<R> ignoreRows(int number);

    /**
     * Specify the quote character. By default, this is <code>"</code>
     */
    @Support
    LoaderCSVOptionsStep<R> quote(char quote);

    /**
     * Specify the separator character. By default, this is <code>,</code>
     */
    @Support
    LoaderCSVOptionsStep<R> separator(char separator);

    /**
     * Specify the input string representation of <code>NULL</code>.
     * <p>
     * By default, this is set to <code>null</code>, which means that all empty
     * strings are loaded into the database as such. In some databases (e.g.
     * {@link SQLDialect#ORACLE}), this is effectively the same as loading
     * <code>NULL</code>.
     * <p>
     * In order to treat empty strings as <code>null</code>, you can set the
     * <code>nullString</code> to <code>""</code>. If the null string is
     * overridden with something like <code>{null}</code>, for instance, then
     * empty strings will also be loaded as such by jOOQ.
     */
    @Support
    LoaderCSVOptionsStep<R> nullString(String nullString);
}
