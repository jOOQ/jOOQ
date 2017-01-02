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

/**
 * The <code>Loader</code> API is used for configuring data loads.
 * <p>
 * The step in constructing the {@link Loader} object where you can set the
 * optional CSV loader options.
 *
 * @author Lukas Eder
 */
public interface LoaderCSVOptionsStep<R extends Record> extends LoaderListenerStep<R> {

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
