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

import java.io.OutputStream;
import java.io.Writer;
import java.sql.ResultSet;

import org.jooq.exception.IOException;

import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * A type that can format its contents.
 * <p>
 * This type provides a common interface for {@link Result} and {@link Cursor}
 * formatting functionality, which includes formatting results to:
 * <ul>
 * <li>Charts</li>
 * <li>CSV</li>
 * <li>HTML</li>
 * <li>INSERT statements</li>
 * <li>JSON</li>
 * <li>Text</li>
 * <li>XML</li>
 * </ul>
 * <p>
 * Calling formatting methods on a {@link Result} is repeatable as the
 * {@link Result} has been previously materialised in memory and the database
 * resource is closed. Calling these methods on a {@link Cursor} is not
 * repeatable as the {@link Cursor} (and the underlying JDBC {@link ResultSet})
 * is consumed entirely, and closed eagerly after consumption.
 *
 * @author Lukas Eder
 */
public interface Formattable {

    // -------------------------------------------------------------------------
    // Formatting methods
    // -------------------------------------------------------------------------

    /**
     * Get a simple formatted representation of this result.
     * <p>
     * This is the same as calling {@link #format(int)} with
     * <code>maxRows = Integer.MAX_VALUE</code>
     *
     * @return The formatted result
     */
    String format();

    /**
     * Get a simple formatted representation of this result.
     *
     * @param maxRecords The maximum number of records to include in the
     *            formatted result
     * @return The formatted result
     */
    String format(int maxRecords);

    /**
     * Get a simple formatted representation of this result.
     *
     * @param format The formatting information
     * @return The formatted result
     */
    String format(TXTFormat format);

    /**
     * Get a simple formatted representation of this result as HTML.
     * <p>
     * The HTML code is formatted as follows: <code><pre>
     * &lt;table&gt;
     *   &lt;thead&gt;
     *     &lt;tr&gt;
     *       &lt;th&gt;field-1&lt;/th&gt;
     *       &lt;th&gt;field-2&lt;/th&gt;
     *       ...
     *       &lt;th&gt;field-n&lt;/th&gt;
     *     &lt;/tr&gt;
     *   &lt;/thead&gt;
     *   &lt;tbody&gt;
     *     &lt;tr&gt;
     *       &lt;th&gt;value-1-1&lt;/th&gt;
     *       &lt;th&gt;value-1-2&lt;/th&gt;
     *       ...
     *       &lt;th&gt;value-1-n&lt;/th&gt;
     *     &lt;/tr&gt;
     *     &lt;tr&gt;
     *       &lt;th&gt;value-2-1&lt;/th&gt;
     *       &lt;th&gt;value-2-2&lt;/th&gt;
     *       ...
     *       &lt;th&gt;value-2-n&lt;/th&gt;
     *     &lt;/tr&gt;
     *     ...
     *   &lt;/tbody&gt;
     * &lt;/table&gt;
     * </pre></code>
     *
     * @return The formatted result
     */
    String formatHTML();

    /**
     * Get a simple formatted representation of this result as CSV.
     * <p>
     * This is the same as calling <code>formatCSV(true, ',', "")</code>
     *
     * @return The formatted result
     */
    String formatCSV();

    /**
     * Get a simple formatted representation of this result as CSV.
     * <p>
     * This is the same as calling <code>formatCSV(true, delimiter, "")</code>
     *
     * @param delimiter The delimiter to use between records
     * @return The formatted result
     */
    String formatCSV(char delimiter);

    /**
     * Get a simple formatted representation of this result as CSV.
     * <p>
     * This is the same as calling <code>formatCSV(true, delimiter, nullString)</code>
     *
     * @param delimiter The delimiter to use between records
     * @param nullString A special string for encoding <code>NULL</code> values.
     * @return The formatted result
     */
    String formatCSV(char delimiter, String nullString);

    /**
     * Get a simple formatted representation of this result as CSV.
     * <p>
     * This is the same as calling <code>formatCSV(',', "")</code>
     *
     * @param header Whether to emit a CSV header line
     * @return The formatted result
     */
    String formatCSV(boolean header);

    /**
     * Get a simple formatted representation of this result as CSV.
     * <p>
     * This is the same as calling <code>formatCSV(delimiter, "")</code>
     *
     * @param header Whether to emit a CSV header line
     * @param delimiter The delimiter to use between records
     * @return The formatted result
     */
    String formatCSV(boolean header, char delimiter);

    /**
     * Get a simple formatted representation of this result as CSV.
     *
     * @param header Whether to emit a CSV header line
     * @param delimiter The delimiter to use between records
     * @param nullString A special string for encoding <code>NULL</code> values.
     * @return The formatted result
     */
    String formatCSV(boolean header, char delimiter, String nullString);

    /**
     * Get a simple formatted representation of this result as CSV.
     *
     * @return The formatted result
     */
    String formatCSV(CSVFormat format);

    /**
     * Get a simple formatted representation of this result as a JSON array of
     * array.
     * <p>
     * The format is the following: <code><pre>
     * {"fields":[{"name":"field-1","type":"type-1"},
     *            {"name":"field-2","type":"type-2"},
     *            ...,
     *            {"name":"field-n","type":"type-n"}],
     *  "records":[[value-1-1,value-1-2,...,value-1-n],
     *             [value-2-1,value-2-2,...,value-2-n]]}
     * </pre></code>
     *
     * @return The formatted result
     */
    String formatJSON();

    /**
     * Get a simple formatted representation of this result as a JSON data
     * structure, according to the format.
     *
     * @return The formatted result
     * @see JSONFormat
     */
    String formatJSON(JSONFormat format);

    /**
     * Get this result formatted as XML.
     *
     * @see <a
     *      href="http://www.jooq.org/xsd/jooq-export-3.10.0.xsd">http://www.jooq.org/xsd/jooq-export-3.10.0.xsd</a>
     */
    String formatXML();

    /**
     * Get this result formatted as XML.
     *
     * @see <a
     *      href="http://www.jooq.org/xsd/jooq-export-3.10.0.xsd">http://www.jooq.org/xsd/jooq-export-3.10.0.xsd</a>
     */
    String formatXML(XMLFormat format);

    String formatChart();

    String formatChart(ChartFormat format);

    /**
     * Get this result as a set of <code>INSERT</code> statements.
     * <p>
     * This uses the the first record's {@link TableRecord#getTable()}, if the
     * first record is a {@link TableRecord}. Otherwise, this generates
     * <code>INSERT</code> statements into an <code>"UNKNOWN_TABLE"</code>. In
     * both cases, the {@link Result#fields()} are used for column names.
     */
    String formatInsert();

    /**
     * Get this result as a set of <code>INSERT</code> statements.
     * <p>
     * This explicitly specifies the table (and optionally the fields) to insert
     * into. If the <code>fields</code> argument is left empty, the
     * {@link Result#fields()} are used, instead.
     */
    String formatInsert(Table<?> table, Field<?>... fields);

    /**
     * Like {@link #format()}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void format(OutputStream stream) throws IOException;

    /**
     * Like {@link #format(int)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void format(OutputStream stream, int maxRecords) throws IOException;

    /**
     * Like {@link #format(TXTFormat)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void format(OutputStream stream, TXTFormat format) throws IOException;

    /**
     * Like {@link #formatHTML()}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatHTML(OutputStream stream) throws IOException;

    /**
     * Like {@link #formatCSV()}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(OutputStream stream) throws IOException;

    /**
     * Like {@link #formatCSV(char)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(OutputStream stream, char delimiter) throws IOException;

    /**
     * Like {@link #formatCSV(char, String)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(OutputStream stream, char delimiter, String nullString) throws IOException;

    /**
     * Like {@link #formatCSV(boolean)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(OutputStream stream, boolean header) throws IOException;

    /**
     * Like {@link #formatCSV(boolean, char)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(OutputStream stream, boolean header, char delimiter) throws IOException;

    /**
     * Like {@link #formatCSV(boolean, char, String)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(OutputStream stream, boolean header, char delimiter, String nullString) throws IOException;

    /**
     * Like {@link #formatCSV(CSVFormat)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(OutputStream stream, CSVFormat format) throws IOException;

    /**
     * Like {@link #formatJSON()}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatJSON(OutputStream stream) throws IOException;

    /**
     * Like {@link #formatJSON(JSONFormat)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatJSON(OutputStream stream, JSONFormat format) throws IOException;

    /**
     * Like {@link #formatXML()}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatXML(OutputStream stream) throws IOException;

    /**
     * Like {@link #formatXML(XMLFormat)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatXML(OutputStream stream, XMLFormat format) throws IOException;

    /**
     * Like {@link #formatChart()}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatChart(OutputStream stream) throws IOException;

    /**
     * Like {@link #formatChart(ChartFormat)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatChart(OutputStream stream, ChartFormat format) throws IOException;

    /**
     * Like {@link #formatInsert()}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatInsert(OutputStream stream) throws IOException;

    /**
     * Like {@link #formatInsert(Table, Field...)}, but the data is output onto an {@link OutputStream}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatInsert(OutputStream stream, Table<?> table, Field<?>... fields) throws IOException;

    /**
     * Like {@link #format()}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void format(Writer writer) throws IOException;

    /**
     * Like {@link #format(int)}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void format(Writer writer, int maxRecords) throws IOException;

    /**
     * Like {@link #format(TXTFormat)}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void format(Writer writer, TXTFormat format) throws IOException;

    /**
     * Like {@link #formatHTML()}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatHTML(Writer writer) throws IOException;

    /**
     * Like {@link #formatCSV()}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(Writer writer) throws IOException;

    /**
     * Like {@link #formatCSV(char)}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(Writer writer, char delimiter) throws IOException;

    /**
     * Like {@link #formatCSV(char, String)}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(Writer writer, char delimiter, String nullString) throws IOException;

    /**
     * Like {@link #formatCSV(boolean)}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(Writer writer, boolean header) throws IOException;

    /**
     * Like {@link #formatCSV(boolean, char)}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(Writer writer, boolean header, char delimiter) throws IOException;

    /**
     * Like {@link #formatCSV(boolean, char, String)}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(Writer writer, boolean header, char delimiter, String nullString) throws IOException;

    /**
     * Like {@link #formatCSV(CSVFormat)}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatCSV(Writer writer, CSVFormat format) throws IOException;

    /**
     * Like {@link #formatJSON()}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatJSON(Writer writer) throws IOException;

    /**
     * Like {@link #formatJSON(JSONFormat)}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatJSON(Writer writer, JSONFormat format) throws IOException;

    /**
     * Like {@link #formatXML()}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatXML(Writer writer) throws IOException;

    /**
     * Like {@link #formatXML(XMLFormat)}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatXML(Writer writer, XMLFormat format) throws IOException;

    /**
     * Like {@link #formatChart()}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatChart(Writer writer) throws IOException;

    /**
     * Like {@link #formatChart(ChartFormat)}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatChart(Writer writer, ChartFormat format) throws IOException;

    /**
     * Like {@link #formatInsert()}, but the data is output onto a {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatInsert(Writer writer) throws IOException;

    /**
     * Like {@link #formatInsert(Table, Field...)}, but the data is output onto an {@link Writer}.
     *
     * @throws IOException - an unchecked wrapper for {@link java.io.IOException}, if anything goes wrong.
     */
    void formatInsert(Writer writer, Table<?> table, Field<?>... fields) throws IOException;

    /**
     * Get this result as XML.
     *
     * @see #formatXML()
     * @see <a
     *      href="http://www.jooq.org/xsd/jooq-export-3.10.0.xsd">http://www.jooq.org/xsd/jooq-export-3.10.0.xsd</a>
     */
    Document intoXML();

    /**
     * Get this result as XML.
     *
     * @see #formatXML()
     * @see <a
     *      href="http://www.jooq.org/xsd/jooq-export-3.10.0.xsd">http://www.jooq.org/xsd/jooq-export-3.10.0.xsd</a>
     */
    Document intoXML(XMLFormat format);

    /**
     * Get this result as XML using a SAX <code>ContentHandler</code>.
     *
     * @param handler The custom content handler.
     * @return The argument content handler is returned for convenience.
     * @see #formatXML()
     * @see <a
     *      href="http://www.jooq.org/xsd/jooq-export-3.10.0.xsd">http://www.jooq.org/xsd/jooq-export-3.10.0.xsd</a>
     */
    <H extends ContentHandler> H intoXML(H handler) throws SAXException;

    /**
     * Get this result as XML using a SAX <code>ContentHandler</code>.
     *
     * @param handler The custom content handler.
     * @return The argument content handler is returned for convenience.
     * @see #formatXML()
     * @see <a
     *      href="http://www.jooq.org/xsd/jooq-export-3.10.0.xsd">http://www.jooq.org/xsd/jooq-export-3.10.0.xsd</a>
     */
    <H extends ContentHandler> H intoXML(H handler, XMLFormat format) throws SAXException;

}
