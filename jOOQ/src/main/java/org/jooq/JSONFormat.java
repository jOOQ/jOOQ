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
 * <p>
 * The type is immutable, meaning calls to setters like {@link #header(boolean)}
 * do not modify the original reference, but return a new one instead.
 *
 * @author Lukas Eder
 */
public final class JSONFormat {

    public final static JSONFormat DEFAULT_FOR_RESULTS = new JSONFormat();
    public final static JSONFormat DEFAULT_FOR_RECORDS = new JSONFormat().header(false);

    final boolean                  format;
    final String                   newline;
    final int                      globalIndent;
    final int                      indent;
    final String[]                 indented;
    final boolean                  header;
    final RecordFormat             recordFormat;
    final boolean                  wrapSingleColumnRecords;
    final boolean                  quoteNested;

    public JSONFormat() {
        this(
            false,
            "\n",
            0,
            2,
            null,
            true,
            RecordFormat.ARRAY,
            true,
            false
        );
    }

    private JSONFormat(
        boolean format,
        String newline,
        int globalIndent,
        int indent,
        String[] indented,
        boolean header,
        RecordFormat recordFormat,
        boolean wrapSingleColumnRecords,
        boolean quoteNested
    ) {
        this.format = format;
        this.newline = newline;
        this.globalIndent = globalIndent;
        this.indent = indent;
        this.indented = indented != null ? indented : new String[] {
                                                "",
            format ? rightPad("", indent * 1) : "",
            format ? rightPad("", indent * 2) : "",
            format ? rightPad("", indent * 3) : ""
        };
        this.header = header;
        this.recordFormat = recordFormat;
        this.wrapSingleColumnRecords = wrapSingleColumnRecords;
        this.quoteNested = quoteNested;
    }

    /**
     * The new value for the formatting flag, defaulting to <code>false</code>.
     */
    public final JSONFormat format(boolean newFormat) {
        return new JSONFormat(
            newFormat,
            newline,
            globalIndent,
            indent,
            null,
            header,
            recordFormat,
            wrapSingleColumnRecords,
            quoteNested
        );
    }

    /**
     * The formatting flag.
     */
    public final boolean format() {
        return format;
    }

    /**
     * The new newline character, defaulting to <code>\n</code>.
     */
    public final JSONFormat newline(String newNewline) {
        return new JSONFormat(
            format,
            newNewline,
            globalIndent,
            indent,
            indented,
            header,
            recordFormat,
            wrapSingleColumnRecords,
            quoteNested
        );
    }

    /**
     * The formatting flag.
     */
    public final String newline() {
        return format ? newline : "";
    }

    /**
     * The new global indentation size applied on all levels, defaulting to <code>0</code>.
     */
    public final JSONFormat globalIndent(int newGlobalIndent) {
        return new JSONFormat(
            format,
            newline,
            newGlobalIndent,
            indent,
            null,
            header,
            recordFormat,
            wrapSingleColumnRecords,
            quoteNested
        );
    }

    /**
     * The global indentation applied on all levels.
     */
    public final int globalIndent() {
        return globalIndent;
    }

    /**
     * The new indentation size per level value, defaulting to <code>2</code>.
     */
    public final JSONFormat indent(int newIndent) {
        return new JSONFormat(
            format,
            newline,
            globalIndent,
            newIndent,
            null,
            header,
            recordFormat,
            wrapSingleColumnRecords,
            quoteNested
        );
    }

    /**
     * The indentation size per level.
     */
    public final int indent() {
        return indent;
    }

    /**
     * Convenience method to get an indentation string at a given level.
     */
    public final String indentString(int level) {
        int i = level + globalIndent / indent;

        if (i < indented.length)
            return indented[i];
        else if (format)
            return rightPad("", globalIndent + indent * level);
        else
            return "";
    }

    /**
     * Whether to emit a header row with column names, defaulting to
     * <code>true</code>.
     */
    public final JSONFormat header(boolean newHeader) {
        return new JSONFormat(
            format,
            newline,
            globalIndent,
            indent,
            indented,
            newHeader,
            recordFormat,
            wrapSingleColumnRecords,
            quoteNested
        );
    }

    /**
     * Whether to emit a header row with column names, defaulting to
     * <code>true</code>.
     */
    public final boolean header() {
        return header;
    }

    /**
     * The record format to be applied, defaulting to
     * {@link RecordFormat#ARRAY}.
     */
    public final JSONFormat recordFormat(RecordFormat newRecordFormat) {
        return new JSONFormat(
            format,
            newline,
            globalIndent,
            indent,
            indented,
            header,
            newRecordFormat,
            wrapSingleColumnRecords,
            quoteNested
        );
    }

    /**
     * The record format to be applied, defaulting to
     * {@link RecordFormat#ARRAY}.
     */
    public final RecordFormat recordFormat() {
        return recordFormat;
    }

    /**
     * Whether to wrap single column records in the {@link #recordFormat()}.
     */
    public final JSONFormat wrapSingleColumnRecords(boolean newWrapSingleColumnRecords) {
        return new JSONFormat(
            format,
            newline,
            globalIndent,
            indent,
            indented,
            header,
            recordFormat,
            newWrapSingleColumnRecords,
            quoteNested
        );
    }

    /**
     * Whether to wrap single column records in the {@link #recordFormat()}.
     */
    public final boolean wrapSingleColumnRecords() {
        return wrapSingleColumnRecords;
    }

    /**
     * Whether nested {@link JSON} or {@link JSONB} content should be quoted
     * like a string, or nested into JSON formatted output.
     */
    public final JSONFormat quoteNested(boolean newQuoteNested) {
        return new JSONFormat(
            format,
            newline,
            globalIndent,
            indent,
            indented,
            header,
            recordFormat,
            wrapSingleColumnRecords,
            newQuoteNested
        );
    }

    /**
     * Whether nested {@link JSON} or {@link JSONB} content should be quoted
     * like a string, or nested into JSON formatted output.
     */
    public final boolean quoteNested() {
        return quoteNested;
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
