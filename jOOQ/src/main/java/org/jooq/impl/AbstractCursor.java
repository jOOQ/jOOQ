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

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.jooq.XMLFormat.RecordFormat.COLUMN_NAME_ELEMENTS;
import static org.jooq.XMLFormat.RecordFormat.VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE;
import static org.jooq.conf.SettingsTools.renderLocale;
import static org.jooq.impl.DSL.insertInto;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.tools.StringUtils.abbreviate;
import static org.jooq.tools.StringUtils.leftPad;
import static org.jooq.tools.StringUtils.rightPad;

import java.io.Writer;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jooq.CSVFormat;
import org.jooq.ChartFormat;
import org.jooq.ChartFormat.Display;
import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.Formattable;
import org.jooq.JSONFormat;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Schema;
import org.jooq.TXTFormat;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.XMLFormat;
import org.jooq.exception.IOException;
import org.jooq.tools.StringUtils;
import org.jooq.tools.json.JSONValue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author Lukas Eder
 */
abstract class AbstractCursor<R extends Record> extends AbstractFormattable implements Iterable<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -3412555195899758746L;

    final Fields<R>           fields;
    Configuration             configuration;

    AbstractCursor(Configuration configuration, Fields<R> fields) {
        this.configuration = configuration;
        this.fields = fields;
    }

    @Override
    public final void format(Writer writer, TXTFormat format) {
        try {

            // Numeric columns have greater max width because values are aligned
            final int NUM_COL_MAX_WIDTH = format.maxColWidth() == Integer.MAX_VALUE ? Integer.MAX_VALUE : 2 * format.maxColWidth();

            // The max number of records that will be considered for formatting purposes
            final int MAX_RECORDS = min(50, format.maxRows());
            final Deque<R> buffer = new ArrayDeque<R>();
            final Iterator<R> it = iterator();

            // Buffer some rows for formatting purposes
            for (int i = 0; i < MAX_RECORDS && it.hasNext(); i++)
                buffer.offer(it.next());

            // Get max decimal places for numeric type columns
            final int[] decimalPlaces = new int[fields.fields.length];
            final int[] widths = new int[fields.fields.length];

            for (int index = 0; index < fields.fields.length; index++) {
                if (Number.class.isAssignableFrom(fields.fields[index].getType())) {
                    List<Integer> decimalPlacesList = new ArrayList<Integer>(1 + buffer.size());

                    // Initialize
                    decimalPlacesList.add(0);

                    // Collect all decimal places for the column values
                    for (R record : buffer)
                        decimalPlacesList.add(decimalPlaces(format0(record.get(index), record.changed(index), true)));

                    // Find max
                    decimalPlaces[index] = Collections.max(decimalPlacesList);
                }
            }

            // Get max column widths
            int colMaxWidth;
            for (int index = 0; index < fields.fields.length; index++) {

                // Is number column?
                boolean isNumCol = Number.class.isAssignableFrom(fields.fields[index].getType());

                colMaxWidth = isNumCol ? NUM_COL_MAX_WIDTH : format.maxColWidth();

                // Collect all widths for the column
                List<Integer> widthList = new ArrayList<Integer>(1 + buffer.size());

                // Add column name width first
                widthList.add(min(colMaxWidth, max(format.minColWidth(), fields.fields[index].getName().length())));

                // Add column values width
                for (R record : buffer) {
                    String value = format0(record.get(index), record.changed(index), true);

                    // Align number values before width is calculated
                    if (isNumCol)
                        value = alignNumberValue(decimalPlaces[index], value);

                    widthList.add(min(colMaxWidth, value.length()));
                }

                // Find max
                widths[index] = Collections.max(widthList);
            }

            // Begin the writing
            // ---------------------------------------------------------------------

            // Write top line
            if (format.horizontalTableBorder())
                formatHorizontalLine(writer, format, widths);

            // Write headers
            if (format.verticalTableBorder())
                writer.append('|');

            for (int index = 0; index < fields.fields.length; index++) {
                if (index > 0)
                    if (format.verticalCellBorder())
                        writer.append('|');
                    else
                        writer.append(' ');

                String padded;

                if (Number.class.isAssignableFrom(fields.fields[index].getType()))
                    padded = leftPad(fields.fields[index].getName(), widths[index]);
                else
                    padded = rightPad(fields.fields[index].getName(), widths[index]);

                writer.append(abbreviate(padded, widths[index]));
            }

            if (format.verticalTableBorder())
                writer.append('|');

            writer.append('\n');

            // Write separator
            if (format.horizontalHeaderBorder())
                formatHorizontalLine(writer, format, widths);

            // Write records
            int i;

            recordLoop:
            for (i = 0; i < format.maxRows(); i++) {
                R record = buffer.pollFirst();

                if (record == null)
                    if (it.hasNext())
                        record = it.next();
                    else
                        break recordLoop;

                // Write separator
                if (i > 0 && format.horizontalCellBorder())
                    formatHorizontalLine(writer, format, widths);

                if (format.verticalTableBorder())
                    writer.append('|');

                for (int index = 0; index < fields.fields.length; index++) {
                    if (index > 0)
                        if (format.verticalCellBorder())
                            writer.append('|');
                        else
                            writer.append(' ');

                    String value =
                        StringUtils.replace(
                            StringUtils.replace(
                                StringUtils.replace(
                                    format0(record.get(index), record.changed(index), true), "\n", "{lf}"
                                ), "\r", "{cr}"
                            ), "\t", "{tab}"
                        );

                    String padded;
                    if (Number.class.isAssignableFrom(fields.fields[index].getType())) {
                        // Align number value before left pad
                        value = alignNumberValue(decimalPlaces[index], value);

                        // Left pad
                        padded = leftPad(value, widths[index]);
                    }
                    else {
                        // Right pad
                        padded = rightPad(value, widths[index]);
                    }

                    writer.append(abbreviate(padded, widths[index]));
                }

                if (format.verticalTableBorder())
                    writer.append('|');

                writer.append('\n');
            }

            // Write bottom line
            if (format.horizontalTableBorder() && i > 0)
                formatHorizontalLine(writer, format, widths);

            // Write truncation message, if applicable
            if (it.hasNext()) {
                if (format.verticalTableBorder())
                    writer.append('|');

                writer.append("...record(s) truncated...\n");
            }

            writer.flush();
        }
        catch (java.io.IOException e) {
            throw new IOException("Exception while writing TEXT", e);
        }
    }

    private final void formatHorizontalLine(Writer writer, TXTFormat format, final int[] widths) throws java.io.IOException {
        if (format.verticalTableBorder())
            if (format.intersectLines())
                writer.append('+');
            else
                writer.append('-');

        for (int index = 0; index < fields.fields.length; index++) {
            if (index > 0)
                if (format.verticalCellBorder())
                    if (format.intersectLines())
                        writer.append('+');
                    else
                        writer.append('-');
                else
                    writer.append(' ');

            writer.append(rightPad("", widths[index], "-"));
        }

        if (format.verticalTableBorder())
            if (format.intersectLines())
                writer.append('+');
            else
                writer.append('-');

        writer.append('\n');
    }

    private static final String alignNumberValue(Integer columnDecimalPlaces, String value) {
        if (!"{null}".equals(value) && columnDecimalPlaces != 0) {
            int decimalPlaces = decimalPlaces(value);
            int rightPadSize = value.length() + columnDecimalPlaces - decimalPlaces;

            if (decimalPlaces == 0) {
                // If integer value, add one for decimal point
                value = rightPad(value, rightPadSize + 1);
            }
            else {
                value = rightPad(value, rightPadSize);
            }
        }

        return value;
    }

    private static final int decimalPlaces(String value) {
        int decimalPlaces = 0;

        int dotIndex = value.indexOf(".");
        if (dotIndex != -1)
            decimalPlaces = value.length() - dotIndex - 1;

        return decimalPlaces;
    }

    @Override
    public final void formatCSV(Writer writer, CSVFormat format) {
        try {
            if (format.header()) {
                String sep1 = "";
                for (Field<?> field : fields.fields) {
                    writer.append(sep1);
                    writer.append(formatCSV0(field.getName(), format));

                    sep1 = format.delimiter();
                }

                writer.append(format.newline());
            }

            for (Record record : this) {
                String sep2 = "";

                for (int index = 0; index < fields.fields.length; index++) {
                    writer.append(sep2);
                    writer.append(formatCSV0(record.getValue(index), format));

                    sep2 = format.delimiter();
                }

                writer.append(format.newline());
            }

            writer.flush();
        }
        catch (java.io.IOException e) {
            throw new IOException("Exception while writing CSV", e);
        }
    }

    private final String formatCSV0(Object value, CSVFormat format) {

        // [#2741] TODO: This logic will be externalised in new SPI
        // [#4746] Escape null and empty strings
        if (value == null)
            return format.nullString();

        if ("".equals(value.toString()))
            return format.emptyString();

        String result = format0(value, false, false);
        switch (format.quote()) {
            case NEVER:
                return result;

            case SPECIAL_CHARACTERS:
                if (!StringUtils.containsAny(result, ',', ';', '\t', '"', '\n', '\r', '\'', '\\'))
                    return result;

                // no break
            case ALWAYS:
            default:
                return format.quoteString()
                     + StringUtils.replace(
                           StringUtils.replace(
                               result, "\\", "\\\\"
                           ), format.quoteString(), format.quoteString() + format.quoteString()
                       )
                     + format.quoteString();
        }
    }

    @Override
    public final void formatJSON(Writer writer, JSONFormat format) {
        if (format == null)
            format = JSONFormat.DEFAULT_FOR_RESULTS;

        try {
            String separator;
            int recordLevel = format.header() ? 2 : 1;

            if (format.header()) {
                if (format.format())
                    writer.append('{').append(format.newline())
                          .append(format.indentString(1)).append("\"fields\": [");
                else
                    writer.append("{\"fields\":[");

                separator = "";

                for (Field<?> field : fields.fields) {
                    writer.append(separator);

                    if (format.format())
                        writer.append(format.newline()).append(format.indentString(2));

                    writer.append('{');

                    if (format.format())
                        writer.append(format.newline()).append(format.indentString(3));

                    if (field instanceof TableField) {
                        Table<?> table = ((TableField<?, ?>) field).getTable();

                        if (table != null) {
                            Schema schema = table.getSchema();

                            if (schema != null) {
                                writer.append("\"schema\":");

                                if (format.format())
                                    writer.append(' ');

                                JSONValue.writeJSONString(schema.getName(), writer);
                                writer.append(',');

                                if (format.format())
                                    writer.append(format.newline()).append(format.indentString(3));
                            }

                            writer.append("\"table\":");

                            if (format.format())
                                writer.append(' ');

                            JSONValue.writeJSONString(table.getName(), writer);
                            writer.append(',');

                            if (format.format())
                                writer.append(format.newline()).append(format.indentString(3));
                        }
                    }

                    writer.append("\"name\":");

                    if (format.format())
                        writer.append(' ');

                    JSONValue.writeJSONString(field.getName(), writer);
                    writer.append(',');

                    if (format.format())
                        writer.append(format.newline()).append(format.indentString(3));

                    writer.append("\"type\":");

                    if (format.format())
                        writer.append(' ');

                    JSONValue.writeJSONString(field.getDataType().getTypeName().toUpperCase(renderLocale(configuration.settings())), writer);

                    if (format.format())
                        writer.append(format.newline()).append(format.indentString(2));

                    writer.append('}');
                    separator = ",";
                }

                if (format.format())
                    writer.append(format.newline()).append(format.indentString(1)).append("],")
                          .append(format.newline()).append(format.indentString(1)).append("\"records\": ");
                else
                    writer.append("],\"records\":");
            }

            writer.append('[');
            separator = "";

            switch (format.recordFormat()) {
                case ARRAY:
                    for (Record record : this) {
                        writer.append(separator);

                        if (format.format())
                            writer.append(format.newline());

                        formatJSONArray0(record, fields, format, recordLevel, writer);
                        separator = ",";
                    }

                    break;
                case OBJECT:
                    for (Record record : this) {
                        writer.append(separator);

                        if (format.format())
                            writer.append(format.newline());

                        formatJSONMap0(record, fields, format, recordLevel, writer);
                        separator = ",";
                    }

                    break;
                default:
                    throw new IllegalArgumentException("Format not supported: " + format);
            }

            if (format.format()) {
                writer.append(format.newline());

                if (format.header())
                    writer.append(format.indentString(1));
            }

            writer.append(']');

            if (format.header())
                writer.append(format.newline()).append('}');

            writer.flush();
        }
        catch (java.io.IOException e) {
            throw new IOException("Exception while writing JSON", e);
        }
    }

    private static final void formatJSON0(Object value, Writer writer, JSONFormat format) throws java.io.IOException {

        // [#2741] TODO: This logic will be externalised in new SPI
        if (value instanceof byte[]) {
            JSONValue.writeJSONString(DatatypeConverter.printBase64Binary((byte[]) value), writer);
        }

        // [#6563] Arrays can be serialised natively in JSON
        else if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            writer.append('[');

            for (int i = 0; i < array.length; i++) {
                if (i > 0)
                    writer.append(',');

                formatJSON0(array[i], writer, format);
            }

            writer.append(']');
        }

        // [#7782] Nested records should generate nested JSON data structures
        else if (value instanceof Formattable) {
            ((Formattable) value).formatJSON(writer, format);
        }

        else {
            JSONValue.writeJSONString(value, writer);
        }
    }

    static final void formatJSONMap0(Record record, Fields<?> fields, JSONFormat format, int recordLevel, Writer writer) throws java.io.IOException {
        String separator = "";

        if (format.format())
            writer.append(format.indentString(recordLevel)).append('{');
        else
            writer.append('{');

        for (int index = 0; index < fields.fields.length; index++) {
            writer.append(separator);

            if (format.format())
                writer.append(format.newline()).append(format.indentString(recordLevel + 1));

            JSONValue.writeJSONString(record.field(index).getName(), writer);
            writer.append(':');
            if (format.format())
                writer.append(' ');

            formatJSON0(record.get(index), writer, format);
            separator = ",";
        }

        if (format.format())
            writer.append(format.newline()).append(format.indentString(recordLevel));

        writer.append('}');
    }

    static final void formatJSONArray0(Record record, Fields<?> fields, JSONFormat format, int recordLevel, Writer writer) throws java.io.IOException {
        String separator = "";

        if (format.format())
            writer.append(format.indentString(recordLevel)).append('[');
        else
            writer.append('[');

        for (int index = 0; index < fields.fields.length; index++) {
            writer.append(separator);

            if (format.format())
                writer.append(format.newline()).append(format.indentString(recordLevel + 1));

            formatJSON0(record.get(index), writer, format);
            separator = ",";
        }

        if (format.format())
            writer.append(format.newline()).append(format.indentString(recordLevel));

        writer.append(']');
    }

    @Override
    public final void formatXML(Writer writer, XMLFormat format) {
        if (format == null)
            format = XMLFormat.DEFAULT_FOR_RESULTS;

        String newline = format.newline();
        int recordLevel = format.header() ? 2 : 1;

        try {
            writer.append("<result");
            if (format.xmlns())
                writer.append(" xmlns=\"" + Constants.NS_EXPORT + "\"");
            writer.append(">");

            if (format.header()) {
                writer.append(newline).append(format.indentString(1)).append("<fields>");

                for (Field<?> field : fields.fields) {
                    writer.append(newline).append(format.indentString(2)).append("<field");

                    if (field instanceof TableField) {
                        Table<?> table = ((TableField<?, ?>) field).getTable();

                        if (table != null) {
                            Schema schema = table.getSchema();

                            if (schema != null) {
                                writer.append(" schema=\"");
                                writer.append(escapeXML(schema.getName()));
                                writer.append("\"");
                            }

                            writer.append(" table=\"");
                            writer.append(escapeXML(table.getName()));
                            writer.append("\"");
                        }
                    }

                    writer.append(" name=\"");
                    writer.append(escapeXML(field.getName()));
                    writer.append("\"");
                    writer.append(" type=\"");
                    writer.append(field.getDataType().getTypeName().toUpperCase(renderLocale(configuration.settings())));
                    writer.append("\"/>");
                }

                writer.append(newline).append(format.indentString(1)).append("</fields>");
                writer.append(newline).append(format.indentString(1)).append("<records>");
            }

            for (Record record : this) {
                writer.append(newline).append(format.indentString(recordLevel));
                formatXMLRecord(writer, format, recordLevel, record, fields);
            }

            if (format.header())
                writer.append(newline).append(format.indentString(1)).append("</records>");

            writer.append(newline).append("</result>");
            writer.flush();
        }
        catch (java.io.IOException e) {
            throw new IOException("Exception while writing XML", e);
        }
    }

    static final void formatXMLRecord(
        Writer writer,
        XMLFormat format,
        int recordLevel,
        Record record,
        Fields<?> fields
    )
    throws java.io.IOException {
        String newline = format.newline();

        writer.append("<record");
        if (format.xmlns())
            writer.append(" xmlns=\"" + Constants.NS_EXPORT + "\"");
        writer.append(">");

        for (int index = 0; index < fields.fields.length; index++) {
            Object value = record.get(index);

            writer.append(newline).append(format.indentString(recordLevel + 1));
            String tag = format.recordFormat() == COLUMN_NAME_ELEMENTS
                ? escapeXML(fields.fields[index].getName())
                : "value";

            writer.append("<" + tag);
            if (format.recordFormat() == VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE) {
                writer.append(" field=\"");
                writer.append(escapeXML(fields.fields[index].getName()));
                writer.append("\"");
            }

            if (value == null) {
                writer.append("/>");
            }
            else {
                writer.append(">");

                if (value instanceof Formattable)
                    ((Formattable) value).formatXML(writer, format);
                else
                    writer.append(escapeXML(format0(value, false, false)));

                writer.append("</" + tag + ">");
            }
        }

        writer.append(newline).append(format.indentString(recordLevel)).append("</record>");
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void formatChart(Writer writer, ChartFormat format) {
        Result<R> result;

        if (this instanceof Result)
            result = (Result<R>) this;
        else if (this instanceof Cursor)
            result = ((Cursor<R>) this).fetch();
        else
            throw new IllegalStateException();

        try {
            DSLContext ctx = configuration.dsl();
            Field<?> category = fields.field(format.category());
            TreeMap<Object, Result<R>> groups = new TreeMap<Object, Result<R>>(result.intoGroups(format.category()));

            if (!format.categoryAsText()) {
                if (Date.class.isAssignableFrom(category.getType())) {
                    Date categoryMin = (Date) groups.firstKey();
                    Date categoryMax = (Date) groups.lastKey();

                    for (Date i = categoryMin; i.before(categoryMax); i = new Date(i.getYear(), i.getMonth(), i.getDate() + 1))
                        if (!groups.containsKey(i))
                            groups.put(i, (Result<R>) ctx.newResult(fields.fields));
                }
            }

            List<?> categories = new ArrayList<Object>(groups.keySet());

            int categoryPadding = 1;
            int categoryWidth = 0;
            for (Object o : categories)
                categoryWidth = Math.max(categoryWidth, ("" + o).length());

            double axisMin = Double.POSITIVE_INFINITY;
            double axisMax = Double.NEGATIVE_INFINITY;

            for (Result<R> values : groups.values()) {
                double sum = 0;

                for (int i = 0; i < format.values().length; i++) {
                    if (format.display() == Display.DEFAULT)
                        sum = 0;

                    for (Record r : values)
                        sum = sum + r.get(format.values()[i], double.class);

                    if (sum < axisMin)
                        axisMin = sum;

                    if (sum > axisMax)
                        axisMax = sum;
                }
            }

            int verticalLegendWidth = format.showVerticalLegend()
                ? Math.max(
                      format.numericFormat().format(axisMin).length(),
                      format.numericFormat().format(axisMax).length()
                  )
                : 0;

            int horizontalLegendHeight = format.showHorizontalLegend() ? 1 : 0;

            int verticalBorderWidth = format.showVerticalLegend() ? 1 : 0;
            int horizontalBorderHeight = format.showHorizontalLegend() ? 1 : 0;

            int chartHeight = format.height() - horizontalLegendHeight - horizontalBorderHeight;
            int chartWidth = format.width() - verticalLegendWidth - verticalBorderWidth;

            double barWidth = (double) chartWidth / groups.size();
            double axisStep = (axisMax - axisMin) / (chartHeight - 1);

            for (int y = chartHeight - 1; y >= 0; y--) {
                double axisLegend = axisMax - (axisStep * (chartHeight - 1 - y));
                double axisLegendPercent = (axisLegend - axisMin) / (axisMax - axisMin);

                if (format.showVerticalLegend()) {
                    String axisLegendString = (format.display() == Display.HUNDRED_PERCENT_STACKED)
                        ? format.numericFormat().format(axisLegendPercent * 100.0) + "%"
                        : format.numericFormat().format(axisLegend);

                    for (int x = axisLegendString.length(); x < verticalLegendWidth; x++)
                        writer.write(' ');

                    writer.write(axisLegendString);

                    for (int x = 0; x < verticalBorderWidth; x++)
                        writer.write('|');
                }

                for (int x = 0; x < chartWidth; x++) {
                    int index = (int) (x / barWidth);

                    Result<R> group = groups.get(categories.get(index));
                    double[] values = new double[format.values().length];

                    for (Record record : group)
                        for (int i = 0; i < values.length; i++)
                            values[i] = values[i] + record.get(format.values()[i], double.class);

                    if (format.display() == Display.STACKED || format.display() == Display.HUNDRED_PERCENT_STACKED)
                        for (int i = 1; i < values.length; i++)
                            values[i] = values[i] + values[i - 1];

                    if (format.display() == Display.HUNDRED_PERCENT_STACKED)
                        for (int i = 0; i < values.length; i++)
                            values[i] = values[i] / values[values.length - 1];

                    int shadeIndex = -1;
                    for (int i = values.length - 1; i >= 0; i--)
                        if ((format.display() == Display.HUNDRED_PERCENT_STACKED ? axisLegendPercent : axisLegend) > values[i])
                            break;
                        else
                            shadeIndex = i;

                    if (shadeIndex == -1)
                        writer.write(' ');
                    else
                        writer.write(format.shades()[shadeIndex % format.shades().length]);
                }

                writer.write(format.newline());
            }

            if (format.showHorizontalLegend()) {
                for (int y = 0; y < horizontalBorderHeight; y++) {
                    if (format.showVerticalLegend()) {
                        for (int x = 0; x < verticalLegendWidth; x++)
                            writer.write('-');

                        for (int x = 0; x < verticalBorderWidth; x++)
                            writer.write('+');
                    }

                    for (int x = 0; x < chartWidth; x++)
                        writer.write('-');

                    writer.write(format.newline());
                }

                for (int y = 0; y < horizontalLegendHeight; y++) {
                    if (format.showVerticalLegend()) {
                        for (int x = 0; x < verticalLegendWidth; x++)
                            writer.write(' ');

                        for (int x = 0; x < verticalBorderWidth; x++)
                            writer.write('|');
                    }

                    double rounding = 0.0;
                    for (double x = 0.0; x < chartWidth;) {
                        String label = "" + categories.get((int) (x / barWidth));
                        int length = label.length();

                        double padding = Math.max(categoryPadding, (barWidth - length) / 2);

                        rounding = (rounding + padding - Math.floor(padding)) % 1;
                        x = x + (padding + rounding);
                        for (int i = 0; i < (int) (padding + rounding); i++)
                            writer.write(' ');

                        x = x + length;
                        if (x >= chartWidth)
                            break;
                        writer.write(label);

                        rounding = (rounding + padding - Math.floor(padding)) % 1;
                        x = x + (padding + rounding);
                        for (int i = 0; i < (int) (padding + rounding); i++)
                            writer.write(' ');
                    }

                    writer.write(format.newline());
                }
            }
        }
        catch (java.io.IOException e) {
            throw new IOException("Exception while writing Chart", e);
        }
    }

    @Override
    public final void formatInsert(Writer writer) {
        formatInsert(writer, null, fields.fields);
    }

    @Override
    public final void formatInsert(Writer writer, Table<?> table, Field<?>... f) {
        DSLContext ctx = configuration.dsl();

        try {
            for (R record : this) {
                if (table == null)
                    if (record instanceof TableRecord)
                        table = ((TableRecord<?>) record).getTable();
                    else
                        table = table(name("UNKNOWN_TABLE"));

                writer.append(ctx.renderInlined(insertInto(table, f).values(record.intoArray())))
                      .append(";\n");
            }

            writer.flush();
        }
        catch (java.io.IOException e) {
            throw new IOException("Exception while writing INSERTs", e);
        }
    }

    @Override
    public final void formatHTML(Writer writer) {
        try {
            writer.append("<table>");
            writer.append("<thead>");
            writer.append("<tr>");

            for (Field<?> field : fields.fields) {
                writer.append("<th>");
                writer.append(escapeXML(field.getName()));
                writer.append("</th>");
            }

            writer.append("</tr>");
            writer.append("</thead>");
            writer.append("<tbody>");

            for (Record record : this) {
                writer.append("<tr>");

                for (int index = 0; index < fields.fields.length; index++) {
                    writer.append("<td>");
                    writer.append(escapeXML(format0(record.getValue(index), false, true)));
                    writer.append("</td>");
                }

                writer.append("</tr>");
            }

            writer.append("</tbody>");
            writer.append("</table>");

            writer.flush();
        }
        catch (java.io.IOException e) {
            throw new IOException("Exception while writing HTML", e);
        }
    }

    @Override
    public final Document intoXML(XMLFormat format) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element eResult = document.createElement("result");

            if (format.xmlns())
                eResult.setAttribute("xmlns", Constants.NS_EXPORT);
            document.appendChild(eResult);

            Element eRecordParent = eResult;

            if (format.header()) {
                Element eFields = document.createElement("fields");
                eResult.appendChild(eFields);

                for (Field<?> field : fields.fields) {
                    Element eField = document.createElement("field");

                    if (field instanceof TableField<?, ?>) {
                        Table<?> table = ((TableField<?, ?>) field).getTable();

                        if (table != null) {
                            Schema schema = table.getSchema();

                            if (schema != null) {
                                eField.setAttribute("schema", schema.getName());
                            }

                            eField.setAttribute("table", table.getName());
                        }
                    }

                    eField.setAttribute("name", field.getName());
                    eField.setAttribute("type", field.getDataType().getTypeName().toUpperCase(renderLocale(configuration.settings())));
                    eFields.appendChild(eField);
                }

                Element eRecords = document.createElement("records");
                eResult.appendChild(eRecords);
                eRecordParent = eRecords;
            }

            for (Record record : this) {
                Element eRecord = document.createElement("record");
                eRecordParent.appendChild(eRecord);

                for (int index = 0; index < fields.fields.length; index++) {
                    Field<?> field = fields.fields[index];
                    Object value = record.get(index);

                    String tag = format.recordFormat() == COLUMN_NAME_ELEMENTS
                        ? escapeXML(fields.fields[index].getName())
                        : "value";

                    Element eValue = document.createElement(tag);

                    if (format.recordFormat() == VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE)
                        eValue.setAttribute("field", field.getName());
                    eRecord.appendChild(eValue);

                    if (value != null) {
                        eValue.setTextContent(format0(value, false, false));
                    }
                }
            }

            return document;
        }
        catch (ParserConfigurationException ignore) {
            throw new RuntimeException(ignore);
        }
    }

    @Override
    public final <H extends ContentHandler> H intoXML(H handler, XMLFormat format) throws SAXException {
        Attributes empty = new AttributesImpl();

        handler.startDocument();

        if (format.xmlns())
            handler.startPrefixMapping("", Constants.NS_EXPORT);

        handler.startElement("", "", "result", empty);
        if (format.header()) {
            handler.startElement("", "", "fields", empty);

            for (Field<?> field : fields.fields) {
                AttributesImpl attrs = new AttributesImpl();

                if (field instanceof TableField<?, ?>) {
                    Table<?> table = ((TableField<?, ?>) field).getTable();

                    if (table != null) {
                        Schema schema = table.getSchema();

                        if (schema != null) {
                            attrs.addAttribute("", "", "schema", "CDATA", schema.getName());
                        }

                        attrs.addAttribute("", "", "table", "CDATA", table.getName());
                    }
                }

                attrs.addAttribute("", "", "name", "CDATA", field.getName());
                attrs.addAttribute("", "", "type", "CDATA", field.getDataType().getTypeName().toUpperCase(renderLocale(configuration.settings())));

                handler.startElement("", "", "field", attrs);
                handler.endElement("", "", "field");
            }

            handler.endElement("", "", "fields");
            handler.startElement("", "", "records", empty);
        }

        for (Record record : this) {
            handler.startElement("", "", "record", empty);

            for (int index = 0; index < fields.fields.length; index++) {
                Field<?> field = fields.fields[index];
                Object value = record.get(index);

                String tag = format.recordFormat() == COLUMN_NAME_ELEMENTS
                    ? escapeXML(fields.fields[index].getName())
                    : "value";

                AttributesImpl attrs = new AttributesImpl();

                if (format.recordFormat() == VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE)
                    attrs.addAttribute("", "", "field", "CDATA", field.getName());

                handler.startElement("", "", tag, attrs);

                if (value != null) {
                    char[] chars = format0(value, false, false).toCharArray();
                    handler.characters(chars, 0, chars.length);
                }

                handler.endElement("", "", tag);
            }

            handler.endElement("", "", "record");
        }

        if (format.header())
            handler.endElement("", "", "records");

        if (format.xmlns())
            handler.endPrefixMapping("");

        handler.endDocument();
        return handler;
    }

    /**
     * @param value The value to be formatted
     * @param visual Whether the formatted output is to be consumed visually
     *            (HTML, TEXT) or by a machine (CSV, JSON, XML)
     */
    private static final String format0(Object value, boolean changed, boolean visual) {

        // [#2741] TODO: This logic will be externalised in new SPI
        String formatted = changed && visual ? "*" : "";

        if (value == null) {
            formatted += visual ? "{null}" : null;
        }
        else if (value.getClass() == byte[].class) {
            formatted += DatatypeConverter.printBase64Binary((byte[]) value);
        }
        else if (value.getClass().isArray()) {
            // [#6545] Nested arrays
            if (value.getClass().getComponentType().isArray())
                formatted += Arrays.deepToString((Object[]) value);
            else
                formatted += Arrays.toString((Object[]) value);
        }
        else if (value instanceof EnumType) {
            formatted += ((EnumType) value).getLiteral();
        }
        else if (value instanceof Record) {
            formatted += ((Record) value).valuesRow().toString();
        }

        // [#5238] Oracle DATE is really a TIMESTAMP(0)...
        else if (value instanceof Date) {
            String date = value.toString();

            if (Date.valueOf(date).equals(value))
                formatted += date;
            else
                formatted += new Timestamp(((Date) value).getTime());
        }
        else {
            formatted += value.toString();
        }

        return formatted;
    }

    private static final String escapeXML(String string) {
        return StringUtils.replaceEach(string,
            new String[] { "\"", "'", "<", ">", "&" },
            new String[] { "&quot;", "&apos;", "&lt;", "&gt;", "&amp;"});
    }
}
