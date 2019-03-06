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

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Lukas Eder
 */
final class XMLHandler extends DefaultHandler {

    private final DSLContext     ctx;
    private boolean              inResult;
    private boolean              inFields;
    private boolean              inRecords;
    private int                  inRecord;
    Result<Record>               result;
    private Field<?>[]           fieldsArray;
    private final List<Field<?>> fields;
    private final List<String>   values;
    private int                  column;

    XMLHandler(DSLContext ctx) {
        this.ctx = ctx;
        this.fields = new ArrayList<Field<?>>();
        this.values = new ArrayList<String>();
    }

    @Override
    public final void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (!inResult && "result".equals(qName)) {
            inResult = true;
        }
        else if (inResult && "fields".equals(qName)) {
            inFields = true;
        }
        else if (inResult && inFields && "field".equals(qName)) {
            String catalog = attributes.getValue("catalog");
            String schema = attributes.getValue("schema");
            String table = attributes.getValue("table");
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");

            fields.add(field(name(catalog, schema, table, name), getDataType(ctx.dialect(), type)));
        }
        else if (inResult && "records".equals(qName)) {
            inRecords = true;
        }
        else if (inResult && "record".equals(qName)) {
            inRecord++;
            column = 0;
        }
        else if (result == null) {
            String fieldName;

            if (("value").equals(qName) && (fieldName = attributes.getValue("field")) != null)
                fields.add(field(name(fieldName), VARCHAR));
            else
                fields.add(field(name(qName), VARCHAR));
        }
    }

    @Override
    public final void endElement(String uri, String localName, String qName) throws SAXException {
        if (inResult && inRecord == 0 && "result".equals(qName)) {
            inResult = false;
        }
        else if (inResult && inFields && "fields".equals(qName)) {
            inFields = false;
            initResult();
        }
        else if (inResult && "records".equals(qName)) {
            inRecords = false;
        }
        else if (inRecord > 0 && "record".equals(qName)) {
            inRecord--;

            initResult();
            Record r = ctx.newRecord(fieldsArray);
            r.from(values);
            result.add(r);

            values.clear();
        }
        else {
            column++;
        }
    }

    private void initResult() {
        if (result == null)

            // Parsing RecordFormat.VALUE_ELEMENTS format
            if (onlyValueFields(fields))
                result = ctx.newResult(fieldsArray = Tools.fields(fields.size()));
            else
                result = ctx.newResult(fieldsArray = fields.toArray(EMPTY_FIELD));
    }

    private static boolean onlyValueFields(List<Field<?>> fields) {
        if (fields.size() <= 1)
            return false;

        for (Field<?> field : fields)
            if (!"value".equals(field.getName()))
                return false;

        return true;
    }

    @Override
    public final void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length);

        if (values.size() == column)
            values.add(value);
        else
            values.set(column, values.get(column) + value);
    }
}
