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

import static org.jooq.tools.StringUtils.rightPad;

/**
 * A JSON formatting type, which can be used to configure JSON imports /
 * exports.
 * <p>
 * The default format is the following, using {@link #header()} equal to
 * <code>true</code> and applying {@link RecordFormat#ARRAY}: <code><pre>
 * {"fields":[{"name":"field-1","type":"type-1"},
 *            {"name":"field-2","type":"type-2"},
 *             ...,
 *            {"name":"field-n","type":"type-n"}],
 * "records":[[value-1-1,value-1-2,...,value-1-n],
 *            [value-2-1,value-2-2,...,value-2-n]]}</pre></code>
 * <p>
 * If {@link #header()} is set to <code>false</code>, then the result is simply
 * the records array, either using {@link RecordFormat#ARRAY}: <code><pre>
 * [[value-1-1,value-1-2,...,value-1-n],
 *  [value-2-1,value-2-2,...,value-2-n]]</pre></code>
 * <p>
 * or, using {@link RecordFormat#OBJECT}: <code><pre>
 * [{"field-1": value-1-1, "field-2": value-1-2,..., "field-n": value-1-n},
 *  {"field-1": value-2-1, "field-2": value-2-2,..., "field-n": value-2-n}]</pre></code>
 *
 * @author Lukas Eder
 */
public final class JSONFormat {

    public static final JSONFormat DEFAULT_FOR_RESULTS = new JSONFormat();
    public static final JSONFormat DEFAULT_FOR_RECORDS = new JSONFormat().header(false);

    final boolean                  format;
    final String                   newline;
    final int                      indent;
    final String[]                 indented;
    final boolean                  header;
    final RecordFormat             recordFormat;

    public JSONFormat() {
        this(
            false,
            "\n",
            2,
            null,
            true,
            RecordFormat.ARRAY
        );
    }

    private JSONFormat(
        boolean format,
        String newline,
        int indent,
        String[] indented,
        boolean header,
        RecordFormat recordFormat
    ) {
        this.format = format;
        this.newline = newline;
        this.indent = indent;
        this.indented = indented != null ? indented : new String[] {
                                                "",
            format ? rightPad("", indent * 1) : "",
            format ? rightPad("", indent * 2) : "",
            format ? rightPad("", indent * 3) : ""
        };
        this.header = header;
        this.recordFormat = recordFormat;
    }

    /**
     * The new value for the formatting flag, defaulting to <code>false</code>.
     */
    public JSONFormat format(boolean newFormat) {
        return new JSONFormat(
            newFormat,
            newline,
            indent,
            null,
            header,
            recordFormat
        );
    }

    /**
     * The formatting flag.
     */
    public boolean format() {
        return format;
    }

    /**
     * The new newline character, defaulting to <code>\n</code>.
     */
    public JSONFormat newline(String newNewline) {
        return new JSONFormat(
            format,
            newNewline,
            indent,
            indented,
            header,
            recordFormat
        );
    }

    /**
     * The formatting flag.
     */
    public String newline() {
        return format ? newline : "";
    }

    /**
     * The new indentation value, defaulting to <code>2</code>.
     */
    public JSONFormat indent(int newIndent) {
        return new JSONFormat(
            format,
            newline,
            newIndent,
            null,
            header,
            recordFormat
        );
    }

    /**
     * The indentation.
     */
    public int indent() {
        return indent;
    }

    /**
     * Convenience method to get an indentation string at a given level.
     */
    public String indentString(int level) {
        if (level < indented.length)
            return indented[level];
        else if (format)
            return rightPad("", indent * level);
        else
            return "";
    }

    /**
     * Whether to emit a header row with column names, defaulting to
     * <code>true</code>.
     */
    public JSONFormat header(boolean newHeader) {
        return new JSONFormat(
            format,
            newline,
            indent,
            indented,
            newHeader,
            recordFormat
        );
    }

    /**
     * Whether to emit a header row with column names, defaulting to
     * <code>true</code>.
     */
    public boolean header() {
        return header;
    }

    /**
     * The record format to be applied, defaulting to
     * {@link RecordFormat#ARRAY}.
     */
    public JSONFormat recordFormat(RecordFormat newRecordFormat) {
        return new JSONFormat(
            format,
            newline,
            indent,
            indented,
            header,
            newRecordFormat
        );
    }

    /**
     * The record format to be applied, defaulting to
     * {@link RecordFormat#ARRAY}.
     */
    public RecordFormat recordFormat() {
        return recordFormat;
    }

    /**
     * The format of individual JSON records.
     */
    public enum RecordFormat {

        /**
         * A record is a JSON array.
         * <p>
         * This format allows for accessing columns by index, saving space by
         * avoiding repetitive column names in large result sets. Use this
         * preferrably with {@link JSONFormat#header()} equal to
         * <code>true</code>.
         */
        ARRAY,

        /**
         * A record is a JSON object.
         * <p>
         * This format allows for accessing columns by name, repeating column
         * names in each record.
         */
        OBJECT,
    }
}
