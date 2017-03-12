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
 * An XML formatting type, which can be used to configure XML imports / exports.
 *
 * @author Lukas Eder
 */
public final class XMLFormat {

    public static final XMLFormat DEFAULT_FOR_RESULTS = new XMLFormat();
    public static final XMLFormat DEFAULT_FOR_RECORDS = new XMLFormat().header(false).xmlns(false);

    final boolean                 xmlns;
    final boolean                 format;
    final String                  newline;
    final int                     indent;
    final String[]                indented;
    final boolean                 header;
    final RecordFormat            recordFormat;

    public XMLFormat() {
        this(
            true,
            false,
            "\n",
            2,
            true,
            RecordFormat.VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE
        );
    }

    private XMLFormat(
        boolean xmlns,
        boolean format,
        String newline,
        int indent,
        boolean header,
        RecordFormat recordFormat
    ) {
        this.xmlns = xmlns;
        this.format = format;
        this.newline = newline;
        this.indent = indent;
        this.indented = new String[] {
                                                "",
            format ? rightPad("", indent * 1) : "",
            format ? rightPad("", indent * 2) : "",
            format ? rightPad("", indent * 3) : ""
        };
        this.header = header;
        this.recordFormat = recordFormat;
    }

    /**
     * The new value for the xmlns flag, defaulting to <code>true</code>.
     */
    public XMLFormat xmlns(boolean newXmlns) {
        return new XMLFormat(
            newXmlns,
            format,
            newline,
            indent,
            header,
            recordFormat
        );
    }

    /**
     * The xmlns flag.
     */
    public boolean xmlns() {
        return xmlns;
    }

    /**
     * The new value for the formatting flag, defaulting to <code>false</code>.
     */
    public XMLFormat format(boolean newFormat) {
        return new XMLFormat(
            xmlns,
            newFormat,
            newline,
            indent,
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
    public XMLFormat newline(String newNewline) {
        return new XMLFormat(
            xmlns,
            format,
            newNewline,
            indent,
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
    public XMLFormat indent(int newIndent) {
        return new XMLFormat(
            xmlns,
            format,
            newline,
            newIndent,
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
     * The new header value, defaulting to <code>true</code>.
     * <p>
     * This flag governs whether the <code>/result/fields element should be
     * generated on export.
     * <p>
     * This flag is ignored on {@link Record#formatXML(XMLFormat)} and similar
     * methods.
     */
    public XMLFormat header(boolean newHeader) {
        return new XMLFormat(
            xmlns,
            format,
            newline,
            indent,
            newHeader,
            recordFormat
        );
    }

    /**
     * The header.
     */
    public boolean header() {
        return header;
    }

    /**
     * The record format to be applied, defaulting to
     * {@link RecordFormat#VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE}.
     */
    public XMLFormat recordFormat(RecordFormat newRecordFormat) {
        return new XMLFormat(
            xmlns,
            format,
            newline,
            indent,
            header,
            newRecordFormat
        );
    }

    /**
     * The record format to be applied, defaulting to
     * {@link RecordFormat#VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE}.
     */
    public RecordFormat recordFormat() {
        return recordFormat;
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
