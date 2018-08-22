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
package org.jooq.impl;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

import org.jooq.CSVFormat;
import org.jooq.ChartFormat;
import org.jooq.Field;
import org.jooq.Formattable;
import org.jooq.JSONFormat;
import org.jooq.TXTFormat;
import org.jooq.Table;
import org.jooq.XMLFormat;

import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * A common base implementation for {@link Formattable}, implementing all the
 * various convenience overloads.
 *
 * @author Lukas Eder
 */
abstract class AbstractFormattable implements Formattable, Serializable {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 8634798716074039885L;

    @Override
    public final String format() {
        return format(TXTFormat.DEFAULT);
    }

    @Override
    public final String format(int maxRecords) {
        return format(TXTFormat.DEFAULT.maxRows(maxRecords));
    }

    @Override
    public final String format(TXTFormat format) {
        StringWriter writer = new StringWriter();
        format(writer, format);
        return writer.toString();
    }

    @Override
    public final void format(OutputStream stream) {
        format(new OutputStreamWriter(stream));
    }

    @Override
    public final void format(OutputStream stream, int maxRecords) {
        format(new OutputStreamWriter(stream), maxRecords);
    }

    @Override
    public final void format(OutputStream stream, TXTFormat format) {
        format(new OutputStreamWriter(stream), format);
    }

    @Override
    public final void format(Writer writer) {
        format(writer, TXTFormat.DEFAULT);
    }

    @Override
    public final void format(Writer writer, int maxRecords) {
        format(writer, TXTFormat.DEFAULT.maxRows(maxRecords));
    }

    @Override
    public final String formatCSV() {
        return formatCSV(true);
    }

    @Override
    public final String formatCSV(boolean header) {
        StringWriter writer = new StringWriter();
        formatCSV(writer, header);
        return writer.toString();
    }

    @Override
    public final void formatCSV(OutputStream stream) {
        formatCSV(stream, true);
    }

    @Override
    public final void formatCSV(OutputStream stream, boolean header) {
        formatCSV(new OutputStreamWriter(stream), header);
    }

    @Override
    public final void formatCSV(Writer writer) {
        formatCSV(writer, true);
    }

    @Override
    public final void formatCSV(Writer writer, boolean header) {
        formatCSV(writer, header, ',', "\"\"");
    }

    @Override
    public final String formatCSV(char delimiter) {
        return formatCSV(true, delimiter);
    }

    @Override
    public final String formatCSV(boolean header, char delimiter) {
        StringWriter writer = new StringWriter();
        formatCSV(writer, delimiter);
        return writer.toString();
    }

    @Override
    public final void formatCSV(OutputStream stream, char delimiter) {
        formatCSV(stream, true, delimiter);
    }

    @Override
    public final void formatCSV(OutputStream stream, boolean header, char delimiter) {
        formatCSV(new OutputStreamWriter(stream), delimiter);
    }

    @Override
    public final void formatCSV(Writer writer, char delimiter) {
        formatCSV(writer, true, delimiter);
    }

    @Override
    public final void formatCSV(Writer writer, boolean header, char delimiter) {
        formatCSV(writer, header, delimiter, "\"\"");
    }

    @Override
    public final String formatCSV(char delimiter, String nullString) {
        return formatCSV(true, delimiter, nullString);
    }

    @Override
    public final String formatCSV(boolean header, char delimiter, String nullString) {
        StringWriter writer = new StringWriter();
        formatCSV(writer, header, delimiter, nullString);
        return writer.toString();
    }

    @Override
    public final String formatCSV(CSVFormat format) {
        StringWriter writer = new StringWriter();
        formatCSV(writer, format);
        return writer.toString();
    }

    @Override
    public final void formatCSV(OutputStream stream, char delimiter, String nullString) {
        formatCSV(stream, true, delimiter, nullString);
    }

    @Override
    public final void formatCSV(OutputStream stream, boolean header, char delimiter, String nullString) {
        formatCSV(new OutputStreamWriter(stream), header, delimiter, nullString);
    }

    @Override
    public final void formatCSV(OutputStream stream, CSVFormat format) {
        formatCSV(new OutputStreamWriter(stream), format);
    }

    @Override
    public final void formatCSV(Writer writer, char delimiter, String nullString) {
        formatCSV(writer, true, delimiter, nullString);
    }

    @Override
    public final void formatCSV(Writer writer, boolean header, char delimiter, String nullString) {
        formatCSV(writer, new CSVFormat().header(header).delimiter(delimiter).nullString(nullString));
    }

    @Override
    public final String formatJSON() {
        StringWriter writer = new StringWriter();
        formatJSON(writer);
        return writer.toString();
    }

    @Override
    public final String formatJSON(JSONFormat format) {
        StringWriter writer = new StringWriter();
        formatJSON(writer, format);
        return writer.toString();
    }

    @Override
    public final void formatJSON(OutputStream stream) {
        formatJSON(new OutputStreamWriter(stream));
    }

    @Override
    public final void formatJSON(OutputStream stream, JSONFormat format) {
        formatJSON(new OutputStreamWriter(stream), format);
    }

    @Override
    public final void formatJSON(Writer writer) {
        formatJSON(writer, JSONFormat.DEFAULT_FOR_RESULTS);
    }

    @Override
    public final String formatXML() {
        return formatXML(XMLFormat.DEFAULT_FOR_RESULTS);
    }

    @Override
    public final String formatXML(XMLFormat format) {
        StringWriter writer = new StringWriter();
        formatXML(writer, format);
        return writer.toString();
    }

    @Override
    public final void formatXML(OutputStream stream) {
        formatXML(stream, XMLFormat.DEFAULT_FOR_RESULTS);
    }

    @Override
    public final void formatXML(OutputStream stream, XMLFormat format) {
        formatXML(new OutputStreamWriter(stream), format);
    }

    @Override
    public final void formatXML(Writer writer) {
        formatXML(writer, XMLFormat.DEFAULT_FOR_RESULTS);
    }

    @Override
    public final String formatChart() {
        StringWriter writer = new StringWriter();
        formatChart(writer);
        return writer.toString();
    }

    @Override
    public final String formatChart(ChartFormat format) {
        StringWriter writer = new StringWriter();
        formatChart(writer, format);
        return writer.toString();
    }

    @Override
    public final void formatChart(OutputStream stream) {
        formatChart(new OutputStreamWriter(stream));
    }

    @Override
    public final void formatChart(OutputStream stream, ChartFormat format) {
        formatChart(new OutputStreamWriter(stream), format);
    }

    @Override
    public final void formatChart(Writer writer) {
        formatChart(writer, ChartFormat.DEFAULT);
    }

    @Override
    public final String formatInsert() {
        StringWriter writer = new StringWriter();
        formatInsert(writer);
        return writer.toString();
    }

    @Override
    public final void formatInsert(OutputStream stream) {
        formatInsert(new OutputStreamWriter(stream));
    }

    @Override
    public final String formatInsert(Table<?> table, Field<?>... f) {
        StringWriter writer = new StringWriter();
        formatInsert(writer, table, f);
        return writer.toString();
    }

    @Override
    public final void formatInsert(OutputStream stream, Table<?> table, Field<?>... f) {
        formatInsert(new OutputStreamWriter(stream), table, f);
    }

    @Override
    public final String formatHTML() {
        StringWriter writer = new StringWriter();
        formatHTML(writer);
        return writer.toString();
    }

    @Override
    public final void formatHTML(OutputStream stream) {
        formatHTML(new OutputStreamWriter(stream));
    }

    @Override
    public final Document intoXML() {
        return intoXML(XMLFormat.DEFAULT_FOR_RESULTS);
    }

    @Override
    public final <H extends ContentHandler> H intoXML(H handler) throws SAXException {
        return intoXML(handler, XMLFormat.DEFAULT_FOR_RESULTS);
    }
}
