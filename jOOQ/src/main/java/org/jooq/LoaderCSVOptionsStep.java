/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
