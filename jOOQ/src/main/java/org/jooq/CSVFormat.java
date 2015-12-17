/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

/**
 * A CSV formatting type, which can be used to configure CSV imports / exports.
 *
 * @author Lukas Eder
 */
public final class CSVFormat {

    final String  delimiter;
    final String  nullString;
    final String  emptyString;
    final String  newline;
    final boolean header;

    public CSVFormat() {
        this(
            ",",
            "\"\"",
            "\"\"",
            "\n",
            true
        );
    }

    private CSVFormat(
        String delimiter,
        String nullString,
        String emptyString,
        String newline,
        boolean header
    ) {
        this.delimiter = delimiter;
        this.nullString = nullString;
        this.emptyString = emptyString;
        this.newline = newline;
        this.header = header;
    }

    /**
     * The delimiter to be used between CSV cells, defaulting to
     * <code>","</code>.
     * <p>
     * <table border="1">
     * <tr>
     * <td>Using <code>","</code></td>
     * <td><code>a,b,c</code></td>
     * </tr>
     * <tr>
     * <td>Using <code>";"</code></td>
     * <td><code>a;b;c</code></td>
     * </tr>
     * </table>
     */
    public CSVFormat delimiter(String newDelimiter) {
        return new CSVFormat(
            newDelimiter,
            nullString,
            emptyString,
            newline,
            header
        );
    }

    /**
     * The delimiter to be used between CSV cells, defaulting to
     * <code>","</code>.
     * <p>
     * <table border="1">
     * <tr>
     * <td>Using <code>","</code></td>
     * <td><code>a,b,c</code></td>
     * </tr>
     * <tr>
     * <td>Using <code>";"</code></td>
     * <td><code>a;b;c</code></td>
     * </tr>
     * </table>
     */
    public CSVFormat delimiter(char newDelimiter) {
        return delimiter("" + newDelimiter);
    }

    /**
     * The delimiter to be used between CSV cells, defaulting to
     * <code>","</code>.
     * <p>
     * <table border="1">
     * <tr>
     * <td>Using <code>","</code></td>
     * <td><code>a,b,c</code></td>
     * </tr>
     * <tr>
     * <td>Using <code>";"</code></td>
     * <td><code>a;b;c</code></td>
     * </tr>
     * </table>
     */
    public String delimiter() {
        return delimiter;
    }

    /**
     * The string to be used for <code>null</code> values, defaulting to the
     * empty string.
     * <p>
     * <table border="1">
     * <tr>
     * <td>Using <code>""</code></td>
     * <td><code>a,,c</code></td>
     * </tr>
     * <tr>
     * <td>Using <code>"\"\""</code></td>
     * <td><code>a,"",c</code></td>
     * </tr>
     * <tr>
     * <td>Using <code>"{null}"</code></td>
     * <td><code>a,{null},c</code></td>
     * </tr>
     * </table>
     */
    public CSVFormat nullString(String newNullString) {
        return new CSVFormat(
            delimiter,
            newNullString,
            emptyString,
            newline,
            header
        );
    }

    /**
     * The string to be used for <code>null</code> values, defaulting to the
     * empty string.
     * <p>
     * <table border="1">
     * <tr>
     * <td>Using <code>""</code></td>
     * <td><code>a,,c</code></td>
     * </tr>
     * <tr>
     * <td>Using <code>"\"\""</code></td>
     * <td><code>a,"",c</code></td>
     * </tr>
     * <tr>
     * <td>Using <code>"{null}"</code></td>
     * <td><code>a,{null},c</code></td>
     * </tr>
     * </table>
     */
    public String nullString() {
        return nullString;
    }

    /**
     * The string to be used for <code>""</code> values, defaulting to the
     * empty string.
     * <p>
     * <table border="1">
     * <tr>
     * <td>Using <code>""</code></td>
     * <td><code>a,,c</code></td>
     * </tr>
     * <tr>
     * <td>Using <code>"\"\""</code></td>
     * <td><code>a,"",c</code></td>
     * </tr>
     * </table>
     */
    public CSVFormat emptyString(String newEmptyString) {
        return new CSVFormat(
            delimiter,
            nullString,
            newEmptyString,
            newline,
            header
        );
    }

    /**
     * The string to be used for <code>""</code> values, defaulting to the
     * empty string.
     * <p>
     * <table border="1">
     * <tr>
     * <td>Using <code>""</code></td>
     * <td><code>a,,c</code></td>
     * </tr>
     * <tr>
     * <td>Using <code>"\"\""</code></td>
     * <td><code>a,"",c</code></td>
     * </tr>
     * </table>
     */
    public String emptyString() {
        return emptyString;
    }

    /**
     * The string to be used to separate rows, defaulting to <code>\n</code>.
     */
    public CSVFormat newline(String newNewline) {
        return new CSVFormat(
            delimiter,
            nullString,
            emptyString,
            newNewline,
            header
        );
    }

    /**
     * The string to be used to separate rows, defaulting to <code>\n</code>.
     */
    public String newline() {
        return newline;
    }

    /**
     * Whether to emit a header row with column names, defaulting to
     * <code>true</code>.
     */
    public CSVFormat header(boolean newHeader) {
        return new CSVFormat(
            delimiter,
            nullString,
            emptyString,
            newline,
            newHeader
        );
    }

    /**
     * Whether to emit a header row with column names, defaulting to
     * <code>true</code>.
     */
    public boolean header() {
        return header;
    }
}
