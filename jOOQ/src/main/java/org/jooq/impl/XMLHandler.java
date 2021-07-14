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

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DefaultDataType.getDataType;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.fields;
import static org.jooq.impl.Tools.newRecord;
import static org.jooq.impl.Tools.row0;
import static org.jooq.tools.StringUtils.defaultIfBlank;

import java.io.ByteArrayInputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.JooqLogger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Lukas Eder
 */
final class XMLHandler<R extends Record> extends DefaultHandler {
    private static final JooqLogger log   = JooqLogger.getLogger(XMLHandler.class);
    private static final boolean    debug = false;
    private final DSLContext        ctx;
    private final Deque<State<R>>   states;
    private State<R>                s;

    private static class State<R extends Record> {
        AbstractRow<R>           row;
        final Class<? extends R> recordType;
        boolean                  inResult;
        boolean                  inFields;
        int                      inRecord;
        boolean                  inColumn;
        Result<R>                result;
        final List<Field<?>>     fields;
        final List<Object>       values;
        int                      column;

        @SuppressWarnings("unchecked")
        State(AbstractRow<R> row, Class<? extends R> recordType) {
            this.row = row;
            this.recordType = recordType != null ? recordType : (Class<? extends R>) Record.class;
            this.fields = new ArrayList<>();
            this.values = new ArrayList<>();
        }
    }

    XMLHandler(DSLContext ctx, AbstractRow<R> row, Class<? extends R> recordType) {
        this.ctx = ctx;
        this.states = new ArrayDeque<>();
        s = new State<>(row, recordType);
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

        if (!s.inResult && "result".equalsIgnoreCase(qName)) {
            s.inResult = true;
        }
        else if (s.inColumn && "result".equalsIgnoreCase(qName)) {
            Field<?> f = s.row.field(s.column);

            if (f.getDataType().isMultiset()) {
                states.push(s);
                s = new State<>((AbstractRow<R>) f.getDataType().getRow(), (Class<R>) f.getDataType().getRecordType());
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

            s.fields.add(field(name(catalog, schema, table, name), getDataType(ctx.dialect(), defaultIfBlank(type, "VARCHAR"))));
        }
        else if (s.inResult && "records".equalsIgnoreCase(qName)) {}
        else if (s.inResult && "record".equalsIgnoreCase(qName)) {
            s.inRecord++;

            if (s.inColumn) {
                Field<?> f = s.row.field(s.column);

                if (f.getDataType().isRecord()) {
                    states.push(s);
                    // TODO: Support UDTRecord, EmbeddableRecord types
                    s = new State<>((AbstractRow<R>) f.getDataType().getRow(), (Class<R>) Record.class);
                    s.inResult = true;
                }
                else
                    throw new UnsupportedOperationException("Nested records not supported yet");
            }
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

            DataType<?> t = s.fields.get(s.column).getDataType();
            if (!t.isMultiset() && !t.isRecord())
                s.values.add(null);
        }
    }

    @Override
    public final void endElement(String uri, String localName, String qName) throws SAXException {
        if (debug)
            if (log.isDebugEnabled())
                log.debug("< " + qName);

        if (states.isEmpty() && s.inResult && s.inRecord == 0 && "result".equalsIgnoreCase(qName)) {
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
            s.result.add(newRecord(true, s.recordType, s.row, ctx.configuration()).operate(r -> {
                r.from(s.values);
                return r;
            }));

            s.values.clear();
            s.column = 0;
        }

        else x: {
            if (!states.isEmpty()) {
                State<R> peek = states.peek();
                Field<?> f = peek.row.field(peek.column);

                if ("record".equalsIgnoreCase(qName) && f.getDataType().isRecord()) {
                    peek.values.add(newRecord(true, s.recordType, s.row, ctx.configuration()).operate(r -> {
                        r.from(s.values);
                        return r;
                    }));

                    s = states.pop();
                    break x;
                }
                else if ("result".equalsIgnoreCase(qName) && f.getDataType().isMultiset()) {
                    initResult();
                    peek.values.add(s.result);
                    s = states.pop();
                    break x;
                }
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
            return !anyMatch(fields, f -> !"value".equalsIgnoreCase(f.getName()));
    }

    @Override
    public final void characters(char[] ch, int start, int length) throws SAXException {
        if (s.inColumn
                && !(s.fields.get(s.column).getDataType().isRecord())
                && !(s.fields.get(s.column).getDataType().isMultiset())) {
            String value = new String(ch, start, length);
            Object old;

            if (s.values.size() == s.column)
                s.values.add(value);
            else if ((old = s.values.get(s.column)) == null)
                s.values.set(s.column, value);
            else
                s.values.set(s.column, old + value);
        }
    }
}
