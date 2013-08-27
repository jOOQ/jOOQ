/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
