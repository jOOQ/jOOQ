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
 * An XML formatting type, which can be used to configure XML imports / exports.
 * <p>
 * The type is immutable, meaning calls to setters like {@link #header(boolean)}
 * do not modify the original reference, but return a new one instead.
 *
 * @author Lukas Eder
 */
public final class XMLFormat {

    public final static XMLFormat DEFAULT_FOR_RESULTS = new XMLFormat();
    public final static XMLFormat DEFAULT_FOR_RECORDS = new XMLFormat().header(false).xmlns(false);

    final boolean                 xmlns;
    final boolean                 format;
    final String                  newline;
    final int                     globalIndent;
    final int                     indent;
    final String[]                indented;
    final boolean                 header;
    final RecordFormat            recordFormat;
    final boolean                 quoteNested;

    public XMLFormat() {
        this(
            true,
            false,
            "\n",
            0,
            2,
            null,
            true,
            RecordFormat.VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE,
            false
        );
    }

    private XMLFormat(
        boolean xmlns,
        boolean format,
        String newline,
        int globalIndent,
        int indent,
        String[] indented,
        boolean header,
        RecordFormat recordFormat,
        boolean quoteNested
    ) {
        this.xmlns = xmlns;
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
        this.quoteNested = quoteNested;
    }

    /**
     * The new value for the xmlns flag, defaulting to <code>true</code>.
     */
    public final XMLFormat xmlns(boolean newXmlns) {
        return new XMLFormat(
            newXmlns,
            format,
            newline,
            globalIndent,
            indent,
            indented,
            header,
            recordFormat,
            quoteNested
        );
    }

    /**
     * The xmlns flag.
     */
    public final boolean xmlns() {
        return xmlns;
    }

    /**
     * The new value for the formatting flag, defaulting to <code>false</code>.
     */
    public final XMLFormat format(boolean newFormat) {
        return new XMLFormat(
            xmlns,
            newFormat,
            newline,
            globalIndent,
            indent,
            null,
            header,
            recordFormat,
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
    public final XMLFormat newline(String newNewline) {
        return new XMLFormat(
            xmlns,
            format,
            newNewline,
            globalIndent,
            indent,
            indented,
            header,
            recordFormat,
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
    public final XMLFormat globalIndent(int newGlobalIndent) {
        return new XMLFormat(
            xmlns,
            format,
            newline,
            newGlobalIndent,
            indent,
            null,
            header,
            recordFormat,
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
    public final XMLFormat indent(int newIndent) {
        return new XMLFormat(
            xmlns,
            format,
            newline,
            globalIndent,
            newIndent,
            null,
            header,
            recordFormat,
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
     * The new header value, defaulting to <code>true</code>.
     * <p>
     * This flag governs whether the <code>/result/fields element should be
     * generated on export.
     * <p>
     * This flag is ignored on {@link Record#formatXML(XMLFormat)} and similar
     * methods.
     */
    public final XMLFormat header(boolean newHeader) {
        return new XMLFormat(
            xmlns,
            format,
            newline,
            globalIndent,
            indent,
            indented,
            newHeader,
            recordFormat,
            quoteNested
        );
    }

    /**
     * The header.
     */
    public final boolean header() {
        return header;
    }

    /**
     * The record format to be applied, defaulting to
     * {@link RecordFormat#VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE}.
     */
    public final XMLFormat recordFormat(RecordFormat newRecordFormat) {
        return new XMLFormat(
            xmlns,
            format,
            newline,
            globalIndent,
            indent,
            indented,
            header,
            newRecordFormat,
            quoteNested
        );
    }

    /**
     * The record format to be applied, defaulting to
     * {@link RecordFormat#VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE}.
     */
    public final RecordFormat recordFormat() {
        return recordFormat;
    }

    /**
     * Whether nested {@link XML} content should be quoted like a string, or
     * nested into XML formatted output.
     */
    public final XMLFormat quoteNested(boolean newQuoteNested) {
        return new XMLFormat(
            xmlns,
            format,
            newline,
            globalIndent,
            indent,
            indented,
            header,
            recordFormat,
            newQuoteNested
        );
    }

    /**
     * Whether nested {@link XML} content should be quoted like a string, or
     * nested into XML formatted output.
     */
    public final boolean quoteNested() {
        return quoteNested;
    }

    /**
     * The format of individual XML records.
     */
    public enum RecordFormat {

        /**
         * The default: <code>/record/value[@field="colname"]/text()</code>.
         */
        VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE,

        /**
         * Simplified: <code>/record/value/text()</code>.
         */
        VALUE_ELEMENTS,

        /**
         * Simplified: <code>/record/colname/text()</code>.
         */
        COLUMN_NAME_ELEMENTS,
    }
}
