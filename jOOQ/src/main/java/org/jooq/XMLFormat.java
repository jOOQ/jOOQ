/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

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

    final boolean                 mutable;



    boolean                       xmlns;
    boolean                       format;
    String                        newline;
    int                           globalIndent;
    int                           indent;
    String[]                      indented;
    boolean                       header;
    RecordFormat                  recordFormat;
    boolean                       quoteNested;
    NullFormat                    nullFormat;
    ArrayFormat                   arrayFormat;

    public XMLFormat() {
        this(
            false,



            true,
            false,
            "\n",
            0,
            2,
            null,
            true,
            RecordFormat.VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE,
            false,
            NullFormat.EMPTY_ELEMENT,
            ArrayFormat.STRING
        );
    }

    private XMLFormat(
        boolean mutable,



        boolean xmlns,
        boolean format,
        String newline,
        int globalIndent,
        int indent,
        String[] indented,
        boolean header,
        RecordFormat recordFormat,
        boolean quoteNested,
        NullFormat nullFormat,
        ArrayFormat arrayFormat
    ) {
        this.mutable = mutable;



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
        this.nullFormat = nullFormat;
        this.arrayFormat = arrayFormat;
    }

    /**
     * Whether this configuration object is mutable.
     */
    public final boolean mutable() {
        return mutable;
    }

    /**
     * The new value for the mutable flag, defaulting to <code>false</code>.
     */
    @NotNull
    public final XMLFormat mutable(boolean newMutable) {
        if (mutable ^ newMutable)
            return new XMLFormat(
                newMutable,



                xmlns,
                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                quoteNested,
                nullFormat,
                arrayFormat
            );
        else
            return this;
    }









































    /**
     * The new value for the xmlns flag, defaulting to <code>true</code>.
     */
    @NotNull
    public final XMLFormat xmlns(boolean newXmlns) {
        if (mutable) {
            xmlns = newXmlns;
            return this;
        }
        else
            return new XMLFormat(
                mutable,



                newXmlns,
                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                quoteNested,
                nullFormat,
                arrayFormat
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
    @NotNull
    public final XMLFormat format(boolean newFormat) {
        if (mutable) {
            format = newFormat;
            return this;
        }
        else
            return new XMLFormat(
                mutable,



                xmlns,
                newFormat,
                newline,
                globalIndent,
                indent,
                null,
                header,
                recordFormat,
                quoteNested,
                nullFormat,
                arrayFormat
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
    @NotNull
    public final XMLFormat newline(String newNewline) {
        if (mutable) {
            newline = newNewline;
            return this;
        }
        else
            return new XMLFormat(
                mutable,



                xmlns,
                format,
                newNewline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                quoteNested,
                nullFormat,
                arrayFormat
            );
    }

    /**
     * The formatting flag.
     */
    @NotNull
    public final String newline() {
        return format ? newline : "";
    }

    /**
     * The new global indentation size applied on all levels, defaulting to <code>0</code>.
     */
    @NotNull
    public final XMLFormat globalIndent(int newGlobalIndent) {
        if (mutable) {
            globalIndent = newGlobalIndent;
            return this;
        }
        else
            return new XMLFormat(
                mutable,



                xmlns,
                format,
                newline,
                newGlobalIndent,
                indent,
                null,
                header,
                recordFormat,
                quoteNested,
                nullFormat,
                arrayFormat
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
    @NotNull
    public final XMLFormat indent(int newIndent) {
        if (mutable) {
            indent = newIndent;
            return this;
        }
        else
            return new XMLFormat(
                mutable,



                xmlns,
                format,
                newline,
                globalIndent,
                newIndent,
                null,
                header,
                recordFormat,
                quoteNested,
                nullFormat,
                arrayFormat
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
    @NotNull
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
     * This flag governs whether the <code>/result/fields</code> element should
     * be generated on export.
     * <p>
     * This flag is ignored on {@link Record#formatXML(XMLFormat)} and similar
     * methods.
     */
    @NotNull
    public final XMLFormat header(boolean newHeader) {
        if (mutable) {
            header = newHeader;
            return this;
        }
        else
            return new XMLFormat(
                mutable,



                xmlns,
                format,
                newline,
                globalIndent,
                indent,
                indented,
                newHeader,
                recordFormat,
                quoteNested,
                nullFormat,
                arrayFormat
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
    @NotNull
    public final XMLFormat recordFormat(RecordFormat newRecordFormat) {
        if (mutable) {
            recordFormat = newRecordFormat;
            return this;
        }
        else
            return new XMLFormat(
                mutable,



                xmlns,
                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                newRecordFormat,
                quoteNested,
                nullFormat,
                arrayFormat
            );
    }

    /**
     * The record format to be applied, defaulting to
     * {@link RecordFormat#VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE}.
     */
    @NotNull
    public final RecordFormat recordFormat() {
        return recordFormat;
    }

    /**
     * Whether nested {@link XML} content should be quoted like a string, or
     * nested into XML formatted output.
     */
    @NotNull
    public final XMLFormat quoteNested(boolean newQuoteNested) {
        if (mutable) {
            quoteNested = newQuoteNested;
            return this;
        }
        else
            return new XMLFormat(
                mutable,



                xmlns,
                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                newQuoteNested,
                nullFormat,
                arrayFormat
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
     * Whether nested {@link XML} content should be quoted like a string, or
     * nested into XML formatted output.
     */
    @NotNull
    public final XMLFormat nullFormat(NullFormat newNullFormat) {
        if (mutable) {
            nullFormat = newNullFormat;
            return this;
        }
        else
            return new XMLFormat(
                mutable,



                xmlns,
                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                quoteNested,
                newNullFormat,
                arrayFormat
            );
    }

    /**
     * Whether nested {@link XML} content should be quoted like a string, or
     * nested into XML formatted output.
     */
    public final NullFormat nullFormat() {
        return nullFormat;
    }

    /**
     * The representation of array types.
     */
    @NotNull
    public final XMLFormat arrayFormat(ArrayFormat newArrayFormat) {
        if (mutable) {
            arrayFormat = newArrayFormat;
            return this;
        }
        else
            return new XMLFormat(
                mutable,



                xmlns,
                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                quoteNested,
                nullFormat,
                newArrayFormat
            );
    }

    /**
     * The representation of array types.
     */
    public final ArrayFormat arrayFormat() {
        return arrayFormat;
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

    /**
     * The format of a <code>null</code> value.
     */
    public enum NullFormat {

        /**
         * A <code>null</code> value is represented by an empty tag.
         */
        EMPTY_ELEMENT,

        /**
         * A <code>null</code> value is represented by an absent tag.
         */
        ABSENT_ELEMENT,

        /**
         * A <code>null</code> value is represented by a
         * <code>xsi:nil="true"</code> attribute if {@link XMLFormat#xmlns()} is
         * set, or <code>nil="true"</code>, if it is not set.
         */
        XSI_NIL
    }

    /**
     * The format of array values.
     */
    public enum ArrayFormat {

        /**
         * The array is exported as a [1, 2, ..., N] style string, similar to
         * what {@link Arrays#toString(Object[])} produces, leaving parsing of
         * the string to users.
         */
        STRING,

        /**
         * The array is exported as a PostgreSQL style set of
         * <code>&lt;element/&gt;</code> elements.
         */
        ELEMENTS
    }
}
