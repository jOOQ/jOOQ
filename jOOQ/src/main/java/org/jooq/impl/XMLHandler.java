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
package org.jooq.impl;

// ...
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.XML.xml;
import static org.jooq.impl.AbstractResult.escapeXML;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DefaultDataType.getDataType;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.allMatch;
import static org.jooq.impl.Tools.converterContext;
import static org.jooq.impl.Tools.fields;
import static org.jooq.impl.Tools.newRecord;
import static org.jooq.impl.Tools.row0;
import static org.jooq.tools.StringUtils.defaultIfBlank;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jooq.ContextConverter;
import org.jooq.ConverterContext;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
// ...
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Lukas Eder
 */
final class XMLHandler<R extends Record>
extends
    DefaultHandler
implements
    LexicalHandler
{
    private static final JooqLogger log                  = JooqLogger.getLogger(XMLHandler.class);
    private static final boolean    debug                = false;




    private final DSLContext        ctx;
    private final Deque<State<R>>   states;
    private State<R>                s;

    static class XMLWriter extends DefaultHandler implements LexicalHandler {
        final StringWriter out;
        int                level;
        String             lastElement;
        String[]           lastAttributes;
        boolean            cdata;

        XMLWriter() {
            out = new StringWriter();

            // [#19229] TODO: StringWriter seems good enough for our test cases. Perhaps, switch to XMLStreamWriter, instead?
        }

        private boolean flushLastElement(boolean end) {
            if (lastElement != null) {
                out.write('<');
                out.write(lastElement);

                if (lastAttributes != null) {
                    for (int i = 0; i < lastAttributes.length; i += 2) {
                        out.write(' ');
                        out.write(lastAttributes[i]);
                        out.write("=\"");
                        out.write(escapeXML(lastAttributes[i + 1]));
                        out.write("\"");
                    }
                }

                if (end)
                    out.write("/>");
                else
                    out.write('>');

                lastElement = null;
                lastAttributes = null;
                return true;
            }
            else
                return false;
        }

        // --------------------------------------------------------------------
        // ContentHandler API
        // --------------------------------------------------------------------

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            level++;

            flushLastElement(false);
            lastElement = qName;

            // [#19229] Attributes is a mutable object in some parsers (e.g. ojdbc ships its own),
            //          so we have to copy its contents
            if (atts != null && atts.getLength() > 0) {
                lastAttributes = new String[atts.getLength() * 2];

                for (int i = 0; i < atts.getLength(); i++) {
                    lastAttributes[i * 2] = atts.getQName(i);
                    lastAttributes[i * 2 + 1] = atts.getValue(i);
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (!flushLastElement(true)) {
                out.write("</");
                out.write(qName);
                out.write('>');
            }

            level--;
        }

        @Override
        public void processingInstruction(String target, String data) throws SAXException {
            flushLastElement(false);
            out.write("<?");
            out.write(target);

            if (!StringUtils.isEmpty(data)) {
                out.write(' ');
                out.write(data);
            }

            out.write("?>");
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            flushLastElement(false);

            if (cdata)
                out.write(ch, start, length);
            else
                out.write(escapeXML(new String(ch, start, length)));
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            flushLastElement(false);
            out.write(ch, start, length);
        }

        // --------------------------------------------------------------------
        // LexicalHandler API
        // --------------------------------------------------------------------

        @Override
        public void startCDATA() throws SAXException {
            cdata = true;
            flushLastElement(false);
            out.write("<![CDATA[");
        }

        @Override
        public void endCDATA() throws SAXException {
            flushLastElement(false);
            out.write("]]>");
            cdata = false;
        }

        @Override
        public void comment(char[] ch, int start, int length) throws SAXException {
            flushLastElement(false);

            out.write("<!--");
            out.write(ch, start, length);
            out.write("-->");
        }

        // [#19229] TODO: Implement these if needed

        @Override
        public void startDTD(String name, String publicId, String systemId) throws SAXException {
        }

        @Override
        public void endDTD() throws SAXException {
        }

        @Override
        public void startEntity(String name) throws SAXException {
        }

        @Override
        public void endEntity(String name) throws SAXException {
        }

        // --------------------------------------------------------------------
        // Object API
        // --------------------------------------------------------------------

        @Override
        public String toString() {
            return out.toString();
        }
    }

    private static class State<R extends Record> {
        final DSLContext         ctx;
        AbstractRow<R>           row;
        final Class<? extends R> recordType;
        boolean                  inResult;
        boolean                  inFields;
        int                      inRecord;
        boolean                  inColumn;
        boolean                  inElement;
        Result<R>                result;
        final List<Field<?>>     fields;
        final List<Object>       values;
        List<Object>             elements;
        int                      column;
        XMLWriter                writer;

        @SuppressWarnings("unchecked")
        State(DSLContext ctx, AbstractRow<R> row, Class<? extends R> recordType) {
            this.ctx = ctx;
            this.row = row;
            this.recordType = recordType != null ? recordType : (Class<? extends R>) Record.class;
            this.fields = new ArrayList<>();
            this.values = new ArrayList<>();
        }

        final R into(R r) {
            ConverterContext cc = null;

            // [#12134] Patch base64 encoded binary values
            for (int i = 0; i < fields.size(); i++) {
                Object v = values.get(i);
                DataType<?> t = fields.get(i).getDataType();

                if (v instanceof String s) {
                    if (t.isBinary()) {





                        values.set(i, Base64.getDecoder().decode(s));
                    }
                }

                // [#18190] For historic reasons, Record.from() will not apply Converter<T, T>, so any potential
                //          Converter<String, String> should be applied eagerly, before loading data into the record.
                if (v == null || v instanceof String) {
                    if (t instanceof ConvertedDataType && t.getFromType() == String.class && t.getToType() == String.class) {
                        values.set(i, ((ContextConverter<String, String>) t.getConverter()).from(
                            (String) v,
                            cc == null ? (cc = converterContext(ctx.configuration())) : cc
                        ));
                    }
                }
            }

            r.from(values);
            r.touched(false);
            return r;
        }
    }

    XMLHandler(DSLContext ctx, AbstractRow<R> row, Class<? extends R> recordType) {
        this.ctx = ctx;
        this.states = new ArrayDeque<>();
        this.s = new State<>(ctx, row, recordType);
    }

    Result<R> read(String string) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            // -----------------------------------------------------------------
            // [JOOX #136] FIX START: Prevent OWASP attack vectors
            try {
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            }
            catch (ParserConfigurationException | SAXNotRecognizedException ignore) {}

            try {
                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            }
            catch (ParserConfigurationException | SAXNotRecognizedException ignore) {}

            try {
                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            }
            catch (ParserConfigurationException | SAXNotRecognizedException ignore) {}

            // [#149] Not implemented on Android
            try {
                factory.setXIncludeAware(false);
            }
            catch (UnsupportedOperationException ignore) {}

            // [JOOX #136] FIX END
            // -----------------------------------------------------------------

            SAXParser saxParser = factory.newSAXParser();
            // TODO: Why does the SAXParser replace \r by \n?

            try {
                saxParser.setProperty("http://xml.org/sax/properties/lexical-handler", this);
            }
            catch (SAXNotRecognizedException | SAXNotSupportedException ignore) {}

            saxParser.parse(new ByteArrayInputStream(string.getBytes(ctx.configuration().charsetProvider().provide())), this);
            return s.result;
        }
        catch (Exception e) {
            throw new DataAccessException("Could not read the XML string", e);
        }
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public final void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (debug)
            if (log.isDebugEnabled())
                log.debug("> " + qName);

        if (s.writer != null) {
            s.writer.startElement(uri, localName, qName, attributes);
        }
        else if (!s.inResult && "result".equalsIgnoreCase(qName)) {
            s.inResult = true;
        }
        else if (s.inColumn && "result".equalsIgnoreCase(qName)) {
            Field<?> f = s.row.field(s.column);

            if (f.getDataType().isMultiset()) {
                states.push(s);
                s = new State<>(ctx, (AbstractRow<R>) f.getDataType().getRow(), (Class<R>) f.getDataType().getRecordType());
                s.inResult = true;
            }
            else
                throw new UnsupportedOperationException("Nested result sets not supported yet");
        }
        else if (s.inResult && "fields".equalsIgnoreCase(qName)) {
            s.inFields = true;
        }
        else if (s.inResult && s.inFields && "field".equalsIgnoreCase(qName)) {
            String catalog = attributes.getValue("catalog");
            String schema = attributes.getValue("schema");
            String table = attributes.getValue("table");
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");

            // [#13426] Don't use the dialect specific data type, because that isn't what's being exported, either.
            s.fields.add(field(name(catalog, schema, table, name), getDataType(null, defaultIfBlank(type, "VARCHAR"))));
        }
        else if (s.inResult && "records".equalsIgnoreCase(qName)) {}
        else if (s.inResult && "record".equalsIgnoreCase(qName)) {
            s.inRecord++;

            if (s.inColumn) {
                Field<?> f = s.row.field(s.column);

                if (f.getDataType().isRecord()) {
                    states.push(s);
                    s = new State<>(ctx, (AbstractRow<R>) f.getDataType().getRow(), (Class<R>) f.getDataType().getRecordType());
                    s.inResult = true;
                }
                else
                    throw new UnsupportedOperationException("Nested records not supported yet");
            }
        }
        else if (s.inColumn && "element".equalsIgnoreCase(qName) && s.elements != null) {
            s.inElement = true;
        }
        else {
            if (s.result == null) {
                String fieldName;

                if (("value").equalsIgnoreCase(qName) && (fieldName = attributes.getValue("field")) != null)
                    ;
                else
                    fieldName = qName;

                if (s.row != null)
                    s.fields.add(s.row.field(s.fields.size()));
                else
                    s.fields.add(field(name(fieldName), VARCHAR));
            }

            s.inColumn = true;

            Field<?> f = s.fields.get(s.column);
            DataType<?> t = f.getDataType();

            // [#13181] String NULL and '' values cannot be distinguished without xsi:nil
            if (t.isString() && !isNil(attributes)) {
                s.values.add("");
            }
            else if (t.isArray() && !isNil(attributes)) {
                s.elements = new ArrayList<>();
            }
            else if (!t.isMultiset() && !t.isRecord()) {
                s.values.add(null);

                // [#19229] Copy XML content
                if (f.getDataType().isXML() && s.writer == null)
                    s.writer = new XMLWriter();
            }
        }
    }

    private final boolean isNil(Attributes attributes) {
        return "true".equals(attributes.getValue("xsi:nil"))
            || "true".equals(attributes.getValue("nil"));
    }

    @Override
    public final void endElement(String uri, String localName, String qName) throws SAXException {
        if (debug)
            if (log.isDebugEnabled())
                log.debug("< " + qName);

        if (s.writer != null && s.writer.level == 0)
            s.writer = null;

        if (s.writer != null) {
            s.writer.endElement(uri, localName, qName);

            if (s.writer.level == 0) {
                s.values.set(s.values.size() - 1, xml(s.writer.out.toString()));
                s.writer = null;
            }
        }
        else if (states.isEmpty() && s.inResult && s.inRecord == 0 && "result".equalsIgnoreCase(qName)) {
            if (s.result == null)
                initResult();

            s.inResult = false;
        }
        else if (s.inResult && s.inFields && "fields".equalsIgnoreCase(qName)) {
            s.inFields = false;
            initResult();
        }
        else if (s.inResult && s.inFields && "field".equalsIgnoreCase(qName)) {}
        else if (s.inResult && "records".equalsIgnoreCase(qName)) {}
        else if (s.inRecord > 0 && "record".equalsIgnoreCase(qName)) {
            s.inRecord--;

            initResult();
            s.result.add(newRecord(true, ctx.configuration(), s.recordType, s.row).operate(s::into));
            s.values.clear();
            s.column = 0;
        }
        else if (s.inColumn && "element".equalsIgnoreCase(qName) && s.elements != null) {
            s.inElement = false;
            s.elements.add(s.values.get(s.column));
            s.values.remove(s.column);
        }

        else x: {
            if (!states.isEmpty()) {
                State<R> peek = states.peek();
                Field<?> f = peek.row.field(peek.column);

                if ("record".equalsIgnoreCase(qName) && f.getDataType().isRecord()) {
                    peek.values.add(newRecord(true, ctx.configuration(), s.recordType, s.row).operate(s::into));
                    s = states.pop();
                    s.inRecord--;
                    break x;
                }
                else if ("result".equalsIgnoreCase(qName) && f.getDataType().isMultiset()) {
                    initResult();
                    peek.values.add(s.result);
                    s = states.pop();
                    break x;
                }
            }
            else if (s.elements != null) {
                s.values.add(s.elements);
                s.elements = null;
            }

            s.inColumn = false;
            s.column++;
        }
    }

    @SuppressWarnings("unchecked")
    private void initResult() {
        if (s.result == null) {
            if (s.row == null)

                // Parsing RecordFormat.VALUE_ELEMENTS format
                if (onlyValueFields(s.fields))
                    s.row = (AbstractRow<R>) row0(fields(s.fields.size()));
                else
                    s.row = (AbstractRow<R>) row0(s.fields.toArray(EMPTY_FIELD));

            s.result = new ResultImpl<>(ctx.configuration(), s.row);
        }
    }

    private static boolean onlyValueFields(List<Field<?>> fields) {
        if (fields.size() <= 1)
            return false;
        else
            return allMatch(fields, f -> "value".equalsIgnoreCase(f.getName()));
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (s.writer != null)
            s.writer.ignorableWhitespace(ch, start, length);
    }

    @Override
    public final void characters(char[] ch, int start, int length) throws SAXException {
        DataType<?> t;

        if (s.writer != null) {
            s.writer.characters(ch, start, length);
        }
        else if (s.inColumn
            && !(t = s.fields.get(s.column).getDataType()).isRecord()
            && !t.isMultiset()
            && (!t.isArray() || s.inElement)
        ) {
            String value = new String(ch, start, length);
            Object old;

            // [#13872] If we're reading an array (s.inElement), the element
            //          content is still appended to s.values for now, until
            //          "element" is finished
            if (s.values.size() == s.column)
                s.values.add(value);
            else if ((old = s.values.get(s.column)) == null)
                s.values.set(s.column, value);
            else
                s.values.set(s.column, old + value);
        }
    }

    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        if (s != null && s.writer != null)
            s.writer.startDTD(name, publicId, systemId);
    }

    @Override
    public void endDTD() throws SAXException {
        if (s != null && s.writer != null)
            s.writer.endDTD();
    }

    @Override
    public void startEntity(String name) throws SAXException {
        if (s != null && s.writer != null)
            s.writer.startEntity(name);
    }

    @Override
    public void endEntity(String name) throws SAXException {
        if (s != null && s.writer != null)
            s.writer.endEntity(name);
    }

    @Override
    public void startCDATA() throws SAXException {
        if (s != null && s.writer != null)
            s.writer.startCDATA();
    }

    @Override
    public void endCDATA() throws SAXException {
        if (s != null && s.writer != null)
            s.writer.endCDATA();
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (s != null && s.writer != null)
            s.writer.comment(ch, start, length);
    }
}
