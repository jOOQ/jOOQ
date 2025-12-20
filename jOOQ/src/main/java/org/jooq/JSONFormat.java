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

import java.util.Base64;
import java.util.HexFormat;

import org.jetbrains.annotations.NotNull;

/**
 * A JSON formatting type, which can be used to configure JSON imports /
 * exports.
 * <p>
 * The default format is the following, using {@link #header()} equal to
 * <code>true</code> and applying {@link RecordFormat#ARRAY}: <pre><code>
 * {"fields":[{"name":"field-1","type":"type-1"},
 *            {"name":"field-2","type":"type-2"},
 *             ...,
 *            {"name":"field-n","type":"type-n"}],
 * "records":[[value-1-1,value-1-2,...,value-1-n],
 *            [value-2-1,value-2-2,...,value-2-n]]}</code></pre>
 * <p>
 * If {@link #header()} is set to <code>false</code>, then the result is simply
 * the records array, either using {@link RecordFormat#ARRAY}: <pre><code>
 * [[value-1-1,value-1-2,...,value-1-n],
 *  [value-2-1,value-2-2,...,value-2-n]]</code></pre>
 * <p>
 * or, using {@link RecordFormat#OBJECT}: <pre><code>
 * [{"field-1": value-1-1, "field-2": value-1-2,..., "field-n": value-1-n},
 *  {"field-1": value-2-1, "field-2": value-2-2,..., "field-n": value-2-n}]</code></pre>
 * <p>
 * The type is immutable, meaning calls to setters like {@link #header(boolean)}
 * do not modify the original reference, but return a new one instead.
 *
 * @author Lukas Eder
 */
public final class JSONFormat {

    public final static JSONFormat DEFAULT_FOR_RESULTS = new JSONFormat();
    public final static JSONFormat DEFAULT_FOR_RECORDS = new JSONFormat().header(false);

    final boolean                  mutable;



    boolean                        format;
    String                         newline;
    int                            globalIndent;
    int                            indent;
    String[]                       indented;
    boolean                        header;
    RecordFormat                   recordFormat;
    NullFormat                     objectNulls;
    NullFormat                     arrayNulls;
    boolean                        wrapSingleColumnRecords;
    boolean                        quoteNested;
    boolean                        nanAsString;
    boolean                        infinityAsString;
    BinaryFormat                   binaryFormat;

    public JSONFormat() {
        this(
            false,



            false,
            "\n",
            0,
            2,
            null,
            true,
            RecordFormat.ARRAY,
            NullFormat.NULL_ON_NULL,
            NullFormat.NULL_ON_NULL,
            true,
            false,
            false,
            false,
            BinaryFormat.BASE64
        );
    }

    private JSONFormat(
        boolean mutable,



        boolean format,
        String newline,
        int globalIndent,
        int indent,
        String[] indented,
        boolean header,
        RecordFormat recordFormat,
        NullFormat objectNulls,
        NullFormat arrayNulls,
        boolean wrapSingleColumnRecords,
        boolean quoteNested,
        boolean nanAsString,
        boolean infinityAsString,
        BinaryFormat binaryFormat
    ) {
        this.mutable = mutable;



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
        this.objectNulls = objectNulls;
        this.arrayNulls = arrayNulls;
        this.wrapSingleColumnRecords = wrapSingleColumnRecords;
        this.quoteNested = quoteNested;
        this.nanAsString = nanAsString;
        this.infinityAsString = infinityAsString;
        this.binaryFormat = binaryFormat;
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
    public final JSONFormat mutable(boolean newMutable) {
        if (mutable ^ newMutable)
            return new JSONFormat(
                newMutable,



                format,
                newline,
                globalIndent,
                indent,
                null,
                header,
                recordFormat,
                objectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                infinityAsString,
                binaryFormat
            );
        else
            return this;
    }












































    /**
     * The new value for the formatting flag, defaulting to <code>false</code>.
     */
    @NotNull
    public final JSONFormat format(boolean newFormat) {
        if (mutable) {
            format = newFormat;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                newFormat,
                newline,
                globalIndent,
                indent,
                null,
                header,
                recordFormat,
                objectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                infinityAsString,
                binaryFormat
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
    public final JSONFormat newline(String newNewline) {
        if (mutable) {
            newline = newNewline;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newNewline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                objectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                infinityAsString,
                binaryFormat
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
    public final JSONFormat globalIndent(int newGlobalIndent) {
        if (mutable) {
            globalIndent = newGlobalIndent;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newline,
                newGlobalIndent,
                indent,
                null,
                header,
                recordFormat,
                objectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                infinityAsString,
                binaryFormat
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
    public final JSONFormat indent(int newIndent) {
        if (mutable) {
            indent = newIndent;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newline,
                globalIndent,
                newIndent,
                null,
                header,
                recordFormat,
                objectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                infinityAsString,
                binaryFormat
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
     * Whether to emit a header row with column names, defaulting to
     * <code>true</code>.
     */
    @NotNull
    public final JSONFormat header(boolean newHeader) {
        if (mutable) {
            header = newHeader;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newline,
                globalIndent,
                indent,
                indented,
                newHeader,
                recordFormat,
                objectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                infinityAsString,
                binaryFormat
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
    @NotNull
    public final JSONFormat recordFormat(RecordFormat newRecordFormat) {
        if (mutable) {
            recordFormat = newRecordFormat;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                newRecordFormat,
                objectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                infinityAsString,
                binaryFormat
            );
    }

    /**
     * The record format to be applied, defaulting to
     * {@link RecordFormat#ARRAY}.
     */
    @NotNull
    public final RecordFormat recordFormat() {
        return recordFormat;
    }

    /**
     * The null format to be applied to objects, defaulting to
     * {@link NullFormat#NULL_ON_NULL}.
     */
    @NotNull
    public final JSONFormat objectNulls(NullFormat newObjectNulls) {
        if (mutable) {
            objectNulls = newObjectNulls;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                newObjectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                infinityAsString,
                binaryFormat
            );
    }

    /**
     * The null format to be applied to objects, defaulting to
     * {@link NullFormat#NULL_ON_NULL}.
     */
    @NotNull
    public final NullFormat objectNulls() {
        return objectNulls;
    }

    /**
     * The null format to be applied to arrays, defaulting to
     * {@link NullFormat#NULL_ON_NULL}.
     */
    @NotNull
    public final JSONFormat arrayNulls(NullFormat newArrayNulls) {
        if (mutable) {
            arrayNulls = newArrayNulls;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                objectNulls,
                newArrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                infinityAsString,
                binaryFormat
            );
    }

    /**
     * The null format to be applied to arrays, defaulting to
     * {@link NullFormat#NULL_ON_NULL}.
     */
    @NotNull
    public final NullFormat arrayNulls() {
        return arrayNulls;
    }

    /**
     * Whether to wrap single column records in the {@link #recordFormat()}.
     */
    @NotNull
    public final JSONFormat wrapSingleColumnRecords(boolean newWrapSingleColumnRecords) {
        if (mutable) {
            wrapSingleColumnRecords = newWrapSingleColumnRecords;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                objectNulls,
                arrayNulls,
                newWrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                infinityAsString,
                binaryFormat
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
    @NotNull
    public final JSONFormat quoteNested(boolean newQuoteNested) {
        if (mutable) {
            quoteNested = newQuoteNested;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                objectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                newQuoteNested,
                nanAsString,
                infinityAsString,
                binaryFormat
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
     * Whether {@link Double#NaN} and {@link Float#NaN} values should be
     * formatted as <code>"NaN"</code> strings. The default is to format them as
     * <code>null</code>, as JSON doesn't support <code>NaN</code> values.
     */
    @NotNull
    public final JSONFormat nanAsString(boolean newNanAsString) {
        if (mutable) {
            nanAsString = newNanAsString;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                objectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                newNanAsString,
                infinityAsString,
                binaryFormat
            );
    }

    /**
     * Whether {@link Double#NaN} and {@link Float#NaN} values should be
     * formatted as <code>"NaN"</code> strings. The default is to format them as
     * <code>null</code>, as JSON doesn't support <code>NaN</code> values.
     */
    public final boolean nanAsString() {
        return nanAsString;
    }

    /**
     * Whether {@link Double#POSITIVE_INFINITY},
     * {@link Double#NEGATIVE_INFINITY}, {@link Float#POSITIVE_INFINITY}, and
     * {@link Float#NEGATIVE_INFINITY} values should be formatted as
     * <code>"Infinity"</code> or <code>"-Infinity"</code> strings. The default
     * is to format them as <code>null</code>, as JSON doesn't support
     * <code>Infinity</code> values.
     */
    @NotNull
    public final JSONFormat infinityAsString(boolean newInfinityAsString) {
        if (mutable) {
            infinityAsString = newInfinityAsString;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                objectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                newInfinityAsString,
                binaryFormat
            );
    }

    /**
     * Whether {@link Double#POSITIVE_INFINITY},
     * {@link Double#NEGATIVE_INFINITY}, {@link Float#POSITIVE_INFINITY}, and
     * {@link Float#NEGATIVE_INFINITY} values should be formatted as
     * <code>"Infinity"</code> or <code>"-Infinity"</code> strings. The default
     * is to format them as <code>null</code>, as JSON doesn't support
     * <code>Infinity</code> values.
     */
    public final boolean infinityAsString() {
        return infinityAsString;
    }

    /**
     * The {@link BinaryFormat} to use when formatting binary data.
     */
    @NotNull
    public final JSONFormat binaryFormat(BinaryFormat newBinaryFormat) {
        if (mutable) {
            binaryFormat = newBinaryFormat;
            return this;
        }
        else
            return new JSONFormat(
                mutable,



                format,
                newline,
                globalIndent,
                indent,
                indented,
                header,
                recordFormat,
                objectNulls,
                arrayNulls,
                wrapSingleColumnRecords,
                quoteNested,
                nanAsString,
                infinityAsString,
                newBinaryFormat
            );
    }

    /**
     * The {@link BinaryFormat} to use when formatting binary data.
     */
    public final BinaryFormat binaryFormat() {
        return binaryFormat;
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
         * preferably with {@link JSONFormat#header()} equal to
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

    /**
     * The format of <code>null</code> values in JSON objects or arrays.
     */
    public enum NullFormat {

        /**
         * A <code>null</code> value in source data is represented by an
         * explicit <code>null</code> value in the JSON object (the key is
         * present) or array.
         */
        NULL_ON_NULL,

        /**
         * A <code>null</code> value in source data is represented by an absent
         * value in the JSON object (the key is absent) or array.
         */
        ABSENT_ON_NULL,
    }

    /**
     * The format of binary values in JSON documents.
     */
    public enum BinaryFormat {

        /**
         * Binary values are formatted as {@link Base64} encoded strings.
         */
        BASE64,

        /**
         * Binary values are formatted as {@link HexFormat} encoded strings.
         */
        HEX
    }
}
